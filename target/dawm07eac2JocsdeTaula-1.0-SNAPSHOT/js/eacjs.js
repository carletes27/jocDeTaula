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


    var jocs = $("#jocs");
    var servletURL = "LlistaJocsServlet?action=llistaJocs";
    $.ajax({
        type: "GET",
        crossDomain: true,
        dataType: "json",
        async: true,
        url: servletURL,
        success: function (data) {
            var myHtml = renderLlistaJocs(data);
            jocs.html(myHtml);
            var dades = millorJoc(data)
            $("#joc-millor-valorat").text(dades[0]);
            $("#mitjana-joc-millor-valorat").text(dades[1]);
        },
        error: function (jqXHR, textStatus, errorThrown) {
            console.info('eac.js 1 in error');
            console.log(jqXHR, textStatus, errorThrown);
            alert("You can not send Cross Domain AJAX requests: " + errorThrown);
        }
    });

  //p3//
    var servletURL = "user?action=formUser";
    $.ajax({
        type: "GET",
        crossDomain: true,
        dataType: "json",
        async: true,
        url: servletURL,
        success: function (data) {
            if(data.name == ""){
                $("#completename").html("<h5>Col·lecció de jocs</h5>");
                $("#username").html("<h5>Valoracions de l'usuari</h5>");
            } else {
                $("#completename").html("<h5>Col·lecció de jocs de: " + data.name + ' '+ data.lastname +'</h5>');
                $("#username").html("<h5>Valoracions de l'usuari: " + data.user +'</h5>');      
            }
            
        },
        error: function (jqXHR, textStatus, errorThrown) {
            console.info('in error');
            console.log(jqXHR, textStatus, errorThrown);
        }
    });


});
$(document).on('click', '[class*="valoracio"]', function () {
    var valoracioJoc = $(this).attr("id").split("-");
    var joc = valoracioJoc[0];
    var valoracio = valoracioJoc[1];
    var afegir = $(this.parentElement);
    var servletURL = "LlistaJocsServlet?action=afegirValoracioJoc&joc=" + joc + "&valoracio=" + valoracio;
    $.ajax({
        type: "GET",
        crossDomain: true,
        dataType: "json",
        async: true,
        url: servletURL,
        success: function (data) {
            if (data.mitjanaJoc == -1.0) {
                $("#mitjana-" + data.jocValorat).text('0');
                alert("Sense mitjana. Només una valoració");
            } else {
                afegir.hide();
                $("#check-" + joc).show();
                $("#mitjana-" + data.jocValorat).text(data.mitjanaJoc);
                $("#valoracio-" + data.jocValorat).text(valoracio);
                
                var mitjana_millor_joc_valorat = parseFloat($("#mitjana-joc-millor-valorat").text());
                if(data.mitjanaJoc > mitjana_millor_joc_valorat){
                    $("#joc-millor-valorat").text(data.jocValorat);
                    $("#mitjana-joc-millor-valorat").text(data.mitjanaJoc);
                }                
            }

        },
        error: function (jqXHR, textStatus, errorThrown) {
            console.info('eac.js 2 in error');
            console.log(jqXHR, textStatus, errorThrown);
            alert("You can not send Cross Domain AJAX requests: " + errorThrown);
        }
    });
});



function renderLlistaJocs(data) {
    var myHtml = "";
    $.each(data.jsonArray, function (index) {
        myHtml += '<div class="col s12 m3 l3"> <div class="card grey lighten-4 hoverable">';
        myHtml += renderJoc(data.jsonArray[index]);
        myHtml += '</div></div>';
    });
    return myHtml;
}

function renderJoc(dataJoc) {
    var myHtmlP = "";
    var joc = "";
    var mitjana = 0.0;
    var valoracio = 0;
    var quantity = 0;
    var med = 0.0
    var valorat = false;
    $.each(dataJoc, function (key, value) {
        if (key == 'name') {
            joc = value;
        }
        if (key == 'valoracio') {
            valoracio = parseInt(value);
        }
        if (key == 'mitjana') {
            mitjana = parseFloat(value);
        }
        if (key == 'valorat') {
            if (value == 'SI') {
                valorat = true;
            } else {
                valorat = false;
            }
        }
    });
    myHtmlP += '<div class="card-image"><img class="circle responsive-img" src="img/' + joc + '.png"/><span class="card-title" style="color: white; text-shadow: 1px 1px 0 #000, -1px 1px 0 #000, 1px -1px 0 #000, -1px -1px 0 #000;">' + joc + '</span></div>';
    myHtmlP += '<div class="card-content"><div class="chip"><h6>Valoració: <span id="valoracio-' + joc + '">' + valoracio + '</h6></div>';
    myHtmlP += '<div class="chip"><h6>Mitjana: <span id="mitjana-' + joc + '">' + mitjana + '</span></h6></div></div>';
    
    if (valorat) {
        myHtmlP += '<div class="card-action right-align">';
        myHtmlP += '<img id ="check-' + joc + '" style="width: 10px;" src="img/check.png"/></div>';

    } else {
        myHtmlP += '<div class="card-action right-align"><div>';
       for (var i = 1; i <= 5; i++){
        myHtmlP += '<a class ="valoracio" href="#" id="' + joc + '-' +i+'">'+i+'</a>';
       }
        myHtmlP += '</div><img id ="check-' + joc + '" style="display: none;width: 10px;" src="img/check.png"/></div>';
    }

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