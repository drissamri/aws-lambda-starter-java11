package com.drissamri.favorites.config;

import com.drissamri.favorites.model.Favorite;
import com.drissamri.favorites.service.DynamoDbService;
import com.drissamri.favorites.service.FavoriteService;
import com.fasterxml.jackson.databind.ObjectMapper;
import software.amazon.awssdk.auth.credentials.EnvironmentVariableCredentialsProvider;
import software.amazon.awssdk.core.client.config.ClientOverrideConfiguration;
import software.amazon.awssdk.enhanced.dynamodb.DynamoDbEnhancedClient;
import software.amazon.awssdk.enhanced.dynamodb.DynamoDbTable;
import software.amazon.awssdk.enhanced.dynamodb.TableSchema;
import software.amazon.awssdk.http.urlconnection.UrlConnectionHttpClient;
import software.amazon.awssdk.regions.Region;
import software.amazon.awssdk.services.dynamodb.DynamoDbClient;

public class AppConfig {
    private static final String TABLE_NAME = System.getenv("TABLE_NAME");

    private static DynamoDbEnhancedClient enhancedDynamoDbClient() {
        DynamoDbClient dynamoDbClient = DynamoDbClient.builder()
                .httpClient(UrlConnectionHttpClient.builder().build())
                .credentialsProvider(EnvironmentVariableCredentialsProvider.create())
                .overrideConfiguration(ClientOverrideConfiguration.builder().build())
                .region(Region.of(System.getenv("REGION")))
                .build();
        return DynamoDbEnhancedClient.builder().dynamoDbClient(dynamoDbClient).build();
    }

    public static ObjectMapper objectMapper() {
        return new ObjectMapper();
    }

    public static DynamoDbService dynamoDbService() {
        DynamoDbEnhancedClient client = enhancedDynamoDbClient();
        DynamoDbTable<Favorite> dynamoDbTable = client.table(TABLE_NAME, TableSchema.fromBean(Favorite.class));
        return new DynamoDbService(dynamoDbTable);
    }

    public static FavoriteService favoriteService() {
        return new FavoriteService(dynamoDbService());
    }


}
