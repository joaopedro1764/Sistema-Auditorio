const btAtualizar = document.querySelectorAll("#btnALterar");

function atualizar(id) {

	function fazerGet(url) {
		var request = new XMLHttpRequest()
		request.open("GET", url, false)
		request.send()
		return request.responseText;
	}

	function main() {

		let data = fazerGet("http://localhost:8080/api/usuario/" + id);
		let usuario = JSON.parse(data);

		$('#modalUpdateUsuario #idUsuario').val(usuario.id)
		$('#modalUpdateUsuario #nomeUsuario').val(usuario.nome)
		$('#modalUpdateUsuario #nifUsuario').val(usuario.nif)
		$('#modalUpdateUsuario').modal('show')

	}

	main()
}