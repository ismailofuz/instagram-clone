package uz.pdp.instagramclone.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import uz.pdp.instagramclone.entity.Direct;

import java.util.Optional;

public interface DirectRepository extends JpaRepository<Direct, Long> {
    Optional<Direct> findByReceiver_IdAndSender_Id(Long receiverId,Long senderId);
}