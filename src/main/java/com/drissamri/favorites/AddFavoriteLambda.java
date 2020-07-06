package com.drissamri.favorites;

import com.amazonaws.services.lambda.runtime.Context;
import com.amazonaws.services.lambda.runtime.RequestHandler;
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
import software.amazon.awssdk.http.HttpStatusCode;

import java.util.Map;

public class AddFavoriteLambda implements RequestHandler<APIGatewayV2HTTPEvent, APIGatewayV2HTTPResponse> {
    private static Logger LOG = LoggerFactory.getLogger(AddFavoriteLambda.class);
    private ObjectMapper objectMapper;
    private FavoriteService favoriteService;

    public AddFavoriteLambda() {
        this(AppConfig.favoriteService(), AppConfig.objectMapper());
    }

    public AddFavoriteLambda(FavoriteService favoriteService, ObjectMapper objectMapper) {
        this.favoriteService = favoriteService;
        this.objectMapper = objectMapper;
    }

    @Override
    public APIGatewayV2HTTPResponse handleRequest(APIGatewayV2HTTPEvent input, Context context) {
        Favorite savedFavorite = favoriteService.add(parseRequest(input));
        LOG.info("Favorite created: {}", savedFavorite);

        APIGatewayV2HTTPResponse response;
        try {
            response = APIGatewayV2HTTPResponse.builder()
                    .withStatusCode(HttpStatusCode.OK)
                    .withBody(objectMapper.writeValueAsString(savedFavorite))
                    .build();
        } catch (Exception ex) {
            LOG.error("Exception: {}", ex.getMessage());
            response = createErrorResponse();
        }

        response.setHeaders(Map.of("Content-Type", "application/json"));
        return response;
    }

    private APIGatewayV2HTTPResponse createErrorResponse() {
        return APIGatewayV2HTTPResponse.builder()
                .withStatusCode(HttpStatusCode.INTERNAL_SERVER_ERROR)
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