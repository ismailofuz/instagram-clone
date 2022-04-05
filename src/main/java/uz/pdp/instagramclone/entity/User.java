package uz.pdp.instagramclone.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import javax.persistence.*;
import javax.validation.constraints.Email;
import java.sql.Timestamp;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.UUID;

@AllArgsConstructor
@NoArgsConstructor
@Data
@Entity(name = "users")
public class User {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;

    @Column(nullable = false)
    private String name;

    @Column(unique = true,nullable = false)
    private String username;

    @Column(nullable = false,unique = true)
    private String phoneNumber;

    @Column(nullable = false)
    private String password;

    @Email // unique
    @Column(unique = true)
    private String email; // optional

    private String website; // optional

    private String bio; // optional

    @OneToOne
    private Attachment profilePhoto; //optional

    @CreationTimestamp // doim new bo'lganda saqlaydi
    private Timestamp createdAt;

    @UpdateTimestamp // oxirgi edit bo'lgan vaqtni saqlaydi edit bo'lgan
    private Timestamp lastUpdatedAt;

    // stories qo'shiladi

    @JsonIgnore // bu ko'rimasligi uchun
    @OneToMany(mappedBy = "user")
    private List<Story> stories;

    // followerlar va following :

    @ManyToMany(cascade = CascadeType.ALL)
    private Set<User> following;

    @ManyToMany(cascade = CascadeType.ALL)
    private Set<User> followers;

    // hozir live qilayaptimi yo'qmi ?
    private boolean isLive = false; // default false

    @ManyToMany
    private Set<Post> likedPosts;

    @ManyToMany
    private Set<Post> savedPosts;

}
