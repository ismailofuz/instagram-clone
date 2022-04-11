package uz.pdp.instagramclone.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import uz.pdp.instagramclone.entity.Message;

import java.util.UUID;

public interface MessageRepository extends JpaRepository<Message, UUID> {
}