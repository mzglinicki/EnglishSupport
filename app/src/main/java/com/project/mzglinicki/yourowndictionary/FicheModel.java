package com.project.mzglinicki.yourowndictionary;

/**
 * Created by mzglinicki.96 on 29.08.2016.
 */
public class FicheModel {

    private String polishWord;
    private String englishWord;

    public FicheModel(final String polishWord, final String englishWord) {
        this.polishWord = polishWord;
        this.englishWord = englishWord;
    }

    public String getPolishWord() {
        return polishWord;
    }

    public String getEnglishWord() {
        return englishWord;
    }
}