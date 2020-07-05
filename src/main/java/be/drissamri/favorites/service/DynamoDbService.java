package be.drissamri.favorites.service;

import be.drissamri.favorites.model.Favorite;
import software.amazon.awssdk.enhanced.dynamodb.DynamoDbTable;
import software.amazon.awssdk.enhanced.dynamodb.Key;

public class DynamoDbService {
    private static DynamoDbTable<Favorite> FAVORITES_TABLE;

    public DynamoDbService(DynamoDbTable<Favorite> table) {
        init(table);
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

    public void init(DynamoDbTable<Favorite> favoritesTables) {
        FAVORITES_TABLE = favoritesTables;
        initMarshallers();
    }

    /**
     * Eager initialize AWS SDK Marshallers by doing a call
     */
    private void initMarshallers() {
        this.get("non-existing-key");
    }
}
