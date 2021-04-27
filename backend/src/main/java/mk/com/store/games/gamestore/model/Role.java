package mk.com.store.games.gamestore.model;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import lombok.Data;
import lombok.NoArgsConstructor;
import mk.com.store.games.gamestore.model.enumeration.ERole;

@NoArgsConstructor
@Data
@Document(value = "roles")
public class Role {
    @Id
    public String id;

    private ERole name;

    public Role(ERole name) {
        this.name = name;
    }
}