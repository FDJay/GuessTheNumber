package com.sg.guessnumber.view;

import com.sg.guessnumber.models.Game;
import com.sg.guessnumber.models.Round;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;
import org.springframework.jdbc.core.RowMapper;

import java.sql.*;
import java.util.List;


@Repository
public class RoundDatabaseDao implements RoundDao{

    private final JdbcTemplate jdbcTemplate;
    @Autowired
    public RoundDatabaseDao(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }


    @Override
    public List<Round> guess(Game game, String guess) {

        final String sql = "INSERT INTO Rounds(GameID, Guess, Results) VALUES(?, ?, ? );";
        String Result = calculate(guess, game.getAnswer());
        jdbcTemplate.update(sql, game.getGameID(), guess, Result);
        final String roundSql = "Select * from Rounds where GameID = ? order by RoundID desc;";
        List<Round> newRounds =  jdbcTemplate.query(roundSql, new RoundMapper(), game.getGameID());
        if( Result.charAt(2) == '4' ) {
            game.setFinished(true);
            final String sql1 = "update Game set IsFinished = true where GameID = ?;";
            jdbcTemplate.update(sql1, game.getGameID());
        }

        return newRounds;
    }

    private String calculate(String guess, String answer){
        int exact = 0;
        int partial = 0;

        for(int i =0; i< 4; i++){
            if(answer.charAt(i) == guess.charAt(i)){
                exact++;
            }
            else if(answer.contains(guess.charAt(i) + "")){
                partial++;
            }
        }

        return "e:" + exact + ":p:" + partial;
    }

    @Override
    public List<Round> getAllRounds(int GameID) {   //Method will display all rounds for specific game

        final String sql = "Select * from Rounds where GameID = ? order by RoundID ASC;";
        return jdbcTemplate.query(sql, new RoundDatabaseDao.RoundMapper(), GameID);
    }



    //add RoundMapper:
    private static final class RoundMapper implements RowMapper<Round> {

        @Override
        public Round mapRow(ResultSet rs, int index) throws SQLException {
            Round round = new Round();
            round.setRoundID(rs.getInt("RoundID"));
            round.setGameID((rs.getInt("GameID")));
            round.setTime(rs.getTimestamp("TimeOfGuess"));
            round.setGuess((rs.getString("Guess")));
            round.setResults((rs.getString("Results")));
            return round;
        }
    }
}
