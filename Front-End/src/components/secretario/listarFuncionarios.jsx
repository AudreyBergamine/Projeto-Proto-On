import axios from "axios";
import React, { useState, useEffect } from "react";
import { useNavigate } from "react-router-dom";
import URL from '../services/url';

function ListarFuncionarios() {
  const axiosInstance = axios.create({
    baseURL: URL, // Adjust the base URL as needed
    withCredentials: true, // Set withCredentials to true
  });

      // Recuperar o token do localStorage
const token = localStorage.getItem('token');

// Adicionar o token ao cabeçalho de autorização
axiosInstance.defaults.headers.common['Authorization'] = `Bearer ${token}`;

  // const [idSecretariaSelecionada, setIdSecretariaSelecionada] = useState("");
  const [funcionarios, setFuncionarios] = useState([]);
  const [pesquisarFun, setPesquisarFun] = useState(''); //Pesquisar funcionários
  const navigate = useNavigate(); // Use o hook useNavigation para acessar a navegação
 
  useEffect(() => {
    async function fetchFuncionarios() {
      try {
        const response1 = await axiosInstance.get('/protoon/funcionarios');
        // Filtrar os funcionários com as roles desejadas
        const funcionariosFiltrados = response1.data.filter(funcionario => {
          return funcionario.role === "FUNCIONARIO" || funcionario.role === "COORDENADOR";
        });
        setFuncionarios(funcionariosFiltrados);
      } catch (error) {
        console.error('Erro ao buscar os funcionários:', error);
      }
    }
    fetchFuncionarios();
  }, []);

  const handleClick = (id) => {
    // Redirecionar para outra página com o ID do protocolo na URL usando navigate
    navigate(`/funcionario/${id}`);
  };
  
  const voltarIndex = async() =>{
    navigate("/")
  }

  //Função para formatar a data e a hora com base no Brasil/sp
  const formatarDataHora = (dataString) => {
    const data = new Date(dataString);
    const options = {
      day: "2-digit",
      month: "2-digit",
      year: "numeric",
      timeZone: "America/Sao_Paulo"
    };
    return data.toLocaleString("pt-BR", options);
  };

  // Função para filtrar os funcionarios com base no nome do funcionário
  const filteredFuncionarios = funcionarios.filter((funcionario) => {
    return funcionario.nome.toLowerCase().includes(pesquisarFun.toLowerCase());
  });

  return (
    <>
    <div style={{ padding: 20 }}>
      <h1>Lista de Funcionários</h1>
      <input
        type="text"
        placeholder="Pesquisar por nome de funcionário..."
        value={pesquisarFun}
        onChange={(e) => setPesquisarFun(e.target.value)}
      />
      <table style={{ margin: 'auto', borderCollapse: 'collapse', width: '100%', padding: 30 }}>
        <thead>
          <tr>
            <th>Nome</th>
            <th>Email</th>
            <th>Cargo</th>
            <th>CPF</th>
            <th>Data de Nascimento</th>
            <th>Celular</th>
            <th>Telefone Fixo</th>
          </tr>
        </thead>
        <div style={{ marginTop: 30 }}></div>
        <tbody>
          {filteredFuncionarios.map((funcionario, index) => (
            <React.Fragment key={funcionario.id} >              
              <tr onClick={() => handleClick(funcionario.id)} className="rowTable">
                <td style={{ textAlign: 'center', minWidth: 300 }}>{funcionario.nome}</td>
                <td style={{ textAlign: 'center', minWidth: 200 }}>{funcionario.email}</td>
                <td style={{ textAlign: 'center', minWidth: 200 }}>{funcionario.role}</td>
                <td style={{ textAlign: 'center', minWidth: 200 }}>{funcionario.num_CPF}</td>
                <td style={{ textAlign: 'center', minWidth: 200 }}>{formatarDataHora(funcionario.data_nascimento)}</td>
                <td style={{ textAlign: 'center', minWidth: 200 }}>{funcionario.celular}</td>
                <td style={{ textAlign: 'center', minWidth: 200 }}>{funcionario.numTelefoneFixo}</td>
                <td style={{ textAlign: 'center', minWidth: 250, maxWidth: 450, wordWrap: 'break-word' }}>
                  <div style={{ maxHeight: '50px', overflowY: 'auto' }}>
                    {funcionario.descricao}
                  </div>
                </td>
              </tr>
              {index !== filteredFuncionarios.length - 1 && <tr><td colSpan="6"><hr style={{ margin: 0 }} /></td></tr>}
            </React.Fragment>
          ))}
        </tbody>
      </table>
      <button className="btn-log" onClick={voltarIndex}>Voltar</button>

    </div >
    </>
  );
}

export default ListarFuncionarios