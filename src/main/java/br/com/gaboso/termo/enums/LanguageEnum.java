package br.com.gaboso.termo.enums;

import br.com.gaboso.termo.core.exception.LanguageException;

import java.util.Arrays;

public enum LanguageEnum {

    PT("Português", "pt-br_dictionary.txt"),
    EN("English", "en_dictionary.txt");


    private final String title;
    private final String fileName;

    LanguageEnum(String title, String fileName) {
        this.title = title;
        this.fileName = fileName;
    }

    public static LanguageEnum fromTitle(String title) throws LanguageException {
        return Arrays.stream(values())
                .filter(languageEnum -> languageEnum.getTitle().equals(title)).findFirst()
                .orElseThrow(LanguageException::new);
    }

    public String getTitle() {
        return title;
    }

    public String getFileName() {
        return fileName;
    }
}
