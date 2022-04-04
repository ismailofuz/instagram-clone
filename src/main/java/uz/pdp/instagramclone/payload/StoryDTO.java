package uz.pdp.instagramclone.payload;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import uz.pdp.instagramclone.entity.Attachment;

import java.sql.Timestamp;
import java.util.List;

@AllArgsConstructor
@NoArgsConstructor
@Data
public class StoryDTO {
    private Integer userId;

    private boolean isHighlighted = false;

    private List<Attachment> files;
}
