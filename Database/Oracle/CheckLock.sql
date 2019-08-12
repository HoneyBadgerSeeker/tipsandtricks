BEGIN
   dbms_output.enable(1000000);
   for do_loop in (select session_id, a.object_id, xidsqn, oracle_username, b.owner owner,
   b.object_name object_name, b.object_type object_type
   FROM v$locked_object a, dba_objects b
   WHERE xidsqn != 0
   and b.object_id = a.object_id)
  loop
   dbms_output.put_line('.');
   dbms_output.put_line('Blocking Session : '||do_loop.session_id);
   dbms_output.put_line('Object (Owner/Name): '||do_loop.owner||'.'||do_loop.object_name);
   dbms_output.put_line('Object Type : '||do_loop.object_type);
   for next_loop in (select sid from v$lock
   where id2 = do_loop.xidsqn
   and sid != do_loop.session_id)
   LOOP
 dbms_output.put_line('Sessions being blocked : '||next_loop.sid);
   end loop;
   end loop;
   END;
   /
   
 
ALTER SYSTEM KILL SESSION 'sid,serial#' IMMEDIATE;

SELECT sess.sid,sess.serial#,lo.oracle_username,lo.os_user_name,ao.object_name,lo.locked_mode 
FROM v$locked_object lo,dba_objects ao,v$session sess 
WHERE ao.object_id = lo.object_id AND lo.session_id = sess.sid;

