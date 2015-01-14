var API_BASE_URL = "http://localhost:8080/blackmarket-api";
var USERNAME = "";
var PASSWORD = "";

$(document).ready(function() {
});


$.ajaxSetup({
    headers: { 'Authorization': "Basic "+ btoa(USERNAME+':'+PASSWORD) }
});

$("#login").click(function(e) {
	e.preventDefault();
	if($("#login_username").val() == "" || $("#login_password").val() == "")
	{
		if($("#login_username").val() == "")
		{
			document.getElementById('login_username').style.background='#F6B5B5';
			$('#login_username').attr('placeholder','RELLENE EL CAMPO');
		}
		if($("#login_password").val() == "")
		{
			document.getElementById('login_password').style.background='#F6B5B5';
			$('#login_password').attr('placeholder','RELLENE EL CAMPO');
		}
	}
	else
	{
	var login = new Object();
	login.username = $("#login_username").val();
	login.password = $("#login_password").val();	
	getlogin(login);
	}
});

$("#logincancelar").click(function(e) {
	e.preventDefault();
	document.getElementById('login_username').style.background='#FFFFFF';
	document.getElementById('login_password').style.background='#FFFFFF';
	$('#login_username').attr('placeholder','Username');
	$('#login_password').attr('placeholder','Password');
	document.getElementById('login_username').value=null;
	document.getElementById('login_password').value=null;
});



function getlogin(logearse){
var url = API_BASE_URL + '/users/login';
var data = JSON.stringify(logearse);
	$.ajax({
		url : url,
		type : 'POST',
		crossDomain : true,
		dataType : 'json',
		url : url,
		contentType : 'application/vnd.blackmarket.api.user.collection+json',
		data : data,
	}).done(function(data, status, jqxhr) {	
				var inf = data;
				if(inf.loginSuccessful== true){
				
					if(USERNAME == "" && PASSWORD == "")
					{
					USERNAME = $("#login_username").val();
					PASSWORD = $("#login_password").val();
					}
						if(inf.rol=="registered"){
						$.cookie('username', USERNAME);
						$.cookie('password', PASSWORD);
						var username1 = $.cookie('username');
						
						console.log(username1);
						window.location = "http://localhost/registered.html"

						}
						else{
						$.cookie('username', USERNAME);
						$.cookie('password', PASSWORD);
						window.location = "http://localhost/admin.html"
						
						}
				}
				else{
					document.getElementById('login_username').style.background='#FFFFFF';
					document.getElementById('login_password').style.background='#F6B5B5';
					document.getElementById('login_password').value=null;
					$('#login_password').attr('placeholder','CONTRASEÑA INCORRECTA');
					}
					
					
				
	}).fail(function() {
	//CAMBIAR EL COLOR
	document.getElementById('login_username').style.background='#F6B5B5';
	document.getElementById('login_username').value=null;
	$('#login_username').attr('placeholder','NOMBRE DE USUARIO INCORRECTO');
	
	});
}

$("#sign").click(function(e) {
	e.preventDefault();
	if($("#new_usuario_nombre").val() == "" || $("#new_usuario_username").val() == "" || $("#new_usuario_password").val() == "" || $("#new_usuario_email").val() == "")
	{
		if($("#new_usuario_nombre").val() == "")
		{
			document.getElementById('new_usuario_nombre').style.background='#F6B5B5';
			$('#new_usuario_nombre').attr('placeholder','RELLENE EL CAMPO');
		}
		if($("#new_usuario_username").val() == "")
		{
			document.getElementById('new_usuario_username').style.background='#F6B5B5';
			$('#new_usuario_username').attr('placeholder','RELLENE EL CAMPO');
		}
		if($("#new_usuario_password").val() == "")
		{
			document.getElementById('new_usuario_password').style.background='#F6B5B5';
			$('#new_usuario_password').attr('placeholder','RELLENE EL CAMPO');
		}
		if($("#new_usuario_email").val() == "")
		{
			document.getElementById('new_usuario_email').style.background='#F6B5B5';
			$('#new_usuario_email').attr('placeholder','RELLENE EL CAMPO');
		}
	}
	else
	{
	var login = new Object();
	login.nombre = $("#new_usuario_nombre").val();
	login.username= $("#new_usuario_username").val();
	login.password = $("#new_usuario_password").val();	
	login.email = $("#new_usuario_email").val();		
	getn_usuario(login);
	}
});

