package mk.com.store.games.gamestore.model;


import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.mapping.Field;

import lombok.Data;
import lombok.Getter;
import lombok.NoArgsConstructor;

@NoArgsConstructor
@Getter
@Data
@Document(value = "studios")
public class Developer {

    @Id
    String id;

    @Field
    String name;

    public Developer(String name){
        this.name = name;
    }
}