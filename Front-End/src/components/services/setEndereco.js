import React, { useState, useEffect } from 'react';
import axios from 'axios';

const SetEndereco = ({ cepId }) => {
    const [endereco, setEndereco] = useState({
        rua: '',
        bairro: '',
        cidade: '',
        uf: '',
    });

    const limpaFormularioCep = () => {
        setEndereco({
            rua: '',
            bairro: '',
            cidade: '',
            uf: '',
        });
    };

    useEffect(() => {
        const cepInput = document.getElementById(cepId);

        const handleBlur = async () => {
            const cepValue = cepInput.value.replace(/\D/g, '');

            if (cepValue.length !== 8) {
                limpaFormularioCep();
                alert('Formato de CEP inválido.');
                return;
            }

            try {
                const response = await axios.get(`https://viacep.com.br/ws/${cepValue}/json/`);
                const data = response.data;

                if (!data.erro) {
                    setEndereco({
                        rua: data.logradouro,
                        bairro: data.bairro,
                        cidade: data.localidade,
                        uf: data.uf,
                    });
                } else {
                    limpaFormularioCep();
                    alert('CEP não encontrado.');
                }
            } catch (error) {
                limpaFormularioCep();
                alert('Erro ao buscar CEP.');
            }
        };

        cepInput.addEventListener('blur', handleBlur);

        return () => {
            cepInput.removeEventListener('blur', handleBlur);
        };
    }, [cepId]);

    return null; // ou algum outro componente ou mensagem, dependendo do caso
};

export default SetEndereco;
