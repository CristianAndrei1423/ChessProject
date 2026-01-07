package Utils;

import Exceptions.InvalidCommandException;
import Exceptions.InvalidMoveException;
import Pieces.Piece;
import UIPanels.GamePanel;

import java.util.*;

import static UIPanels.MainFrame.gameFrame;

public class Game {
    int gameId;
    Board board;
    Player player, opponent;
    List<Move> moveList;
    public Colors currentPlayerColor;
    public int currentPlayerInd;
    public boolean gameStillValid;
    public int endGameState;

    public Game(int gameId){
        this.gameId = gameId;
        moveList = new ArrayList<Move>();
        board = new Board();
        gameStillValid = true;
        // indicate a new game
        currentPlayerInd = -1;
        currentPlayerColor = Colors.WHITE;
    }

    public Game(int gameId, Board board){
        this.gameId = gameId;
        moveList = new ArrayList<Move>();
        this.board = board;
        gameStillValid = true;
    }

    public List<Move> getMoveList() {
        return moveList;
    }

    public void initalizeBoard(){
        board.initialize();
    }

    public void initalizeOwnedPieces(){
        if(player.pieceColor == Colors.WHITE){
            for(ChessPair<Position, Piece> cp : board.pieces){
                if(cp.getValue().getColor() == Colors.WHITE){
                    player.addOwnedPiece(cp);
                } else{
                    opponent.addOwnedPiece(cp);
                }
            }

        }
        else{
            for(ChessPair<Position, Piece> cp : board.pieces){
                if(cp.getValue().getColor() == Colors.WHITE){
                    opponent.addOwnedPiece(cp);
                } else{
                    player.addOwnedPiece(cp);
                }
            }
        }
    }

    public Board getBoard(){
        return board;
    }

    public Player getPlayer(){
        return player;
    }
    public Player getOpponent(){
        return opponent;
    }

    public void start(boolean newGame){
        // incepe un joc nou de sah
       // if(newGame){
            // board.initialize();
            // gameStillValid = true;

            // aici trebuie sa pun in owned pieces
            // pentru fiecare

//            if(player.pieceColor == Colors.WHITE){
//                for(ChessPair<Position, Piece> cp : board.pieces){
//                    if(cp.getValue().getColor() == Colors.WHITE){
//                        player.addOwnedPiece(cp);
//                    } else{
//                        opponent.addOwnedPiece(cp);
//                    }
//                }
//
//            }
//            else{
//                for(ChessPair<Position, Piece> cp : board.pieces){
//                    if(cp.getValue().getColor() == Colors.WHITE){
//                        opponent.addOwnedPiece(cp);
//                    } else{
//                        player.addOwnedPiece(cp);
//                    }
//                }
//            }

//            System.out.println("Game started !");
//
//              currentPlayerInd = 1;
        //}

//        System.out.println("Commands : ");
//        System.out.println(" - Get possible moves for piece : (Position)");
//        System.out.println(" - Move piece (fromPosition-toPosition)");
//        System.out.println(" - Forfeit (ff)");
//        System.out.println(" - Leave game (leave)");

//        while(true){
//            // first check whether it's the player or the opponent's turn
//            Player curPlayer;
//
//            if(currentPlayerInd % 2 == 1){
//                if(player.pieceColor == Colors.WHITE)
//                    curPlayer = player;
//                else curPlayer = opponent;
//            }
//            else{
//                if(player.pieceColor == Colors.WHITE)
//                    curPlayer = opponent;
//                else curPlayer = player;
//            }
//
//            // System.out.println(board.toString(curPlayer.pieceColor));
//            // update board
//
//
//            for(ChessPair<Position, Piece> cp : board.pieces){
//                System.out.print(cp.getValue().toString() + " ");
//            }
//
//            System.out.println();
//
//            if(curPlayer.name.equals("computer")){
//
//                if(runForComputer())
//                    continue;
//                return;
//            }
//            else{
//
////                if(runForPlayer())
////                    // jocul se continua normal
////                    continue;
//                return;
//            }
//        }
    }

    private boolean last3MovesSame() {
        if(moveList.size() < 6)
            return false;
        
        int n = moveList.size();
        for(int i = 0 ;i<3;i++){
            if(moveList.get(n-i-1) != moveList.get(n-i-3))
                return false;
        }
        return true;
    }


