import React, { useState, useEffect } from 'react';

const SetCelular = ({ celularValue, onCelularChange }) => {

    
    const [celular, setCelular] = useState('');    
    useEffect(() => {
        setCelular(celularValue);
      }, [celularValue]);

    const formatCelular = (event) => {
        let value = event.target.value;
        value = value.replace(/\D/g, '');
        if (value.length > 0) {
            value = '(' + value;
        }
        if (value.length > 3) {
            value = value.substring(0, 3) + ') ' + value.substring(3);
        }
        if (value.length > 10) {
            value = value.substring(0, 10) + '-' + value.substring(10);
        }
        if (value.length > 15) {
            return
        }
        setCelular(value);
        onCelularChange(value);
    };

    return (
        <input
            type="text"
            name="celular"
            placeholder="Ex.: (11)99999-9999"
            value={celular}
            onChange={formatCelular}
            required
            minLength={15}
        />
    );
};

export default SetCelular;
