$(function(){
    //istanciamento da variavel do token e a url
    //token ficticio
    const url = "https://graph.facebook.com/17921217800437558/top_media?user_id=17841453171843486&fields=id,media_type,media_url,comments_count,like_count&access_token=EAAISF9D7XfEBANeQqeTj1dt4sPOE33yNYlV6emYM3cCKyo5rCizOpV0LQbCeUZB7lAfaNTfw4mRVZBHVfXZAPbx7fg8uwwcZBAie7akIsZAFRmJ2DwNwfXlArI3HLa2VEVlkenVIGShAxcaDsCs3KucK7UeRCah9rR3IersUsBtZCeunypYZAQc"

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