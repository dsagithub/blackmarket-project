drop database if exists blacksdb;
create database blacksdb;
 
use blacksdb;
 
create table users (
username	varchar(20) not null primary key,
password	char(100) not null,
nombre	        varchar(50) not null,
email	        varchar(50) not null
);
 
create table user_roles (
username	varchar(20) not null,
rolename        varchar(20) not null,
foreign key(username) references users(username) on delete cascade,
primary key (username, rolename)
);

create table asignaturas(
id_asignatura int not null auto_increment primary key,
nombre varchar(20) not null,
curso varchar (5) not null
);

create table users_asignaturas(
username varchar(20) not null,
id_asignatura  int not null,
foreign key (username)  references users(username) on delete cascade,
foreign key (id_asignatura)  references asignaturas(id_asignatura)  on delete cascade
);

/*
 * 
create table carpetas(
id_asignatura varchar(20) not null,
id_carpeta int  not null auto_increment primary key,
nombre varchar(20) not null,
foreign key (id_asignatura) references asignaturas(id_asignatura) on delete cascade
);*/

/*
 * Id_tipo (1= Teoria, 2= Ejercicios, 3= Examenes)
 */
create table contenidos (
id_contenido int not null auto_increment primary key,
id_asignatura int not null,
/*id_carpeta int  not null,*/
id_tipo int not null,
titulo varchar(30) not null,
descripcion varchar(100),
fecha timestamp default current_timestamp ON UPDATE CURRENT_TIMESTAMP,
autor varchar (50) not null,
link varchar (200) not null,
invalid int,
/*foreign key (id_carpeta) references carpetas(id_carpeta) on delete cascade,*/
foreign key (id_asignatura) references asignaturas(id_asignatura) on delete cascade,
foreign key (autor) references users (username) on delete cascade
);

create table comentarios (
autor varchar(20) not null primary key,
id_contenido int not null,
comentario varchar (100) not null,
foreign key (id_contenido) references contenidos(id_contenido) on delete cascade,
foreign key (autor) references users(username) on delete cascade
);