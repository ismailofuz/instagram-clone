package uz.pdp.instagramclone.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import uz.pdp.instagramclone.entity.Direct;
import uz.pdp.instagramclone.entity.Message;
import uz.pdp.instagramclone.entity.Post;
import uz.pdp.instagramclone.entity.User;
import uz.pdp.instagramclone.payload.ApiResponse;
import uz.pdp.instagramclone.payload.DirectMessageDto;
import uz.pdp.instagramclone.repository.DirectRepository;
import uz.pdp.instagramclone.repository.MessageRepository;
import uz.pdp.instagramclone.repository.PostRepository;
import uz.pdp.instagramclone.repository.UserRepository;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class MessageService {

    private final MessageRepository messageRepository;
    private final PostRepository postRepository;
    private final UserRepository userRepository;
    private final DirectRepository directRepository;

    public ApiResponse delete(Long user_id, UUID message_id, Long post_id) {
        Optional<Post> optionalPost = postRepository.findById(post_id);
        if (optionalPost.isPresent()) {
            Post post = optionalPost.get();
            List<Message> comments = post.getComments();
            // hamma o'zi yozgan messageni o'chiradi
            if (messageRepository.findById(message_id).isPresent()) {
                Message message = messageRepository.getById(message_id);
                if (message.getSender().getId() == user_id) {
                    comments.remove(message);
                    postRepository.save(post);
                    messageRepository.delete(message);
                    return new ApiResponse("Message deleted", true);
                }
            }
        }
        return new ApiResponse("Something went wrong", false);
    }

    public ApiResponse addComment(Long user_id, Long post_id, String text) {
        Optional<User> optionalUser = userRepository.findById(user_id);
        if (optionalUser.isPresent()) {
            User user = optionalUser.get();
            Optional<Post> optionalPost = postRepository.findById(post_id);
            if (optionalPost.isPresent()) {
                Post post = optionalPost.get();
                List<Message> comments = post.getComments();

                Message message = new Message();
                message.setDescription(text);
                message.setSender(user);

                messageRepository.save(message);

                comments.add(message);
                postRepository.save(post);
                return new ApiResponse("Added", true, comments);
            }
        }
        return new ApiResponse("Something went wrong", false);
    }

    public ApiResponse directMessage(DirectMessageDto dto) {
        Long receiverId = dto.getReceiverId();
        Long senderId = dto.getSenderId();

        Optional<User> optionalReceiver = userRepository.findById(receiverId);
        Optional<User> optionalSender = userRepository.findById(senderId);

        if (optionalReceiver.isPresent() && optionalSender.isPresent()) {
            User receiver = optionalReceiver.get();
            User sender = optionalSender.get();

            // qaysidir xabar junatsa har ikkising ham directiga tushishi kerak :

            Message message = new Message();
            message.setSender(sender);
            message.setDescription(dto.getText());
            messageRepository.save(message); // message is saved there

            Optional<Direct> byReceiver_idAndSender_id = directRepository.findByReceiver_IdAndSender_Id(receiverId, senderId);
            if (byReceiver_idAndSender_id.isPresent()) {
                Direct sender_direct = byReceiver_idAndSender_id.get(); // agar unda bo'lsa bunda ham ochilgan bo'ladi
                Direct receiverDirect = directRepository.findByReceiver_IdAndSender_Id(senderId, receiverId).get();

                sender_direct.getMessages().add(message);
                directRepository.save(sender_direct);
                receiverDirect.getMessages().add(message);
                directRepository.save(receiverDirect);

                return new ApiResponse("Message is sent",true,sender_direct);

            } else {
                // demak hali ikkalsai ham chat ochilmagan :

                Direct direct_for_sender = new Direct();

                direct_for_sender.setSender(sender);
                direct_for_sender.setReceiver(receiver);
                direct_for_sender.setMessages(Arrays.asList(message));
                directRepository.save(direct_for_sender);

                 Direct direct_for_receiver = new Direct();

                direct_for_sender.setSender(receiver);
                direct_for_sender.setReceiver(sender);
                direct_for_sender.setMessages(Arrays.asList(message));
                directRepository.save(direct_for_receiver);


                return new ApiResponse("new Chat is created and sent message",true,direct_for_sender);

            }
        }
        return new ApiResponse("Something went wrong",false);
    }
}
