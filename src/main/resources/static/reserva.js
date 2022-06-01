
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



		navLinks: true, // can click day/week names to navigate views
		selectable: true,

		eventSources: {
			url: 'http://localhost:8080/api/evento',
			method: 'GET'

		},

		select: function(arg) {
			// ATRIBUINDO A DATA E HORA - INÍCIO E FIM AO FORMULÁRIO
			$('#modalId').modal('show');
			$('#modalId #start2').val(arg.start.toISOString().substring(0, 16));
			$('#modalId #end2').val(arg.start.toISOString().substring(0, 16));


			$('#date-form2').on('focusout', function() {
				var dateObj1 = new Date($('#start2').val());
				var dateObj2 = new Date($('#end2').val());

				
			});

			console.log(arg.start.toISOString().substring(0, 16).replace());
			console.log(arg.start.toISOString())
			console.log(arg.start.toISOString())
			//console.log(arg.end.toLocaleString())
		},

		//bloqueia dias passados ao atual
		validRange: {
			start: today,

		},

		// faz com que remova o horario do front
		//displayEventTime: false,

		eventClick: function(info) {


			$('#modalUpdate #idUpdate').val(info.event.id)
			let idUsuarioLogado = document.getElementById("idUsuario").value
			let botaoAlterar = document.getElementById("btnAlterar");
			let botaoExcluir = document.getElementById("btnExcluir");
			console.log(idUsuario)
			$('#modalUpdate #titleUpdate').val(info.event.title)
			$('#modalUpdate #startUpdate').val(info.event.start.toISOString().substring(0, 16))
			$('#modalUpdate #endUpdate').val(info.event.end.toISOString().substring(0, 16))
			$('#modalUpdate #usuarioUpdate').val(info.event.extendedProps.usuario.nome)
			console.log(idUsuarioLogado)
			console.log(info.event.extendedProps.usuario.nome)
			if (info.event.extendedProps.usuario.id == idUsuarioLogado) {
				botaoAlterar.style.visibility = "visible"
				botaoExcluir.style.visibility = "visible"
			} else {
				botaoAlterar.style.visibility = "hidden"
				botaoExcluir.style.visibility = "hidden"
			}
			$('#modalUpdate').modal('show')

		},

		selectMirror: true,
		eventDidMount: function(info) {
			console.log(1);
		},


		editable: true,
		dayMaxEvents: true, // allow "more" link when too many events

	});

	function validaEvento() {



	}



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