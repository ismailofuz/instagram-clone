package uz.pdp.instagramclone.payload;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@NoArgsConstructor
@Data
public class DirectMessageDto {
    private Long senderId;
    private Long receiverId;
    private String text;
}
