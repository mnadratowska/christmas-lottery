package com.javagda24.christmaslottery.repository;

import com.javagda24.christmaslottery.model.Comment;
import com.javagda24.christmaslottery.model.Gift;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface CommentRepository extends JpaRepository<Comment, Long> {
    boolean existsByIdAndAccount_Username(Long id, String username);

    List<Comment> findAllByGiftOrderByAdditionTimeAsc(Gift gift);
}
