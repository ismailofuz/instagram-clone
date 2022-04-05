package uz.pdp.instagramclone.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import uz.pdp.instagramclone.entity.Story;
import uz.pdp.instagramclone.payload.ApiResponse;
import uz.pdp.instagramclone.payload.StoryDTO;
import uz.pdp.instagramclone.repository.StoryRepository;
import uz.pdp.instagramclone.service.StoryService;

import java.util.List;
import java.util.Optional;

@RequiredArgsConstructor
@RestController
@RequestMapping("/api/story")
public class StoryController {

    final StoryRepository storyRepository;
    final StoryService storyService;

    @GetMapping("/list")
    public HttpEntity<?> getStories(@RequestParam(defaultValue = "1") int page){
        Pageable pageable = PageRequest.of(page,10, Sort.by("createdAt"));
        Page<Story> stories = storyRepository.findAll(pageable);
        return ResponseEntity.ok().body(stories);
    }

    @GetMapping("/{id}")
    public HttpEntity<?> getStory(@PathVariable Long id){
        Optional<Story> optionalStory = storyRepository.findById(id);
        if (optionalStory.isPresent()) {
            return ResponseEntity.ok().body(optionalStory);
        }
        return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
    }

    @PostMapping("/create")
    public HttpEntity<?> add(@RequestBody StoryDTO dto){
        ApiResponse apiResponse = storyService.add(dto);
        return ResponseEntity.status(apiResponse.isSuccess() ? HttpStatus.CREATED : HttpStatus.CONFLICT).body(apiResponse.getObject());
    }

    @DeleteMapping("/delete/{id}")
    public HttpEntity<?> delete(@PathVariable Long id){
        ApiResponse apiResponse = storyService.delete(id);
        return ResponseEntity.status(apiResponse.isSuccess() ? HttpStatus.OK : HttpStatus.NOT_FOUND).body(apiResponse);
    }
}
