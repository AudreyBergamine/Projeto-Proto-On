import axios from "axios";
import React, { useState, useEffect } from "react";
import { useNavigate } from "react-router-dom";
import URL from '../services/url';
import styled from 'styled-components';

const Container = styled.div`
  padding: 20px;
`;

const Header = styled.div`
  margin-bottom: 20px;
`;

const StatusSelect = styled.select`
  padding: 5px;
  font-size: 14px;
  border-radius: 5px;
`;

const DateHeader = styled.div`
  cursor: pointer;
  background: #f0f0f0;
  padding: 10px;
  margin: 10px 0;
  border-radius: 5px;
`;

const RedirectionList = styled.ul`
  list-style: none;
  padding: 0;
`;

const RedirectionItem = styled.li`
  border: 1px solid #ddd;
  padding: 10px;
  margin: 5px 0;
  border-radius: 5px;
`;

const Checkbox = styled.input`
  margin-right: 25px;
  width: 15px; 
  height: 15px; 
`;

const Select = styled.select`
  padding: 10px;
  margin-right: 20px;
  font-size: 14px;
  border-radius: 5px;
`;

const Button = styled.button`
  --clr-font-main: hsla(0 0% 20% / 100);
  --btn-bg-1: #9AD0C2;
  --btn-bg-2: #2D9596;
  --btn-bg-color: hsla(360 100% 100% / 1);
  --radii: 0.5em;
  cursor: pointer;
  padding: 0.9em 1.4em;
  min-width: 120px;
  min-height: 44px;
  font-family: "Encode Sans Expanded", sans-serif;
  font-weight: 500;
  font-style: normal;
  transition: 0.8s;
  background-size: 280% auto;
  background-image: linear-gradient(325deg, var(--btn-bg-2) 0%, var(--btn-bg-1) 55%, var(--btn-bg-2) 90%);
  border: none;
  border-radius: var(--radii);
  color: var(--btn-bg-color);
  box-shadow: 0px 0px 20px rgb(38 175 114 / 50%), 0px 5px 5px -1px rgb(7 56 4 / 25%), inset 4px 4px 8px rgb(89 188 177 / 50%), inset -4px -4px 8px rgba(19, 95, 216, 0.35);
  
  &:hover {
    background-position: right top;
  }

  &:focus, &:focus-visible, &:active {
    outline: none;
    box-shadow: 0 0 0 3px var(--btn-bg-color), 0 0 0 6px var(--btn-bg-2);
  }
`;

const SecretariaSelect = styled.select`
  padding: 10px;
  margin-top: 10px;
  margin-left:20px;
  font-size: 14px;
  border-radius: 5px;
`;

