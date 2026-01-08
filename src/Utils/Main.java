package Utils;

import Exceptions.InvalidCommandException;
import Pieces.Piece;
import PointStrategies.*;
import UIPanels.MainFrame;
import org.json.simple.JSONArray;
import org.json.simple.parser.ParseException;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.IOException;
import java.nio.file.Path;
import java.util.*;
import java.util.List;

public class Main {

    List<User> userList;
    public Map<Integer, Game> gameMap;
    public User currentUser;
    public int lastGameId;
    private static Main ChessGame;

    private Main(){
        userList = new ArrayList<User>();
        gameMap = new HashMap<Integer, Game>();
        currentUser = null;
        lastGameId = 0;
    }

    // singleton pattern
    public static Main getChessGame(){
        // lazy initialization
        if(ChessGame == null)
            ChessGame = new Main();
        return ChessGame;
    }

    public void read () throws IOException, ParseException {
        // read from input files
        // initialize colections of users and games

        Path path = Path.of("src", "Teste", "TestOutputStd", "games.json");
        System.out.println("Path of games : " + path.toAbsolutePath());

        gameMap = JsonReaderUtil.readGamesAsMap(path);

        int maxId= 0;
        for(Integer i : gameMap.keySet()){
            if(i > maxId)
                maxId = i;
        }
        lastGameId = maxId + 1;

        System.out.println("Game size = " + lastGameId);

        path = Path.of("src", "Teste", "TestOutputStd", "accounts.json");
        System.out.println("Path of accounts : " + path.toAbsolutePath());
        userList = JsonReaderUtil.readAccounts(path);

        for(User user : userList){
            List<Integer> GIDList = user.getActiveGameGIDs();
            for(Integer gid : GIDList) {
                if (gameMap.containsKey(gid)) {
                    Game game = gameMap.get(gid);
                    user.addGame(game);

                    // put for each user it's owned pieces
                    if (game.player.pieceColor == Colors.WHITE) {
                        for (ChessPair<Position, Piece> cp : game.board.pieces) {
                            if (cp.getValue().getColor() == Colors.WHITE) {
                                game.player.addOwnedPiece(cp);
                            } else {
                                game.opponent.addOwnedPiece(cp);
                            }
                        }

                    } else {
                        for (ChessPair<Position, Piece> cp : game.board.pieces) {
                            if (cp.getValue().getColor() == Colors.WHITE) {
                                game.opponent.addOwnedPiece(cp);
                            } else {
                                game.player.addOwnedPiece(cp);
                            }
                        }
                    }

                    // for each player of game but coresponding points
                    if (game.board.pieces.size() != 32) {
                        Board newBoard = new Board();
                        newBoard.initialize();

                        // pass a copy of the pieces treeset so that it doesn't modify it in the board
                        List<Piece> pieseLipsa = getNonCommonPieces(new Board(new TreeSet<>(game.board.pieces)), newBoard);
                        for (Piece p : pieseLipsa) {
                            if (p.getColor() == game.player.pieceColor) {
                                game.opponent.addCapturedPiece(p);
                                game.opponent.setPoints(game.opponent.getPoints() + Board.pointsFromCapture(p));
                            } else {
                                game.player.addCapturedPiece(p);
                                game.player.setPoints(game.player.getPoints() + Board.pointsFromCapture(p));
                            }
                        }
                    }

                }
            }
        }
    }

    private List<Piece> getNonCommonPieces(Board board1, Board board2) {
        List<Piece> ans = new ArrayList<Piece>();

        boolean ok = true;
        while(ok){
            ok = false;
            for(ChessPair<Position, Piece> ps1 : board1.pieces){
                for(ChessPair<Position,Piece> ps2 : board2.pieces){
                    if(ps2.getValue().equals(ps1.getValue())){
                        board1.pieces.remove(ps1);
                        board2.pieces.remove(ps2);
                        break;
                    }
                }
                if(!ok)
                    break;
            }
        }

        if(board1.pieces.isEmpty()){
            for(ChessPair<Position, Piece> ps : board2.pieces) {
                ans.add(ps.getValue());
            }
        } else if(board2.pieces.isEmpty()){
            for(ChessPair<Position, Piece> ps : board1.pieces) {
                ans.add(ps.getValue());
            }
        } else{
            System.out.println("ERROR : when reading, both boards are empty");
        }
        return ans;
    }

    public void write(){
        /// write in JSON files the state of games

        Path pathGames = Path.of("src", "Teste", "TestOutputStd", "games.json");
        Path pathAcc = Path.of("src", "Teste", "TestOutputStd", "accounts.json");

        try{
            JsonWriterUtil.writeAccounts(pathAcc, userList);
            JsonWriterUtil.writeGamesAsMap(pathGames, gameMap);
        } catch (IOException e) {
            System.out.println("Files dont exist");
        }

    }

    public User login(String email, String password) {
        // search in the internal collection of user the credentials

        for(User user : userList){
            if(user.getEmail().equals(email)){
                if(user.verifyPassword(password))
                {
                    currentUser = user;
                    return user;
                }
                return null;
            }
        }
        return null;
    }

    public User newAccount(String email, String password, Integer points){
        User newUser = new User(email, password, points);

        // check if email is already in use
        for(User user : userList){
            if(user.equals(newUser))
                return null;
        }

        userList.add(newUser);
        currentUser = newUser;
        return newUser;
    }

    public void run(){
        MainFrame.gameFrame = new MainFrame();
    }

    public PointsStrategy pointStrategy(int state){
        switch(state){
            case 2 : return new PlayerWMateStrategy();
            case 1 : return new PlayerWFFStrategy();
            case 0 : return new StalemateStrategy();
            case -1 : return new PlayerLFFStrategy();
            case -2 : return new PlayerLMateStrategy();
        }
        return null;
    }

    public void handleEndGame(Game game) {
        currentUser.setPoints(currentUser.getPoints() + game.player.getPoints() +
                pointStrategy(game.endGameState).pointsDeducted());

        MainFrame.GamePanel.endOfGameLabelState.setVisible(true);
        gameMap.remove(game.gameId);
        currentUser.removeGame(game);
    }

    public static void main(String[] args) throws IOException, ParseException, InterruptedException {
        Main.getChessGame().read();

        Main.getChessGame().run();
        Main.getChessGame().write();
    }
}
