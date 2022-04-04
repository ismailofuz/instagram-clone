package uz.pdp.instagramclone.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import uz.pdp.instagramclone.entity.Post;

import java.util.UUID;

public interface PostRepository extends JpaRepository<Post, Integer> {
}

