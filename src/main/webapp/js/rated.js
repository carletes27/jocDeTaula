$(document).ready(function () {
    /*
     * MATERIAZLIZE
     */
    $('ul.tabs').tabs('select_tab', 'test4');
    $('.chips').material_chip();
    $('.chips-initial').material_chip({
        data: [{tag: 'Apple'}, {tag: 'Microsoft'}, {tag: 'Google'}]
    });
    $('.chips-placeholder').material_chip({
        placeholder: 'Enter a tag',
        secondaryPlaceholder: '+Tag'
    });
    $('.chips').on('chip.add', function (e, chip) {
        alert("on add");
        alert(chip.tag);
        $('.chips-initial').material_chip('data').forEach(function (untag) {
            alert(untag.tag);
        });
    });

    $('.chips').on('chip.delete', function (e, chip) {
        alert("on delete");
    });

    $('.chips').on('chip.select', function (e, chip) {
        alert("on select");
    });
    $(".button-collapse").sideNav();
    $('.modal-trigger').leanModal();
    /*
     * END MATERIALIZE
     */


    var jocsValorats = $("#jocsValorats");
    var servletURL = "valorat?action=llistaJocsValorats";
    $.ajax({
        type: "GET",
        crossDomain: true,
        dataType: "json",
        async: true,
        url: servletURL,
        success: function (data) {
            var myHtml = renderLlistaJocs(data);
            jocsValorats.html(myHtml);
            var dades = millorJoc(data);
            var nomJoc = dades[0].toString();
            $("#imatge-joc-millor-valorat").attr("src", "img/"+nomJoc+".png");
            $("#joc-millor-valorat").text(dades[0]);
            $("#mitjana-joc-millor-valorat").text(dades[1]);
        },
        error: function (jqXHR, textStatus, errorThrown) {
            console.info('rated.js 1 in error');
            console.log(jqXHR, textStatus, errorThrown);
            alert("You can not send Cross Domain AJAX requests: " + errorThrown);
        }
    });
});
$(document).on('click', '[class*="eliminarJoc"]', function () {
    var joc = $(this).attr("id");

    var servletURL = "valorat?action=eliminarValoracioJoc&joc=" + joc;
    $.ajax({
        type: "GET",
        crossDomain: true,
        dataType: "json",
        async: true,
        url: servletURL,
        success: function (data) {
            window.location.href = "valoracions.html";

        },
        error: function (jqXHR, textStatus, errorThrown) {
            console.info('rated.js 2 in error');
            console.log(jqXHR, textStatus, errorThrown);
            alert("You can not send Cross Domain AJAX requests: " + errorThrown);
        }
    });
});



function renderLlistaJocs(data) {
    var myHtml = "";
    $.each(data.jsonArray, function (index) {
        myHtml += '<div class="col s12 m12 l12"> <div class="card grey lighten-4 hoverable">';
        myHtml += renderJoc(data.jsonArray[index]);
        myHtml += '</div></div>';
    });
    return myHtml;
}

function renderJoc(dadesJoc) {
    //TODO si stock és 0
    var myHtmlP = "";
    var joc = "";
    var mitjana = 0.0;
    var valoracio = 0;
    var llistaValoracions;
    $.each(dadesJoc, function (key, value) {
        if (key == 'name') {
            joc = value;
        }
        if (key == 'mitjana') {
            mitjana = parseFloat(value);
        }
        if (key == 'valoracio') {
            valoracio = parseInt(value);
        }
        if (key == 'llistaValoracions') {
            llistaValoracions = value;
        }
    });
 
    myHtmlP +='<div class="card-panel grey lighten-5 z-depth-1 hoverable"><div class="row valign-wrapper">';
    myHtmlP += '<div class="col s2"><img  class="circle responsive-img" src="img/' + joc + '.png"/></div>';
    myHtmlP += '<div class="col s10">\n\
                <div class="chip"><h6>Joc: <span id="name-' + joc + '">' + joc + '</h6></div>';
    myHtmlP += '<div class="chip"><h6>Mitjana: <span id="mitjana-' + joc + '">' + mitjana + '</h6></div>';
    myHtmlP += '<div class="chip"><h6>Última Valoració: <span id="valoracio-' + joc + '">' + valoracio + '</span></h6></div> ';
    myHtmlP += '<div class="chip"><h6>Totes les valoracions: <span id="list-' + joc + '">' + llistaValoracions + '</span></h6></div></div>';
    myHtmlP += '<div class="card-action right-align"><a class ="eliminarJoc" href="#" id="' + joc + '">Eliminar</a>';
    myHtmlP += '</div></div></div>';
    return myHtmlP;
}

function millorJoc(data){
    var dades = ["",0.0];
     $.each(data.jsonArray, function (index) {
        
        var joc = data.jsonArray[index];
        var m = joc["mitjana"];
        if (m > dades[1]){
            dades[0] = joc["name"];
            dades[1] = parseFloat(m);
        }
     });
    
  
    return dades;
}


