
$( document ).ready(function() {
    $('#sign').remove();

    $('.to-main').on('click', function(event){
        event.stopPropagation();
        event.stopImmediatePropagation();
        window.location.href = '/';
    });

    $('.back-to-settings').on('click', function(event){
        event.stopPropagation();
        event.stopImmediatePropagation();
        $('#success-modal').modal('hide');
        $('#failure-modal').modal('hide'); // rewrite this using parents!
    });

    $('#settings').submit(function (event) {
        event.preventDefault();
        var formAction = $('#settings').attr('action');
        var color = $('#submit').css('background-color');
        var data = new FormData($('#settings')[0]);

        $('#submit').css('background-color','grey');

        $("#submit").prop("disabled", true);


        $.ajax({
            type: "POST",
            url: formAction,
            data: data,
            enctype: 'multipart/form-data',
            processData: false, //prevent jQuery from automatically transforming the data into a query string
            contentType: false,
            cache: false,
            timeout: 600000,
            success: function (result) {

                $("#submit").prop("disabled", false);
                $('#submit').css('background-color',color);
                $('#success-modal').modal('toggle');
                console.log(result);

            },
            error: function (e) {
                $("#submit").prop("disabled", false);
                $('#submit').css('background-color',color);
                $('#error-message').text(e.responseText);
                $('#failure-modal').modal('toggle');
                console.log(e.responseText);
            }
        });
    })
});