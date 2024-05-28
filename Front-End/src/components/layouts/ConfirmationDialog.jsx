import React from 'react';
import '../../style/ConfirmationDialog.css'

function ConfirmationDialog({ message, onConfirm, onCancel }) {
  return (
    <div className="confirmation-dialog">
      <p>{message}</p>
      <button className="btn-cad" onClick={onConfirm}>Confirmar</button>
      <button className="btn-cad" onClick={onCancel} style={{ marginLeft: 80 }}>Cancelar</button>
    </div>
  );
}

export default ConfirmationDialog;
