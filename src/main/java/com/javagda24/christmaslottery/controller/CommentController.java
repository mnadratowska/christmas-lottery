package com.javagda24.christmaslottery.controller;


import com.javagda24.christmaslottery.service.CommentService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import javax.servlet.http.HttpServletRequest;
import java.security.Principal;

@Controller
@RequestMapping(path = "/comment/")
public class CommentController {

    @Autowired
    private CommentService commentService;

    @PostMapping("/add/{id}")
    public String addComment(String comment, @PathVariable("id") Long giftId, Principal principal, HttpServletRequest request) {
        commentService.saveComment(comment, giftId, principal.getName());
        return "redirect:" + request.getHeader("referer");
    }

    @GetMapping("/delete/{commentId}")
    public String deleteComment(@PathVariable("commentId") Long commentId,
                                Principal principal, HttpServletRequest request) {
        commentService.delete(commentId, principal.getName());
        return "redirect:" + request.getHeader("referer");
    }
}
