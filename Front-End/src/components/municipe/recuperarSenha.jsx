import React, { useState, useEffect } from "react";
import { useNavigate } from "react-router-dom";
import axios from 'axios';
import Loading from '../layouts/Loading';
import Message from '../layouts/Message'
import URL from '../services/url';

function RecuperarSenhaForm() {
  const [message, setMessage] = useState()
  const [type, setType] = useState()
  const [alert, setAlert] = useState('')
  const [emailNull, setEmailNull] = useState(false);
  const [removeLoading, setRemoveLoading] = useState(true)
  const navigate = useNavigate();


  const axiosInstance = axios.create({
    baseURL: URL, // Adjust the base URL as needed
    withCredentials: true, // Set withCredentials to true
  });
    // Recuperar o token do localStorage
const token = localStorage.getItem('token');

// Adicionar o token ao cabeçalho de autorização
axiosInstance.defaults.headers.common['Authorization'] = `Bearer ${token}`;

  const [email, setEmail] = useState('');
  const [formData, setFormData] = useState({
    email: ""
  });

  useEffect(() => {
    setFormData({
      ...formData,
      email: email
    });
  }, [email]);

  const handleSubmit = async (e) => {
    e.preventDefault();

    setMessage('')

    if (!email) {
      setAlert('Por favor, preencha o email!')
      setEmailNull(true)
      setTimeout(() => {
        setEmailNull(false)
        setAlert('');
      }, 3000)
      return
    } else if (!isValidEmail(email)) {
      setAlert('Por favor, insira um email válido!')
      setEmailNull(true)
      setTimeout(() => {
        setEmailNull(false)
        setAlert('');
      }, 3000)
      return
    }

    try {
      const response = await axiosInstance.post(URL + '/protoon/municipe/municipes/recuperarSenha/', {        
          email: email        
      });

      setRemoveLoading(false)
      if(response.data === "Email não encontrado!") {
        setTimeout(() => {
          setRemoveLoading(true)
          setMessage('Email não encontrado.')
          setType('error')
        }, 3000)
      } else {
        setTimeout(() => {
          setRemoveLoading(true)
          navigate('/atualizar-senha');
        }, 3000)
      }
    } catch (error) {
      console.error("Erro ao buscar os usuários:", error);
      setMessage('Erro ao Buscar email!')
      setType('error')
    }
  };

  const isValidEmail = (email) => {
    // Expressão regular para verificar se o email é válido
    const emailPattern = /^[a-zA-Z0-9._%+-]+@[a-zA-Z0-9.-]+\.[a-zA-Z]{2,}$/;
    return emailPattern.test(email);
  };

  return (
    <form onSubmit={handleSubmit}>
      <h1 style={{ marginTop: 150, marginBottom: 50 }}>Digite Seu email para recuperação de Senha</h1>
      <div className="input-container">
        <div className="input-container">
          <label style={{ marginBottom: 15, marginLeft: 10 }}>Email </label>
          {emailNull &&
            <span style={{ color: 'red' }}>{alert}<br></br></span>}
          <input
            type="text"
            name="email"
            placeholder="Insira o email cadastrado"
            value={email}
            onChange={(e) => setEmail(e.target.value.toLowerCase())}
          />
        </div>
      </div>

      {removeLoading && <><button type="submit" className="btn-log">Enviar</button>
      <button className="btn-log" onClick={() => (window.location.href = '/login')}>Voltar</button></>}
      {!removeLoading && <Loading />}
      {message && <Message type={type} msg={message} />}
    </form>
  );
}

export default RecuperarSenhaForm;
