source blacksdb.sql
insert into users values('alicia', MD5('alicia'), 'Alicia', 'alicia@gmail.com');
insert into user_roles values ('alicia', 'registered');

insert into users values('blas', MD5('blas'), 'Blas', 'blas@gmail.com');
insert into user_roles values ('blas', 'registered');

insert into users values('admin', MD5('admin'), 'Admin', 'adminblackmarket@gmail.com');
insert into user_roles values ('admin', 'administrador');


insert into asignaturas (nombre,curso) values ('ADX','2a');
insert into asignaturas (nombre,curso) values ('DSA','2a');
insert into asignaturas (nombre,curso) values ('FC','2a');
insert into asignaturas (nombre,curso) values ('API','2b');
insert into asignaturas (nombre,curso) values ('OESC','2b');
insert into asignaturas (nombre,curso) values ('CESA','2b');

/*insert into matriculas (nombre) values ('DSA');
insert into matriculas (nombre) values ('ADX');*/

insert into users_matriculas (username_matriculas,id_asignatura_u_matriculas) values ('Alicia', 1);
insert into users_matriculas (username_matriculas,id_asignatura_u_matriculas) values ('Blas', 2);
insert into users_matriculas (username_matriculas,id_asignatura_u_matriculas) values ('Blas', 1);

insert into contenidos (id_asignatura,id_tipo,titulo,descripcion,autor,link,invalid) values (1,1,'ColasVacaciones','Pdf sobre las Vacaciones','Alicia','C:\\Dsa\1',0);
insert into contenidos (id_asignatura,id_tipo,titulo,descripcion,autor,link,invalid) values (2,3,'ColasVaca','Pdf sobre ','Alicia','C:\\Dsa\1',0);
insert into contenidos (id_asignatura,id_tipo,titulo,descripcion,autor,link,invalid) values (3,2,'Colas','Pdf ','Alicia','C:\\Dsa\1',0);
insert into contenidos (id_asignatura,id_tipo,titulo,descripcion,autor,link,invalid) values (1,2,'ColasVacacionesBlas','sobre las Vacaciones','Blas','C:\\Dsa\1',0); 

insert into comentarios (autor,id_contenido,comentario) values ('Alicia',1,'Me ha sido de mucha ayuda');