package com.drissamri.favorites.service;

import com.drissamri.favorites.model.Favorite;
import software.amazon.awssdk.enhanced.dynamodb.DynamoDbTable;
import software.amazon.awssdk.enhanced.dynamodb.Key;

public class DynamoDbService {
    private static DynamoDbTable<Favorite> FAVORITES_TABLE;

    public DynamoDbService(DynamoDbTable<Favorite> table) {
        FAVORITES_TABLE = table;
    }

    public Favorite save(Favorite favorite) {
        FAVORITES_TABLE.putItem(favorite);
        return favorite;
    }

    public Favorite get(String id) {
        Key key = Key.builder()
                .partitionValue(id)
                .build();
        return FAVORITES_TABLE.getItem(key);
    }
}
