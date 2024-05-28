import React, { useState } from 'react';
import URL from '../services/url';
import axios from 'axios';
import { useNavigate, Link } from "react-router-dom";
import Loading from '../layouts/Loading';
import Message from '../layouts/Message'

const LoginForm = () => {
  const navigate = useNavigate();
  const [message, setMessage] = useState('')
  const [alert, setAlert] = useState('')
  const [type, setType] = useState()
  const [removeLoading, setRemoveLoading] = useState(true)

  const [email, setEmail] = useState('');
  const [senha, setSenha] = useState('');
  const [senhaFocused, setSenhaFocused] = useState(false);
  const [senhaNull, setSenhaNull] = useState(false);
  const [errorMessage, setErrorMessage] = useState('');
  const axiosInstance = axios.create({
    baseURL: URL, 
    withCredentials: true, 
  });

  const sendToCadastrar = async () => {
    navigate("/cadastrar-municipe"); 
}

  const handleLogin = async () => {

    if (email.trim() === '') {
      setSenhaFocused(true)
      setTimeout(() => {
        setSenhaFocused(false)
        setMessage('');
      }, 3000)
      return
    } else if (!isValidEmail(email)) {
      setSenhaFocused(true)
      setTimeout(() => {
        setSenhaFocused(false)
        setMessage('');
      }, 3000)
      return
    }

    if (senha.trim() === '') {
      setSenhaNull(true)
      setAlert("Preencha a Senha")
      setTimeout(() => {
        setSenhaNull(false)
      }, 3000)
      return
    } else if (senha.length < 6) {
      setSenhaNull(true)
      setAlert("Senha deve conter no minimo 6 digitos")
      setTimeout(() => {
        setSenhaNull(false)
      }, 3000)
      return
    }

    try {
      const response = await axiosInstance.post('/protoon/auth/authenticate', {
        email: email,
        senha: senha
      });

      setRemoveLoading(false)
 
      
      localStorage.setItem("role", response.data.role);
      localStorage.setItem("token", response.data.access_token)
      
      setTimeout(() => {
        console.log('Authentication successful:', response.data);
        // If authentication is successful, you can handle the response as needed
        window.location.href = '/'; // Redirecionar para a página inicial após o login
        // Optionally, you can redirect the user to another page upon successful authentication
        // history.push('/dashboard');
        setRemoveLoading(true)
      }, 3000)
    } catch (error) {
      // Handle authentication errors
      console.error('Authentication error:', error.response.data);
      setErrorMessage(error.response.data.message); // Set error message to display to the user
      setRemoveLoading(false)
      setTimeout(() => {
        setRemoveLoading(true)
        setMessage('Credenciais Inválidas!')
        setTimeout(() => {
          setMessage('')
        }, 3000)
        setType('error')
      }, 3000)
    }
  };

  const isValidEmail = (email) => {
    // Expressão regular para verificar se o email é válido
    const emailPattern = /^[a-zA-Z0-9._%+-]+@[a-zA-Z0-9.-]+\.[a-zA-Z]{2,}$/;
    return emailPattern.test(email);
  };

  return (
    <div>
      <h1>Login</h1>
      <div>
        {senhaFocused && email.trim() === '' ? <span style={{ color: 'red' }}>Preencha o Email<br></br></span> :
          senhaFocused && !isValidEmail(email) && <span style={{ color: 'red' }}>Email Inválido<br></br></span>}

        <input
          type="email"
          placeholder="Email"
          value={email}
          onChange={(e) => setEmail(e.target.value.toLowerCase())}
        />
      </div>
      <div>
        {senhaNull &&
          <span style={{ color: 'red' }}>{alert}<br></br></span>}
        <input
          type="password"
          placeholder="Senha"
          value={senha}
          onChange={(e) => setSenha(e.target.value)}
          onFocus={() => setSenhaFocused(true)}
          onBlur={() => setSenhaFocused(false)}
        />
      </div>
      <Link to='/recuperar-senha' style={{ textDecoration: 'none' }}>Esqueceu a Senha?</Link>
      <div style={{ marginBottom: 30 }}>
        {!removeLoading && <Loading />}
        {message && <Message type={type} msg={message} />}
        {removeLoading && <><button onClick={handleLogin} className="btn-cad" style={{ marginRight: '100px' }}>Logar</button>
        <button className="btn-log" onClick={sendToCadastrar}>Criar Conta</button></>}
      </div>
      {errorMessage && <p>{errorMessage}</p>}
    </div>
  );
};

export default LoginForm;