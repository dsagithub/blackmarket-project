var API_BASE_URL = "http://147.83.7.155:8080/blackmarket-api";
var USERNAME;
var PASSWORD;
var ASIGNATURA;

$(document).ready(function() {
USERNAME = $.cookie('username');
PASSWORD = $.cookie('password');
ASIGNATURA = $.cookie('idasignatura');
$("#usernameregistred").text(USERNAME);
//console.debug($.cookie("username"));
//console.debug($.cookie("password"));
//getultimoscontenidos;
//getmatriculas();	
getnombrebyid(ASIGNATURA);
getteoria(ASIGNATURA,'1');
});




$.ajaxSetup({
    headers: { 'Authorization': "Basic "+ btoa(USERNAME+':'+PASSWORD) }
});

$("#logout").click(function(e) {
	e.preventDefault();
	$.removeCookie('username');
	$.removeCookie('password');
	window.location = "http://localhost/index.html"
});

$("#teoria").click(function(e) {
	e.preventDefault();
	getteoria(ASIGNATURA,1)
	console.log("Teoria");
});
$("#actividades").click(function(e) {
	e.preventDefault();
	getteoria(ASIGNATURA,2)
	console.log("Actividades");
});
$("#examenes").click(function(e) {
	e.preventDefault();
	getteoria(ASIGNATURA,3)
	console.log("Examenes");
});

$("#atrasregistred").click(function(e) {
	e.preventDefault();
	$.removeCookie('idasignatura');
	window.location = "http://localhost/registered.html"
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
		var asig = data;
		//nombreasignatura = asig.nombre; 
		$("#nombreasignatura").text("Contenido de "+asig.nombre);
		}).fail(function() {
		});
}

function getteoria(id,id_tipo){
console.log(id_tipo);
var url = API_BASE_URL + '/blacks/contenidos?idasignatura='+id+'&idtipo='+id_tipo;
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
					$('<tr><th><a id = "'+content.id_contenido+'" onclick="titulo(id)" href="#"  data-toggle="modal" data-target="#contenido" data-whatever="@fat">'+content.titulo+'</a></th><th><a id = "'+content.autor+'" onclick="pagautor(id)" href="#">'+content.autor+'</th><th>'+content.descripcion+'</th></tr>').appendTo($('#contenido_result'));
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

function pagautor(id){
	$.removeCookie('texto');
	$.removeCookie('busqueda');
	$.cookie('autor',id)
	window.location = "http://localhost/autor.html"
	
}
function comentariosclick(idcontenidocomentario)
{
$.cookie('comentario',idcontenidocomentario);
window.location = "http://localhost/comentarios.html"
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



