package mk.com.store.games.gamestore.model.exception;

public class GameNotFoundException extends Exception{
    public GameNotFoundException() {
        super("Could not find the specified game");
    }
}