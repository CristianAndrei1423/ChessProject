package Utils;

import Pieces.Piece;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;

import java.awt.*;
import java.io.IOException;
import java.io.Reader;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.*;
import java.util.List;

/**
 * Utility class for reading JSON documents using JSON.simple ("simple-json").
 *
 * ###### IMPORTANT: This is just an example of how to read JSON documents using the library.
 * Your classes might differ slightly, so don’t hesitate to update this class as needed.
 *
 * Expected structures:
 * - accounts.json: an array of objects with fields: email (String), password (String), points (Number), games (array of numbers)
 * - games.json: an array of objects with fields matching the JSON provided:
 *   id (Number), players (array of {email, color}), currentPlayerColor (String),
 *   board (array of {type, color, position}), moves (array of {playerColor, from, to})
 */
public final class JsonReaderUtil {

    private JsonReaderUtil() {
    }

    /**
     * Reads the accounts from the given JSON file path.
     *
     * @param path path to accounts.json
     * @return list of Account objects (empty list if file not found or array empty)
     * @throws IOException    if I/O fails
     * @throws ParseException if JSON is invalid
     */
    public static List<User> readAccounts(Path path) throws IOException, ParseException {
        if (path == null || !Files.exists(path)) {
            return new ArrayList<>();
        }
        try (Reader reader = Files.newBufferedReader(path, StandardCharsets.UTF_8)) {
            JSONParser parser = new JSONParser();
            Object root = parser.parse(reader);
            JSONArray arr = asArray(root);
            List<User> result = new ArrayList<>();

            if (arr == null) {
                return result;
            }

            for (Object item : arr) {
                JSONObject obj = asObject(item);
                if (obj == null) {
                    continue;
                }
                User acc = new User(asString(obj.get("email")), asString(obj.get("password"))
                 , asInt(obj.get("points"),0));

                JSONArray games = asArray(obj.get("games"));

                if(games != null){
                    for(Object gid : games){
                        acc.addGID(asInt(gid,0));
                    }
                }

                result.add(acc);
            }
            return result;
        }
    }

    /**
     * Reads the games from the given JSON file path and returns them as a map by id.
     * The structure strictly follows games.json as provided (no title/genre).
     *
     * @param path path to games.json
     * @return map id -> Game (empty if file missing or array empty)
     * @throws IOException    if I/O fails
     * @throws ParseException if JSON is invalid
     */
    public static Map<Integer, Game> readGamesAsMap(Path path) throws IOException, ParseException {
        Map<Integer, Game> map = new HashMap<>();
        if (path == null || !Files.exists(path)) {
            return map;
        }
        try (Reader reader = Files.newBufferedReader(path, StandardCharsets.UTF_8)) {
            JSONParser parser = new JSONParser();
            Object root = parser.parse(reader);
            JSONArray arr = asArray(root);
            if (arr == null) return map;
            for (Object item : arr) {
                JSONObject obj = asObject(item);
                if (obj == null) {
                    continue;
                }
                int id = asInt(obj.get("id"), -1);
                if (id < 0) {
                    continue;
                }// skip invalid
                Game g = new Game(id);
                // g.setId(id);

                // players array
                JSONArray playersArr = asArray(obj.get("players"));
                if (playersArr != null) {
                    List<Player> players = new ArrayList<>();
                    for (Object pItem : playersArr) {
                        JSONObject pObj = asObject(pItem);
                        if (pObj == null) {
                            continue;
                        }
                        String email = asString(pObj.get("email"));
                        String color = asString(pObj.get("color"));
                        Colors colors = Colors.valueOf(color);
                        players.add(new Player(email, colors));

                        // aici ar trebui sa pui si owned pieces
                        // dar pun cand dau read

                    }
                    g.setPlayers(players);
                }

                // currentPlayerColor
                g.setCurrentPlayerColor(Colors.valueOf(asString(obj.get("currentPlayerColor"))));

                // board array
                JSONArray boardArr = asArray(obj.get("board"));

                if (boardArr != null) {
                    TreeSet<ChessPair<Position, Piece>> boardList = new TreeSet<ChessPair<Position, Piece>>();
                    for (Object bItem : boardArr) {
                        JSONObject bObj = asObject(bItem);
                        if (bObj == null) {
                            continue;
                        }

                        String type = asString(bObj.get("type"));
                        String color = asString(bObj.get("color"));
                        String position = asString(bObj.get("position"));
                        Position posInst = new Position();
                        Board boardInst = new Board();

                        boardList.add(new ChessPair<Position, Piece>(posInst.fromString(position),
                                boardInst.initializeTypeOfPiece(type, Colors.valueOf(color),
                                        posInst.fromString(position))));
                    }
                    g.setBoard(boardList);
                }

                // Parse optional moves array
                JSONArray movesArr = asArray(obj.get("moves"));
                if (movesArr != null) {
                    // List<Move> moves = new ArrayList<>();
                    for (Object mItem : movesArr) {
                        JSONObject mObj = asObject(mItem);
                        if (mObj == null) {
                            continue;
                        }

                        Position posInst = new Position();

                        String playerColor = asString(mObj.get("playerColor"));
                        String from = asString(mObj.get("from"));
                        String to = asString(mObj.get("to"));
                        g.addMove(new Move(Colors.valueOf(playerColor), posInst.fromString(from),
                                posInst.fromString(to)));
                    }
                    // g.setMoves(moves);
                }
                map.put(id, g);
            }
        }
        return map;
    }

    // -------- helper converters --------

    private static JSONArray asArray(Object o) {
        return (o instanceof JSONArray) ? (JSONArray) o : null;
    }

    private static JSONObject asObject(Object o) {
        return (o instanceof JSONObject) ? (JSONObject) o : null;
    }

    private static String asString(Object o) {
        return o == null ? null : String.valueOf(o);
    }

    private static int asInt(Object o, int def) {
        if (o instanceof Number) return ((Number) o).intValue();
        try {
            return o != null ? Integer.parseInt(String.valueOf(o)) : def;
        } catch (NumberFormatException e) {
            return def;
        }
    }
}
