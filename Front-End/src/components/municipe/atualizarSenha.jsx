import React, { useState, useEffect } from "react";
import { useParams, useNavigate } from "react-router-dom";
import axios from 'axios';
import Loading from '../layouts/Loading';
import Message from '../layouts/Message'
import URL from '../services/url';

function AtualizarForm() {
  const [message, setMessage] = useState()
  const [type, setType] = useState()
  const [removeLoading, setRemoveLoading] = useState(true)
  const { username } = useParams();
  const navigate = useNavigate();
  const [formData, setFormData] = useState({
    email: "",
    codigo: "",
    senha: ""
  });

  useEffect(() => {
    setFormData({
      ...formData,
      username: username
    });
  }, [username]);

  const handleChange = (e) => {
    setFormData({
      ...formData,
      [e.target.name]: e.target.value
    });
  };

  const handleSubmit = async (e) => {
    e.preventDefault();
    try {
      const response = await axios.post(URL + '/protoon/municipe/municipes/recuperarSenha/novaSenha', {
        email: formData.email,
        codigo: formData.codigo,
        senha: formData.senha
      })
      setRemoveLoading(false)
      if (response.data === "Senha alterada com sucesso!") {
        setTimeout(() => {
          setRemoveLoading(true)
          setMessage(response.data)
          setType("success")
          setTimeout(() => {
            navigate('/login');
          }, 3000)
        }, 3000)
      } else {
        setTimeout(() => {
          setRemoveLoading(true)
          setMessage(response.data)
          setType("error")
        }, 3000)
        setMessage("")
      }
    } catch (error) {
      setMessage("Erro ao atualizar a senha!")
      setType("error")
      console.log(error)
    }
  };

  return (
    <form onSubmit={handleSubmit}>
      <h1>Atualizar Usuário</h1>
      <div className="input-container">
        <div className="input-container">
          <label style={{ marginBottom: 5, marginLeft: 10 }}>Email</label>
          <input
            type="text"
            name="email"
            value={formData.email}
            onChange={handleChange}
          />
        </div>
      </div>
      <div className="input-container">
        <div className="input-container">
          <label style={{ marginBottom: 5, marginLeft: 10 }}>Código</label>
          <input
            type="text"
            name="codigo"
            value={formData.codigo}
            onChange={handleChange}
          />
        </div>
      </div>
      <div className="input-container">
        <div className="input-container">
          <label style={{ marginBottom: 5, marginLeft: 10 }}>Senha</label>
          <input
            type="password"
            name="senha"
            value={formData.password}
            onChange={handleChange}
          />
        </div>

      </div>
      {!removeLoading && <Loading />}
      {message && <Message type={type} msg={message} />}
      {removeLoading && <><button type="submit" className="btn-log">Enviar</button>
      <button className="btn-log" onClick={() => (window.location.href = '/recuperarSenha')}>Voltar</button></>}
    </form>
  );
}

export default AtualizarForm;
