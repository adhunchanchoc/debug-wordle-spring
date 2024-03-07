package com.adhunchanchoc.debugwordlespring.web;

import com.adhunchanchoc.debugwordlespring.service.WordleService;
import lombok.AllArgsConstructor;
import org.hibernate.engine.spi.Resolution;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;

import java.util.ArrayList;
import java.util.List;

@Controller
//@AllArgsConstructor
public class WebController {
    private final WordleService wordleService;
    private static List<String> attempts = new ArrayList<>();

    public WebController(WordleService wordleService) {
        this.wordleService = wordleService;
    }

    @GetMapping("/guessMVC")
    public String guess(Model model) {
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

        List<WebResult[]> results = attempts.stream().map((String attempt)->
            WebResult.create(wordleService.calculateResult(attempt),attempt)).toList(); //.collect(Collectors.toList());
        model.addAttribute("entries",results);
        return "WordleGuess";
    }
}
