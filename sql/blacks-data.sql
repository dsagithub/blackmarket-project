source blacksdb.sql
insert into users values('alicia', MD5('alicia'), 'Alicia', 'alicia@gmail.com');
insert into user_roles values ('alicia', 'registered');

insert into users values('blas', MD5('blas'), 'Blas', 'blas@gmail.com');
insert into user_roles values ('blas', 'registered');



insert into asignaturas (nombre,curso) values ('ADX','2a');
insert into asignaturas (nombre,curso) values ('DSA','2a');
insert into asignaturas (nombre,curso) values ('SAI','2a');

insert into carpetas (id_asignatura,nombre) values (1,"Teoria");
insert into carpetas (id_asignatura,nombre) values (1,"Ejercicios");
insert into carpetas (id_asignatura,nombre) values (1,"Examenes");
insert into carpetas (id_asignatura,nombre) values (2,"Teoria");
insert into carpetas (id_asignatura,nombre) values (2,"Ejercicios");
insert into carpetas (id_asignatura,nombre) values (2,"Examenes");
insert into carpetas (id_asignatura,nombre) values (3,"Teoria");
insert into carpetas (id_asignatura,nombre) values (3,"Ejercicios");
insert into carpetas (id_asignatura,nombre) values (3,"Examenes");
