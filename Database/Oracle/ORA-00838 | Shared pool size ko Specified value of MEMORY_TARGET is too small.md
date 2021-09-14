ORA-00838: Specified value of MEMORY_TARGET is too small, needs to be at least 22912M

C:\oracle\>sqlplus / as sysdba

SQL*Plus: Release 12.2.0.1.0 Production on Fri Apr 7 13:57:50 2017

Copyright (c) 1982, 2016, Oracle.  All rights reserved.

Connected to an idle instance.


SQL> create pfile = 'c:\temp\init.ora' from spfile;

File created.

Then you edit the file and use that to start, ie

SQL> startup pfile=c:\temp\init.ora

and then, once your database is open

SQL> create spfile from pfile=c:\temp\init.ora
