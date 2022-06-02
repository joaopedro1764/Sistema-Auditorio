function colocaValor() {
	let token = sessionStorage.getItem("token");
	let payload = parseJwt(token);
	console.log(payload);
	var id = payload.id_usuario;
	console.log(id);
	let element = document.getElementsByClassName('.idU');
	console.log(element)
	element.value = id;
	//document.querySelectorAll('#idUsuario').value = id;
};

function parseJwt(token) {
	var base64Url = token.split('.')[1];
	var base64 = base64Url.replace(/-/g, '+').replace(/_/g, '/');
	var jsonPayload = decodeURIComponent(atob(base64).split('').map(function(c) {
		return '%' + ('00' + c.charCodeAt(0).toString(16)).slice(-2);
	}).join(''));

	return JSON.parse(jsonPayload);
};
colocaValor()