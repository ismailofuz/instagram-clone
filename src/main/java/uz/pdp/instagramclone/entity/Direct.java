package uz.pdp.instagramclone.entity;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.OneToOne;
import java.util.UUID;

@AllArgsConstructor
@NoArgsConstructor
@Data
@Entity
public class Direct {
    @Id
    private UUID id = UUID.randomUUID();
    // kimga tegishliligi bo'ladi
    @OneToOne// har bir odamda bitta direct bo'ladi
    private User user;

}
