import axios from "axios";
import { useState, useEffect } from "react";
import Loading from '../layouts/Loading';
import { useParams } from "react-router-dom";

function TodasDevolutivas() {
  const baseURL = 'http://localhost:8080';
  const axiosInstance = axios.create({
    baseURL: baseURL,
    withCredentials: true,
  });

  const { id } = useParams();
  const [devolutivas, setDevolutivas] = useState([]);
  const [loading, setLoading] = useState(true);
  const [allDevolutivasLoaded, setAllDevolutivasLoaded] = useState(false);

  useEffect(() => {
    async function fetchDevolutivas() {
      try {
        const devolutivasResponse = await axiosInstance.get(`/protoon/devolutiva/devolutiva-protocolo/${id}`);
        setDevolutivas(devolutivasResponse.data);
        setLoading(false);
        setAllDevolutivasLoaded(true);
      } catch (error) {
        console.error('Erro ao buscar as devolutivas:', error);
      }
    }

    fetchDevolutivas();
  }, [id]);

  return (
    <>
      <h1 style={{ textAlign: 'center', marginTop: '20px' }}>Todas as Devolutivas</h1>
      {loading && <Loading />}
      {!loading && (
        <div style={{ display: 'flex', flexDirection: 'column', alignItems: 'center' }}>
          {devolutivas.map(devolutiva => (
            <div key={devolutiva.id} style={{ border: '1px solid #ddd', backgroundColor: '#d0d0d0', padding: '10px', borderRadius: '5px', marginTop: '20px', width: '50%', textAlign: 'left' }}>
              <p style={{ fontWeight: 'bold' }}>Data e Hora: {devolutiva.momento_devolutiva}</p>
              {devolutiva.id_funcionario ? (
                <>
                  <p style={{ fontWeight: 'bold' }}>Funcionário: {devolutiva.id_funcionario.nome}</p>
                  <p style={{ fontWeight: 'bold' }}>Secretaria: {devolutiva.id_funcionario.secretaria.nome_secretaria}</p>
                </>
              ) : (
                <p style={{ fontWeight: 'bold' }}>Nome do Funcionário: Não especificado</p>
              )}
              <div style={{ backgroundColor: '#f5f5f5', padding: '10px', borderRadius: '5px', marginTop: '10px' }}>
              <p style={{ textAlign: 'justify' }}>Descrição: {devolutiva.devolutiva}</p>
              </div>
            </div>
          ))}
          {allDevolutivasLoaded && <p style={{ textAlign: 'center' }}>Todas as devolutivas foram carregadas.</p>}
        </div>
      )}

    </>
  );
}

export default TodasDevolutivas;
