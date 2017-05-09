$("#algorithms").change(function(e) {
	$.ajax({
		type : "POST",
		url : "/search/initAnalyzer",
		data : {
			algorithm : $(this).val()
		},
		success : function(result) {
		},
		error : function(result) {
		}
	});
});
$("#file").change(function(e) {
	var data = new FormData();
	$.each($('#file')[0].files, function(i, file) {
	    data.append('file', file);
	});
	$.ajax({
		type : "POST",
		url : "/search/upload",
		data : data,
		cache : false,
		contentType : false,
		processData : false,
		success : function(data) {
			alert(data);
		}
	});
});