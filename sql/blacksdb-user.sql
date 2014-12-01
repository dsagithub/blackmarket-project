drop user 'blacks'@'localhost';
create user 'blacks'@'localhost' identified by 'blacks';
grant all privileges on blacksdb.* to 'blacks'@'localhost';
flush privileges;