package com.sg.guessnumber.controllers;

import com.sg.guessnumber.view.GameDao;
import com.sg.guessnumber.view.RoundDao;
import com.sg.guessnumber.models.Game;
import com.sg.guessnumber.models.Round;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/BullsAndCows")
public class Controller {

    private final GameDao gDao;
    private final RoundDao rDao;

    public Controller(GameDao gDao, RoundDao rDao) {
        this.gDao = gDao;
        this.rDao = rDao;
    }

    //begin game - POST
    @PostMapping("/begin")
    @ResponseStatus(HttpStatus.CREATED)
    public Game begin(){    //CREATE A NEW GAME
        Game newGame = new Game();
        gDao.begin(newGame);
        return newGame;
    }

    //make a guess - POST
    // - NEED TO MAKE SURE IT UPDATES TRUE/FALSE
    @PostMapping("guess/{GameID}/{guess}")
    public Round rGuess(@PathVariable int GameID, @PathVariable String guess){

        List<Round> rs = rDao.guess(gDao.findById(GameID), guess);
        gDao.update(gDao.findById(GameID));
        if(rs == null)
            return null;

        return rs.get(0);
    }

    //return list of all games - GET
    @GetMapping("/AllGames")
    public List<Game> getAllGames(){

        List<Game> list = gDao.getAllGames();
        for(int i = 0; i< list.size();i++){
            Game g = list.get(i);
            if(g.isFinished() == false) {
                g.setAnswer("****");
            }
            list.set(i, g);
        }

        return list;
    }

    //return specific game by id - GET
    @GetMapping("GetGame/{GameID}")
    public Game gameById(@PathVariable int GameID){
        Game game = gDao.findById(GameID);
        if(game.isFinished() == false)
            game.setAnswer("****");
        return game;
    }

    //returns list of rounds for specific game sorted by "time" (id to make it easier) - GET
    @GetMapping("/Rounds/{GameID}")
    public List<Round> getRoundsByID(@PathVariable int GameID){
        List<Round> rs = rDao.getAllRounds(GameID);
        if(rs == null)
            return null;
        return rs;
    }


}
