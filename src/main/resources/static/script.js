function sendToService() {
    var phoneNumberJson = {
        number: $("#phoneNumber").val()
    };

    $('#console').html('sending..');

    $.ajax({
        url: '/country',
        headers: {
            'Accept': 'application/json',
            'Content-Type': 'application/json'
        },
        type: 'post',
        dataType: 'json',
        success: function (data) {
            if (data.errorMessage) {
                $('#console').html(data.errorMessage);
            } else {
                $('#console').html(data.names.join(","));
            }
        },
        error: function (jqXHR, textStatus, errorThrown) {
            $('#console').html(jqXHR)
        },
        data: JSON.stringify(phoneNumberJson)
    });

}