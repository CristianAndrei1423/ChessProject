package Utils;

import Exceptions.InvalidCommandException;
import Exceptions.InvalidMoveException;
import Pieces.Piece;
import UIPanels.GameObserver;
import UIPanels.GamePanel;

import java.util.*;

import static UIPanels.MainFrame.gameFrame;

public class Game {
    public int gameId;
    Board board;
    Player player, opponent;
    List<Move> moveList;
    public String playerAlias;
    public Colors currentPlayerColor;
    public int currentPlayerInd;
    public boolean gameStillValid;
    public int endGameState;

    private List<GameObserver> observers = new ArrayList<GameObserver>();

    public void addObserver(GameObserver observer){
        observers.add(observer);
    }

    private void notifiyMoveMade(Move move){
        for(GameObserver obs : observers) {
            obs.onMoveMade(move);
        }
    }

    public Game(int gameId){
        this.gameId = gameId;
        moveList = new ArrayList<Move>();
        board = new Board();
        gameStillValid = true;
        // indicate a new game
        currentPlayerInd = -1;
        currentPlayerColor = Colors.WHITE;
        playerAlias = new String("Player");
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

    private boolean last3MovesSame() {
        if(moveList.size() < 12)
            return false;

        int n = moveList.size();
        for(int i = 0 ;i<8;i++){
            Move m1 = moveList.get(n-i-1);
            Move m2 = moveList.get(n-i-5);

            if(!m1.equals(m2))
                return false;
        }
        return true;
    }

    public void handleMove(Move move) {
        Piece ps = board.getPieceAt(move.getFrom());

        Player p = (ps.getColor() == player.pieceColor ? player : opponent);
        Player o = (ps.getColor() == player.pieceColor ? opponent : player);

        p.makeMove(move.getFrom(), move.getTo(), board, this, o);

        if(last3MovesSame()){
            System.out.println("Last 3 moves the same");
            handleEndOfGame(0);
        }
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

        // check if Computer has no moves left
        if(possibleMoves.size() == 0){
            System.out.println("Computer has no moves left");

            boolean isCheck = false;
            Position compKingPos = board.getKingPos(opponent.pieceColor);

            // Check if any of Player's pieces attack the Computer's King
            for(ChessPair<Position, Piece> cp : player.getOwnedPieces()){
                if(cp.getValue().checkForCheck(board, compKingPos)){
                    isCheck = true;
                    break;
                }
            }

            if(isCheck){
                // player wins
                handleEndOfGame(2);
            } else {
                // draw
                handleEndOfGame(0);
            }

            return false;
        }

        int ind = rand.nextInt(possibleMoves.size());
        Move move = possibleMoves.get(ind);

        notifiyMoveMade(move);

        if(checkForCheckMate(player)){
            boolean isCheck = false;
            Position playerKingPos = board.getKingPos(player.pieceColor);

            // check if any of computer's pieces attack the clayer's king
            for(ChessPair<Position, Piece> cp : opponent.getOwnedPieces()){
                if(cp.getValue().checkForCheck(board, playerKingPos)){
                    isCheck = true;
                    break;
                }
            }

            if(isCheck){
                // player lost (somehow)
                handleEndOfGame(-2);
            } else {
                // draw
                handleEndOfGame(0);
            }
            return false;
        }

        return true;
    }

    public void switchPlayer(){
        currentPlayerInd++;
        currentPlayerColor = currentPlayerColor == Colors.WHITE ? Colors.BLACK : Colors.WHITE;
    }

    public boolean checkForCheckMate(Player player){
        // check if the parameter player has valid moves left
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
        // game marked as over
        System.out.println("Game is over. State: " + state);
        gameStillValid = false;
        // state :
        // 2 - player won through mate
        // 1 - player won through forfeit
        // 0 - stalemate
        // -1 - player resigned
        // -2 - player lost through mate
        endGameState = state;
    }

    public void handleExitGame(Game game){
        Main.getChessGame().write();

        if(game.gameStillValid){
            Main.getChessGame().gameMap.remove(Main.getChessGame().lastGameId-1);
            Main.getChessGame().gameMap.put(Main.getChessGame().lastGameId-1, game);
        }
        else {
            if(!gameFrame.GamePanel.endOfGameLabelState.isVisible())
                Main.getChessGame().handleEndGame(game);
        }
    }

    @Override
    public String toString(){
        String str = "Game id : " + this.gameId + " between " + player.toString() + " and " + opponent.toString() + "\n";
        return str;
    }

}