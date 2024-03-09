package com.adhunchanchoc.debugwordlespring.web;

import com.adhunchanchoc.debugwordlespring.service.WordleService;
import lombok.AllArgsConstructor;
import org.hibernate.engine.spi.Resolution;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;

import java.util.ArrayList;
import java.util.List;

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
    public String guess(Model model) {
        showAttempts(model); // initializes the list of attempts
        return "WordleGuess";

    }
    @PostMapping("/guessMVC")
    public String submitGuess(String guess, Model model){
        String error = wordleService.validate(guess);
        if(error != null) {
            model.addAttribute("errorMessage",error);
        } else {
            model.addAttribute("errorMessage","");
            attempts.add(guess);
        }
        showAttempts(model);
        return "WordleGuess";
    }

    private void showAttempts(Model model) {
        List<WebResult[]> results = attempts.stream().map((String attempt)->
            WebResult.create(wordleService.calculateResult(attempt),attempt)).toList(); //.collect(Collectors.toList());
        model.addAttribute("entries",results);
    }
}
