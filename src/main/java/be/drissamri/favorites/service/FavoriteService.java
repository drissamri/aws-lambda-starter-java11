package be.drissamri.favorites.service;

import be.drissamri.favorites.dto.AddFavoriteRequest;
import be.drissamri.favorites.model.Favorite;

import java.util.UUID;

public class FavoriteService {

    public Favorite add(AddFavoriteRequest request) {
        Favorite newFavorite = new Favorite();
        newFavorite.setId(UUID.randomUUID().toString());
        newFavorite.setName(request.getName());

        return Favorite.save(newFavorite);
    }
}
