import React from "react";
import { useNavigate } from "react-router-dom";

function PaginaInicialSecretario() {
  const navigate = useNavigate()
   const handleCadastrarClick = () => {
     navigate("/cadastrar-funcionarios");
   };
   const sendToFuncionarios = async () => {
        navigate("/funcionarios"); // Redirecionar para protocolo
    }

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
        {/* <button className="btn-log" onClick={handleCadastrarClick}>
          Cadastrar Funcionários
        </button> */}
        <button className="btn-log" onClick={handleCadastrarClick}>
          Cadastrar Funcionários
        </button>
        <button className="btn-log" onClick={sendToFuncionarios}>
          Alterar dados de Funcionários
        </button>
      </div>
    </>
  );
}

export default PaginaInicialSecretario;
