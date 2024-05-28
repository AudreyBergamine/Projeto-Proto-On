import { useNavigate } from "react-router-dom";



function PaginaInicialMunicipe() {
  const navigate = useNavigate()
  
  const sendToReclamar = async()=>{
    navigate("/reclamar")
  }
  const sendToConsultar = async()=>{
    navigate("/consultar")
  }
  const sendToRetornadas = async()=>{
    navigate("/retornadas")
  }
  return (
    <>
      <div style={{ display: 'flex', flexDirection: 'column', width: 200, alignItems: 'center', margin: 'auto', justifyContent: 'space-between', height: '40vh', padding: 100 }}>
        <button className="btn-log" onClick={sendToReclamar}>Abrir uma Reclamação</button>
        <button className="btn-log" onClick={sendToConsultar}>Consultar Reclamações</button>
        {/* <button className="btn-log" onClick={sendToRetornadas}>Reclamações Retornadas</button> */}
      </div>
    </>
  );
}

export default PaginaInicialMunicipe;
