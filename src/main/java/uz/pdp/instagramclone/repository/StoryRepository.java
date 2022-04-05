package uz.pdp.instagramclone.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import uz.pdp.instagramclone.entity.Story;

import java.util.UUID;

public interface StoryRepository extends JpaRepository<Story, Long> {
}
