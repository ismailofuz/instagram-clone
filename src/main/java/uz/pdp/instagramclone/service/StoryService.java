package uz.pdp.instagramclone.service;

import lombok.RequiredArgsConstructor;
import org.hibernate.query.criteria.internal.expression.function.CurrentTimestampFunction;
import org.springframework.stereotype.Service;
import uz.pdp.instagramclone.entity.Story;
import uz.pdp.instagramclone.entity.User;
import uz.pdp.instagramclone.payload.ApiResponse;
import uz.pdp.instagramclone.payload.StoryDTO;
import uz.pdp.instagramclone.repository.StoryRepository;
import uz.pdp.instagramclone.repository.UserRepository;

import java.sql.Timestamp;
import java.time.LocalTime;
import java.util.Date;
import java.util.Optional;

@RequiredArgsConstructor
@Service
public class StoryService {

    final StoryRepository storyRepository;
    final UserRepository userRepository;

    public ApiResponse add(StoryDTO dto) {

        Optional<User> optionalUser = userRepository.findById(dto.getUserId());
        if (optionalUser.isPresent()) {
            Story story = new Story();
            story.setFiles(dto.getFiles());
            story.setUser(optionalUser.get());
            story.setHighlighted(dto.isHighlighted());
            storyRepository.save(story);
        }
        return new ApiResponse("This story successfully created!", true);
    }
}