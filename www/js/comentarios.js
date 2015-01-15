var API_BASE_URL = "http://147.83.7.155:8080/blackmarket-api";
var USERNAME="";
var PASSWORD="";
var IDCONTENIDO="";
//HAY QUE MIRAR ESTO
//$.removeCookie('idasignatura');
var NOMBREASIGNATURA =0;
var NOMBREASIGNATURA2 =0;


$(document).ready(function() {
USERNAME = $.cookie('username');
PASSWORD = $.cookie('password');
IDCONTENIDO = $.cookie('comentario');
$("#usernameregistred").text(USERNAME);
console.log($.cookie('comentario'));
getdatoscontenido(IDCONTENIDO);
getcomentarios(IDCONTENIDO);

});


$.ajaxSetup({
    headers: { 'Authorization': "Basic "+ btoa(USERNAME+':'+PASSWORD) }
});

$("#atrasregistred").click(function(e) {
	e.preventDefault();
	$.removeCookie('comentario');
	window.location = "http://www.blackmarket.dsa/registered.html"
});


$("#logout").click(function(e) {
	e.preventDefault();
	$.removeCookie('username');
	$.removeCookie('password');
	window.location = "http://www.blackmarket.dsa/index.html"
});


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


function getcomentarios(idcontenidocomentario){
var url = API_BASE_URL + '/comentarios/contenido/'+idcontenidocomentario;
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
			$.each(repo, function(l, k) {
			var coment = k;
			if(coment.id_comentario != undefined){

					$('<tr><th style="width:1px;text-align:center"><button class="btn btn-default btn-sm" style="padding:2px" id="'+coment.id_comentario+'" onclick="eliminar(id)"><span class="glyphicon glyphicon-ban-circle"></span></button></th><th><th>'+coment.autor+' </th><th>'+coment.comentario+'</th>').appendTo($('#contenido_result'));
				}
				});
			});
		}).fail(function() {
		$("#contenido_result").text("NO HAY CONTENIDO");		
	});
}
function comentar(){
var url = API_BASE_URL + '/comentarios';
console.log( $("#comentarios").val() );
if($("#comentarios").val() != ""){
var coment = new Object();
	coment.autor = USERNAME;
	coment.id_contenido = IDCONTENIDO;
	coment.comentario = $("#comentarios").val();

var data = JSON.stringify(coment);
		$.ajax({
		 headers: { 'Authorization': "Basic "+ btoa(USERNAME+':'+PASSWORD)},
			url : url,
			type : 'POST',
			crossDomain : true,
			dataType : 'json',
			data : data,
			contentType : 'application/vnd.blackmarket.api.comentario+json',
		}).done(function(data, status, jqxhr) {
		window.location = "http://www.blackmarket.dsa/comentarios.html"
		}).fail(function() {		
	});
	}
}
function getdatoscontenido(id)
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
					$("#Nombrearchivo").text(cotenidoinfo.titulo);
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
					
					console.log(cotenidoinfo.fecha);
					$("#popupfecha").text(cotenidoinfo.fecha);
					
					var imagen = document.getElementById("imagen"); 
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
function eliminar(id){
console.log(id);
var url = API_BASE_URL + '/comentarios/'+id;
		$.ajax({
		 headers: { 'Authorization': "Basic "+ btoa(USERNAME+':'+PASSWORD)},
			url : url,
			type : 'DELETE',
			crossDomain : true,
			dataType : 'json',
			url : url,
		}).done(function(data, status, jqxhr) {
		window.location = "http://www.blackmarket.dsa/comentarios.html"
		}).fail(function() {
		});

}

