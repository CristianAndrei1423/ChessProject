package Utils;

import Pieces.Piece;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;

import java.awt.*;
import java.io.IOException;
import java.io.Writer;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.*;
import java.util.List;

public final class JsonWriterUtil {

    private JsonWriterUtil (){

    }

    public static void writeAccounts(Path path, List<User> accounts) throws IOException{

        if(path == null || !Files.exists(path)){
            throw new IOException("File does not exist");
        }

        try (Writer writer = Files.newBufferedWriter(path, StandardCharsets.UTF_8)) {

            JSONArray jsonAccounts = new JSONArray();

            for (User u : accounts) {
                JSONObject acc = new JSONObject();
                acc.put("email", u.getEmail());
                acc.put("password", u.getPassword());
                acc.put("points", u.getPoints());

                JSONArray gameArray = new JSONArray();
                for (Game g : u.getActiveGames()) {
                    gameArray.add(g.gameId);
                }

                acc.put("games", gameArray);

                jsonAccounts.add(acc);
            }

            writer.write(jsonAccounts.toJSONString());
        }
    }


    public static void writeGamesAsMap(Path path, Map<Integer, Game> map) throws IOException{

        if(path == null || !Files.exists(path))
            throw new IOException("File does not exist");

        try (Writer writer = Files.newBufferedWriter(path, StandardCharsets.UTF_8)) {

            // array e cu []
            // object e cu {}

            JSONArray games = new JSONArray();

            for(Integer i : map.keySet()){
                JSONObject gameObj = new JSONObject();

                Game game = map.get(i);

                gameObj.put("id", game.gameId);

                // put players as in example
                JSONArray playerList = new JSONArray();

                JSONObject player1 = new JSONObject();
                player1.put("email", game.player.name);
                player1.put("color", game.player.pieceColor.toString());

                JSONObject player2 = new JSONObject();
                player2.put("email", game.opponent.name);
                player2.put("color", game.opponent.pieceColor.toString());

                playerList.add(player1);
                playerList.add(player2);
                gameObj.put("players", playerList);
                //-------------------------------------------

                gameObj.put("currentPlayerColor", game.currentPlayerColor.toString());

                // array e cu []
                // object e cu {}

                // put each and every piece from board
                JSONArray board = new JSONArray();
                for(ChessPair<Position, Piece> cp : game.board.pieces){
                    JSONObject piece = new JSONObject();
                    Piece p = cp.getValue();

                    piece.put("type", String.valueOf(p.type()));
                    piece.put("color", p.getColor().toString());
                    piece.put("position", p.getPosition().toString());

                    board.add(piece);
                }

                gameObj.put("board", board);
                // ----------------------------------------------


                // put moves
                JSONArray movesList = new JSONArray();

                for(Move m : game.moveList){
                    JSONObject moveObj = new JSONObject();
                    moveObj.put("playerColor", m.getPlayerColor().toString());
                    moveObj.put("from", m.getFrom().toString());
                    moveObj.put("to", m.getTo().toString());
                    movesList.add(moveObj);
                }
                gameObj.put("moves", movesList);
                // -------------------------------------------

                games.add(gameObj);
            }

            writer.write(games.toJSONString());
        }

    }

}
