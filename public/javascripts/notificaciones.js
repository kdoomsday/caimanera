function notificar(textMessage, levelChoose) {
	$.ajax({
		url: "/notificaciones/",
		type: "POST",
		data: {
			messageKey: textMessage
		},
		success: function(data, textStatus, jqXHR) {
			$.notify(data, levelChoose);
	    },
	    error: function(jqXHR, textStatus, errorThrown) {
	    	$.notify(textStatus, 'error');
        }
	});
}