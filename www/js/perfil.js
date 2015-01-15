var API_BASE_URL = "http://147.83.7.155:8080/blackmarket-api";
var USERNAME="";
var PASSWORD="";
var NOMBREASIGNATURA =0;
var NOMBREASIGNATURA2 =0;

$.ajaxSetup({
    headers: { 'Authorization': "Basic "+ btoa(USERNAME+':'+PASSWORD) }
});


$(document).ready(function() {
USERNAME = $.cookie('username');
PASSWORD = $.cookie('password');
console.log(USERNAME);
console.log(PASSWORD);
$("#usernameregistred").text("Mi Perfil");
$("#nombreperfil").text(USERNAME);
gettodo($.cookie('username'));
getmiperfil(USERNAME);
getmatriculas();
});

function actualizarperfil(){
var url = API_BASE_URL + '/users/'+USERNAME;

var update = new Object();
	if($("#nombreinput").val() != "")
	{
	update.nombre = $("#nombreinput").val();
	}
	if ($("#correoinput").val() != "")
	{
	update.email = $("#correoinput").val();
	}
	var data = JSON.stringify(update);
	$.ajax({
	 headers: { 'Authorization': "Basic "+ btoa(USERNAME+':'+PASSWORD)},
		url : url,
		type : 'PUT',
		crossDomain : true,
		dataType : 'json',
		url : url,
		contentType : 'application/vnd.blackmarket.api.user+json',
		data : data,
	}).done(function(data, status, jqxhr) {	
	
	window.location = "http://www.blackmarket.dsa/perfil.html"
				
	}).fail(function() {

	});
}

$('form#formcontenido').submit(function(e){
console.log("hola");
	e.preventDefault();
$("#tipoid").val(document.getElementById('tipo').options[document.getElementById('tipo').selectedIndex].value);
$("#asignaturaid").val(document.getElementById('asig').options[document.getElementById('asig').selectedIndex].value);
$("#autorid").val(USERNAME);
	
var url = API_BASE_URL + '/blacks';
	var formData = new FormData($("form#formcontenido")[0]);
	console.log(formData);

	$.ajax({
	 headers: { 'Authorization': "Basic "+ btoa(USERNAME+':'+PASSWORD)},
		url: url,
		type: 'POST',
		crossDomain : true,
		data: formData,
		cache: false,
		contentType: false,
        processData: false
	})
	.done(function (data, status, jqxhr) {
	window.location = "http://www.blackmarket.dsa/perfil.html"

	})
    .fail(function (jqXHR, textStatus) {

	});
});


function nuevocontenido(){
console.log(document.getElementById('asig').options[document.getElementById('asig').selectedIndex].value);
console.log(document.getElementById('tipo').options[document.getElementById('tipo').selectedIndex].value);
console.log(document.getElementById("inputFile").value);
}
function getmiperfil(username){
var url = API_BASE_URL + '/users/'+username;
		$.ajax({
			url : url,
			type : 'GET',
			crossDomain : true,
			dataType : 'json',
			url : url,

		}).done(function(data, status, jqxhr) {
			var perfil = data;
			$("#nombreperfil").text("Perfil de "+perfil.nombre);
			$("#usernamep").text("Username: "+perfil.username);
			$("#nombrep").text("Nombre de usuario: "+perfil.nombre);
			$("#correop").text("Correo: "+perfil.email);
			
		}).fail(function() {
		});
}

function getmatriculas(){
var url = API_BASE_URL + '/asignatura';
var asign=0;
	$.ajax({
		url : url,
		type : 'GET',
		crossDomain : true,
		dataType : 'json',
	}).done(function(data, status, jqxhr) {	
				var repos = data;
				$.each(repos, function(i, v) {
					var repo = v;
					var s1=document.getElementById('asig');
					$.each(repo, function(j, k) {
					var asig=k;
					
					s1.options[asign]=new Option(""+asig.nombre+"",""+asig.id_asignatura+"",""+asig.id_asignatura+"");
					asign++;

					});
				});
	}).fail(function() {
	});
}




function registretweb(){
window.location = "http://www.blackmarket.dsa/registered.html"
}

$("#logout").click(function(e) {
	e.preventDefault();
	$.removeCookie('username');
	$.removeCookie('password');
	window.location = "http://www.blackmarket.dsa/index.html"
});

