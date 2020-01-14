select constraint_name,table_name from user_constraints where r_constraint_name in (select constraint_name from user_constraints where table_name = 'TABLE_NAME') ;
alter table TABLE_NAME disable constraint FK_1;
alter table TABLE_NAME disable constraint FK_2;
TRUNCATE TABLE TABLE_NAME;
