package com.sg.guessnumber.view;
import com.sg.guessnumber.models.Game;

import java.util.List;

public interface GameDao {
    //create a game
    Game begin(Game game);

    //read all games
    List<Game> getAllGames();

    //find a game
    Game findById(int gameID);

    //update a game
    boolean update(Game game);

}