function getnombrebyid(id){
var url = API_BASE_URL + '/asignatura/'+id;
console.log (id);
		$.ajax({
			url : url,
			type : 'GET',
			crossDomain : true,
			dataType : 'json',
			url : url,
			contentType : 'application/vnd.blackmarket.api.asignatura+json',
		}).done(function(data, status, jqxhr) {
		console.log(NOMBREASIGNATURA2);
		var asig = data;
		$("#"+id+""+NOMBREASIGNATURA2+"").text(asig.nombre);
		console.log(asig.nombre);
		NOMBREASIGNATURA2++;
		}).fail(function() {
		});
}



function gettodo(username){
var url = API_BASE_URL + '/blacks/'+username;
$("#contenido_result").text("");
		$.ajax({
			url : url,
			type : 'GET',
			crossDomain : true,
			dataType : 'json',
			url : url,
		}).done(function(data, status, jqxhr) {
		var repos = data;
				$.each(repos, function(i, v) {
					var repo = v;

					$.each(repo, function(j, k) {
					var content=k;
					
				if(content.id_asignatura != undefined){
					$('<tr><th style="width:1px;text-align:center""><button class="btn btn-default btn-sm" style="padding:2px" id="'+content.id_contenido+'" onclick="eliminar(id)"><span class="glyphicon glyphicon-ban-circle"></span></button></th>     <th><a id = "'+content.id_contenido+'" onclick="titulo(id)" href="#"  data-toggle="modal" data-target="#contenido" data-whatever="@fat">'+content.titulo+'</th><th><span id="'+content.id_asignatura+''+NOMBREASIGNATURA+'">'+content.id_asignatura+'<span></th><th>'+content.autor+'</th><th>'+content.descripcion+'</th></tr>').appendTo($('#contenido_result'));
						NOMBREASIGNATURA++;
						getnombrebyid(content.id_asignatura);
						}
						
						
						
				});
			});	
		}).fail(function() {
		$("#contenido_result").text("NO HAY CONTENIDO");

		
	});
}


function titulo(id)
{
console.log(id);
var url = API_BASE_URL + '/blacks/contenido/'+id;
		$.ajax({
			url : url,
			type : 'GET',
			crossDomain : true,
			dataType : 'json',
			url : url,
		}).done(function(data, status, jqxhr) {
		var cotenidoinfo = data;
					$("#popuptitulo").text(cotenidoinfo.titulo);
					if(cotenidoinfo.id_tipo == 1)
					{
					$("#popupasignatura").text(cotenidoinfo.id_asignatura+'--TEORIA');
					}
					if(cotenidoinfo.id_tipo == 2)
					{
					$("#popupasignatura").text(cotenidoinfo.id_asignatura+'--EJERCICIOS');
					}
					if(cotenidoinfo.id_tipo == 3)
					{
					$("#popupasignatura").text(cotenidoinfo.id_asignatura+'--EXAMENES');
					}
					
					$("#descripcioncontent").text(cotenidoinfo.descripcion);
					
					$("#invalidoboton").text("");
					$("#comentariosboton").text("");
					
					console.log(cotenidoinfo.fecha);
					$("#popupfecha").text(cotenidoinfo.fecha);
					
					var imagen = document.getElementById("popupimagen"); 
					//var nimagen = cotenidoinfo.id_contenido;
					console.log(cotenidoinfo.id_contenido);
					imagen.src = "/img//"+cotenidoinfo.id_contenido+".png";
					
					var a = document.getElementById("download"); 
					a.href = "/img//"+cotenidoinfo.id_contenido+".png";
					
					$('<button type="button" class="btn btn-danger" id="'+id+'" onclick="invalidoclick(id)" ><a class=" glyphicon glyphicon-thumbs-down" style="color:#FFFFFF" id="prueba"> Invalido</a></button>').appendTo($('#invalidoboton'));
				$('<button type="button" class="btn btn-primary"  id="'+id+'" onclick="comentariosclick(id)" ><a class="glyphicon glyphicon-pencil" style="color:#FFFFFF" id="coments">Comentarios</a></button>').appendTo($('#comentariosboton'));
				
					
		}).fail(function() {
		});
		
console.log(id);

}

function comentariosclick(idcontenidocomentario)
{
$.cookie('comentario',idcontenidocomentario);
window.location = "http://www.blackmarket.dsa/comentarios.html"
}
function eliminar(id){

console.log(id);
var url = API_BASE_URL + '/blacks/'+id;
		$.ajax({
		 headers: { 'Authorization': "Basic "+ btoa(USERNAME+':'+PASSWORD)},
			url : url,
			type : 'DELETE',
			crossDomain : true,
			dataType : 'json',
			url : url,
		}).done(function(data, status, jqxhr) {
		window.location = "http://www.blackmarket.dsa/perfil.html"
		}).fail(function() {
		
		});

}


