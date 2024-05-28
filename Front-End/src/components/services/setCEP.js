import React, { useState, useEffect } from 'react';
import axios from 'axios';

const SetCEP = ({ onEnderecoChange, onCEPChange, onAlertChange }) => {
    const [cep, setCep] = useState('');
    const [alert, setAlert] = useState('')
    const [endereco, setEndereco] = useState({
        logradouro: '',
        bairro: '',
        cidade: '',
        estado: '',
        pais: '',
    });

    const formatCep = (event) => {
        let value = event.target.value;
        value = value.replace(/\D/g, '');
        if (value.length > 5) {
            value = value.substring(0, 5) + '-' + value.substring(5);
        }
        if (value.length > 9) {
            return
        }
        setCep(value);
        onCEPChange(value);
    };

    useEffect(() => {
        if (cep.length === 9) {
            buscarEndereco();
        } else {
            onAlertChange(" ")
        }
    }, [cep]);

    const buscarEndereco = async () => {
        try {
            const response = await axios.get(`https://viacep.com.br/ws/${cep}/json/`);
            const data = response.data;

            if (!data.erro) {
                setEndereco({
                    logradouro: data.logradouro,
                    bairro: data.bairro,
                    cidade: data.localidade,
                    estado: data.uf,
                    pais: 'Brasil',
                });
                onEnderecoChange(data.logradouro, data.bairro, data.localidade, data.uf, data.pais);
                setAlert('');
                onAlertChange('')
            } else {
                setEndereco({
                    logradouro: '',
                    bairro: '',
                    cidade: '',
                    estado: '',
                    pais: '',
                });
                setAlert('CEP não encontrado.');
                onAlertChange('CEP não encontrado.')
            }
        } catch (error) {
            setEndereco({
                logradouro: '',
                bairro: '',
                cidade: '',
                estado: '',
                pais: '',
            });
            setAlert('Erro ao buscar CEP.');
            onAlertChange('Erro ao buscar CEP.')
            setTimeout(() => {
                setAlert('');
            }, 3000)
        }
    };

    return (
        <>
            <input
                type="text"
                name="cep"
                placeholder="Ex.: 08500-000"
                value={cep}
                onChange={formatCep}
                minLength={9}
            />
            <span style={{ color: 'red' }}>{alert}<br></br></span>
        </>
    );
};

export default SetCEP;
