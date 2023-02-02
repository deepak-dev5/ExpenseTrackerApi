drop database expensetrackerdb;
drop user 'expensetracker'@'localhost';
flush privileges;

create user 'expensetracker'@'localhost' identified by 'password';
create database expensetrackerdb;
GRANT ALL ON expensetrackerdb.* To 'expensetracker'@'localhost' WITH GRANT OPTION;

use  expensetrackerdb;

create table et_users(
user_id int not null AUTO_INCREMENT,
first_name varchar(20) not  null,
last_name varchar(20) not null,
email varchar(30) not null,
password text not null,
primary key (user_id)
);

create table et_categories(
category_id int not null,
user_id int not null,
title varchar(20) not null,
description varchar(50) not null,
primary key (category_id)
);

alter table et_categories add constraint cat_users_fk
foreign key (user_id) references et_users (user_id);

create table et_transactions(
transaction_id int not null,
category_id int not null,
user_id int not null,
amount numeric(10,2) not null,
note varchar(50) not null,
transaction_date bigint not null,
primary key(transaction_id)
);

alter table et_transactions add constraint trans_cat_fk
foreign key (category_id) references et_categories(category_id);

alter table et_transactions add constraint trans_users_fk
foreign key (user_id) references et_users (user_id);

#create sequence et_users_seq increment 1 start 1;

#ALTER TABLE et_users CHANGE roll roll INT(10) AUTO_INCREMENT PRIMARY KEY;




