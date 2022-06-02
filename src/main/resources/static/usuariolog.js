
//function colocaValor() {

// token = sessionStorage.getItem("token")
//payload = parseJwt(token)
//console.log(payload)
//var element = document.getElementById('idUSuario')
//element.innerHTML = payload.id
//};

function logarUsuario() {

	console.log("entrou")
	let url = "http://localhost:8080/api/usuario/login";
	let senha = document.getElementById("senha").value;
	let nif = document.getElementById("nif").value;

	console.log(senha)
	console.log(nif)
	console.log(url)

	var usuario = {
		nif,
		senha
	}

	console.log(usuario)

	const myHeaders = new Headers()
	myHeaders.append("Content-Type", "application/json")

	let fetchData = {
		method: 'POST',
		body: JSON.stringify(usuario),
		headers: myHeaders
	}

	console.log('passou aqui')

	fetch(url, fetchData).then((resp) => {
		console.log("AAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAA")
		console.log(resp)
		resp.json().then((resposta) => {
			console.log(resposta)

			// tentou deu certo
			sessionStorage.setItem("token", resposta.token)
			console.log(resposta.token)

			// console.log(sessionStorage)

			window.location.href = '/painelReserva'
		})

			.catch((e) => {
				// le o erro
				console.log(e)
			})
	})

	//location.reload();
	// fazPost(url, body)
};


function parseJwt(token) {
	var base64Url = token.split('.')[1];
	var base64 = base64Url.replace(/-/g, '+').replace(/_/g, '/');
	var jsonPayload = decodeURIComponent(atob(base64).split('').map(function(c) {
		return '%' + ('00' + c.charCodeAt(0).toString(16)).slice(-2);
	}).join(''));

	return JSON.parse(jsonPayload);
};

//logarUsuario()
