package com.sg.guessnumber.view;

import com.sg.guessnumber.models.Game;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.stereotype.Repository;

import java.sql.*;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;


@Repository
public class GameDatabaseDao implements GameDao {

    private final JdbcTemplate jdbcTemplate;

    @Autowired
    public GameDatabaseDao(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    @Override
    public Game begin(Game game) {
        String rs = "";
        String finalRs =  generateAnswer(rs);

        final String sql = "INSERT INTO Game(Answer, IsFinished) VALUES(?,?);";
        GeneratedKeyHolder keyHolder = new GeneratedKeyHolder();
        jdbcTemplate.update((Connection conn) -> {

            PreparedStatement statement = conn.prepareStatement(
                    sql,
                    Statement.RETURN_GENERATED_KEYS);

            statement.setString(1, finalRs);
            statement.setBoolean(2, game.isFinished());
            return statement;

        }, keyHolder);

        game.setGameID(keyHolder.getKey().intValue());
        game.setAnswer("****");

        return game;
    }

    private String generateAnswer(String rs) {
        ArrayList<Integer> numbers = new ArrayList<>();
        for(int i = 0; i < 10; i++){
            numbers.add(i);
        }
        Collections.shuffle(numbers);

        for(int i = 0; i < 4; i++)
            rs += numbers.get(i);

        return rs;
    }

    @Override
    public List<Game> getAllGames() {
        final String sql = "Select * from Game;";
        List<Game> gameList = jdbcTemplate.query(sql, new GameMapper());
        return gameList;
    }


    @Override
    public Game findById(int gameID) {
        final String sql = "Select * from Game where GameID = ?;";
        return jdbcTemplate.queryForObject(sql, new GameMapper(), gameID);
    }

    @Override
    public boolean update(Game game) {
        final String sql = "UPDATE Game SET "
                + "Answer = ?, "
                + "IsFinished = ? "
                + "WHERE GameID = ?;";

        return jdbcTemplate.update(sql,
                game.getAnswer(),
                game.isFinished(),
                game.getGameID()) > 0;
    }

    private static final class GameMapper implements RowMapper<Game> {

        @Override
        public Game mapRow(ResultSet rs, int index) throws SQLException {
            Game g = new Game();
            g.setGameID(rs.getInt("GameID"));
            g.setAnswer((rs.getString("Answer")));
            g.setFinished((rs.getBoolean("IsFinished")));
            return g;
        }

    }
}
