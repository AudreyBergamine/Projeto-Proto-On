import React from "react";
import { useNavigate } from "react-router-dom";

function PaginaInicialCoordenador() {

  const navigate = useNavigate();

  return (
    <>
      <div
        style={{
          display: "flex",
          flexDirection: "column",
          width: 200,
          alignItems: "center",
          margin: "auto",
          justifyContent: "space-between",
          height: "40vh",
          padding: 100,
        }}
      >
        <button className="btn-log" onClick={() => (navigate('/protocolos'))}>
          Listar Protocolos
        </button>
        <button className="btn-log" onClick={() => (navigate('/redirecionamentos-coordenador'))}>
          Aprovar Redirecionamentos
        </button>
        <button className="btn-log" onClick={() => (navigate('/logs'))}>
          Logs
        </button>
      </div>
    </>
  );
}

export default PaginaInicialCoordenador;
