package uz.pdp.instagramclone.payload;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

@AllArgsConstructor
@NoArgsConstructor
@Data
public class UserDto {

    @NotNull(message = "name is required")
    private String name;

    @NotNull(message = "username is required")
    private String username;

    @NotNull(message = "phoneNumber is required")
    private String phoneNumber;

    @NotBlank(message ="Password required" )
    @Size(min = 6,message = "Password must be more than 6")
    private String password;

    @Email // unique
    private String email; // optional

    private String website; // optional

    private String bio; // optional
}
