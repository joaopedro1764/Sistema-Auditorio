$(function(){
    //istanciamento da variavel do token e a url
    //token ficticio
    //const url = "https://graph.facebook.com/17921217800437558/top_media?user_id=17841453171843486&fields=id,media_type,media_url,comments_count,like_count&access_token=EAAISF9D7XfEBAMkQ1gfd6LZB9enezIv4OpDl5akt2RHnhVFAZByrMlteFJjuZAJo0uvyreJBv4YWxmdIQZC8ZCYn5QPLZB40xaq5VWRdPhRtCdhyafCkDM9a5u9htzC8OpBeGpwLY5ZCWqHhkpZBORttmEjqIAwFEIEgCF0IKZCFMVncM7rBqA4sZB"

    //aqui e o processo do consumo de api do ista 
    $.get(url).then(function(response){
        let dadosJson = response.data
        let conteudo = '<div class="ladoAlado">'
        for(let p=0; p < dadosJson.length; p++){
            let feed = dadosJson[p];
            let titulo = feed.caption !== null ? feed.caption : '';
            let tipo = feed.media_type;
            if(tipo === 'IMAGE'){
                conteudo += '<div class="imagemGrade"><img class="imgHash" title="'+titulo+'" alt="'+titulo+'" src="'+feed.media_url+'";"></div>'
            }
        }
        //aqui faz mostrar na div determinada
        conteudo += '</div>';
        $('#insta').html(conteudo);
        console.log(dadosJson)
    })
})