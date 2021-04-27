package mk.com.store.games.gamestore.model.exception;

public class DeveloperNotFoundException extends Exception {
    public DeveloperNotFoundException() {
        super("Could not find developer with the specified id");
    }
}