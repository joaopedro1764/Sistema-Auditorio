function pegaLinha(evento) {
	console.log(evento)
	var linha = document.createElement("div");
	var img = document.createElement("img")
	img.src = evento.fotos
	img.classList.add('img')
	var tdTitle = document.createElement("h6");
	tdTitle.classList.add('title')
	var tdStart = document.createElement("p");
	tdStart.classList.add('start')
	var tdEnd = document.createElement("p");
	tdEnd.classList.add('end')
	var btn = document.createElement('input');
	var idput = document.createElement('input');
	var botao = document.createElement('button');

	var form = document.createElement("form");
	form.setAttribute("method", "post");
	form.setAttribute("action", "/salvarHistorico");
	form.setAttribute("enctype", "multipart/form-data");


	btn.setAttribute('type', 'file');
	btn.setAttribute('accept', 'image/*');
	btn.id = "fotos";
	btn.name = "fileFotos";


	idput.setAttribute('type', 'hidden');
	idput.name = "idEvento";
	idput.id = "idEvento";

	botao.innerText = "SALVAR";
	botao.setAttribute('type', 'submit');

	tdTitle.innerHTML = evento.title
	tdEnd.innerHTML = evento.end
	tdStart.innerHTML = evento.start
	idput.value = evento.id
	img.value = evento.fotos

	linha.appendChild(tdTitle);
	linha.appendChild(img);
	linha.appendChild(tdStart);
	linha.appendChild(tdEnd);
	form.appendChild(idput);
	form.appendChild(btn);
	linha.appendChild(form);

	form.appendChild(botao);

	return linha;
}

function getHistorico(url) {
	let request = new XMLHttpRequest()
	request.open("GET", url, false)
	request.send()
	return request.responseText;
}

function main() {

	let data = getHistorico("http://10.92.198.11:8080/api/evento")
	let eventos = JSON.parse(data);
	let tabela = document.getElementById("tabela")

	eventos.forEach(element => {
		let linha = pegaLinha(element);
		tabela.appendChild(linha);
	});

	//console.log(eventos)
}
main()