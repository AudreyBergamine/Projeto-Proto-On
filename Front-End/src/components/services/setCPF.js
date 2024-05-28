import React, { useState, useEffect } from 'react';

const SetCPF = ({ onCPFChange, cpfValido }) => {
    const [cpf, setCpf] = useState('');

    const isCpfValido = (strCPF) => {
        var Soma;
        var Resto;
        Soma = 0;

        const cpf = strCPF.replace(/\D/g, ''); // Remove caracteres não numéricos

        if (cpf.length !== 11) return false;

        if (cpf === "00000000000") return false;

        for (let i = 1; i <= 9; i++) 
        Soma += parseInt(cpf.substring(i - 1, i)) * (11 - i);
        Resto = (Soma * 10) % 11;
        
        if ((Resto === 10) || (Resto === 11)) Resto = 0;
        if (Resto !== parseInt(cpf.substring(9, 10))) return false;
        
        Soma = 0;
        for (let i = 1; i <= 10; i++) Soma = Soma + parseInt(cpf.substring(i - 1, i)) * (12 - i);
        Resto = (Soma * 10) % 11;
        
        if ((Resto === 10) || (Resto === 11)) Resto = 0;
        if (Resto !== parseInt(cpf.substring(10, 11))) return false;
        return true;
    };

    const formatCpf = (event) => {
        let value = event.target.value;
        value = value.replace(/\D/g, '');
        if (value.length > 3) {
            value = value.substring(0, 3) + '.' + value.substring(3);
        }
        if (value.length > 7) {
            value = value.substring(0, 7) + '.' + value.substring(7);
        }
        if (value.length > 11) {
            value = value.substring(0, 11) + '-' + value.substring(11);
        }
        if (value.length > 14) {
            return
        }
        setCpf(value);
        onCPFChange(value); // Chama a função de callback com o valor formatado do CPF        
    };

    useEffect(() => {
        if (cpf.length === 14) {
            cpfValido(isCpfValido(cpf));
        }
    }, [cpf]);

    return (
        <>
            <input
                type="text"
                name="cpf"
                placeholder="Ex.: 333.333.333-33"
                value={cpf}
                onChange={formatCpf}
                required
                minLength={14}
            />
            {cpf.length === 14 && !isCpfValido(cpf) ? <span style={{ color: 'red' }}>CPF inválido</span> :
            cpf.length === 14 && <span style={{ color: 'green' }}>CPF válido</span>}
        </>
    );
};

export default SetCPF;
