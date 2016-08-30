package com.project.mzglinicki.yourowndictionary.realmDb;

import io.realm.RealmObject;
import io.realm.annotations.PrimaryKey;

/**
 * Created by Marcin Zglinicki on 18.05.16.
 */
public class WordDbModel extends RealmObject {

    @PrimaryKey
    private int wordId;
    private int lessonId;
    private String nativeWord;
    private String translatedWord;
    private String exampleSentence;
    private boolean editEnabled;

    public WordDbModel() {
        this.editEnabled = false;
    }

    public int getWordId() {
        return wordId;
    }

    public void setWordId(final int wordId) {
        this.wordId = wordId;
    }

    public int getLessonId() {
        return lessonId;
    }

    public void setLessonId(final int lessonId) {
        this.lessonId = lessonId;
    }

    public String getNativeWord() {
        return nativeWord;
    }

    public void setNativeWord(final String nativeWord) {
        this.nativeWord = nativeWord;
    }

    public String getExampleSentence() {
        return exampleSentence;
    }

    public void setExampleSentence(final String exampleSentence) {
        this.exampleSentence = exampleSentence;
    }

    public String getTranslatedWord() {
        return translatedWord;
    }

    public void setTranslatedWord(final String translatedWord) {
        this.translatedWord = translatedWord;
    }

    public void setEditMode(final boolean editMode) {
        editEnabled = editMode;
    }

    public boolean isEditEnabled() {
        return editEnabled;
    }
}
