# Continuous Integration

## Jacoco

### Skipping JaCoCo execution due to missing execution data file.

    <build>
        <plugins>
            <plugin>
                <groupId>org.apache.maven.plugins</groupId>
                <artifactId>maven-surefire-plugin</artifactId>
                <version>3.2.3</version>
                <configuration>
                    <!-- Without this parameter, jacoco.exec is not generated. Mayby due to surfire overriding some JVM argument  -->
                    <argLine>${argLine}</argLine>
                </configuration>
            </plugin>
