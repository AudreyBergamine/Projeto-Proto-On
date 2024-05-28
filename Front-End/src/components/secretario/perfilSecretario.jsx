import React, { useState, useEffect } from "react";
import { useNavigate } from "react-router-dom";
import axios from "axios";
import SetCelular from "../services/setCelular";
import SetCEP from "../services/setCEP";
import Loading from '../layouts/Loading';
import Message from '../layouts/Message'
import URL from '../services/url';

//Função de cadastro de municipe
function PerfilSecretario() {
  const navigate = useNavigate();
  const [celularValue, setCelularValue] = useState('');
  const [message, setMessage] = useState()
  const [type, setType] = useState()
  const [endereco, setEndereco] = useState();
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

  const handleAlertChange = (newAlert) => {
    setAlert(newAlert);    
  };

  useEffect(() => {
    setEndereco('num_cep')
  }, []);


  useEffect(() => {
    async function fetchFuncionario() {
      try {
        if (localStorage.getItem('role') === 'SECRETARIO') {
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
          setCelularValue(data.celular);
          const celularDoFuncionario = response1.data.celular; // Obtém o valor do celular do funcionário
          setCelularValue(celularDoFuncionario); // Define o valor do celular no estado
        }
      } catch (error) {
        console.error('Erro ao buscar o protocolo:', error);
      }
    }
    fetchFuncionario();
  }, []);

  const handleSecretariaChange = (e) => {
    const selectedSecretariaId = e.target.value;
  };

 

  const formatValue = (value) => {
    // Converter para minúsculas e manter "de", "da", "do", "das" e "dos" em minúsculo quando estiverem no sepaados da palavra
    return value.toLowerCase().replace(/( de | da | do | das | dos )/g, (match) => match.toLowerCase())
      .replace(/\b(?!de |da |do |das |dos )\w/g, (char) => char.toUpperCase());
  };


  //Esta função tem o propósito de inserir valores nos dados acima, que estão vázios.
  const handleChange = (e) => {
    //A lista abaixo contém o nome de todos os campos que há em endereço
    const enderecoFields = ['tipo_endereco', 'num_cep', 'logradouro', 'nome_endereco', 'num_endereco', 'complemento', 'bairro', 'cidade', 'estado', 'pais'];

    const { name, value } = e.target;

    let formattedValue = formatValue(value);

    if (enderecoFields.includes(name)) {//Caso em um formulário contenha algum nome da lista, então será alterado o valor do objeto endereco
      setFormData({
        ...formData,
        endereco: {
          ...formData.endereco,
          [name]: formattedValue
        }
      });

    } else {
      setFormData({
        ...formData,
        [name]: formattedValue
      });
    }

  }

  const handleEnderecoChange = (logradouro, bairro, cidade, estado, pais) => {
    setFormData({
      ...formData,
      endereco: {
        ...formData.endereco,
        logradouro,
        bairro,
        cidade,
        estado,
        pais: "Brasil"
      }
    });
  };

  const handleChangePais = (pais) => {
    if (/^[a-zA-Z\s]*$/.test(pais)) {
      const formattedPais = formatValue(pais);
      setFormData({
        ...formData,
        endereco: {
          ...formData.endereco,
          pais: formattedPais
        }
      });
    }
  };

 
  const voltarAnterior = async() =>{
    navigate(-1)
  }
  //A função abaixo lida com a conexão com o backend e a requisição de cadastrar um municipe.
  const handleSubmit = async (e) => {
    e.preventDefault();

    if (formData.celular.length < 15) {
      setMessage('Número de celular inválido')
      setType('error')
      setTimeout(() => {
        setMessage('')
      }, 3000)
      return
    }

    if (formData.endereco.num_cep.length < 9) {
      setMessage('CEP inválido')
      setType('error')
      setTimeout(() => {
        setMessage('')
      }, 3000)
      return
    }

    try {
      console.log(formData)
      const response = await axiosInstance.put(`/protoon/funcionarios/bytoken`,formData
    );

      setRemoveLoading(false)

      setTimeout(() => {
        console.log(response.data);
        setRemoveLoading(true)
        setMessage('Atualização dos Dados feita com Sucesso! Redirecionando...')
        setType('success')
        setTimeout(() => {
          navigate("/");
        }, 3000)
      }, 3000)
    } catch (error) {
      setRemoveLoading(false)
      setTimeout(() => {
        setRemoveLoading(true)
        console.error('Erro ao enviar os dados:', error);
        setMessage('Falha ao tentar fazer o Cadastro!')
        setType('error')
      }, 3000)
    }

  };

  const handleChangeCEP = (formattedCEP) => {
    setFormData({
      ...formData,
      endereco: {
        ...formData.endereco,
        num_cep: formattedCEP
      }
    });
  };

  const handleChangeCelular = (formattedCelular) => {
    setFormData({
      ...formData,
      celular: formattedCelular
    });
  };

  //Por fim é retornado o html para ser exibido no front end, junto com as funções acima.
  return (

    <form onSubmit={handleSubmit}>
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
                onChange={handleChange}
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
                onChange={handleChange}
                readOnly
                className='readonly-bg'
                minLength={6}
              />
            </div>

            <div>
              <label>Celular:</label><br></br>
              <SetCelular
              celularValue={celularValue}
                onCelularChange={handleChangeCelular}
                
              />
            </div>

            <div>
              <label>Data de Nascimento:</label><br></br>
              <input
                type="date"
                name="data_nascimento"
                value={formData.data_nascimento}
                onChange={handleChange}
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
              <SetCEP
                onAlertChange={handleAlertChange}
                onEnderecoChange={handleEnderecoChange}
                onCEPChange={handleChangeCEP}
              />
            </div>

            <div>
              <label>Endereço:</label><br></br>
              <input
                type="text"
                name="logradouro"
                placeholder="Ex.: Rua das Flores"
                value={formData.endereco.logradouro}
                onChange={handleChange}
                required
                readOnly={alert === '' ? true : false}
                className={alert === '' ? 'readonly-bg' : ""}
              />
            </div>
            {/* <div>
              <label>Nome Endereço:</label><br></br>
              <input
                type="text"
                name="nome_endereco"
                placeholder="Ex.: Casa"
                value={formData.endereco.nome_endereco}
                onChange={handleChange}
                required
                minLength={3}
              />
            </div> */}
            <div>
              <label>Número: </label><br></br>
              <input
                type="number"
                name="num_endereco"
                placeholder="Ex.: 1025"
                value={formData.endereco.num_endereco}
                onChange={handleChange}
                required
                min={1}
              />
            </div>
            <div>
              <label>Complemento:</label><br></br>
              <input
                type="text"
                name="complemento"
                placeholder="Bloco, apartamento, casa, fundos..."
                value={formData.endereco.complemento}
                onChange={handleChange}
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
                onChange={handleChange}
                minLength={3}
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
                onChange={handleChange}
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
                onChange={handleChange}
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
                onChange={handleChange}
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
                onChange={(e) => handleChangePais(e.target.value)}
                required
                minLength={3}
              />
            </div>
          </div>
        </div>
        {message && <Message type={type} msg={message} />}
        {!removeLoading && <Loading />}
        {removeLoading && <div style={{ marginTop: -30 }}>
          <button type="submit" className="btn-cad" style={{ marginRight: '100px' }}>Atualizar</button>
          <button className="btn-log" onClick={voltarAnterior}>Voltar</button>
        </div>}
      </div>
      <footer className="footer">
        © 2024 Proto-on. Todos os direitos reservados.
      </footer>
    </form>
  );
}

export default PerfilSecretario;
