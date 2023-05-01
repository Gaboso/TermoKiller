package br.com.gaboso.termo.core;

import br.com.gaboso.termo.enums.LanguageEnum;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.List;

public class Dictionary {

    public List<String> getDictionaryAsList(LanguageEnum languageEnum) {
        String fileName = languageEnum.getFileName();
        InputStream dictionary = getClass().getClassLoader().getResourceAsStream(fileName);

        if (dictionary == null) {
            throw new IllegalArgumentException("Dictionary not found");
        }

        return getAllWords(dictionary);
    }

    private List<String> getAllWords(InputStream baseDictionary) {
        List<String> words = new ArrayList<>();

        try (
                InputStreamReader streamReader = new InputStreamReader(baseDictionary, StandardCharsets.UTF_8);
                BufferedReader bufferedReader = new BufferedReader(streamReader)
        ) {

            String word = bufferedReader.readLine();
            while (word != null) {
                words.add(word);
                word = bufferedReader.readLine();
            }

        } catch (IOException e) {
            e.printStackTrace();
        }

        return words;
    }

}
