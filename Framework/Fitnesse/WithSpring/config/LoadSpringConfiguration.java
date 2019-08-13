package myapp.fixture.config;

import com.google.common.base.Strings;
import myapp.config.Constants;
import myapp.fixture.util.FitnesseUtilities;
import liquibase.Liquibase;
import liquibase.database.Database;
import liquibase.database.DatabaseFactory;
import liquibase.database.jvm.JdbcConnection;
import liquibase.exception.LiquibaseException;
import liquibase.resource.ClassLoaderResourceAccessor;
import org.apache.commons.io.FileUtils;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;
import org.springframework.core.env.ConfigurableEnvironment;

import javax.sql.DataSource;
import java.io.IOException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.Map;
import java.util.NoSuchElementException;
import java.util.Properties;

public class LoadSpringConfiguration {

    private static final String SEPARATOR = ",";
    public static ApplicationContext applicationContext;
    private String confPath;
    private String propertiesFile;

    public boolean load() throws Exception {
        if (applicationContext == null) {
            BuildPropertiesFile.store(propertiesFile);
            if (!Strings.isNullOrEmpty(confPath)) {
                final String[] configurations = confPath.split(SEPARATOR);
                final Class<?>[] configurationClasses = new Class<?>[configurations.length];
                int i = 0;
                for (final String configuration : configurations) {
                    try {
                        configurationClasses[i] = Class.forName(configuration);
                        i++;
                    } catch (Exception e) {
                        LOGGER.error("Error while loading class context: " + configuration, e);
                        return false;
                    }
                }
                applicationContext = new AnnotationConfigApplicationContext();
                ((ConfigurableEnvironment) applicationContext.getEnvironment()).setActiveProfiles("fitnesse");
                ((AnnotationConfigApplicationContext) applicationContext).register(configurationClasses[0]);
                ((AnnotationConfigApplicationContext) applicationContext).refresh();

                Map<String, DataSource> dataSources = LoadSpringConfiguration.applicationContext.getBeansOfType(DataSource.class);
                DataSource bdd = dataSources.get("dataSource");
                try (Connection conn = bdd.getConnection()) {
                    FitnesseUtilities.executeUpdate(conn, "CREATE ALIAS TO_NUMBER AS $$ Long toNumber(String value) { " +
                        "return value == null ? null : Long.valueOf(value); } $$;");
                } catch (SQLException e) {
                    e.printStackTrace();
                }
            }
        } else {
	     // In test mode suite, the spring context is initialized once (at the start of the 1st test)
             // When a test is finished: we drop to the h2 database with the clearDataBase fixture
             // At the beginning of the next test, we go here and trigger the initialization of the database with liquibase            
             runLiquibase();
        }
        return true;
    }

    /**
     * H2 Initialisation on the fly
     */
    private void runLiquibase() {

        final Properties props = BuildPropertiesFile.getPropertiesFile(propertiesFile);
        Connection c = null;
        try {
            c = DriverManager.getConnection(props.getProperty(Constants.PROPERTY_KEY_DATASOURCE_URL),
                props.getProperty(Constants.PROPERTY_KEY_DATASOURCE_USERNAME),
                props.getProperty(Constants.PROPERTY_KEY_DATASOURCE_PASSWORD));

            Database database = DatabaseFactory.getInstance().findCorrectDatabaseImplementation(new JdbcConnection(c));
            String changeLogFile = "config\\liquibase\\master.xml";

            Liquibase liquibase = new Liquibase(changeLogFile, new ClassLoaderResourceAccessor(), database);
            liquibase.update("main");

            FitnesseUtilities.executeUpdate(c, "CREATE ALIAS TO_NUMBER AS $$ Long toNumber(String value) { " +
                "return value == null ? null : Long.valueOf(value); } $$;");

            FitnesseUtilities.executeUpdate(c, "SET SCHEMA SCHEMA_B;");
            FitnesseUtilities.executeUpdate(c, "GRANT ALL ON SCHEMA_B.SQ_TABLE_XY TO SCHEMA_A;");
            FitnesseUtilities.executeUpdate(c, "GRANT ALL ON SCHEMA_B.TB_TABLE_XY TO SCHEMA_A;");
            FitnesseUtilities.executeUpdate(c, "SET SCHEMA SCHEMA_A;");

        } catch (SQLException | LiquibaseException e) {
            e.printStackTrace();
            throw new NoSuchElementException(e.getMessage());
        } finally {
            if (c != null) {
                try {
                    c.rollback();
                    c.close();
                } catch (SQLException e) {
                    //nothing to do
                }
            }
        }
    }

    public void setConfigurationPath(String confPath) {
        this.confPath = confPath;
    }

    public void setPropertiesFile(String propertiesFile) {
        this.propertiesFile = propertiesFile;
    }
}
