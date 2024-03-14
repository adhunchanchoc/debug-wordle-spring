package com.adhunchanchoc.debugwordlespring.web;

import com.adhunchanchoc.debugwordlespring.service.WordleService;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletResponse;
import lombok.AllArgsConstructor;
import org.hibernate.engine.spi.Resolution;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.CookieValue;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Controller
@Scope("session")
//@AllArgsConstructor
public class WebController {
    private final WordleService wordleService;
    private List<String> attempts = new ArrayList<>(); // non-static, different for each instance

    public WebController(WordleService wordleService) {
        this.wordleService = wordleService;
    }

    @GetMapping("/guessMVC")
    public String guess(HttpServletResponse response, @CookieValue(required = false) String userId, Model model) {
        initUserId(response, userId);
        showAttempts(userId,model); // initializes the list of attempts
        return "WordleGuess";

    }

    public String initUserId(HttpServletResponse response, String userId) {
        if (userId == null) {
            userId = UUID.randomUUID().toString();
            Cookie cookie = new Cookie("userId", userId);
            // expiration 10 years
            cookie.setMaxAge(10 * 365 * 24 * 60 * 60);
            response.addCookie(cookie);
        }
        return userId;
    }

    @PostMapping("/guessMVC")
    public String submitGuess(HttpServletResponse response, @CookieValue(required = false) String userId, String guess, Model model) {
        String error = wordleService.validate(guess);
        if (error != null) {
            model.addAttribute("errorMessage", error);
        } else {
            model.addAttribute("errorMessage", "");
            attempts.add(guess);
        }
        initUserId(response, userId);
        showAttempts(userId,model);
        return "WordleGuess";
    }

    private void showAttempts(String userId, Model model) {
        List<WebResult[]> results = attempts.stream().map((String attempt) ->
                WebResult.create(wordleService.calculateResult(userId,attempt), attempt)).toList(); //.collect(Collectors.toList());
        model.addAttribute("entries", results);
    }
}
