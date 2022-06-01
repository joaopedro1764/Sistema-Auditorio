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
function historico() {

	let url = "http://localhost:8080/api/evento/"
	const idUsuario = document.getElementById("idUsuario").value
	console.log(idUsuario)
	let id = document.getElementById("id").value
	let title = document.getElementById("title2").value
	let start = document.getElementById("start2").value
	let end = document.getElementById("end2").value
<<<<<<< HEAD
	
	  
	
	
	if(end < start){
		    alert('A Data final não pode acabar antes da data de inicio!');
		    
		    end.focus()
		   
			}else{
				
			
			
	console.log(id)
	console.log(title)
	console.log(start.toLocaleString())
	console.log(end.toLocaleString())
	
	
	
=======
>>>>>>> 0dc5dbf975d98dfad3ba7586269eb4711fd2869c



	body = {
		"id": id,
		"title": title,
		"start": start,
<<<<<<< HEAD
		"end": end
	}
	
	
=======
		"end": end,
		"usuario": {
			"id": idUsuario,
		}
	};




>>>>>>> 0dc5dbf975d98dfad3ba7586269eb4711fd2869c


	location.reload();
	fazPost(url, body)
<<<<<<< HEAD
	}
=======
>>>>>>> 0dc5dbf975d98dfad3ba7586269eb4711fd2869c
}