    public void handleMove(Move move) {
        Piece ps = board.getPieceAt(move.getFrom());

        Player p = (ps.getColor() == player.pieceColor ? player : opponent);
        Player o = (ps.getColor() == player.pieceColor ? opponent : player);

        p.makeMove(move.getFrom(), move.getTo(), board, this, o);


    }

    public boolean runForComputer(){
        List<Move> possibleMoves = new ArrayList<Move>();

        List<ChessPair<Position, Piece>> ownPieces = opponent.getOwnedPieces();

        for(ChessPair<Position, Piece> cp : ownPieces){
            Piece piece = cp.getValue();
            List<Position> positionList = piece.getPossibleMoves(board);
            for(Position ps : positionList){
                possibleMoves.add(new Move(currentPlayerColor, piece.getPosition()
                        , ps));
            }
        }

        Random rand = new Random();

        if(possibleMoves.size() == 0){
            System.out.println();
        }

        int ind = rand.nextInt(possibleMoves.size());
        Move move = possibleMoves.get(ind);

        gameFrame.GamePanel.onMoveMade(move);
        //opponent.makeMove(move.getFrom(), move.getTo(), board, this, player);

        // si daca prin miracol ajunge sa ti dea mat
        // pe langa ca esti cam praf

        if(checkForCheckMate(player)){
            handleEndOfGame(-2);
            return false;
        }

        // Check if last 3 moves were the same
        if(last3MovesSame()){
            System.out.println("Last 3 moves the same");
            handleEndOfGame(0);
            return false;
        }

        // switchPlayer();
        return true;
    }

    public void switchPlayer(){
        currentPlayerInd++;
        currentPlayerColor = currentPlayerColor == Colors.WHITE ? Colors.BLACK : Colors.WHITE;
    }

    public boolean checkForCheckMate(Player player){
        // trebuie sa vad daca playerul dat ca param are miscari valide
        List<ChessPair<Position,Piece>> cpList = player.getOwnedPieces();

        for(ChessPair<Position,Piece> cp : cpList){
            List<Position> mL = cp.getValue().getPossibleMoves(board);
            if(!mL.isEmpty())
                return false;
        }

        return true;
    }

    public void addMove(Player p, Position from, Position to){
        moveList.add(new Move(p.pieceColor, from, to));
    }

    public void addMove(Move move){
        moveList.add(move);
    }

    public void setPlayers(List<Player> players){
        if(players.getFirst().name.equals("computer"))
        {
            opponent = players.getFirst();
            player = players.getLast();
        }
        else {
            player = players.getFirst();
            opponent = players.getLast();
        }
    }

    public void setCurrentPlayerColor(Colors color){
        currentPlayerColor = color;
    }

    public void setBoard(TreeSet<ChessPair<Position, Piece>> boardList){
        board = new Board(boardList);
    }

    public void handleEndOfGame(int state){
        // jocul marcat ca finalizat
        System.out.println("Game is over");
        gameStillValid = false;
        // state :
        // 2 - playerul a castigat prin mat
        // 1 - playerul a castigat prin ff
        // 0 - remiza / pat
        // -1 - playerul a dat ff
        // -2 - playerul a pierdut prin mat
        endGameState = state;
    }

    public void handleExitGame(Game game){
        Main.getChessGame().write();

        if(game.gameStillValid){
            Main.getChessGame().gameMap.remove(Main.getChessGame().lastGameId-1);
            Main.getChessGame().gameMap.put(Main.getChessGame().lastGameId-1, game);
        }
        else Main.getChessGame().handleEndGame(game);
    }

    @Override
    public String toString(){
        String str = "Game id : " + this.gameId + " between " + player.toString() + " and " + opponent.toString() + "\n";
        // return str;
        return str;
    }

    private int handleInput(String in) throws InvalidCommandException {
        if(in.length() >= 3 && in.charAt(2) == '-')
            return 1;

        if(in.equals("ff"))
            return 3;

        if(in.length() == 2){
            return 2;
        }

        if(in.equals("leave"))
            return 4;

        throw new InvalidCommandException("Invalid command");

    }

}
