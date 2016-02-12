var equipoSpace = {
		current:0,
		next:function() {
			equipoSpace.current += 1;
			return equipoSpace.current;
		},
		mkInput:function(divid, placeholder) {
			$("#"+divid).append('<input type="text" name="equipo[' + equipoSpace.next() +
					']" class="col-md-11" placeholder="' + placeholder + '"/>');
		}
}

function popupEquipo(divid) {
	$("#"+divid).css("diplay", "block");
	alert('ding!')
}