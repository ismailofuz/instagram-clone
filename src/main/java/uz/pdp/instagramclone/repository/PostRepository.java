package uz.pdp.instagramclone.repository;

import org.springframework.data.domain.Example;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import uz.pdp.instagramclone.entity.Post;

import java.util.UUID;

public interface PostRepository extends JpaRepository<Post, Long> {
    Page<Post> findAllByUser_Id(Long userId, Pageable pageable);
    Page<Post> findAll( Pageable pageable);


}

