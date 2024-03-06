package com.adhunchanchoc.debugwordlespring.webservices;

import com.adhunchanchoc.debugwordlespring.service.WordleService;
import org.springframework.core.io.ClassPathResource;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

//@AllArgsConstructor
@RestController
public class WordleWS {

    private final WordleService wordleService;
//    this constructor can be replaced by Lombok @AllArgsConstructor
    public WordleWS(WordleService wordleService){
        this.wordleService = wordleService;
    }

    @GetMapping("/guess")
    public Result guess(String word) {
        var error = wordleService.validate(word);
        if(error != null) {
            return new Result(null,error);
        }
        word = word.toUpperCase();
        return new Result(wordleService.calculateResult(word),null);
    }
}
