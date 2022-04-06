package uz.pdp.instagramclone.payload;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import uz.pdp.instagramclone.entity.Attachment;

import java.util.List;

@AllArgsConstructor
@NoArgsConstructor
@Data
public class PostDto {
    private String description;
    private List<Attachment> files;
}
