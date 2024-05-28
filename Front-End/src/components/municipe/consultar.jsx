import React, { useState } from "react";
import axios from 'axios';
import { format } from 'date-fns';
import URL from '../services/url';
import { FiRefreshCcw, FiArrowLeft } from 'react-icons/fi';
import { useNavigate } from "react-router-dom";
import Message from '../layouts/Message'
import Loading from '../layouts/Loading';

function Consultar() {
  const [protocolos, setProtocolos] = useState([]);
  const [mostrarTabela, setMostrarTabela] = useState(false);
  const [removeLoading, setRemoveLoading] = useState(true)
  const [loading, setLoading] = useState(false);
  const [error, setError] = useState(null);
  const navigate = useNavigate()
  const id = localStorage.getItem('id')
  const [message, setMessage] = useState()
  const [type, setType] = useState()

  const sendToIndex = async () => {
    navigate("/")
  }
  const axiosInstance = axios.create({
    baseURL: URL,
    withCredentials: true,
  });

  const handleClick = (id) => {
    // Redirecionar para outra página com o ID do protocolo na URL usando navigater
    window.open(`/todas-devolutivas/${id}`, '_blank');
  };
  const voltarIndex = async () => {
    navigate("/")
  }

  // Recuperar o token do localStorage
  const token = localStorage.getItem('token');

  // Adicionar o token ao cabeçalho de autorização
  axiosInstance.defaults.headers.common['Authorization'] = `Bearer ${token}`;

  const handleSubmit = async (e) => {
    e.preventDefault();
    setLoading(true);
    setRemoveLoading(false);
    setError(null);
  
    setTimeout(async () => {
      setRemoveLoading(true);
      try {
        const response = await axiosInstance.get(`/protoon/protocolo/meus-protocolos/bytoken`);
        setProtocolos(response.data);
        setMostrarTabela(true)
        if (response.data.length === 0) {
          setMessage('Não há nenhuma Devolutiva até o momento');
          setType('error');
          setTimeout(() => {
            setMessage('');
            navigate('consultar');
          }, 3000);
        }
      } catch (error) {
        console.error('Erro ao buscar os protocolos:', error);
        setError('Erro ao buscar os protocolos. Por favor, tente novamente.');
      } finally {
        setLoading(false);
      }
    }, 3000);
  };
  

  function truncateText(text, maxLength) {
    if (text.length <= maxLength) {
      return text;
    }
    return text.substring(0, maxLength) + '...';
  }

  return (
    <>
      {message ? (<Message type={type} msg={message} />) : (
      <div style={{ height: '100vh', width: '100vw', overflow: 'hidden', paddingTop: 20 }}>
        {error && (
          <div style={{ color: 'red', textAlign: 'center', marginBottom: 20 }}>{error}</div>
        )}

        {!removeLoading && (<Loading />)}
        {mostrarTabela && (
          <table style={{ marginTop: 20, borderCollapse: 'collapse', width: '100%', borderRadius: 10, overflow: 'hidden' }}>
            <thead style={{ backgroundColor: "#f2f2f2" }}>
              <tr>
                <th style={{ padding: 10, textAlign: 'center', fontSize: 16 }}>Assunto</th>
                <th style={{ padding: 10, textAlign: 'center', fontSize: 16 }}>Descrição</th>
                <th style={{ padding: 10, textAlign: 'center', fontSize: 16 }}>Secretaria</th>
                <th style={{ padding: 10, textAlign: 'center', fontSize: 16 }}>Etapa</th>
                <th style={{ padding: 10, textAlign: 'center', fontSize: 16 }}>Valor</th>
                <th style={{ padding: 10, textAlign: 'center', fontSize: 16 }}>Data Prot.</th>
              </tr>
            </thead>
            <tbody>
              {protocolos.map((protocolo, index) => (
                <React.Fragment key={protocolo.id}>
                  <tr onClick={() => handleClick(protocolo.id_protocolo)} className="rowTable">
                    <td style={{ padding: 10, textAlign: 'center', fontSize: 14 }}>{protocolo.assunto}</td>
                    <td style={{ padding: 10, textAlign: 'center', fontSize: 14 }}>
                      <div style={{ maxHeight: '50px', overflowY: 'auto' }}>
                        {truncateText(protocolo.descricao, 50)}
                      </div>
                    </td>
                    <td style={{ padding: 10, textAlign: 'center', fontSize: 14 }}>{protocolo.secretaria.nome_secretaria}</td>
                    <td style={{ padding: 10, textAlign: 'center', fontSize: 14 }}>{protocolo.status}</td>
                    <td style={{ padding: 10, textAlign: 'center', fontSize: 14 }}>{'R$ ' + protocolo.valor.toFixed(2)}</td>
                    <td style={{ padding: 10, textAlign: 'center', fontSize: 14 }}>{format(new Date(protocolo.data_protocolo), 'dd/MM/yyyy HH:mm')}</td>
                  </tr>
                  {index < protocolos.length - 1 && <tr><td colSpan="7" style={{ backgroundColor: "#f2f2f2" }}><hr /></td></tr>}
                </React.Fragment>
              ))}
            </tbody>
          </table>
        )}
        {!mostrarTabela && (
          <div style={{ display: "flex", justifyContent: "center", marginTop: 20 }}>
            <button className="btn-log" onClick={handleSubmit} disabled={loading}>
              {loading ? <FiRefreshCcw style={{ marginRight: 8 }} /> : null}
              {loading ? 'Consultando...' : 'Consultar Protocolos'}
            </button>
          </div>
        )}
        <div style={{ display: "flex", justifyContent: "center", marginTop: 20 }}>
          <button className="btn-log" onClick={sendToIndex}>
            <FiArrowLeft style={{ marginRight: 8 }} />
            Voltar
          </button>
        </div>
      </div>)}
    </>
  );
}

export default Consultar;
