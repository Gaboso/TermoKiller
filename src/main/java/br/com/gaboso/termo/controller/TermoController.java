package br.com.gaboso.termo.controller;

import br.com.gaboso.termo.core.Dictionary;
import br.com.gaboso.termo.enums.LanguageEnum;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.function.Predicate;
import java.util.stream.Collectors;

public class TermoController {

    private final Dictionary dictionary = new Dictionary();

    public List<String> getSuggestions(String dontHave, String certain, String uncertain, LanguageEnum languageEnum) {
        List<String> availableWords = dictionary.getDictionaryAsList(languageEnum);

        availableWords = this.getWordsCertain(availableWords, certain);
        availableWords = this.getWordsUncertain(availableWords, uncertain);
        availableWords = this.getWordsDontHave(availableWords, dontHave);

        return availableWords;
    }

    private List<String> getWordsDontHave(List<String> availableWords, String lettersText) {
        List<String> letters = this.toList(lettersText);

        if (letters.isEmpty()) {
            return availableWords;
        }

        for (String letter : letters) {
            availableWords = this.getWords(availableWords, word -> !word.contains(letter));
        }

        return availableWords;
    }

    private List<String> getWordsCertain(List<String> availableWords, String argumentText) {
        List<String> arguments = this.toList(argumentText);

        if (arguments.isEmpty()) {
            return availableWords;
        }

        for (String argument : arguments) {
            char modifier = argument.charAt(0);
            char letter = argument.charAt(1);

            int letterPosition = Integer.parseInt(modifier + "") - 1;
            availableWords = this.getWords(availableWords, word -> word.charAt(letterPosition) == letter);
        }

        return availableWords;
    }

    private List<String> getWordsUncertain(List<String> availableWords, String argumentText) {
        List<String> arguments = this.toList(argumentText);

        if (arguments.isEmpty()) {
            return availableWords;
        }

        for (String argument : arguments) {
            int letterIndex = Integer.parseInt(argument.charAt(0) + "") - 1;
            char letter = argument.charAt(1);

            availableWords = this.getWords(availableWords, word -> word.contains(letter + "") && word.charAt(letterIndex) != letter);
        }

        return availableWords;
    }

    private List<String> toList(String text) {
        if (isInvalid(text)) {
            return Collections.emptyList();
        }

        String textSanitized = text.replace(" ", "")
                .replace("-", "");

        return Arrays.asList(textSanitized.split(","));
    }

    public boolean isInvalid(String text) {
        if (text == null) {
            return true;
        }

        return text.isBlank();
    }

    private List<String> getWords(List<String> words, Predicate<String> filter) {
        return words.stream()
                .filter(filter)
                .collect(Collectors.toList());
    }

}
