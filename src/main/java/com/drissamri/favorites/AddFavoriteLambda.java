package com.drissamri.favorites;

import com.amazonaws.services.lambda.runtime.events.APIGatewayV2HTTPEvent;
import com.amazonaws.services.lambda.runtime.events.APIGatewayV2HTTPResponse;
import com.drissamri.favorites.config.AppConfig;
import com.drissamri.favorites.dto.AddFavoriteRequest;
import com.drissamri.favorites.model.Favorite;
import com.drissamri.favorites.service.FavoriteService;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class AddFavoriteLambda {
    private static Logger LOG = LoggerFactory.getLogger(AddFavoriteLambda.class);

    private static ObjectMapper objectMapper;
    private static FavoriteService favoriteService;

    public AddFavoriteLambda() {
        favoriteService = AppConfig.favoriteService();
        objectMapper = AppConfig.objectMapper();
    }

    public APIGatewayV2HTTPResponse handle(APIGatewayV2HTTPEvent input) throws JsonProcessingException {
        Favorite savedFavorite = favoriteService.add(parseRequest(input));
        LOG.info("Favorite: {}", savedFavorite);

        return APIGatewayV2HTTPResponse.builder()
                .withStatusCode(200)
                .withBody(objectMapper.writeValueAsString(savedFavorite))
                .build();
    }

    private AddFavoriteRequest parseRequest(APIGatewayV2HTTPEvent input) {
        try {
            return objectMapper.readValue(input.getBody(), AddFavoriteRequest.class);
        } catch (JsonProcessingException e) {
            throw new RuntimeException("Failed marshalling");
        }
    }
}