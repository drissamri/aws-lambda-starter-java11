package com.drissamri.favorites.service;

import com.drissamri.favorites.dto.AddFavoriteRequest;
import com.drissamri.favorites.model.Favorite;

import java.util.UUID;

public class FavoriteService {
    private final DynamoDbService dynamoDbService;

    public FavoriteService(DynamoDbService dynamoDbService) {
        this.dynamoDbService = dynamoDbService;
    }

    public Favorite add(AddFavoriteRequest request) {
        Favorite newFavorite = new Favorite();
        newFavorite.setId(UUID.randomUUID().toString());
        newFavorite.setName(request.getName());

        return dynamoDbService.save(newFavorite);
    }
}
