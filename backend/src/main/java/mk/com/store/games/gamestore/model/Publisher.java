package mk.com.store.games.gamestore.model;


import java.util.ArrayList;
import java.util.List;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.DBRef;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.mapping.Field;

import lombok.Data;
import lombok.Getter;
import lombok.NoArgsConstructor;

@NoArgsConstructor
@Getter
@Data
@Document(value = "publishers")
public class Publisher {
    
    @Id
    private String id;

    @Field
    private String name;

    @Field
    private String description;

    @DBRef
    private List<Developer> studios;

    public Publisher(String name, String description){
        this.name = name;
        this.description = description;
        studios = new ArrayList<>();
    }
}