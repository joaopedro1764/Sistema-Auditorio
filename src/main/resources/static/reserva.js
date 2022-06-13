
document.addEventListener('DOMContentLoaded', function() {



	var initialLocaleCode = 'pt-br';
	var calendarEl = document.getElementById('calendar');

	var today = new Date().toISOString().slice(0, 10);
	var calendar = new FullCalendar.Calendar(calendarEl, {

		timeZone: 'UTC', // the default (unnecessary to specify)
		events: [
			{ start: '2018-09-01T12:30:00Z' }, // will be shifted to local
			{ start: '2018-09-01T12:30:00+XX:XX' }, // already same offset as local, so won't shift
			{ start: '2018-09-01T12:30:00' } // will be parsed as if it were '2018-09-01T12:30:00+XX:XX'
		],

		locale: initialLocaleCode,
		headerToolbar: {
			left: 'prev,next today',
			center: 'title',
			right: 'dayGridMonth,timeGridWeek,timeGridDay'
		},

		// deixa domingo disable
		hiddenDays: [0],
		dayMaxEventRows: 4,
		navLinks: true, // can click day/week names to navigate views
		selectable: true,

		eventSources: {
			url: 'http://10.92.198.11:8080/api/evento',
			method: 'GET',



		},


		select: function(arg) {
			// ATRIBUINDO A DATA E HORA - INÍCIO E FIM AO FORMULÁRIO
			$('#modalId').modal('show');
			$('#modalId #start2').val(arg.start.toISOString().substring(0, 16));
			$('#modalId #end2').val(arg.start.toISOString().substring(0, 16));

		},

		//bloqueia dias passados ao atual
		validRange: {
			start: today,

		},

		// faz com que remova o horario do front
		//displayEventTime: false,

		eventClick: function(info) {

			function parseJwt(token) {
				var base64Url = token.split('.')[1];
				var base64 = base64Url.replace(/-/g, '+').replace(/_/g, '/');
				var jsonPayload = decodeURIComponent(atob(base64).split('')
					.map(
						function(c) {
							return '%'
								+ ('00' + c.charCodeAt(0).toString(
									16)).slice(-2);
						}).join(''));

				return JSON.parse(jsonPayload);
			};

			console.log(info.event)
			let token = sessionStorage.getItem("token");
			let payload = parseJwt(token);
			console.log(payload)
			$('#modalUpdate #idUpdate').val(info.event.id)
			let idUsuarioLogado = document.getElementById("idUsuario").value
			let botaoAlterar = document.getElementById("btnAlterar");
			let botaoExcluir = document.getElementById("btnExcluir");
			let start = document.getElementById("startUpdate");
			let end = document.getElementById("endUpdate");
			let title = document.getElementById("titleUpdate");
			console.log(payload)
			console.log("ENTREi IF" + info.event.extendedProps)
			$('#modalUpdate #usuarioUpdate').val(info.event.extendedProps.usuario.nome)
			$('#modalUpdate #titleUpdate').val(info.event.title)
			$('#modalUpdate #startUpdate').val(info.event.start.toISOString().substring(0, 16))
			$('#modalUpdate #endUpdate').val(info.event.end.toISOString().substring(0, 16))


			console.log(info.event.id)
			if (payload.tipo === "usuario") {
				console.log("USUARIOOO")
				console.log(info.event.extendedProps.usuario.id)
				console.log(payload.id_usuario)

				if (payload.id_usuario == info.event.extendedProps.usuario.id) {
					console.log(info.event.extendedProps.usuario.id)
					console.log(payload.id_usuario)
					console.log("USUARIOOO ALTERAR")
					botaoAlterar.style.visibility = "visible"
					botaoExcluir.style.visibility = "visible"


				} else {
					start.disabled = true;
					end.disabled = true;
					title.disabled = true;
					botaoAlterar.style.visibility = "hidden"
					botaoExcluir.style.visibility = "hidden"
				}

			}
			$('#modalUpdate').modal('show')

		},

		selectMirror: true,
		eventDidMount: function(info) {
			//console.log(1);
		},


		editable: true,
		dayMaxEvents: true, // allow "more" link when too many events

	});


	calendar.render();


});

FullCalendar.globalLocales.push(function() {
	'use strict';

	var ptBr = {
		code: 'pt-br',
		buttonText: {
			prev: 'Anterior',
			next: 'Próximo',
			today: 'Hoje',
			month: 'Mês',
			week: 'Semana',
			day: 'Dia',
			list: 'Lista',
		},
		weekText: 'Sm',
		allDayText: 'dia inteiro',
		moreLinkText: function(n) {
			return 'mais +' + n
		},
		noEventsText: 'Não há eventos para mostrar',
	};

	return ptBr;

}());




function fazPost(url, body) {
	console.log("body=", body)
	let request = new XMLHttpRequest()
	request.open("POST", url, true)
	request.setRequestHeader("content-type", "application/json")
	request.send(JSON.stringify(body))

	request.onload = function() {
		console.log(this.responseText)
	}

	return request.responseText
}