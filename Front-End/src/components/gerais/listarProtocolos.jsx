
import axios from "axios";
import React, { useState, useEffect } from "react";
import { useNavigate } from "react-router-dom";
import URL from '../services/url';

function ListarProtocolosBySecretaria() {
  const axiosInstance = axios.create({
    baseURL: URL, // Adjust the base URL as needed
    withCredentials: true, // Set withCredentials to true
  });

  // const [idSecretariaSelecionada, setIdSecretariaSelecionada] = useState("");
  const [protocolos, setProtocolos] = useState([]);
  const [pesquisarProt, setPesquisarProt] = useState(''); //Pesquisar protocolos
  const navigate = useNavigate(); // Use o hook useNavigation para acessar a navegação
  // Recuperar o token do localStorage
  const token = localStorage.getItem('token');
  
  const [ocultarConcluidos, setOcultarConcluidos] = useState(false);

  // Adicionar o token ao cabeçalho de autorização
  axiosInstance.defaults.headers.common['Authorization'] = `Bearer ${token}`;


  useEffect(() => {
    async function fetchProtocolos() {

      try {
        const response1 = await axiosInstance.get(`/protoon/funcionarios/bytoken`);
        console.log(response1.data)
        console.log(response1.data.secretaria)
        const id_secretaria = response1.data.secretaria.id_secretaria;

        const response2 = await axiosInstance.get(`/protoon/secretaria/protocolos/` + id_secretaria);
        // const response2 = await axiosInstance.get(`/protoon/secretaria/protocolos/${id_secretaria}`);
        setProtocolos(response2.data);
      } catch (error) {
        console.error('Erro ao buscar as secretarias:', error);
      }
    }
    fetchProtocolos();
  }, []);

  const handleClick = (id) => {
    // Redirecionar para outra página com o ID do protocolo na URL usando navigater
    navigate(`/protocolo/${id}`);
  };
  const voltarIndex = async () => {
    navigate("/")
  }

  //Função para formatar a data e a hora com base no Brasil/sp
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

  // Função para filtrar os protocolos com base no número
  const filteredProtocolos = protocolos.filter((protocolo) => {
    return protocolo.numero_protocolo.toLowerCase().includes(pesquisarProt.toLowerCase());
  });

  const handleOcultarConcluidosChange = () => {
    setOcultarConcluidos(!ocultarConcluidos);
  };

  return (
    <>
      <div style={{ padding: 20 }}>
        <h1>Lista de Protocolos</h1>
        <input
          type="text"
          placeholder="Pesquisar por número de protocolo..."
          value={pesquisarProt}
          onChange={(e) => setPesquisarProt(e.target.value)}
        />
        <div style={{ marginTop: 10,  marginBottom: 10 }}>
        <label style={{ fontSize: '20px' }}>
            <input
              type="checkbox"
              checked={ocultarConcluidos}
              onChange={handleOcultarConcluidosChange}
              style={{ marginRight: -140, transform: 'scale(2)' }}
            />
            Mostrar Protocolos Concluídos
          </label>
        </div>
        <table style={{ margin: 'auto', borderCollapse: 'collapse', width: '100%', padding: 30 }}>
          <thead>
            <tr>
              <th>Assunto</th>
              <th>Número</th>
              <th>Data</th>
              <th>Descrição</th>
              <th>Status</th>
              <th>Valor</th>
            </tr>
          </thead>
          <div style={{ marginTop: 30 }}></div>
          <tbody>
          {filteredProtocolos
              .filter(protocolo => ocultarConcluidos || protocolo.status !== "CONCLUIDO")
              .map((protocolo, index) => (
                <React.Fragment key={protocolo.id_protocolo} >
                  <tr onClick={() => handleClick(protocolo.id_protocolo)} className="rowTable">
                    <td style={{ textAlign: 'center', minWidth: 300 }}>{protocolo.assunto}</td>
                    <td style={{ textAlign: 'center', minWidth: 80 }}>{protocolo.numero_protocolo}</td>
                    <td style={{ textAlign: 'center', minWidth: 200 }}>{formatarDataHora(protocolo.data_protocolo)}</td>
                    <td style={{ textAlign: 'center', minWidth: 250, maxWidth: 450, wordWrap: 'break-word' }}>
                      <div style={{ maxHeight: '50px', overflowY: 'auto' }}>
                        {protocolo.descricao}
                      </div>
                    </td>
                    <td style={{ textAlign: 'center', minWidth: 200 }}>{protocolo.status}</td>
                    <td style={{ textAlign: 'center', minWidth: 100 }}>{'R$ ' + protocolo.valor.toFixed(2)}</td>
                  </tr>
                  {index !== filteredProtocolos.length - 1 && <tr><td colSpan="6"><hr style={{ margin: 0 }} /></td></tr>}
                </React.Fragment>
              ))}
          </tbody>
        </table>
        <button className="btn-log" onClick={voltarIndex}>Voltar</button>
      </div >
    </>
  );
}

export default ListarProtocolosBySecretaria