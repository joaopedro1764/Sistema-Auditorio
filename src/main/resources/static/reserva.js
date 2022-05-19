
document.addEventListener('DOMContentLoaded', function() {



	var initialLocaleCode = 'pt-br';
	var calendarEl = document.getElementById('calendar');
	

	var calendar = new FullCalendar.Calendar(calendarEl, {


		locale: initialLocaleCode,
		headerToolbar: {
			left: 'prev,next today',
			center: 'title',
			right: 'dayGridMonth,timeGridWeek,timeGridDay'
		},




		select: function(arg) {

			if (title) {

				historico();

				calendar.addEvent({
					id: id,
					title: title,
					start: arg.start,
					end: arg.end,


				})


			}


			calendar.unselect()

		},


		navLinks: true, // can click day/week names to navigate views
		selectable: true,

		eventSources: {
			url: 'http://localhost:8080/api/evento',
			method: 'GET'

		},

		select: function(info) {
			// ATRIBUINDO A DATA E HORA - INÍCIO E FIM AO FORMULÁRIO
			$('#modalId').modal('show');
			$('#modalId #start2').val(info.start.toISOString().substring(0, 16));
			console.log(info.start.str)
			console.log(info.end.toLocaleString())
		},


		//eventClick: function(info) {

			//$('#eventoModal #idModal').text(info.event.id)
			//$('#eventoModal #titleModal').text(info.event.title)
			//$('#eventoModal #startModal').text(info.event.start.toLocaleString())
			//$('#eventoModal #endModal').text(info.event.end.toLocaleString())
			//$('#eventoModal').modal('show')
		//},
		
		eventClick: function(info) {

			$('#modalUpdate #idUpdate').val(info.event.id)
			$('#modalUpdate #titleUpdate').val(info.event.title)
			$('#modalUpdate #startUpdate').val(info.event.start.toISOString().substring(0, 16))
			$('#modalUpdate #endUpdate').val(info.event.end.toISOString().substring(0, 16))
			$('#modalUpdate').modal('show')
		},
		
		selectMirror: true,
		eventDidMount: function(info) {
			console.log(1);
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