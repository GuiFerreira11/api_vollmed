alter table medicos add ativo tinyint;
update medicos set ativo = 1;
alter table medicos modify ativo tinyint unsigned not null;
