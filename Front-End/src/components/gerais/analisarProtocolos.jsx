import axios from "axios";
import { useState, useEffect } from "react";
import { useParams, useNavigate } from "react-router-dom";
import Loading from '../layouts/Loading';
import Message from '../layouts/Message'
import URL from '../services/url';
import TodasDevolutivas from "./todasDevolutivas";
import ConfirmationDialog from '../layouts/ConfirmationDialog';

function AnalisarProtocolos() {
  const navigate = useNavigate()
  const axiosInstance = axios.create({
    baseURL: URL,
    withCredentials: true,
  });
  // Recuperar o token do localStorage
  const token = localStorage.getItem('token');

  // Adicionar o token ao cabeçalho de autorização
  axiosInstance.defaults.headers.common['Authorization'] = `Bearer ${token}`;

  const [devolutiva, setDevolutiva] = useState('');
  const [protocolo, setProtocolo] = useState(null);
  const [statusSelecionado, setStatusSelecionado] = useState(""); // Estado para armazenar o status selecionado
  const [secretarias, setSecretarias] = useState([]);
  const [idSecretariaSelecionada, setIdSecretariaSelecionada] = useState("");
  const [message, setMessage] = useState()
  const [type, setType] = useState()
  const [removeLoading, setRemoveLoading] = useState(true)
  const [devolutivaMaisRecente, setDevolutivaMaisRecente] = useState(null); // Estado para armazenar a devolutiva mais recente
  const [showConfirmationDialog, setShowConfirmationDialog] = useState(false);
  const [enviandoDevolutiva, setEnviandoDevolutiva] = useState(false);
  const [mensagemAtiva, setMensagemAtiva] = useState(false);

  const { id } = useParams();
  const role = localStorage.getItem('role')

  let message1 = false

  const HistoricoDevolutivas = () => {
    //navigate(`/todas-devolutivas/${id}`); // Na mesma pagina 
    window.open(`/todas-devolutivas/${id}`, '_blank'); // Abre outra pagina, achei melhor...
  };

  useEffect(() => {
    async function fetchProtocolo() {
      try {
        if (localStorage.getItem('role') === 'FUNCIONARIO' || localStorage.getItem('role') === 'COORDENADOR') {
          const response1 = await axiosInstance.get('/protoon/secretaria');
          setSecretarias(response1.data);
        }
        const response2 = await axiosInstance.get(`/protoon/protocolo/pesquisar-id/${id}`);
        setProtocolo(response2.data);
        setStatusSelecionado(response2.data.status);
        const devolutivasResponse = await axiosInstance.get(`/protoon/devolutiva/devolutiva-protocolo/${id}`);
        const devolutivas = devolutivasResponse.data;

        if (devolutivas.length > 0) {
          // Ordenar as devolutivas por momento_devolutiva (do mais recente para o mais antigo)
          devolutivas.sort((a, b) => {
            // Comparar as datas de momento_devolutiva, ordena de forma "alfabetica", ja q data é uma String, igual a atividade da prof carla para organizar os nomes pelo valor de cada carctere
            return b.momento_devolutiva.localeCompare(a.momento_devolutiva);
          });

          // Pegar a devolutiva mais recente
          setDevolutivaMaisRecente(devolutivas[0]);
          console.log("Devolutiva mais recente:", devolutivas[0]);
        }

      } catch (error) {
        console.error('Erro ao buscar o protocolo:', error);
      }
    }
    fetchProtocolo();
  }, [id]);

  const voltarAnterior = async () => {
    navigate('/protocolos')
  }


  const enviarDevolutiva = (event) => {
    setDevolutiva(event.target.value);
  };

  const novaDevolutiva = async () => {
    // Verifica se o campo de descrição está vazio ou nulo
    if (!devolutiva || devolutiva.trim() === '') {
      if (exibirMensagem) {
        exibirMensagem("Campo de descrição vazio. Não é possível enviar a devolutiva.", 'error');
        return; // Retorna sem fazer a requisição
      }
    }

    try {
      setEnviandoDevolutiva(true);

      const response = await axiosInstance.post(`/protoon/devolutiva/criar-devolutiva/${id}`, { devolutiva });

      salvarAlteracoes()

      exibirMensagem("Devolutiva Enviada com Sucesso!", 'success');

      setTimeout(() => {
        setDevolutiva('');
        navigate(`/protocolo/${id}`);
      }, 3000);
    } catch (error) {
      exibirMensagem("Erro ao enviar a devolutiva. Por favor, tente novamente mais tarde.", 'error');
      console.error('Erro ao enviar a devolutiva:', error);
    } finally {
      setEnviandoDevolutiva(false);
    }
  };

  const exibirMensagem = (msg, tipo) => {
    setMessage(msg);
    setType(tipo);
    setMensagemAtiva(true);

    setTimeout(() => {
      setMessage('');
      setType('');
      setMensagemAtiva(false);
    }, 3000);
  };

  useEffect(() => {
    // Habilita o botão quando a mensagem estiver inativa
    const timer = setTimeout(() => {
      setMensagemAtiva(false);
    }, 3000);

    return () => clearTimeout(timer);
  }, [mensagemAtiva]);

  const updateProtocolo = async () => {
    try {
      console.log("Novo status selecionado:", statusSelecionado); // Adicionando console.log para depurar
      console.log(idSecretariaSelecionada)

      if (!devolutiva || devolutiva.trim() === '') {
        if (exibirMensagem) {
          exibirMensagem("Campo de descrição de Devolutiva vazio. Não é possível atualizar status.", 'error');
          return; // Retorna sem fazer a requisição
        }
      }

      const response = await axiosInstance.put(`/protoon/protocolo/alterar-protocolos/status/${protocolo.numero_protocolo}`, {
        ...protocolo,
        status: statusSelecionado
      });
      if (response.status.valueOf() === 200) {
        setRemoveLoading(false)
        setTimeout(() => {
          console.log(response.data);
          setRemoveLoading(true)
          setMessage('Protocolo atualizado com Sucesso!')
          setType('success')
          setTimeout(() => {
            setMessage('')
            navigate('/protocolo/' + id);
          }, 2000)
        }, 2000)
        window.scrollTo({ top: 0, behavior: 'smooth' });
      }
    } catch (error) {
      console.error('Erro ao atualizar o protocolo:', error);
    }
  }

  const redirectProtocolo = async () => {
    setShowConfirmationDialog(true);
  }

  const solicitarRedirecionar = async () => {

    try {
      const response1 = await axiosInstance.get(`/protoon/secretaria/${idSecretariaSelecionada}`)
      console.log(response1.data)
      const secretariaData = response1.data;

      const response2 = await axiosInstance.post(`/protoon/redirecionamento/${id}`,
        { novaSecretaria: secretariaData.nome_secretaria }
      )
      console.log(response2.data)

      // Exibe um alerta de confirmação antes de redirecionar o protocolo

      // const response3 = await axiosInstance.put(`/protoon/protocolo/alterar-protocolos/departamento/${protocolo.numero_protocolo}`, {
      //   ...protocolo,
      //   secretaria: secretariaData
      // });

      if (response2.status.valueOf() === 201) {
        setRemoveLoading(false)
        setTimeout(() => {
          setRemoveLoading(true)
          setMessage('Solicitação de redirecionamento feita com Sucesso!')
          setType('success')
          setTimeout(() => {
            setMessage('')
            navigate('/protocolos');
          }, 2000)
        }, 2000)
        window.scrollTo({ top: 0, behavior: 'smooth' });
      }
    } catch (error) {

      // TODO: ATUALIZAR MENSAGEM QUANDO DER ERRO PERSONALIZADA NO REDIRECIONAMENTO DE PROTOCOLO
      if (error.response && error.response.data && error.response.data.message) {
        setMessage(error.response.data.message);

        console.log(error.response.data.message);
      } else {
        setMessage('Erro desconhecido ocorreu.');
        console.log('Erro desconhecido ocorreu.');
      }
    }
  }


  const redirecionar = async () => {
    if (ConfirmationDialog) {
      setShowConfirmationDialog(false)
      try {
        const response1 = await axiosInstance.get(`/protoon/secretaria/${idSecretariaSelecionada}`)
        console.log(response1.data)
        const secretariaData = response1.data;

        const response2 = await axiosInstance.post(`/protoon/redirecionamento/${id}`,
          { novaSecretaria: secretariaData.nome_secretaria }
        )
        console.log(response2.data)

        // Exibe um alerta de confirmação antes de redirecionar o protocolo

        const response3 = await axiosInstance.put(`/protoon/protocolo/alterar-protocolos/departamento/${protocolo.numero_protocolo}`, {
          ...protocolo,
          secretaria: secretariaData
        });

        if (response3) {
          setRemoveLoading(false)
          setTimeout(() => {
            setRemoveLoading(true)
            setMessage('Redirecionamento feito com Sucesso!')
            setType('success')
            setTimeout(() => {
              setMessage('')
              navigate('/protocolos');
            }, 2000)
          }, 2000)
          window.scrollTo({ top: 0, behavior: 'smooth' });
        }
      } catch (error) {

        // TODO: ATUALIZAR MENSAGEM QUANDO DER ERRO PERSONALIZADA NO REDIRECIONAMENTO DE PROTOCOLO
        if (error.response && error.response.data && error.response.data.message) {
          setMessage(error.response.data.message);

          console.log(error.response.data.message);
        } else {
          setMessage('Erro desconhecido ocorreu.');
          console.log('Erro desconhecido ocorreu.');
        }
      }
    }
  }

  const formatarDataHora = (dataString) => {
    const data = new Date(dataString);
    const options = {
      day: "2-digit",
      month: "2-digit",
      year: "numeric",
      hour: "2-digit",
      minute: "2-digit",
      timeZone: "America/Sao_Paulo"
    };
    return data.toLocaleString("pt-BR", options);
  };
  const handleSecretariaChange = (e) => {
    const selectedSecretariaId = e.target.value;
    setIdSecretariaSelecionada(selectedSecretariaId);  };

  const handleStatusChange = (e) => {
    const novoStatus = e.target.value;
    setStatusSelecionado(novoStatus); // Atualiza o estado com o novo status selecionado
    setProtocolo(prevProtocolo => ({
      ...prevProtocolo,
      status: novoStatus // Atualiza o status do protocolo com o novo status selecionado
    }));  };

  if (!protocolo) {
    return <Loading />;  }

  const salvarAlteracoes = async () => {
    // Evita múltiplos cliques e varias chamadas da função
    if (enviandoDevolutiva || mensagemAtiva) {
      return;
    }
    setMensagemAtiva(true);
    try {
      await updateProtocolo();
      exibirMensagem("Protocolo atualizado com Sucesso!", 'success');
    } catch (error) {
      console.error('Erro ao salvar alterações:', error);
    } finally {
      setMensagemAtiva(false);
    }
  };


  const handleCancelRedirect = () => {
    setShowConfirmationDialog(false);
  }

  return (
    <>
      {message ? <div style={{ marginTop: 300 }}><Message type={type} msg={message} /></div> :
        <div style={{ padding: 40, marginTop: -100 }}>
          {/* {successMessage && <div className="success-message">{successMessage}</div>} */}
          <h1>Detalhes do Protocolo</h1>

          {/* Select para a secretaria */}
          {(role === "COORDENADOR" || role === "FUNCIONARIO") && (
            <div>
              <h3 style={{ marginLeft: -180, marginBottom: -30 }}>Secretaria</h3>
              <select
                style={{ fontSize: 18, marginRight: 10, padding: 10, borderRadius: 10, textAlign: "center" }}
                value={idSecretariaSelecionada} // Aqui se precisa usar idSecretariaSelecionada em vez de selectedSecretaria
                onChange={handleSecretariaChange}
              >
                <option value="">Selecione a secretaria</option>
                {secretarias.map(secretaria => (
                  <option key={secretaria.id_secretaria} value={secretaria.id_secretaria}>
                    {secretaria.nome_secretaria}
                  </option>
                ))}
              </select>
              {role === "FUNCIONARIO" ? (
                <button className="btn-log" onClick={solicitarRedirecionar}>Solicitar Redirecionamento Protocolo</button>
              ) : (
                <button className="btn-log" onClick={redirectProtocolo}>Redirecionar Protocolo</button>
              )}


              {/* {message && <Message type={type} msg={message} />} */}
              {!removeLoading && <Loading />}

              {showConfirmationDialog && (<ConfirmationDialog
                message="Tem certeza que deseja redirecionar o protocolo?"
                onConfirm={redirecionar}
                onCancel={handleCancelRedirect}
              />)}
            </div>
          )}

          <fieldset style={{ border: '1px solid #ddd', backgroundColor: '#d0d0d0', padding: 20, borderRadius: 5, marginTop: 50, position: 'relative' }}>
            <legend style={{ fontWeight: 'bold', fontSize: 20, width: '100%', textAlign: 'center', position: 'absolute', top: '-20px', left: '0', backgroundColor: '#d0d0d0', padding: '10px 0' }}>Protocolo</legend>
            <table style={{ margin: 'auto', borderCollapse: 'collapse', width: '100%', padding: 30 }}>
              <thead>
                <tr>
                  <th style={{ minWidth: 150 }}>Assunto</th>
                  <th style={{ minWidth: 100 }}>Número</th>
                  <th style={{ minWidth: 150 }}>Data</th>
                  <th style={{ minWidth: 100 }}>Valor</th>
                  <th>Alterar Status</th>
                  {/* <th style={{ minWidth: 100 }}>Alterar Status</th> */}
                </tr>
              </thead>
              <tbody>
                <tr>
                  <td style={{ textAlign: 'center' }}>{protocolo.assunto}</td>
                  <td>{protocolo.numero_protocolo}</td>
                  <td style={{ textAlign: 'center' }}>{formatarDataHora(protocolo.data_protocolo)}</td>
                  <td style={{ textAlign: 'center' }}>R$ {protocolo.valor.toFixed(2)}</td>
                  <td>
                    <select
                      value={statusSelecionado}
                      onChange={handleStatusChange}
                    >
                      <option value="PAGAMENTO_PENDENTE">PAGAMENTO PENDENTE</option>
                      <option value="CIENCIA">CIÊNCIA</option>
                      <option value="CIENCIA_ENTREGA">CIÊNCIA ENTREGA</option>
                      <option value="CONCLUIDO">CONCLUÍDO</option>
                    </select>
                  </td>
                  <td style={{ minWidth: 100, textAlign: 'center' }}>
                    {/* <button onClick={salvarAlteracoes} disabled={mensagemAtiva} style={{ opacity: mensagemAtiva ? 0.5 : 1, fontSize: '0.7em' }}>
                      Salvar
                    </button> */}
                  </td>
                </tr>
              </tbody>
            </table>
          </fieldset>

          {/* {message && (<Message type={type} msg={message} />)} */}

          <fieldset style={{ border: '1px solid #ddd', backgroundColor: '#d0d0d0', padding: 20, borderRadius: 5, marginTop: 50, position: 'relative' }}>
            <legend style={{ fontWeight: 'bold', fontSize: 20, width: '100%', textAlign: 'center', position: 'absolute', top: '-20px', left: '0', backgroundColor: '#d0d0d0', padding: '10px 0' }}>Descrição do problema</legend>
            <table style={{ margin: 'auto', borderCollapse: 'collapse', width: '100%', padding: 30 }}>
              <div style={{ backgroundColor: '#f5f5f5', padding: 20, borderRadius: 5 }}>
                <div style={{ maxHeight: '200px', overflowY: 'auto' }}>
                  {protocolo.descricao}
                </div>
              </div>
            </table>
          </fieldset>

          {devolutivaMaisRecente && (
            <fieldset style={{ border: '1px solid #ddd', backgroundColor: '#d0d0d0', padding: 20, borderRadius: 5, marginTop: 50, position: 'relative' }}>
              <legend style={{ fontWeight: 'bold', fontSize: 20, width: '100%', textAlign: 'center', position: 'absolute', top: '-20px', left: '0', backgroundColor: '#d0d0d0', padding: '10px 0' }}>Devolutiva Mais Recente</legend>
              <table style={{ margin: 'auto', borderCollapse: 'collapse', width: '90%', padding: 30 }}>
                <thead>
                  <td style={{ minWidth: 200 }}><p style={{ fontWeight: 700 }}>Data e Hora</p></td>
                  <td style={{ minWidth: 200 }}><p style={{ fontWeight: 700 }}>Funcionário</p></td>
                  <td style={{ minWidth: 200 }}><p style={{ fontWeight: 700 }}>Secretaria</p></td>
                </thead>
                <tbody>
                  <tr>
                    <td style={{ width: 100 }}>{devolutivaMaisRecente.momento_devolutiva ? devolutivaMaisRecente.momento_devolutiva : 'N/A'}</td>
                    <td style={{ width: 100 }}>{devolutivaMaisRecente.id_funcionario && devolutivaMaisRecente.id_funcionario.nome ? devolutivaMaisRecente.id_funcionario.nome : 'N/A'}</td>
                    <td style={{ width: 100 }}>{devolutivaMaisRecente.id_funcionario && devolutivaMaisRecente.id_funcionario.secretaria.nome_secretaria ? devolutivaMaisRecente.id_funcionario.secretaria.nome_secretaria : 'N/A'}</td>
                  </tr>
                </tbody>
              </table>
              <div style={{ backgroundColor: '#f5f5f5', padding: 20, borderRadius: 5, marginTop: 20 }}>
                <p style={{ textAlign: 'justify' }}>
                  {devolutivaMaisRecente.devolutiva ? devolutivaMaisRecente.devolutiva : 'N/A'}
                </p>
              </div>
              <button onClick={HistoricoDevolutivas} style={{ marginTop: 20 }}>Historico de devolutivas</button>
            </fieldset>
          )}

          {protocolo.secretaria ? (
            <>
              <fieldset style={{ border: '1px solid #ddd', backgroundColor: '#d0d0d0', padding: 20, borderRadius: 5, marginTop: 50, position: 'relative' }}>
                <legend style={{ fontWeight: 'bold', fontSize: 20, width: '100%', textAlign: 'center', position: 'absolute', top: '-20px', left: '0', backgroundColor: '#d0d0d0', padding: '10px 0' }}>Secretaria</legend>
                <table style={{ margin: 'auto', borderCollapse: 'collapse', width: '90%', padding: 30 }}>
                  <thead>
                    <td style={{ minWidth: 200 }}><p style={{ fontWeight: 700 }}>Nome</p></td>
                    <td style={{ minWidth: 200 }}><p style={{ fontWeight: 700 }}>Responsável</p></td>
                    <td style={{ minWidth: 200 }}><p style={{ fontWeight: 700 }}>Email</p></td>
                    <td style={{ minWidth: 200 }}><p style={{ fontWeight: 700 }}>Endereço</p></td>
                  </thead>
                  <tbody>
                    <tr>
                      <td style={{ width: 100 }}>{protocolo.secretaria.nome_secretaria}</td>
                      <td style={{ width: 100 }}>{protocolo.secretaria.nome_responsavel}</td>
                      <td style={{ width: 100 }}>{protocolo.secretaria.email}</td>
                      <td style={{ maxWidth: 500 }}>{protocolo.secretaria.endereco.logradouro}, {protocolo.secretaria.endereco.num_endereco}, {protocolo.secretaria.endereco.complemento}, {protocolo.secretaria.endereco.bairro}, {protocolo.secretaria.endereco.cidade} - {protocolo.secretaria.endereco.estado}, {protocolo.secretaria.endereco.pais}</td>
                    </tr>
                  </tbody>
                </table>
              </fieldset>
            </>
          ) : (
            <li>Carregando...</li>
          )}


          <fieldset style={{ border: '1px solid #ddd', backgroundColor: '#d0d0d0', padding: 20, borderRadius: 5, marginTop: 50, position: 'relative' }}>
            <legend style={{ fontWeight: 'bold', fontSize: 20, width: '100%', textAlign: 'center', position: 'absolute', top: '-20px', left: '0', backgroundColor: '#d0d0d0', padding: '10px 0' }}>Municipe</legend>
            <table style={{ margin: 'auto', borderCollapse: 'collapse', width: '90%', padding: 30 }}>
              <thead>
                <td style={{ minWidth: 200 }}><p style={{ fontWeight: 700 }}>Nome</p></td>
                <td style={{ minWidth: 200 }}><p style={{ fontWeight: 700 }}>Email</p></td>
                <td style={{ minWidth: 150 }}><p style={{ fontWeight: 700 }}>CPF</p></td>
                <td style={{ minWidth: 200 }}><p style={{ fontWeight: 700 }}>Data de Nascimento</p></td>
                <td style={{ minWidth: 150 }}><p style={{ fontWeight: 700 }}>Celular</p></td>
                <td style={{ minWidth: 200 }}><p style={{ fontWeight: 700 }}>Endereço</p></td>
              </thead>
              <tbody>
                <tr>
                  <td style={{ width: 100 }}>{protocolo.municipe.nome}</td>
                  <td style={{ width: 100 }}>{protocolo.municipe.email}</td>
                  <td style={{ width: 100 }}>{protocolo.municipe.num_CPF}</td>
                  <td style={{ width: 100 }}>{protocolo.municipe.data_nascimento}</td>
                  <td style={{ maxWidth: 50 }}>{protocolo.municipe.celular}</td>
                  <td style={{ maxWidth: 500, minWidth: 300 }}>{protocolo.secretaria.endereco.logradouro}, {protocolo.secretaria.endereco.num_endereco}, {protocolo.secretaria.endereco.complemento}, {protocolo.secretaria.endereco.bairro}, {protocolo.secretaria.endereco.cidade} - {protocolo.secretaria.endereco.estado}, {protocolo.secretaria.endereco.pais}</td>
                </tr>
              </tbody>
            </table>
          </fieldset>


          <fieldset style={{ border: '1px solid #ddd', backgroundColor: '#d0d0d0', padding: 20, borderRadius: 5, marginTop: 50, position: 'relative' }}>
            <legend style={{ fontWeight: 'bold', fontSize: 20, width: '100%', textAlign: 'center', position: 'absolute', top: '-20px', left: '0', backgroundColor: '#d0d0d0', padding: '10px 0' }}>Endereço do Protocolo</legend>
            <table style={{ margin: 'auto', borderCollapse: 'collapse', width: '90%', padding: 30 }}>
              <thead>
                <td style={{ minWidth: 100 }}><p style={{ fontWeight: 700 }}>CEP</p></td>
                <td style={{ minWidth: 250 }}><p style={{ fontWeight: 700 }}>Logradouro</p></td>
                <td style={{ minWidth: 100 }}><p style={{ fontWeight: 700 }}>Número</p></td>
                <td style={{ minWidth: 150 }}><p style={{ fontWeight: 700 }}>Complemento</p></td>
                <td style={{ minWidth: 100 }}><p style={{ fontWeight: 700 }}>Bairro</p></td>
                <td style={{ minWidth: 200 }}><p style={{ fontWeight: 700 }}>Cidade</p></td>
                <td style={{ minWidth: 100 }}><p style={{ fontWeight: 700 }}>Estado</p></td>
                <td style={{ minWidth: 100 }}><p style={{ fontWeight: 700 }}>País</p></td>
              </thead>
              <tbody>
                <tr>
                  <td style={{ width: 100 }}>{protocolo.endereco.num_cep}</td>
                  <td style={{ width: 100 }}>{protocolo.endereco.logradouro}</td>
                  <td style={{ width: 100 }}>{protocolo.endereco.num_endereco}</td>
                  <td style={{ width: 100 }}>{protocolo.endereco.complemento}</td>
                  <td style={{ maxWidth: 50 }}>{protocolo.endereco.bairro}</td>
                  <td style={{ maxWidth: 50 }}>{protocolo.endereco.cidade}</td>
                  <td style={{ maxWidth: 50 }}>{protocolo.endereco.estado}</td>
                  <td style={{ maxWidth: 50 }}>{protocolo.endereco.pais}</td>
                </tr>
              </tbody>
            </table>
          </fieldset>

          <fieldset style={{ border: '1px solid #ddd', backgroundColor: '#d0d0d0', padding: 20, borderRadius: 5, marginTop: 50, position: 'relative' }}>
            <legend style={{ fontWeight: 'bold', fontSize: 20, width: '100%', textAlign: 'center', position: 'absolute', top: '-20px', left: '0', backgroundColor: '#d0d0d0', padding: '10px 0' }}>Escreva sua devolutiva</legend>
            <div style={{ backgroundColor: '#f5f5f5', padding: 20, borderRadius: 5, marginTop: 20 }}>
              <textarea
                value={devolutiva}
                onChange={enviarDevolutiva}
                placeholder="Digite sua devolutiva para poder enviar as alterações..."
                style={{ width: '100%', minHeight: 100, padding: 10 }}
              />
            </div>
            {!devolutiva || devolutiva.trim() !== '' && <button onClick={novaDevolutiva} disabled={enviandoDevolutiva || mensagemAtiva} style={{ marginTop: 20 }}>
              {enviandoDevolutiva ? 'Enviando...' : 'Enviar Devolutiva'}
            </button>}
            {/* {message && <Message type={type} msg={message} />} */}
          </fieldset>

          {/*Coloquei no botão de salvar alteração para salvar tanto protocolo quanto devolutivas*/}
          {/*{removeLoading && (<><button onClick={updateProtocolo} className="btn-cad" style={{ marginRight: '100px' }}>Salvar Alterações</button> */}
          {removeLoading && (
            <>
              {/*<button onClick={salvarAlteracoes} disabled={mensagemAtiva} className="btn-cad" style={{ marginRight: '100px', opacity: mensagemAtiva ? 0.6 : 1 }}>
              Salvar Alterações
            </button>*/}
              <button className="btn-log" onClick={voltarAnterior} style={{ opacity: mensagemAtiva ? 0.6 : 1 }}>Voltar</button>
            </>
          )}

        </div >
      }
    </>
  );
}

export default AnalisarProtocolos
