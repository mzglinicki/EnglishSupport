package com.project.mzglinicki.yourowndictionary;

/**
 * Created by mzglinicki.96 on 29.08.2016.
 */
public class TranslatedWord {

    private String translatedWord;
    private String exampleSentence;

    public TranslatedWord(final String translatedWord, final String exampleSentence) {
        this.translatedWord = translatedWord;
        this.exampleSentence = exampleSentence;
    }

    public String getExampleSentence() {
        return exampleSentence;
    }

    public String getTranslatedWord() {
        return translatedWord;
    }
}