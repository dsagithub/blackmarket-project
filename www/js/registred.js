var API_BASE_URL = "http://localhost:8080/blackmarket-api";
var USERNAME ="";
var PASSWORD="";

$(document).ready(function() {
	var USERNAME = $.cookie('username');
	console.debug($.cookie("username"));
	console.debug($.cookie('username'));
});




$.ajaxSetup({
    headers: { 'Authorization': "Basic "+ btoa(USERNAME+':'+PASSWORD) }
});

getmatriculas();
function getmatriculas(){
var url = API_BASE_URL + '/asignatura';
	$.ajax({
		url : url,
		type : 'GET',
		crossDomain : true,
		dataType : 'json',
	}).done(function(data, status, jqxhr) {	
				var repos = data;
				
				$.each(repos, function(i, v) {
					var repo = v;
					$.each(repo, function(j, k) {
					var asig=k;
					$('<h4> Name: ' + asig.id + '</h4>').appendTo($('#asignatura_result'));
					
					$('<p>').appendTo($('#asignatura_result'));	
					
					$('<strong> ID: </strong> ' + asig.nombre + '<br>').appendTo($('#asignatura_result'));
					
					$('<strong> URL: </strong> ' + asig.curso+ '<br>').appendTo($('#asignatura_result'));
					
					$('</p>').appendTo($('#asignatura_result'));
				});
				});
				
					
				
	}).fail(function() {
		$("#repos_result").text("Nombre de usuario incorrecto.");
	});
}

