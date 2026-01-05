package Utils;

import Exceptions.InvalidCommandException;
import Pieces.Piece;
import org.json.simple.JSONArray;
import org.json.simple.parser.ParseException;

import java.io.IOException;
import java.nio.file.Path;
import java.util.*;

public class Main {

    List<User> userList;
    Map<Integer, Game> gameMap;
    User currentUser;
    int lastGameId;

    public Main(){
        userList = new ArrayList<User>();
        gameMap = new HashMap<Integer, Game>();
        currentUser = null;
        lastGameId = 0;
    }

    public void read () throws IOException, ParseException {
        // citeste datele din fisierele de intrare
        // initializeaza colectiile de utilizatori si jocuri

        // mai intai citesti jocurile gameMap

        Path path = Path.of("src", "Teste", "TestWrite", "games.json");
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
        path = Path.of("src", "Teste", "TestWrite", "accounts.json");
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
            System.out.println("EROAREEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEE");
        }
        return ans;
    }

    public void write(){
        /// scrie in fisierele JSON starea curenta a util si a jocurilor
        //ex : puncte, jocuri noi, jocuri sterse etc

        Path pathGames = Path.of("src", "Teste", "TestWrite", "games.json");
        Path pathAcc = Path.of("src", "Teste", "TestWrite", "accounts.json");

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
        // porneste flowul aplicatiei:
        // - gestioneaza procesul de auth
        // - afiseaza meniul princ si permite util sa aleaga intre inceperea unui joc nou, cont unui joc
        // existent sau delogare

        UI.clearScreen();
        String ans;
        Scanner s = new Scanner(System.in);

        if(currentUser == null) {

            System.out.println("Welcome to ChessGame, homework for course Object Oriented Programming");

            // step 1
            while (true) {
                System.out.println("Please login (1) or make a new account (2)\n");
                ans = s.next();
                try {
                    handleInput(ans, 1);
                } catch (InvalidCommandException e) {
                    System.out.println(e);
                    continue;
                }
                break;
            }

            UI.clearScreen();
            User user = null;
            if (ans.equals("1")) {
                // login
                while (user == null) {

                    System.out.println("Please enter your email (q to quit to main menu)");
                    String email = s.next();
                    if (email.equals("q")) {
                        run();
                        return;
                    }
                    System.out.println("Please enter your password : ");
                    String pass = s.next();

                    user = login(email, pass);
                    if (user == null)
                        System.out.println("Incorrect password\n");
                }
            }
            else {
                // make new account
                while (true) {
                    System.out.println("Please enter your email (q to quit to main menu)");
                    String email = s.next();
                    if (email.equals("q")) {
                        run();
                        return;
                    }

                    if (!UI.checkOkEmail(email)) {
                        System.out.println("Please provide a valid email");
                        continue;
                    }

                    String pass;

                    while (true) {
                        System.out.println("Please enter you password : ");
                        pass = s.next();

                        System.out.println("Please re-enter your password : ");
                        String pass2 = s.next();

                        if (pass.equals(pass2))
                            break;

                        UI.clearScreen();
                        System.out.println("Password is not the same");
                    }
                    user = newAccount(email, pass, 0);

                    if (user != null) {
                        break;
                    }

                    UI.clearScreen();

                    System.out.println("Email is already in use, please provide a different email or login with this email");
                }
            }

            currentUser = user;
            UI.clearScreen();

            System.out.println("Welcome " + currentUser.getEmail() + "!");
        }

        while(true) {

            try{
                System.out.println("Menu :");
                System.out.println("(1) Start new game");
                System.out.println("(2) Games in progress");
                System.out.println("(3) Log out");

                ans= s.next();
                // step 2
                handleInput( ans, 2);
            } catch(InvalidCommandException e){
                UI.clearScreen();
                System.out.println(e);
                continue;
            }

            UI.clearScreen();

            if (ans.equals("1")) {
                System.out.println("Provide an alias for you : ");
                String alias = s.next();

                Colors color = Colors.GRAY;

                while(true) {

                    // step 3
                    String colorS;
                    try{
                        System.out.println("Provide the color you want to play with (BLACK or WHITE) ");
                        colorS = s.next();

                        handleInput(colorS, 3);
                    } catch (InvalidCommandException e){
                        System.out.println(e);
                        continue;
                    }

                    if(colorS.equals("BLACK"))
                        color = Colors.BLACK;
                    else color = Colors.WHITE;
                    break;
                }

                UI.clearScreen();

                Player player = new Player(currentUser.getEmail(), color);

                Colors opColor = (color == Colors.BLACK ? Colors.WHITE : Colors.BLACK);

                Player opp = new Player("computer", opColor);

                // make game

                Game game = new Game(lastGameId); // aici se initializeaza si board
                currentUser.addGID(lastGameId);

                lastGameId++;

                List<Player> players = new ArrayList<Player>();
                players.add(player);
                players.add(opp);
                game.setPlayers(players);

                game.setCurrentPlayerColor(color);

                currentUser.addGame(game);

                gameMap.put(lastGameId-1, game);

                game.start(true, s);

                write();

                if(game.gameStillValid){
                    gameMap.remove(lastGameId-1);
                    gameMap.put(lastGameId-1, game);
                    continue;
                }
                else handleEndGame(game);

            }
            else if (ans.equals("2")) {

                System.out.println("Available games :");

                List<Game> gameList = currentUser.getActiveGames();

                if(gameList.isEmpty()){
                    System.out.println("No games to show");
                    continue;
                }
                else{
                    for(Game game : gameList){
                        if(game != null)
                            System.out.println(game.toString() + "\n");
                    }
                }
                Game queriedGame = null;
                while(true) {

                    System.out.println("Select a game with gameID (-1 to go back to main menu)");
                    ans = s.next();

                    try{
                        //step 4
                        handleInput(ans, 4);
                    } catch(InvalidCommandException e){
                        System.out.println(e);
                        continue;
                    }


                    if(ans.equals("-1"))
                        break;

                    for (Game game : gameList) {
                        if (game != null && ans.equals("" + game.gameId)) {
                            queriedGame = game;
                            break;
                        }
                    }

                    if (queriedGame == null) {
                        System.out.println("The game id you have provided has no games associated with it");
                        continue;
                    }
                    break;
                }

                if(ans.equals("-1"))
                    continue;


                while(true){
                    try{
                        System.out.println("What do you wish to do :");
                        System.out.println("(1) See details");
                        System.out.println("(2) Continue game");
                        System.out.println("(3) Delete game");

                        ans = s.next();

                        //step 5
                        handleInput(ans, 5);
                    } catch (InvalidCommandException e){
                        System.out.println(e);
                        continue;
                    }
                    break;
                }

                if(ans.equals("1")){
                    System.out.println(queriedGame.board.toString(queriedGame.player.pieceColor));

                }
                else if(ans.equals("2")){
                    queriedGame.resume(s);
                    if(queriedGame.gameStillValid){
                        gameMap.remove(queriedGame.gameId);
                        gameMap.put(queriedGame.gameId, queriedGame);
                    }
                    else handleEndGame(queriedGame);

                } else {
                    gameList.remove(queriedGame);
                }

                write();
            }
            else {
                // log out
                currentUser = null;
                run();
                write();
                return;
            }
        }

    }

    private void handleEndGame(Game game) {
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
        Main ChessGame = new Main();
        ChessGame.read();

        ChessGame.run();
        ChessGame.write();
    }
}

