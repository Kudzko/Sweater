package by.kudko.domain;

import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;
import java.util.Set;

@Entity
@Getter
@Setter
public class User {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;
    private String username;
    private String password;
    private boolean active;

    @ElementCollection(targetClass = Role.class, fetch = FetchType.EAGER)
    @CollectionTable(name = "user_role", joinColumns = @JoinColumn(name = "user_id"))  // описывает что данное поле будет храниться в отдельной таблице /кот. не описан маппинг
    @Enumerated(EnumType.STRING)    // enum будет храниться в виде строки
    private Set<Role> roles;

}
