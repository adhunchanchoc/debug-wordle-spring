package com.adhunchanchoc.debugwordlespring.webservices;

import org.springframework.core.io.ClassPathResource;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

@RestController
public class WordleWS {
    private static final String WORD = "LYMPH";
    private static final List<String> DICTIONARY = new ArrayList<>();

    static {
        try (InputStream is = new ClassPathResource("words.txt").getInputStream()) {
            Scanner sc = new Scanner(is).useDelimiter("\n"); // BEWARE - windows files have \r\n instead of just \n
            while(sc.hasNext()){
                DICTIONARY.add(sc.next().toUpperCase());
            }
        }catch (IOException e){
            throw new RuntimeException(e);
        }
    }

    @GetMapping("/guess")
    public Result guess(String word) {
        if (word.length() != 5) {
            return new Result(null,"Bad length");
        }
        word = word.toUpperCase();
        if (!DICTIONARY.contains(word)){
            return new Result (null, "Not in the dictionary");
        }
        CharacterResult[] result = new CharacterResult[WORD.length()];
        for(int i = 0; i< word.length();i++){
            char currentChar = word.charAt(i);
            if(currentChar == WORD.charAt(i)){
                result[i] = CharacterResult.GREEN;
                continue;
            }
            if (WORD.indexOf(currentChar) > -1){
                result[i] = CharacterResult.YELLOW;
                continue;
            }
            result[i]= CharacterResult.BLACK;
        }
        return new Result(result,null);
    }
}
