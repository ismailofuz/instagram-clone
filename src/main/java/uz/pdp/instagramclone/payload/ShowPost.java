package uz.pdp.instagramclone.payload;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import uz.pdp.instagramclone.entity.Post;

@AllArgsConstructor
@NoArgsConstructor
@Data
public class ShowPost {

    private Post post;

    private boolean postLikedByThisUser;

    private boolean savedByThisUser;
}
