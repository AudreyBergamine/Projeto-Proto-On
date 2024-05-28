import React, { useState, useEffect } from "react";
import { useNavigate } from "react-router-dom";
import axios from "axios";
import Loading from '../layouts/Loading';
import Message from '../layouts/Message'
import URL from '../services/url';

//Função de cadastro de municipe
function PerfilFuncionario() {
  const navigate = useNavigate();
  const [message, setMessage] = useState()
  const [type, setType] = useState()
  const [alert, setAlert] = useState('');
  const [removeLoading, setRemoveLoading] = useState(true)
  const id = localStorage.getItem("id")

  const axiosInstance = axios.create({
    baseURL: URL, // Adjust the base URL as needed
    withCredentials: true, // Set withCredentials to true
  });
  // Recuperar o token do localStorage
  const token = localStorage.getItem('token');

  // Adicionar o token ao cabeçalho de autorização
  axiosInstance.defaults.headers.common['Authorization'] = `Bearer ${token}`;
  //Este campo abaixo é um objeto em json que é enviado ao backend para requisitar o cadastro!
  const [formData, setFormData] = useState({
    nome: "",
    email: "",
    num_CPF: "",
    celular: "",
    data_nascimento: "",
    role: "",
    endereco: {
      tipo_endereco: "",
      num_cep: "",
      logradouro: "",
      nome_endereco: null,
      num_endereco: "",
      complemento: "",
      bairro: "",
      cidade: "",
      estado: "",
      pais: ""
    }
  });

  useEffect(() => {
    async function fetchFuncionario() {
      try {
        const response1 = await axiosInstance.get(`/protoon/funcionarios/bytoken`);
        const data = response1.data;
        setFormData({
          endereco: {
            tipo_endereco: data.endereco.tipo_endereco,
            num_cep: data.endereco.num_cep,
            logradouro: data.endereco.logradouro,
            nome_endereco: data.endereco.nome_endereco,
            num_endereco: data.endereco.num_endereco,
            complemento: data.endereco.complemento,
            bairro: data.endereco.bairro,
            cidade: data.endereco.cidade,
            estado: data.endereco.estado,
            pais: data.endereco.pais
          },
          nome: data.nome,
          email: data.email,
          role: data.role,
          num_CPF: data.num_CPF,
          data_nascimento: data.data_nascimento,
          celular: data.celular,
          numTelefoneFixo: data.numTelefoneFixo
        });
        const celularDoFuncionario = response1.data.celular; // Obtém o valor do celular do funcionário

      } catch (error) {
        console.error('Erro ao buscar o protocolo:', error);
      }
    }
    fetchFuncionario();
  }, []);

  const voltarIndex = async () => {
    navigate("/")
  }

  //Por fim é retornado o html para ser exibido no front end, junto com as funções acima.
  return (

    <form >
      <div style={{ paddingBottom: '50px' }}>

        <h3>Dados Pessoais</h3>
        <div className="register-form">
          <div className="input-container">

            <div>
              <label>Nome:</label><br></br>
              <input
                type="text"
                name="nome"
                placeholder="Ex.: Cláudio Silva"
                value={formData.nome}
                readOnly
                minLength={3}
                className='readonly-bg'
              />
            </div>

            <div>
              <label>Email:</label><br></br>
              <input
                type="email"
                name="email"
                placeholder="Ex.: claudio.silva@gmail.com"
                value={formData.email}
                readOnly
                className='readonly-bg'
                minLength={7}
              />
            </div>
          </div>

          <div className="input-container">
            <div>
              <label>CPF:</label><br></br>
              <input
                type="text"
                name="cpf"
                value={formData.num_CPF}
                readOnly
                className='readonly-bg'
                minLength={6}
              />
            </div>

            <div>
              <label>Celular:</label><br></br>
              <input
                type="text"
                name="logradouro"
                placeholder="Ex.: Rua das Flores"
                value={formData.celular}
                required
                readOnly={alert === '' ? true : false}
                className={alert === '' ? 'readonly-bg' : ""}
              />

            </div>

            <div>
              <label>Data de Nascimento:</label><br></br>
              <input
                type="date"
                name="data_nascimento"
                value={formData.data_nascimento}
                readOnly
                className='readonly-bg'
              />
            </div>
          </div>
        </div>

        <hr></hr>
        <h3>Endereço</h3>
        <div className="register-form">
          <div className="input-container">
            <div>
              <label>Número do CEP:</label><br></br>
              <input
                type="text"
                name="cep"
                value={formData.endereco.num_cep}
                required
                readOnly={alert === '' ? true : false}
                className={alert === '' ? 'readonly-bg' : ""}
              />
            </div>
            <div>
              <label>Endereço:</label><br></br>
              <input
                type="text"
                name="logradouro"
                placeholder="Ex.: Rua das Flores"
                value={formData.endereco.logradouro}
                required
                readOnly={alert === '' ? true : false}
                className={alert === '' ? 'readonly-bg' : ""}
              />
            </div>
            <div>
              <label>Número:</label><br></br>
              <input
                type="number"
                name="num_endereco"
                placeholder="Ex.: 1025"
                value={formData.endereco.num_endereco}
                required
                min={1}
                readOnly={alert === '' ? true : false}
                className={alert === '' ? 'readonly-bg' : ""}
              />
            </div>
            <div>
              <label>Complemento:</label><br></br>
              <input
                type="text"
                name="complemento"
                placeholder="Bloco, apartamento, casa, fundos..."
                value={formData.endereco.complemento}
                readOnly={alert === '' ? true : false}
                className={alert === '' ? 'readonly-bg' : ""}
              />
            </div>
          </div>
        </div>

        <div className="register-form">
          <div className="input-container">

            <div>
              <label>Tipo de Endereço:</label><br></br>
              <input
                type="text"
                name="tipo_endereco"
                placeholder="Ex.: casa, apartamento"
                value={formData.endereco.tipo_endereco}
                minLength={3}
                readOnly={alert === '' ? true : false}
                className={alert === '' ? 'readonly-bg' : ""}
                required
              />
            </div>
            <div>
              <label>Bairro:</label><br></br>
              <input
                type="text"
                name="bairro"
                placeholder="Ex.: Bairro das Flores"
                value={formData.endereco.bairro}
                required
                minLength={2}
                readOnly={alert === '' ? true : false}
                className={alert === '' ? 'readonly-bg' : ""}
              />
            </div>
            <div>
              <label>Cidade:</label><br></br>
              <input
                type="text"
                name="cidade"
                placeholder="Ex.: Ferraz de Vasconcelos"
                value={formData.endereco.cidade}
                required
                minLength={2}
                readOnly={alert === '' ? true : false}
                className={alert === '' ? 'readonly-bg' : ""}
              />
            </div>
            <div>
              <label>Estado:</label><br></br>
              <input
                type="text"
                name="estado"
                placeholder="Ex.: São Paulo"
                value={formData.endereco.estado}
                required
                minLength={2}
                readOnly={alert === '' ? true : false}
                className={alert === '' ? 'readonly-bg' : ""}
              />
            </div>
            <div>
              <label>País:</label><br></br>
              <input
                type="text"
                name="pais"
                placeholder="Ex.: Brasil"
                value={formData.endereco.pais ? formData.endereco.pais : ""}
                readOnly={alert === '' ? true : false}
                className={alert === '' ? 'readonly-bg' : ""}
                required
                minLength={3}
              />
            </div>
          </div>
        </div>
        {message && <Message type={type} msg={message} />}
        {!removeLoading && <Loading />}
        {removeLoading && <div style={{ marginTop: -30 }}>
          <button className="btn-log" onClick={voltarIndex}>Voltar</button>
        </div>}
      </div>
      <footer className="footer">
        © 2024 Proto-on. Todos os direitos reservados.
      </footer>
    </form>
  );
}

export default PerfilFuncionario;
