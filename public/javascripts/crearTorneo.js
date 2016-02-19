var equipoSpace = {
		current:0,
		next:function() {
			equipoSpace.current += 1;
			return equipoSpace.current;
		},
		mkInput:function(divid, placeholder) {
			$("#"+divid).append('<input type="text" name="equipo[' + equipoSpace.next() +
					']" class="col-md-11" placeholder="' + placeholder + '"/>');
		},
		setCurrent:function(curr) {
			current = curr;
		}
}

function addEquipo(route, dialogId, textNombre, idtorneo) {
	$.ajax({
		url: route,
		type: "POST",
		data: {
			Nombre: $("#"+textNombre).val(),
			idtorneo: idtorneo,
			idequipo: -1
		},
		success: function(data, textStatus, jqXHR) {
			$('#'+textNombre).val('');
			$('#'+dialogId).modal('toggle');
	        location.reload();
	    },
	    error: function(jqXHR, textStatus, errorThrown) {
          window.alert(textStatus);
        }
	});
}