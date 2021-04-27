package mk.com.store.games.gamestore.model.exception;

public class PublisherNotFoundException extends Exception {
    public PublisherNotFoundException() {
        super("Could not find a publisher with the specified id");
    }    
}