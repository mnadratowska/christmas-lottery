package com.javagda24.christmaslottery.service;

import com.javagda24.christmaslottery.model.Account;
import com.javagda24.christmaslottery.model.Comment;
import com.javagda24.christmaslottery.model.Gift;
import com.javagda24.christmaslottery.repository.AccountRepository;
import com.javagda24.christmaslottery.repository.CommentRepository;
import com.javagda24.christmaslottery.repository.GiftRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class CommentService {

    @Autowired
    private CommentRepository commentRepository;
    @Autowired
    private GiftRepository giftRepository;
    @Autowired
    private AccountRepository accountRepository;

    public void saveComment(String commentContent, Long giftId, String name) {
        Optional<Gift> optionalGift = giftRepository.findById(giftId);
        Optional<Account> optionalAccount = accountRepository.findByUsername(name);
        if (optionalGift.isPresent() && optionalAccount.isPresent()) {
            Account account = optionalAccount.get();
            Gift gift = optionalGift.get();
            Comment comment = new Comment();
            comment.setAccount(account);
            comment.setGift(gift);
            comment.setContent(commentContent);
            commentRepository.save(comment);
        }
    }

    public void delete(Long commentId, String name) {
        if (commentRepository.existsByIdAndAccount_Username(commentId, name)) {
            commentRepository.deleteById(commentId);
        }
    }

    public List<Comment> getCommentsByGift(Gift gift) {
        return commentRepository.findAllByGiftOrderByAdditionTimeAsc(gift);
    }
}
