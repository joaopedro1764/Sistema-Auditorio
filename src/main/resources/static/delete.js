function fazApagar(url, body) {
	console.log("body=", body)
	let request = new XMLHttpRequest()
	request.open("DELETE", url, true)
	request.setRequestHeader("content-type", "application/json")
	request.send(JSON.stringify(body))



	request.onload = function() {
		console.log(this.responseText)
	}



	return request.responseText
}
function apagar() {

	console.log("passei no deletar")

	let id = document.getElementById("idUpdate").value
	let url = "http://localhost:8080/api/evento/" + id

	console.log(id)

	body = {
		"id": id,
	}



	fazApagar(url, body)
	location.reload();
}