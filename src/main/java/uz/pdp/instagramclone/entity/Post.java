package uz.pdp.instagramclone.entity;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.CreationTimestamp;

import javax.persistence.*;
import javax.validation.constraints.Size;
import java.sql.Timestamp;
import java.util.List;
import java.util.UUID;

@AllArgsConstructor
@NoArgsConstructor
@Data
@Entity(name = "posts")
public class Post {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;

    @ManyToOne
    private User user;

    @CreationTimestamp // doim new bo'lganda saqlaydi
    private Timestamp createdAt;  // 24 hours check

    @Column(columnDefinition = "TEXT")  // more 255
    private String description;

    @ManyToMany
    private List<User> postLikedUsers;

    @OneToMany
    private List<Attachment> files;


}
