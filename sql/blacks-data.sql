source blacksdb.sql
insert into users values('alicia', MD5('alicia'), 'Alicia', 'alicia@gmail.com');
insert into user_roles values ('alicia', 'registered');

insert into users values('blas', MD5('blas'), 'Blas', 'blas@gmail.com');
insert into user_roles values ('blas', 'registered');

insert into users values('admin', MD5('admin'), 'Admin', 'adminblackmarket@gmail.com');
insert into user_roles values ('admin', 'administrador');



insert into asignaturas (nombre,curso) values ('ADX','3a');
insert into asignaturas (nombre,curso) values ('DSA','3a');
insert into asignaturas (nombre,curso) values ('SO','2b');
insert into asignaturas (nombre,curso) values ('API','2b');
insert into asignaturas (nombre,curso) values ('OESC','2b');
insert into asignaturas (nombre,curso) values ('CESA','2b');
insert into asignaturas (nombre,curso) values ('ER','2b');
insert into asignaturas (nombre,curso) values ('MXS','3a');
insert into asignaturas (nombre,curso) values ('XLAM','3a');

