package com.adhunchanchoc.debugwordlespring.service;

import com.adhunchanchoc.debugwordlespring.db.DBEntry;
import com.adhunchanchoc.debugwordlespring.db.Database;
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
    private final Database database;

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

    public WordleService(Database database) { // would not be needed if Lombok have done it
        this.database = database;
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
    public CharacterResult[] calculateResult(String userId, String word){
        word = word.toUpperCase(); // included after bugs
        database.insert(new DBEntry(userId, word, WORD, 0)); // attempts incremented in Database insert() method

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
