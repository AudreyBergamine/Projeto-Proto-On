function toastEditar() {
    const toast = document.getElementById('toastIdEditar');
    toast.classList.add('mostrar');
  
    setTimeout(function () {
      toast.classList.remove('mostrar');
    }, 5000);
  }