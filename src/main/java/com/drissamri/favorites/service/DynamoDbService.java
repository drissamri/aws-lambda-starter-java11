package com.drissamri.favorites.service;

import com.drissamri.favorites.model.Favorite;
import software.amazon.awssdk.enhanced.dynamodb.DynamoDbTable;
import software.amazon.awssdk.enhanced.dynamodb.Key;

public class DynamoDbService {
    private DynamoDbTable<Favorite> favoritesTable;

    public DynamoDbService(DynamoDbTable<Favorite> table) {
        this.favoritesTable = table;
    }

    public Favorite save(Favorite favorite) {
        favoritesTable.putItem(favorite);
        return favorite;
    }

    public Favorite get(String id) {
        Key key = Key.builder()
                .partitionValue(id)
                .build();
        return favoritesTable.getItem(key);
    }
}
