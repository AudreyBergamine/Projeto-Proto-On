import React, { useState, useEffect } from "react";
import { useNavigate } from "react-router-dom";
import axios from 'axios';
import Loading from '../layouts/Loading';
import Message from '../layouts/Message';
import URL from '../services/url';

function Reclamar() {
  const navigate = useNavigate();
  const [message, setMessage] = useState();
  const [type, setType] = useState();
  const [removeLoading, setRemoveLoading] = useState(true);
  const [formData, setFormData] = useState({
    assunto: "",
    descricao: "",
    idSecretaria: "",
    status: 0,
    valor: 0
  });

  const axiosInstance = axios.create({
    baseURL: URL, // Adjust the base URL as needed
    withCredentials: true, // Set withCredentials to true
  });

    // Recuperar o token do localStorage
const token = localStorage.getItem('token');

// Adicionar o token ao cabeçalho de autorização
axiosInstance.defaults.headers.common['Authorization'] = `Bearer ${token}`;
  const [assuntos, setAssuntos] = useState([]);

  // Buscar os assuntos do banco de dados
  useEffect(() => {
    async function fetchAssuntos() {
      try {
        const response = await axiosInstance.get('/protoon/assuntos');
        // Atualiza o estado com os assuntos retornados
        setAssuntos(response.data.map(assunto => ({
          ...assunto,
          valor: assunto.valor_protocolo // Adicionando o campo valor_protocolo como valor
        })));
      } catch (error) {
        console.error('Erro ao buscar os assuntos:', error);
      }
    }
    fetchAssuntos();
  }, []);

  const handleChange = (e) => {
    const { name, value } = e.target;
    setFormData({
      ...formData,
      [name]: value
    });

    if (name === 'assunto') {
      const selectedAssunto = assuntos.find(assunto => assunto.assunto === value);
      setFormData(prevState => ({
        ...prevState,
        idSecretaria: selectedAssunto ? selectedAssunto.secretaria.id_secretaria : null,
        valor: selectedAssunto ? selectedAssunto.valor : null
      }));
    }
  };

  const handleSubmit = async (e) => {
    e.preventDefault();
    // const idMunicipe = localStorage.getItem('idMunicipe');
    // if (!idMunicipe) {
    //   console.error('ID do munícipe não encontrado!');
    //   return;
    // }
    if (formData.assunto === "") {
      setMessage('Selecione um problema');
      setType('error')
      setTimeout(() => {
        setMessage('');
      }, 3000);
      return;
    }

    if (formData.descricao.length < 3) {
      setMessage('Descreva o Problema!');
      setType('error')
      setTimeout(() => {
        setMessage('');
      }, 3000);
      return;
    }

    try {
      const currentDate = new Date(); // Obtém a data e hora atuais
      const response = await axiosInstance.post(`/protoon/protocolo/abrir-protocolos/${formData.idSecretaria}`, {
        assunto: formData.assunto,
        descricao: formData.descricao,
        status: formData.status,
        valor: formData.valor,
        data_protocolo: currentDate // Envia a data e hora atuais para data_protocolo
      });

      setRemoveLoading(false);
      setTimeout(() => {
        console.log(response.data);
        setRemoveLoading(true);
        setMessage('Reclamação bem sucedida! Redirecionando...');
        setType('success');
        setTimeout(() => {
          navigate('/');
        }, 3000);
      }, 3000);
    } catch (error) {
      console.error('Erro ao enviar os dados:', error);
    }
  };

  return (
    <>
      <div style={{ paddingBottom: '100px' }}>
        <form onSubmit={handleSubmit}>
          <div>
            <h3>Reclame Aqui</h3>
            <div className="register-form">
              <div className="input-container">
                <div>
                  <label>Problema:</label><br />
                  <select
                    style={{ fontSize: 20, padding: 10, borderRadius: 10, textAlign: "center" }}
                    name="assunto"
                    value={formData.assunto}
                    onChange={handleChange}
                  >
                    <option value="">Selecione um problema</option>
                    {assuntos.map(assunto => (
                      <option key={assunto.id_assunto} value={assunto.assunto}>{assunto.assunto}</option>
                    ))}
                  </select>
                </div>
              </div>
            </div><br />
            <div className="register-form">
              <div className="input-container">
                <div>
                  <label>Descrição</label><br />
                  <textarea style={{ width: 600, padding: 20, borderRadius: 10 }}
                    name="descricao"
                    rows="5"
                    placeholder="Ex.: Buraco em minha rua, com risco de acidentes"
                    value={formData.descricao}
                    onChange={handleChange}
                  ></textarea>
                </div>
              </div>
            </div>
            <div className="register-form">
              <div className="input-container">
                <div>
                  <label style={{ marginLeft: 30, marginTop: 20 }}>Valor do Serviço</label><br />
                  <input
                    type="text"
                    name="valor"
                    value={"R$ " + formData.valor.toFixed(2)}
                    onChange={handleChange}
                    readOnly
                  />
                </div>
              </div>
            </div>
          </div>
          {removeLoading && <div style={{ marginTop: -30 }}>
          {message && <Message type={type} msg={message} />}
          <button type="submit" className="btn-cad" style={{ marginRight: '100px' }}>Confirmar</button>
            <button className="btn-log" onClick={() => navigate('/paginaInicial')}>Voltar</button>
          </div>}
          {!removeLoading && <Loading />}
        </form>
      </div>
    </>
  );
}

export default Reclamar;
