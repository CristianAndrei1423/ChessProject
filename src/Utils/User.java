package Utils;

import java.util.ArrayList;
import java.util.List;

public class User {
    private String email, password;
    private List<Game> activeGameList;
    private List<Integer> activeGameGIDs;
    private int points;

    public User(){
        activeGameList = new ArrayList<Game>();
        activeGameGIDs = new ArrayList<Integer>();
        points = 0;
    }

    public User(String email, String password, Integer points){
        this.email = email;
        this.password = password;
        activeGameList = new ArrayList<Game>();
        activeGameGIDs = new ArrayList<Integer>();
        this.points = points;
    }

    public String getEmail(){
        return email;
    }

    public boolean verifyPassword(String password){
        return this.password.equals(password);
    }

    public void addGame(Game game){
        activeGameList.add(game);
    }

    public void addGID(Integer gameID){
        activeGameGIDs.add(gameID);
    }

    // this removes gameID too
    public void removeGame(Game game){
        activeGameList.remove(game);
        activeGameGIDs.remove((Object)game.gameId);
    }

    public List<Game> getActiveGames(){
        return activeGameList;
    }

    public List<Integer> getActiveGameGIDs() { return activeGameGIDs; }

    public int getPoints(){
        return points;
    }

    public void setPoints(int points){
        this.points = points;
    }

    @Override
    public String toString(){
        return "User : " + email + "\n  Password : " + password + "\n  Points : " + ('0' + points) +
                "\n activeGamesIds : " + activeGameList;
    }

    public String getPassword(){
        return password;
    }

    public boolean equals(User user){
        return this.email.equals(user.email);
    }

}
