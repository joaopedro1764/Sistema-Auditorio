
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
			
			function historico() {

				let url = "http://localhost:8080/api/evento"

				body = {
					"id": id,
					"title": title,
					"start": start,
					"end": end,
				}

				fazPost(url, body)
			}
			if (title) {
				
				historico();

				calendar.addEvent({
					title: title,
					start: arg.start,
					end: arg.end,
					/*allDay: arg.allDay*/

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
		
		dateClick: function(info) {
			$('#modalId').modal('show')
			
		},

		eventClick: function(info) {
			$('#eventoModal').modal('show')
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