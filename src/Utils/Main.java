package Utils;

import Exceptions.InvalidCommandException;
import Pieces.Piece;
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
        // citeste datele din fisierele de intrare
        // initializeaza colectiile de utilizatori si jocuri

        // mai intai citesti jocurile gameMap

        Path path = Path.of("src", "Teste", "TesteIndividuale", "games.json");
        System.out.println("Path of games : " + path.toAbsolutePath());

        gameMap = JsonReaderUtil.readGamesAsMap(path);

        int maxId= 0;
        for(Integer i : gameMap.keySet()){
            if(i > maxId)
                maxId = i;
        }
        lastGameId = maxId + 1;


        System.out.println("Game size = " + lastGameId);

        // dupa citesti userii si pentru fiecare user
        // trebuie sa te uiti in activeGamesGIDS pentru a le pune in lista de jocuri ale userilor
        path = Path.of("src", "Teste", "TesteIndividuale", "accounts.json");
        System.out.println("Path of accounts : " + path.toAbsolutePath());
        userList = JsonReaderUtil.readAccounts(path);

        for(User user : userList){
            List<Integer> GIDList = user.getActiveGameGIDs();
            for(Integer gid : GIDList) {
                if (gameMap.containsKey(gid)) {
                    Game game = gameMap.get(gid);
                    user.addGame(game);

                    // pune pentru fiecare user owned pieces
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


                    // pune pentru jucatorul jocului aferent punctele care trebuiesc
                    // de asemenea captured pieces
                    if (game.board.pieces.size() != 32) {
                        Board newBoard = new Board();
                        newBoard.initialize();

                        List<Piece> pieseLipsa = getNonCommonPieces(new Board(game.board.pieces), newBoard);
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
            // TODO : vezi de ce se intampla asta
            System.out.println("ERROR : when reading, both boards are empty");
        }
        return ans;
    }

    public void write(){
        /// scrie in fisierele JSON starea curenta a util si a jocurilor
        //ex : puncte, jocuri noi, jocuri sterse etc

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
        // cauta in colectia interna utilizatorul care are credentialele
        // daca reuseste seteaza utilizatorul curent si returneaza obiectul User coresp

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

    public void handleEndGame(Game game) {
        switch(game.endGameState) {
            case 2:{
                System.out.println("Playerul a castigat prin mat");
                currentUser.setPoints(currentUser.getPoints() + game.player.getPoints() + 300);
                // opponent -=300;
                break;
            }
            case 1:{
                System.out.println("Playerul a castigat prin ff");
                currentUser.setPoints(currentUser.getPoints() + game.player.getPoints() + 150);
                // opponent -=150;
                break;
            }
            case 0:{
                System.out.println("Jocul s-a terminat prin remiza");
                currentUser.setPoints(currentUser.getPoints() + game.player.getPoints());
                break;
            }
            case -1:{
                System.out.println("Playerul a pierdut prin ff");
                currentUser.setPoints(currentUser.getPoints() + game.player.getPoints() - 150);
                break;
            }
            case -2:{
                System.out.println("Playerul a pierdut prin mat");
                currentUser.setPoints(currentUser.getPoints() + game.player.getPoints() - 300);
                break;
            }

        }

        gameMap.remove(game.gameId);
        currentUser.removeGame(game);
    }

    private void handleInput(String in, int step) throws InvalidCommandException{

        switch (step){
            case 1:{
                if(in.equals("1") || in.equals("2"))
                    return;
                throw new InvalidCommandException("Comanda invalida");
            }

            case 2:{
                if(in.equals("1") || in.equals("2") || in.equals("3"))
                    return;
                throw new InvalidCommandException("Please select a valid option");
            }

            case 3:{
                if(in.equals("BLACK") || in.equals("WHITE"))
                    return;
                throw new InvalidCommandException("Please provide a valid color");
            }

            case 4:{
                int ans = 0;
                try {
                    ans = Integer.parseInt(in);
                } catch (NumberFormatException e) {
                    throw new InvalidCommandException("Please provide a valid index");
                }

                if(ans < 0 && ans !=-1)
                    throw new InvalidCommandException("Please provde a valid index");

                return;
            }

            case 5:{
                if(in.equals("1") || in.equals("2") || in.equals("3"))
                    return;
                throw new InvalidCommandException("Please provide a valid choice");
            }

        }

    }

    public static void main(String[] args) throws IOException, ParseException, InterruptedException {
        Main.getChessGame().read();

        Main.getChessGame().run();
        Main.getChessGame().write();
    }
}
