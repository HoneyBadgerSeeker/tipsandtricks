SELECT FILE_NAME||' '||TABLESPACE_NAME||' '||BYTES/1024/1024FROM DBA_TEMP_FILES;

ALTER DATABASE TEMPFILE '/io/oracle/data/temp.dbf' RESIZE 18M;