$("#signcancelar").click(function(e) {
	e.preventDefault();
	document.getElementById('new_usuario_nombre').style.background='#FFFFFF';
	document.getElementById('new_usuario_username').style.background='#FFFFFF';
	document.getElementById('new_usuario_password').style.background='#FFFFFF';
	document.getElementById('new_usuario_email').style.background='#FFFFFF';
	
	$('#new_usuario_nombre').attr('placeholder','Nombre');
	$('#new_usuario_username').attr('placeholder','Username');
	$('#new_usuario_password').attr('placeholder','Password');
	$('#new_usuario_email').attr('placeholder','Email');
	
	document.getElementById('new_usuario_nombre').value=null;
	document.getElementById('new_usuario_username').value=null;
	document.getElementById('new_usuario_password').value=null;
	document.getElementById('new_usuario_email').value=null;
});


function getn_usuario(logearse){
var url = API_BASE_URL + '/users';
var data = JSON.stringify(logearse);
	$.ajax({
		url : url,
		type : 'POST',
		crossDomain : true,
		dataType : 'json',
		contentType : 'application/vnd.blackmarket.api.user+json',
		data : data,
	}).done(function(data, status, jqxhr) {	
				var repo = data;

				USERNAME = $("#new_usuario_username").val();
				PASSWORD = $("#new_usuario_password").val();

				 $("#vmatricula").click();
				 getmatriculas();					
				
	}).fail(function() {
		document.getElementById('new_usuario_username').style.background='#6600FF';
		document.getElementById('new_usuario_username').value=null;
		$('#new_usuario_username').attr('placeholder','YA EXISTE ESTE USERNAME');
		
	});
}

function getmatriculas(){
var url = API_BASE_URL + '/asignatura';
$("#asignatura_result").text('');
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
					
					$('<tr><td align="center">ID:'+ asig.id_asignatura +'NOMBRE:   ' + asig.nombre + '  CURSO:  ' + asig.curso+ '</td><td align="center"><input type="checkbox"  onchange="matriculaescojida(this,id)" id="'+ asig.id_asignatura +'" autocomplete="off"></td></tr>').appendTo($('#asignatura_result'));
					$('</p>').appendTo($('#asignatura_result'));
					
					
					});
				});
	}).fail(function() {
	});
}

function matriculaescojida(obj,id){
console.log(id);
	if(obj.checked){
	var url = API_BASE_URL + '/matricula';
	var matricula = new Object();
	matricula.username_matriculas = USERNAME;
	matricula.id_asignatura_u_matriculas = id;
	var data = JSON.stringify(matricula);
		$.ajax({
			url : url,
			type : 'POST',
			crossDomain : true,
			dataType : 'json',
			url : url,
			contentType : 'application/vnd.blackmarket.api.matricula+json',
			data : data,
		}).done(function(data, status, jqxhr) {			
		}).fail(function() {
		});
	}
	else{
	var url = API_BASE_URL + '/matricula/'+USERNAME+'?idmatricula='+id;
	$.ajax({
		url : url,
		type : 'DELETE',
		crossDomain : true,
		dataType : 'json',
	}).done(function(data, status, jqxhr) {
  	}).fail(function() {
	});
	}
}

$("#msignin").click(function(e) {
var login = new Object();
	login.username = USERNAME;
	login.password = PASSWORD;	
	getlogin(login);
});




