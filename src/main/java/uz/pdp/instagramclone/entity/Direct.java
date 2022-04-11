package uz.pdp.instagramclone.entity;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.util.List;


@AllArgsConstructor
@NoArgsConstructor
@Data
@Entity
public class Direct {
    // nimasi dir kami bor
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    private User receiver; // kiming directi ekanligi :

    @ManyToOne
    private User sender;

    @OneToMany
    private List<Message> messages;

}
