var API_BASE_URL = "http://147.83.7.155:8080/blackmarket-api";
var USERNAME="";
var PASSWORD="";
var NASIGNATURA="";
var idcontenidoinvalid="";
//HAY QUE MIRAR ESTO
//$.removeCookie('idasignatura');
var NOMBREASIGNATURA =0;
var NOMBREASIGNATURA2 =0;


$(document).ready(function() {
USERNAME = $.cookie('username');
PASSWORD = $.cookie('password');
$.removeCookie('texto');
$.removeCookie('busqueda');
$.removeCookie('comentario');
$("#usernameregistred").text(USERNAME);
getultimoscontenidos();
getasigmatriculas();
});




$.ajaxSetup({
    headers: { 'Authorization': "Basic "+ btoa(USERNAME+':'+PASSWORD) }
});


$("#buscarautor").click(function(e) {
	e.preventDefault();
	if($("#textbuscar").val() != "" )
	{
		buscarcontenidoautor($("#textbuscar").val());
	}
});
$("#buscartitulo").click(function(e) {
	e.preventDefault();
	if($("#textbuscar").val != "" )
	{
		buscarcontenidotitulo($("#textbuscar").val());
	}

});
function buscarcontenidoautor(text) {
$.cookie('texto',text);
$.cookie('busqueda',"autor");
window.location = "http://localhost/search.html"

}

function buscarcontenidotitulo(text) {
$.cookie('texto',text);
$.cookie('busqueda',"titulo");
window.location = "http://localhost/search.html"
}

$("#logout").click(function(e) {
	e.preventDefault();
	$.removeCookie('username');
	$.removeCookie('password');
	window.location = "http://localhost/index.html"
});

$("#miperfil").click(function(e) {
	e.preventDefault();
	window.location = "http://localhost/perfil.html"
});

function getasigmatriculas(){
var url = API_BASE_URL + '/matricula/'+USERNAME;
$("#asignatura_result").text("");
	$.ajax({
	
		url : url,
		type : 'GET',
		crossDomain : true,
		contentType : 'application/vnd.blackmarket.api.matricula+json',
		dataType : 'json',
	}).done(function(data, status, jqxhr) {	
				var repos = data;
				
				$.each(repos, function(i, v) {
					var repo = v;
					$.each(repo, function(j, k) {
					var asig=k;
					getnombrebyid(asig.id_asignatura_u_matriculas);
				});
				});
				
					
				
	}).fail(function() {
		$("#repos_result").text("Nombre de usuario incorrecto.");
	});
}

//ACABAR
function asignaturaescojida(id){
console.log(id);
NASIGNATURA=""
$.cookie('idasignatura',id);
window.location = "http://localhost/asignatura.html"
}

function getnombrebyid(id){
var url = API_BASE_URL + '/asignatura/'+id;
NASIGNATURA=""
console.log (id);
		$.ajax({
			url : url,
			type : 'GET',
			crossDomain : true,
			dataType : 'json',
			url : url,
			contentType : 'application/vnd.blackmarket.api.asignatura+json',
		}).done(function(data, status, jqxhr) {
		var asig = data;
		NASIGNATURA = asig.nombre;
		$('<button type="button" class="buttonasignatura" id="'+id+'" onclick="asignaturaescojida(id)" >'+asig.nombre+'</button> ').appendTo($('#asignatura_result'));
		}).fail(function() {
		});
}

function getnombrebyid3(id){
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
		$("#"+id+""+NOMBREASIGNATURA2+"").text("Asignatura: "+asig.nombre);
		console.log(asig.nombre);
		NOMBREASIGNATURA2++;
		console.log(NOMBREASIGNATURA2);
		
		}).fail(function() {
		});
}

function getnombrebyid2(id){
var url = API_BASE_URL + '/asignatura/'+id;
NASIGNATURA=""
console.log (id);
		$.ajax({
			url : url,
			type : 'GET',
			crossDomain : true,
			dataType : 'json',
			url : url,
			contentType : 'application/vnd.blackmarket.api.asignatura+json',
		}).done(function(data, status, jqxhr) {
		var asig = data;
		NASIGNATURA = asig.nombre;
		return asig.nombre;
		}).fail(function() {
		});
}

