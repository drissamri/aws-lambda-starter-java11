package be.drissamri.favorites.config;

import be.drissamri.favorites.model.Favorite;
import be.drissamri.favorites.service.FavoriteService;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import software.amazon.awssdk.auth.credentials.EnvironmentVariableCredentialsProvider;
import software.amazon.awssdk.core.client.config.ClientOverrideConfiguration;
import software.amazon.awssdk.enhanced.dynamodb.DynamoDbEnhancedClient;
import software.amazon.awssdk.enhanced.dynamodb.TableSchema;
import software.amazon.awssdk.http.urlconnection.UrlConnectionHttpClient;
import software.amazon.awssdk.regions.Region;
import software.amazon.awssdk.services.dynamodb.DynamoDbClient;

public class AppConfig {
    private static String REGION = System.getenv("REGION");
    private static String TABLE_NAME = System.getenv("TABLE_NAME");
    private static DynamoDbClient dynamoDbClient;
    private static DynamoDbEnhancedClient dynamoDbEnhancedClient;
    private static ObjectMapper objectMapper;

    public static void initialize() {
        initDynamoDB();
    }

    private static void initDynamoDB() {
        dynamoDbClient = DynamoDbClient.builder()
                .httpClient(UrlConnectionHttpClient.builder().build())
                .credentialsProvider(EnvironmentVariableCredentialsProvider.create())
                .region(Region.of(REGION))
                .overrideConfiguration(ClientOverrideConfiguration.builder().build())
                .build();

        dynamoDbEnhancedClient = DynamoDbEnhancedClient.builder().dynamoDbClient(dynamoDbClient).build();
        Favorite.init(dynamoDbEnhancedClient.table(TABLE_NAME, TableSchema.fromBean(Favorite.class)));
    }

    public static ObjectMapper objectMapper() {
        if (objectMapper == null) {
            objectMapper = new ObjectMapper().disable(DeserializationFeature.FAIL_ON_IGNORED_PROPERTIES);
        }
        return objectMapper;
    }

    public static FavoriteService favoriteService() {
        return new FavoriteService();
    }


}
