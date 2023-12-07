Roundcube <-> Keycloak trough IDP
Oauth connexion failed => add FIRST_NAME, LAST_NAME and EMAIL attribute to the UserModel

Access ressource 
.. as InputStream
try (var imgstream = session.theme().getTheme(Theme.Type.EMAIL).getResourceAsStream("/img/name.png")) {

}

.. as URL
System.getProperty("user.dir") + "/themes/" + session.theme().getTheme(Theme.Type.EMAIL).getName() + "/resources";
Path.of(path + "/img/name.png").toUri().toURL()
