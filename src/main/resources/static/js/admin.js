
$( document ).ready(function() {
    $('#sign').remove();

    $('#settings').submit(function (event) {
        event.preventDefault();
        var formAction = $('#settings').attr('action');
        var color = $('#submit').css('background-color');
        var data = new FormData($('#settings')[0]);

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
                console.log(result);

            },
            error: function (e) {
                $("#submit").prop("disabled", false);
                $('#submit').css('background-color',color);
                console.log(result);
            }
        });
    })
});