package mk.com.store.games.gamestore.model.exception;

public class DeveloperNotContainedInPublisherException extends Exception {
    
    public DeveloperNotContainedInPublisherException() {
        super("Publisher does not own specified developer");
    }
    
}
