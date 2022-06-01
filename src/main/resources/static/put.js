function fazPut(url, body) {
	console.log("body=", body)
	let request = new XMLHttpRequest()
	request.open("PUT", url, true)
	request.setRequestHeader("content-type", "application/json")
	request.send(JSON.stringify(body))



	request.onload = function() {
		console.log(this.responseText)
	}



	return request.responseText
}
function update() {

	let id = document.getElementById("idUpdate").value
	let idUsuario = document.getElementById("idUsuario").value
	let title = document.getElementById("titleUpdate").value
	let start = document.getElementById("startUpdate").value
	let end = document.getElementById("endUpdate").value
	let url = "http://localhost:8080/api/evento/" + id
	
	
	if(end < start){
		    alert('A Data final não pode acabar antes do inicio!');
	
		    end.focus()
		    		    	
		
					    
	}else if (start > start){
			alert("ERROOOO")		
		}else{
			
		
		-
			
	console.log(id)
	console.log(title)
	console.log(start.toLocaleString())
	console.log(end.toLocaleString())



	body = {
		"id": id,
		"title": title,
		"start": start,
		"end": end,
		"usuario": {
			"id": idUsuario,
		}
	}

	


	fazPut(url, body)
}
}
