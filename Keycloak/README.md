# Roundcube <-> Keycloak
Oauth connexion failed => add FIRST_NAME, LAST_NAME and EMAIL attribute to the UserModel

# Access ressource 
## .. as InputStream
try (var imgstream = session.theme().getTheme(Theme.Type.EMAIL).getResourceAsStream("/img/name.png")) { ... }
## .. as URL
var path = System.getProperty("user.dir") + "/themes/" +  session.theme().getTheme(Theme.Type.EMAIL).getName() + "/" + Theme.Type.EMAIL.name().toLowerCase() + "/resources";
Path.of(path + "/img/name.png").toUri().toURL()

# Get provider name (used for adding property to keycloak.conf)
Go to the Spi definition (org.keycloak.authentication.AuthenticatorSpi#getName)

# Load keycloak.conf properties
spi-authenticator-_spiId_-_propertyName_=_value_

Usually config are loaded in the init() method of the Factory but for some reason on Keycloak 22, if a property is missing, KC freeze its launch and does not create log.
Loading the property in the postInit() method made us able to throw RuntimeException to alert on missing property and stop the launch of KC.

# Access IDP Token
keycloakSession.users().getFederatedIdentity(keycloakSession.getContext().getRealm(), userModel, "identityProviderName").getToken()
+ might need to activate "Store tokens" inside provider config
