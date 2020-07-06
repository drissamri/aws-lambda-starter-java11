package com.drissamri.favorites.service;

import org.junit.jupiter.api.Test;
import org.mockito.Mock;

class FavoriteServiceTest {
    @Mock
    private DynamoDbService dynamoDbService;
    private FavoriteService favoriteService = new FavoriteService(null);

    @Test
    public void shouldReturnNewFavoriteSuccessfully() {

    }

}