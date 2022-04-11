package uz.pdp.instagramclone.controller;


import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpEntity;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import uz.pdp.instagramclone.payload.ApiResponse;
import uz.pdp.instagramclone.payload.DirectMessageDto;
import uz.pdp.instagramclone.service.MessageService;

import java.util.UUID;

@RestController
@RequestMapping("/api/message")
@RequiredArgsConstructor
public class MessageController {

    private final MessageService messageService;

    /**
     * commentarydan xabarni o'chirish
     * @param user_id
     * @param message_id
     * @param post_id
     * @return
     */
    @DeleteMapping("{user_id}/{message_id}")
    public  HttpEntity<?> deleteMessageFromComment(@PathVariable Long user_id, @PathVariable UUID message_id,
    @RequestParam Long post_id) {
        ApiResponse apiResponse = messageService.delete(user_id, message_id,post_id);

        return ResponseEntity.status(apiResponse.isSuccess() ? 203 : 409).body(apiResponse);
    }

    /**
     * comment yozish
     * @param user_id
     * @param post_id
     * @param text
     * @return
     */
    @PostMapping("/{user_id}/{post_id}/writeComment")
    public HttpEntity<?> addComment(@PathVariable Long user_id,@PathVariable Long post_id,@RequestParam String text){
        ApiResponse apiResponse = messageService.addComment(user_id,post_id,text);
        return ResponseEntity.status(apiResponse.isSuccess() ? 201 : 409).body(apiResponse);
    }

    // directga xabar yuborish :
    @PostMapping("/direct_message")
    public HttpEntity<?> directMessage(@RequestBody DirectMessageDto dto){

        ApiResponse apiResponse = messageService.directMessage(dto);

        return ResponseEntity.status(apiResponse.isSuccess() ? 201 : 409).body(apiResponse);
    }


}
