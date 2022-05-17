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

	let url = "http://localhost:8080/api/evento"
	let id = document.getElementById("id").value
	let title = document.getElementById("title2").value
	let start = document.getElementById("start2").value
	let end = document.getElementById("end2").value
	location.reload();

	console.log(id)
	console.log(title)
	console.log(start)
	console.log(end)



	body = {
		"id": id,
		"title": title,
		"start": start,
		"end": end
	}



	fazPost(url, body)
}