const RedirecionamentosCoordenador = () => {
  const [redirecionamentos, setRedirecionamentos] = useState({});
  const [expandedDates, setExpandedDates] = useState({});
  const [selectedStatus, setSelectedStatus] = useState('ANDAMENTO');
  const [selectedRedirecionamentos, setSelectedRedirecionamentos] = useState({});
  const [secretarias, setSecretarias] = useState([]);
  const [idSecretariaSelecionada, setIdSecretariaSelecionada] = useState("");

  const axiosInstance = axios.create({
    baseURL: URL,
    withCredentials: true,
  });

  const token = localStorage.getItem('token');
  axiosInstance.defaults.headers.common['Authorization'] = `Bearer ${token}`;

  useEffect(() => {
    async function fetchRedirecionamentos() {
      try {
        const response1 = await axiosInstance.get('/protoon/secretaria');
        setSecretarias(response1.data);

        const response2 = await axiosInstance.get(`/protoon/redirecionamento`);
        const groupedData = groupByStatusAndDate(response2.data);
        setRedirecionamentos(groupedData);
      } catch (error) {
        console.error('Erro ao buscar os redirecionamentos:', error);
      }
    }
    fetchRedirecionamentos();
  }, []);

  const handleSecretariaChange = (e) => {
    setIdSecretariaSelecionada(e.target.value);
  };

  const groupByStatusAndDate = (redirecionamentos) => {
    return redirecionamentos.reduce((groups, redirecionamento) => {
      const status = redirecionamento.statusRedirecionamento;
      const date = redirecionamento.dtSolicitacao.split('T')[0];
      if (!groups[status]) {
        groups[status] = {};
      }
      if (!groups[status][date]) {
        groups[status][date] = [];
      }
      groups[status][date].push(redirecionamento);
      return groups;
    }, {});
  };

  const toggleExpand = (date) => {
    setExpandedDates((prevExpandedDates) => ({
      ...prevExpandedDates,
      [date]: !prevExpandedDates[date],
    }));
  };

  const handleCheckboxChange = (id, checked) => {
    setSelectedRedirecionamentos((prevSelected) => {
      if (checked) {
        return { ...prevSelected, [id]: '' };
      } else {
        const updatedSelected = { ...prevSelected };
        delete updatedSelected[id];
        return updatedSelected;
      }
    });
  };

  const handleStatusChange = (id, status) => {
    setSelectedRedirecionamentos((prevSelected) => ({
      ...prevSelected,
      [id]: status,
    }));
  };

  const handleUpdateStatus = async (id) => {
    try {
      const status = selectedRedirecionamentos[id];
      if (status) {
        const response1 = await axiosInstance.put(`/protoon/redirecionamento/by-coordenador/${id}`, {
          statusRedirecionamento: status,
        });
        alert('Status atualizado com sucesso!');
        if (response1.data.statusRedirecionamento === "APROVADO") {
          const protocolo = response1.data.protocolo;
          const response2 = await axiosInstance.get(`/protoon/secretaria/${idSecretariaSelecionada}`);
          const secretariaData = response2.data;
          await axiosInstance.put(`/protoon/protocolo/alterar-protocolos/departamento/${response1.data.protocolo.numero_protocolo}`, {
            ...protocolo,
            secretaria: secretariaData,
          });
        }

        const response4 = await axiosInstance.get(`/protoon/redirecionamento`);
        const groupedData = groupByStatusAndDate(response4.data);
        setRedirecionamentos(groupedData);
      } else {
        alert('Selecione um status para atualizar.');
      }
    } catch (error) {
      console.error('Erro ao atualizar o redirecionamento:', error.response ? error.response.data : error);
      alert(error.response ? error.response.data.message : 'Erro ao atualizar o redirecionamento');
    }
  };

  return (
    <Container>
      <Header>
        <label htmlFor="status" >Selecionar Status</label>
        <br />
        <StatusSelect id="status" value={selectedStatus} onChange={(e) => setSelectedStatus(e.target.value)}>
          <option value="ANDAMENTO">ANDAMENTO</option>
          <option value="APROVADO">APROVADO</option>
          <option value="RECUSADO">RECUSADO</option>
        </StatusSelect>
      </Header>

      {redirecionamentos[selectedStatus] && Object.keys(redirecionamentos[selectedStatus]).map((date) => (
  <div key={date}>
    <DateHeader onClick={() => toggleExpand(date)}>
      <strong>{date}</strong>
    </DateHeader>
    {expandedDates[date] && (
      <RedirectionList>
        {redirecionamentos[selectedStatus][date].map((redirecionamento) => (
          <RedirectionItem key={redirecionamento.id}>
            {redirecionamento.statusRedirecionamento === "ANDAMENTO" && (
              <div>
                <Checkbox
                  type="checkbox"
                  onChange={(e) => handleCheckboxChange(redirecionamento.id, e.target.checked)}
                />
                <Select
                  value={selectedRedirecionamentos[redirecionamento.id] || ""}
                  onChange={(e) => handleStatusChange(redirecionamento.id, e.target.value)}
                >
                  <option value="">Selecione</option>
                  <option value="APROVADO">APROVADO</option>
                  <option value="RECUSADO">RECUSADO</option>
                </Select>
                <Button onClick={() => handleUpdateStatus(redirecionamento.id)}>Atualizar Status</Button>
                <SecretariaSelect
                  value={idSecretariaSelecionada}
                  onChange={handleSecretariaChange}
                >
                  <option value="">Selecione a secretaria</option>
                  {secretarias.map(secretaria => (
                    <option key={secretaria.id_secretaria} value={secretaria.id_secretaria}>
                      {secretaria.nome_secretaria}
                    </option>
                  ))}
                </SecretariaSelect>
              </div>
            )}
            <p><strong>Status:</strong> {redirecionamento.statusRedirecionamento}</p>
            <p><strong>Descrição:</strong> {redirecionamento.descricao}</p>
            <p><strong>Nova Secretaria:</strong> {redirecionamento.novaSecretaria || 'N/A'}</p>
            <p><strong>Data de Solicitação:</strong> {new Date(redirecionamento.dtSolicitacao).toLocaleString()}</p>
            {redirecionamento.dtConfirmacao && <p><strong>Data de Confirmação:</strong> {new Date(redirecionamento.dtConfirmacao).toLocaleString()}</p>}
          </RedirectionItem>
        ))}
      </RedirectionList>
    )}
  </div>
      ))}
    </Container>
  );
};

export default RedirecionamentosCoordenador;
