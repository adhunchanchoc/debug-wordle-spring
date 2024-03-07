package com.adhunchanchoc.debugwordlespring.service;

import com.adhunchanchoc.debugwordlespring.webservices.CharacterResult;
import com.adhunchanchoc.debugwordlespring.webservices.Result;
import org.springframework.core.io.ClassPathResource;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

@Service
public class WordleService {
    private static final String WORD = "LYMPH";
    private static final List<String> DICTIONARY = new ArrayList<>();

    static {
        try (InputStream is = new ClassPathResource("words.txt").getInputStream()) {
            Scanner sc = new Scanner(is).useDelimiter("\n"); // BEWARE - windows files have \r\n instead of just \n
            while (sc.hasNext()) {
                DICTIONARY.add(sc.next().toUpperCase());
            }
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    public String validate(String word){
        if (word.length() != 5) {
            return "Bad length";
        }
        word = word.toUpperCase();
        if (!DICTIONARY.contains(word)){
            return "Not in the dictionary";
        }
        return null;
    }
    public CharacterResult[] calculateResult(String word){
        word = word.toUpperCase(); // included after bugs

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
        return result;
    }
}
