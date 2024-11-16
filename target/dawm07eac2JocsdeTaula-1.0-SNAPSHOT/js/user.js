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


    
    var servletURL = "user?action=formUser";
    $.ajax({
        type: "GET",
        crossDomain: true,
        dataType: "json",
        async: true,
        url: servletURL,
        success: function (data) {
            $("#user").val(data.user);
            $("#name").val(data.name);
            $("#lastname").val(data.lastname);
        },
        error: function (jqXHR, textStatus, errorThrown) {
            console.info('user.js 1 in error');
            console.log(jqXHR, textStatus, errorThrown);
            alert("You can not send Cross Domain AJAX requests: " + errorThrown);
        }
    });
});

$(document).on('click', '[class*="fusubmit"]', function (event) {
    event.preventDefault();
    
   
    var servletURL = "user?action=newUser&user=" + $("#user").val()+"&name=" + $("#name").val()+"&lastname=" + $("#lastname").val();
    $.ajax({
        type: "GET",
        crossDomain: true,
        dataType: "json",
        async: true,
        url: servletURL,
        success: function (data) {
            if(data.resposta == "OK"){
                window.location.href = "index.html";
            }else{
                alert(data.resposta);
            }                        
        },
        error: function (jqXHR, textStatus, errorThrown) {
            console.info('user.js 2 in error');
            console.log(jqXHR, textStatus, errorThrown);
            alert("You can not send Cross Domain AJAX requests: " + errorThrown);
        }
    });
});

