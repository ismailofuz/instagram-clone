package uz.pdp.instagramclone.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import uz.pdp.instagramclone.payload.ApiResponse;
import uz.pdp.instagramclone.payload.PostDto;
import uz.pdp.instagramclone.service.PostService;

@RequestMapping("/api/comments")
@RequiredArgsConstructor
@RestController
public class PostController {
    private final PostService postService;

    /**
     * return one post using id
     * @param id
     * @return
     */
    @GetMapping("/{user_id}/{id}")
    public HttpEntity<?> getOne(@PathVariable Long id,@PathVariable Long user_id){
        ApiResponse apiResponse  = postService.getOnePost(id,user_id);
        return ResponseEntity.status(apiResponse.isSuccess() ? 200:404).body(apiResponse);
    }

    /**
     * userning barcha postlari
     * @param page
     * @param user_id
     * @return
     */
    @GetMapping("/{user_id}/list") // user ning postlari ko'rini kerak
    public HttpEntity<?> listOfPosts(@RequestParam(defaultValue = "1",required = false) int page,
                                     @PathVariable Long user_id){
        Pageable pageable = PageRequest.of(page,10, Sort.by("createdAt")); // 10 dana chiqaradi

       ApiResponse apiResponse = postService.getUserPosts(user_id,pageable);

       return ResponseEntity.ok(apiResponse); // doim ok qaytadi chunki user bo'lsa yetadi
    }

    /**
     * adding posts to saved posts of user
     * @param post_id
     * @param user_id
     * @return
     */
    @PutMapping("/save_post/{user_id}/{post_id}")
    public HttpEntity<?> savePost(@PathVariable Long post_id,@PathVariable Long user_id){

        ApiResponse apiResponse = postService.addToSavedPosts(post_id,user_id);
        return ResponseEntity.status(apiResponse.isSuccess() ? 200:409).body(apiResponse);
    }

      @PutMapping("/like_post/{user_id}/{post_id}")
    public HttpEntity<?> likePost(@PathVariable Long post_id,@PathVariable Long user_id){

        ApiResponse apiResponse = postService.addToLikedPosts(post_id,user_id);
        return ResponseEntity.status(apiResponse.isSuccess() ? 200:409).body(apiResponse);
    }

    /**
     * showing the reel in home page
     * @param user_id
     * @return
     */
    // userga kelgan hamma postlarni ko'rsatish xuddi reelga o'shagan
    @GetMapping("/{user_id}/reel")
    public ResponseEntity<?> reel(@PathVariable Long user_id){
        ApiResponse apiResponse = postService.reel(user_id);
        return ResponseEntity.ok(apiResponse);
    }

    @PostMapping("/{user_id}/{attachment_id}")
    public HttpEntity<?> addPost(@PathVariable Long user_id, @RequestBody PostDto postDto,@PathVariable Long attachment_id){
        ApiResponse apiResponse = postService.addPost(user_id,attachment_id, postDto);
        return ResponseEntity.status(apiResponse.isSuccess() ? HttpStatus.CREATED : HttpStatus.CONFLICT).body(apiResponse);
    }








}
