package com.sg.guessnumber.view;
import com.sg.guessnumber.models.Game;
import com.sg.guessnumber.models.Round;

import java.util.List;

public interface RoundDao {

    //create a round - make a guess
    List<Round> guess(Game game, String guess);

    //read all rounds
    List<Round> getAllRounds(int GameID);


}
