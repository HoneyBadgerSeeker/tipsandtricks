# Roundcube <-> Keycloak trough IDP
Oauth connexion failed => add FIRST_NAME, LAST_NAME and EMAIL attribute to the UserModel

# Access ressource 
## .. as InputStream
try (var imgstream = session.theme().getTheme(Theme.Type.EMAIL).getResourceAsStream("/img/name.png")) { ... }
## .. as URL
var path = System.getProperty("user.dir") + "/themes/" +  session.theme().getTheme(Theme.Type.EMAIL).getName() + "/" + Theme.Type.EMAIL.name().toLowerCase() + "/resources";
Path.of(path + "/img/name.png").toUri().toURL()

# Get provider name (used for adding property to keycloak.conf)
Go to the Spi definition (org.keycloak.authentication.AuthenticatorSpi#getName)
