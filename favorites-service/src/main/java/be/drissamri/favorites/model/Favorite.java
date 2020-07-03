package be.drissamri.favorites.model;

import software.amazon.awssdk.enhanced.dynamodb.DynamoDbTable;
import software.amazon.awssdk.enhanced.dynamodb.Key;
import software.amazon.awssdk.enhanced.dynamodb.mapper.annotations.DynamoDbBean;
import software.amazon.awssdk.enhanced.dynamodb.mapper.annotations.DynamoDbPartitionKey;

@DynamoDbBean
public class Favorite {
    private String id;
    private String name;

    @DynamoDbPartitionKey
    public String getId() {
        return this.id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }


    /**
     * DynamoDB operations
     */
    private static DynamoDbTable<Favorite> FAVORITES_TABLE;

    public static Favorite save(Favorite favorite) {
        FAVORITES_TABLE.putItem(favorite);
        return favorite;
    }

    public static Favorite get(String id) {
        Key key = Key.builder()
                .partitionValue(id)
                .build();
        return FAVORITES_TABLE.getItem(key);
    }

    public static void init(DynamoDbTable<Favorite> favoritesTables) {
        Favorite.FAVORITES_TABLE = favoritesTables;
        initMarshallers();
    }

    /**
     * Eager initialize AWS SDK Marshallers by doing a call
     */
    private static void initMarshallers() {
       Favorite.get("non-existing-key");
    }
}
