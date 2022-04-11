package uz.pdp.instagramclone.payload;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@NoArgsConstructor
@Data
public class Registration {
    private String register;
    private String fullName;
    private String username;
    private String password;
}
