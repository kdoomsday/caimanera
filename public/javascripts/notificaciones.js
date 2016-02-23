function notificar(textMessage, levelChoose) {
	$.ajax({
		url: "/notificaciones/",
		type: "POST",
		data: {
			level: levelChoose,
			messageKey: textMessage
		},
		success: function(data, textStatus, jqXHR) {
			$('#notifications').append(data);
	    },
	    error: function(jqXHR, textStatus, errorThrown) {
          window.alert(textStatus);
        }
	});
}