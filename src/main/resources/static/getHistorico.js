function pegaLinha(evento){
    console.log(evento)
    var linha = document.createElement("div");
    var img = document.createElement("img")
    img.src = ''
    img.classList.add('img')
    var tdTitle = document.createElement("h6");
    tdTitle.classList.add('title')
    var tdStart = document.createElement("p");
    tdStart.classList.add('start')
    var tdEnd = document.createElement("p");
    tdEnd.classList.add('end')

    tdTitle.innerHTML = evento.title
    tdEnd.innerHTML = evento.end
    tdStart.innerHTML = evento.start

    linha.appendChild(img)
    linha.appendChild(tdTitle);
    linha.appendChild(tdStart);
    linha.appendChild(tdEnd);

    return linha;
}

function getHistorico(url){
    let request = new XMLHttpRequest()
    request.open("GET", url, false)
    request.send()
    return request.responseText;
}

function main(){
    
    let data = getHistorico("http://localhost:8080/api/evento")
    let eventos = JSON.parse(data);
    let tabela = document.getElementById("tabela")

    eventos.forEach(element => {
        let linha = pegaLinha(element);
        tabela.appendChild(linha);
    });

    //console.log(eventos)
}
main()