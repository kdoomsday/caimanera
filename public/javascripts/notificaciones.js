function notificar(textMessage, levelChoose) {
	$.ajax({
		url: "/notificaciones/",
		type: "POST",
		data: {
			level: levelChoose,
			message: textMessage
		},
		success: function(data, textStatus, jqXHR) {
			$('#notifications').append(data);
	    },
	    error: function(jqXHR, textStatus, errorThrown) {
          window.alert(textStatus);
        }
	});
}