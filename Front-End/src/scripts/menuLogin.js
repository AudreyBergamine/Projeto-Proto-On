export function initializeMenu() {
    const menu = document.getElementById('menu');
    if (menu) {
        menu.style.display = 'none';
        document.addEventListener("click", function(){
            if (menu.style.display === 'block' || menu.style.display === ''){
                toggleMenu();
            }
        });

        const cidadao = document.querySelector(".cidadao");
        if (cidadao) {
            cidadao.addEventListener("click", function(e){
                e.stopPropagation();
                toggleMenu();
            });
        }
    }
}

export function toggleMenu() {
    const menu = document.getElementById('menu');
    if (menu) {
        menu.style.display = (menu.style.display === 'block' || menu.style.display === '') ? 'none' : 'block';
    }
}