const btnALterarAdm = document.querySelectorAll("#btnALterarAdm");

function atualizarAdmin(id) {

	function fazerGet(url) {
		var request = new XMLHttpRequest()
		request.open("GET", url, false)
		request.send()
		return request.responseText;
	}

	function main() {

		let data = fazerGet("http://localhost:8080/api/administrador/" + id);
		let admin = JSON.parse(data);

		$('#modalUpdateAdmin #idAdm').val(admin.id)
		$('#modalUpdateAdmin #nomeAdm').val(admin.nome)
		$('#modalUpdateAdmin #nifAdm').val(admin.nif)
		$('#modalUpdateAdmin').modal('show')


	}


	main()

}