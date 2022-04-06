package uz.pdp.instagramclone.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import javax.persistence.*;
import javax.validation.constraints.Email;
import javax.validation.constraints.Size;
import java.sql.Timestamp;
import java.util.*;

@AllArgsConstructor
@NoArgsConstructor
@Data
@Entity(name = "users")
public class User implements UserDetails {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;

    @Column(nullable = false)
    private String name;

    @Column(unique = true,nullable = false)
    private String username;

    @Column(unique = true)
    private String phoneNumber;

    @Column(nullable = false)
    @Size(min = 6,message = "Password must be more than 6")
    private String password;

    @Email // unique
    @Column(unique = true)
    private String email; // optional

    private String website; // optional

    private String bio; // optional

    @OneToOne
    private Attachment profilePhoto;

    @CreationTimestamp // doim new bo'lganda saqlaydi
    private Timestamp createdAt;

    @UpdateTimestamp // oxirgi edit bo'lgan vaqtni saqlaydi edit bo'lgan
    private Timestamp lastUpdatedAt;

    // stories qo'shiladi

    @JsonIgnore // bu ko'rimasligi uchun
    @OneToMany(mappedBy = "user")
    private List<Story> stories;

     @JsonIgnore // bu ko'rimasligi uchun
    @OneToMany(mappedBy = "user")
    private List<Post> posts;



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



    public User(String name, String username, String password, String email, String phoneNumber) {
        this.name = name;
        this.username = username;
        this.password = password;
        this.email = email;
        this.phoneNumber = phoneNumber;
    }

    private  boolean accountNonExpired = true;
    private  boolean accountNonLocked = true;
    private  boolean credentialsNonExpired = true;
    private  boolean enabled ;


    //=============================Spring security always needs

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return null; // ahamiyatsz
    }

    @Override
    public boolean isAccountNonExpired() {
        return accountNonExpired;
    }

    @Override
    public boolean isAccountNonLocked() {
        return accountNonLocked;
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return credentialsNonExpired;
    }

    @Override
    public boolean isEnabled() {
        return enabled;
    }

    private String emailCode;
}
