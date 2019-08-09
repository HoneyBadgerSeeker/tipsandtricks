# http://dirknachbar.blogspot.com/2016/03/alter-user-rename-half-official-option.html

# Connect as SYS to your database
conn / as sysdba
# At first we set an undocumented parameter to enable the RENAME option
alter session set "_enable_rename_user"=true;
# Bring the Database to restricted session, in order to avoid Memory Problems for huge schema
alter system enable restricted session;
# Now lets RENAME the user DEMO to DEMO_NEW
# and provide a password for the new user DEMO_NEW
alter user DEMO rename to DEMO_NEW identified by "demo";
# Disable restricted session
alter system disable restricted session;
