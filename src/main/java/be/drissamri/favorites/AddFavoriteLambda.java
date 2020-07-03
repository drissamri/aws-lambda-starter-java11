package be.drissamri.favorites;

import be.drissamri.favorites.dto.AddFavoriteRequest;
import be.drissamri.favorites.model.Favorite;
import be.drissamri.favorites.service.FavoriteService;
import be.drissamri.favorites.config.AppConfig;
import com.amazonaws.services.lambda.runtime.events.APIGatewayV2HTTPEvent;
import com.amazonaws.services.lambda.runtime.events.APIGatewayV2HTTPResponse;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;


public class AddFavoriteLambda {
    //  private static Logger logger = LogManager.getLogger();

    private static ObjectMapper objectMapper;
    private static FavoriteService favoriteService;

    public AddFavoriteLambda() {
        AppConfig.initialize();
        objectMapper = AppConfig.objectMapper();
        favoriteService = AppConfig.favoriteService();
    }

    public APIGatewayV2HTTPResponse handle(APIGatewayV2HTTPEvent input) {
        AddFavoriteRequest request = getRequest(input);
        Favorite savedFavorite = favoriteService.add(request);
        //   logger.info("Favorite: {}", savedFavorite);

        APIGatewayV2HTTPResponse response = new APIGatewayV2HTTPResponse();
        response.setBody("Favorite: " + savedFavorite.getId());
        response.setStatusCode(200);
        return response;
    }

    private AddFavoriteRequest getRequest(APIGatewayV2HTTPEvent input) {
        try {
            return objectMapper.readValue(input.getBody(), AddFavoriteRequest.class);
        } catch (JsonProcessingException e) {
            throw new RuntimeException("Failed marshalling");
        }
    }
}