function getultimoscontenidos(){
$("#ultimoscontenidos").text("");

var url = API_BASE_URL + '/blacks/ultimos/'+USERNAME;
		$.ajax({
			url : url,
			type : 'GET',
			crossDomain : true,
			dataType : 'json',
			url : url,
		}).done(function(data, status, jqxhr) {
		var ultimostodo = data;
		
		$.each(ultimostodo, function(j, k) {
					var ultimose=k;
						$.each(ultimose, function(r, v) {
						var ultimos=v;
						
			if(ultimos.id_asignatura != undefined){
						$('<li style="background-color:#F8F7F7"><a id = "'+ultimos.id_contenido+'" onclick="titulo(id)" href="#" data-toggle="modal" data-target="#contenido" data-whatever="@fat"><i class="glyphicon glyphicon-book" ></i style="fontSize:20px"> Titulo: ' + ultimos.titulo + '<div>Autor: ' + ultimos.autor + '</div><div><span id="'+ultimos.id_asignatura+''+NOMBREASIGNATURA+'">'+ultimos.id_asignatura+'<span></div><div align="right">Fecha: '+ultimos.fecha+'</div></a></li><p></p>').appendTo($('#ultimoscontenidos'));
						$('<li role="presentation" class="divider"></li>').appendTo($('#ultimoscontenidos'));
						NOMBREASIGNATURA++;
						getnombrebyid3(ultimos.id_asignatura);
						}
					});	
						});	
						
		}).fail(function() {
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
					//getnombrebyid2(cotenidoinfo.id_asignatura);
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
					imagen.src = "\img\\"+cotenidoinfo.id_contenido+".png";
					
					var a = document.getElementById("download"); 
					a.href = "\img\\"+cotenidoinfo.id_contenido+".png";
					
					$('<button type="button" class="btn btn-danger" id="'+id+'" onclick="invalidoclick(id)" ><a class=" glyphicon glyphicon-thumbs-down" style="color:#FFFFFF" id="prueba"> Invalido</a></button>').appendTo($('#invalidoboton'));
					$('<button type="button" class="btn btn-primary"  id="'+id+'" onclick="comentariosclick(id)" ><a class="glyphicon glyphicon-pencil" style="color:#FFFFFF" id="coments">Comentarios</a></button>').appendTo($('#comentariosboton'));
					

					
					
		}).fail(function() {
		});
		
console.log(id);

}
function invalidoclick(idcontenidoinvalid)
{
	console.log(idcontenidoinvalid);
	var url = API_BASE_URL + '/blacks/invalid/'+idcontenidoinvalid;
			$.ajax({
			headers: { 'Authorization': "Basic "+ btoa(USERNAME+':'+PASSWORD)},
				url : url,
				type : 'PUT',
				crossDomain : true,
				dataType : 'json',
				url : url,
			}).done(function(data, status, jqxhr) {					
			}).fail(function() {
			});
}

function comentariosclick(idcontenidocomentario)
{
$.cookie('comentario',idcontenidocomentario);
window.location = "http://localhost/comentarios.html"
}

function getmatriculas(){
var url = API_BASE_URL + '/asignatura';
var numero = 1;
$("#asignatura_result_matricula").text('');
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
					
					
					if($('#'+numero+'').length){
						$('<tr><td align="center">ID:'+ asig.id_asignatura +'NOMBRE:   ' + asig.nombre + '  CURSO:  ' + asig.curso+ '</td><td align="center"><input type="checkbox" checked="true" onchange="matriculaescojida(this,id)" id="'+ asig.id_asignatura +'" autocomplete="off"></td></tr>').appendTo($('#asignatura_result_matricula'));
						$('</p>').appendTo($('#asignatura_result_matricula'));
						numero++;
					}
					else{
					$('<tr><td align="center">ID:'+ asig.id_asignatura +'NOMBRE:   ' + asig.nombre + '  CURSO:  ' + asig.curso+ '</td><td align="center"><input type="checkbox"  onchange="matriculaescojida(this,id)" id="'+ asig.id_asignatura +'" autocomplete="off"></td></tr>').appendTo($('#asignatura_result_matricula'));
						$('</p>').appendTo($('#asignatura_result_matricula'));
						numero++;
					}
					
					});
				});
	}).fail(function() {
	});
}

$("#aceptar").click(function(e) {
	e.preventDefault();
	window.location = "http://localhost/registered.html"
});

function matriculaescojida(obj,id){
console.log(id);
	if(obj.checked){
	var url = API_BASE_URL + '/matricula';
	var matricula = new Object();
	matricula.username_matriculas = USERNAME;
	matricula.id_asignatura_u_matriculas = id;
	var data = JSON.stringify(matricula);
		$.ajax({
		headers: { 'Authorization': "Basic "+ btoa(USERNAME+':'+PASSWORD)},
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

