const btAtualizar = document.querySelectorAll("#btnALterar");

function atualizar(id) {

	function fazerGet(url) {
		var request = new XMLHttpRequest()
		request.open("GET", url, false)
		request.send()
		return request.responseText;
	}

	function main() {

		let data = fazerGet("http://10.92.198.11:8080/api/usuario/" + id);
		// variavel para acessar os atributos
		let usuario = JSON.parse(data);

		$('#modalUpdateUsuario #idUsuario').val(usuario.id)
		$('#modalUpdateUsuario #nomeUsuario').val(usuario.nome)
		$('#modalUpdateUsuario #nifUsuario').val(usuario.nif)
		$('#modalUpdateUsuario #tipo2').val(usuario.tipo)
		console.log(usuario.tipo)
		$('#modalUpdateUsuario').modal('show')

	}

	main()
}