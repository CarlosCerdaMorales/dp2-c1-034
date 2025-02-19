-- create-database.sql
--
-- Copyright (c) 2012-2025 Rafael Corchuelo.
--
-- In keeping with the traditional purpose of furthering education and research, it is
-- the policy of the copyright owner to permit non-commercial use and redistribution of
-- this software. It has been tested carefully, but it is not guaranteed for any particular
-- purposes. The copyright owner does not offer any warranties or representations, nor do
-- they accept any liabilities with respect to them.

-- HINT: replace "Project-Version" by your project name and version.

drop database if exists `Acme-ANS-D01`;
create database `Acme-ANS-D01` 
	character set = 'utf8mb4'
  	collate = 'utf8mb4_unicode_ci';

grant select, insert, update, delete, create, drop, references, index, alter, 
        create temporary tables, lock tables, create view, create routine, 
        alter routine, execute, trigger, show view
    on `Acme-ANS-D01`.* to 'acme-user'@'%';

drop database if exists `Acme-ANS-D01-Test`;
create database `Acme-ANS-D01-Test` 
	character set = 'utf8mb4'
  	collate = 'utf8mb4_unicode_ci';

grant select, insert, update, delete, create, drop, references, index, alter, 
        create temporary tables, lock tables, create view, create routine, 
        alter routine, execute, trigger, show view
    on `Acme-ANS-D01-Test`.* to 'acme-user'@'%';
