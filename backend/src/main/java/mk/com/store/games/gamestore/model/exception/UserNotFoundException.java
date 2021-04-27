package mk.com.store.games.gamestore.model.exception;

public class UserNotFoundException extends Exception {
    public UserNotFoundException(){
        super("Could not find the specified user");
    }
}