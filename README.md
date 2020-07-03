# favorites-service

Attempt #1045 to create a fully optimized AWS Lambda project to minimize cold starts

After adding log4j2 configuration like recommended in AWS documentation, cold starts go up by 500-1000ms.


Java
```
 private static Logger logger = LogManager.getLogger();
```

pom.xml
```

        <!-- Logging
        <dependency>
            <groupId>com.amazonaws</groupId>
            <artifactId>aws-lambda-java-log4j2</artifactId>
            <version>1.2.0</version>
            <scope>runtime</scope>
        </dependency>
        <dependency>
            <groupId>org.apache.logging.log4j</groupId>
            <artifactId>log4j-core</artifactId>
            <version>${aws.log4j.version}</version>
        </dependency>
        <dependency>
            <groupId>org.apache.logging.log4j</groupId>
            <artifactId>log4j-api</artifactId>
            <version>${aws.log4j.version}</version>
        </dependency>
        <dependency>
            <groupId>org.apache.logging.log4j</groupId>
            <artifactId>log4j-slf4j18-impl</artifactId>
            <version>${aws.log4j.version}</version>
            <scope>runtime</scope>
        </dependency>-->
```

log4j2.xml
```
<?xml version="1.0" encoding="UTF-8"?>
<Configuration packages="com.amazonaws.services.lambda.runtime.log4j2">
    <Appenders>
        <Lambda name="Lambda">
            <JsonLayout
                    compact="true"
                    eventEol="true"
                    objectMessageAsJsonObject="true"
                    properties="true"/>
        </Lambda>
    </Appenders>
    <Loggers>
        <Root level="info">
            <AppenderRef ref="Lambda"/>
        </Root>
    </Loggers>
</Configuration>
```