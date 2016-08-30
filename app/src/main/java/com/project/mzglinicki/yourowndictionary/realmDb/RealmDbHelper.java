package com.project.mzglinicki.yourowndictionary.realmDb;

import android.content.Context;
import android.content.res.Resources;

import com.project.mzglinicki.yourowndictionary.Constants;
import com.project.mzglinicki.yourowndictionary.R;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import io.realm.OrderedRealmCollection;
import io.realm.Realm;
import io.realm.RealmConfiguration;
import io.realm.RealmResults;
import io.realm.exceptions.RealmPrimaryKeyConstraintException;

/**
 * Created by Marcin Zglinicki on 25.05.16.
 */
public class RealmDbHelper {

    private static RealmDbHelper realmDbHelper = null;
    private Realm realm;

    private RealmDbHelper(final Context context) {
        configureDb(context);
    }

    public static RealmDbHelper getInstance(final Context context) {
        if (realmDbHelper == null) {
            realmDbHelper = new RealmDbHelper(context.getApplicationContext());
        }
        return realmDbHelper;
    }

    public void configureDb(final Context context) {
        final RealmConfiguration realmConfig = new RealmConfiguration.Builder(context).build();
        Realm.setDefaultConfiguration(realmConfig);
    }

    public void createDefaultData(final Context context) {

        realm = Realm.getDefaultInstance();

        if (realm.where(WordDbModel.class).findAll().size() == 0) {
            createDefaultLessons(context);
            setupDefaultWords(context);
        }
    }

    private void setupDefaultWords(final Context context) {
        //wszędzie jest po 15
        final String[] defaultPolishWords = context.getResources().getStringArray(R.array.polishDefaultWord);
        final String[] defaultEnglishWords = context.getResources().getStringArray(R.array.englishDefaultWord);
        final String[] defaultSentence = context.getResources().getStringArray(R.array.defaultSentence);

        createWord(defaultPolishWords[0], defaultEnglishWords[0], defaultSentence[0], 1);
        createWord(defaultPolishWords[1], defaultEnglishWords[1], defaultSentence[1], 1);
        createWord(defaultPolishWords[2], defaultEnglishWords[2], defaultSentence[2], 1);
        createWord(defaultPolishWords[3], defaultEnglishWords[3], defaultSentence[3], 1);
        createWord(defaultPolishWords[4], defaultEnglishWords[4], defaultSentence[4], 1);
        createWord(defaultPolishWords[5], defaultEnglishWords[5], defaultSentence[5], 2);
        createWord(defaultPolishWords[6], defaultEnglishWords[6], defaultSentence[6], 2);
        createWord(defaultPolishWords[7], defaultEnglishWords[7], defaultSentence[7], 2);
        createWord(defaultPolishWords[8], defaultEnglishWords[8], defaultSentence[8], 2);
        createWord(defaultPolishWords[9], defaultEnglishWords[9], defaultSentence[9], 2);
        createWord(defaultPolishWords[10], defaultEnglishWords[10], defaultSentence[10], 3);
        createWord(defaultPolishWords[11], defaultEnglishWords[11], defaultSentence[11], 3);
        createWord(defaultPolishWords[12], defaultEnglishWords[12], defaultSentence[12], 3);
        createWord(defaultPolishWords[13], defaultEnglishWords[13], defaultSentence[13], 3);
        createWord(defaultPolishWords[14], defaultEnglishWords[14], defaultSentence[14], 3);
    }

    private void createDefaultLessons(final Context context) {
        final String[] defaultLessons = context.getResources().getStringArray(R.array.defaultLessons);

        for (final String defaultLesson : defaultLessons) {
            createLessonInDb(defaultLesson, getRandomLessonImage());
        }
    }

    private int getRandomLessonImage() {
        final List<Integer> images = new ArrayList<>();
        final Random random = new Random();

        images.add(R.mipmap.london);
        images.add(R.mipmap.bridge);
        images.add(R.mipmap.cab);
        images.add(R.mipmap.flag);
        images.add(R.mipmap.london_eye);
        images.add(R.mipmap.small_big_ben);
        images.add(R.mipmap.palace);
        images.add(R.mipmap.soldiers);

        return images.get(random.nextInt(images.size()));
    }

    public int createLessonInDb(final String lessonName, final int lessonImageResId) {

        realm = Realm.getDefaultInstance();
        realm.beginTransaction();
        final LessonDbModel lesson = realm.createObject(LessonDbModel.class);
        int newLessonID = getLessonModelId(lesson, realm.where(LessonDbModel.class).findAll().size());
        lesson.setLessonName(lessonName);
        lesson.setLessonImageResId(lessonImageResId);
        realm.commitTransaction();
        realm.close();
        return newLessonID;
    }

    private int getLessonModelId(final LessonDbModel lesson, int id) {
        try {
            lesson.setLessonId(id);
        } catch (final RealmPrimaryKeyConstraintException keyExist) {
            getLessonModelId(lesson, ++id);
        }
        return id;
    }

    public WordDbModel createWord(final String nativeWord, final String translateWord, final String sentence, final int lessonId) {

        realm = Realm.getDefaultInstance();
        realm.beginTransaction();
        final WordDbModel wordModel = realm.createObject(WordDbModel.class);
        setWordModelId(wordModel, realm.where(WordDbModel.class).findAll().size());
        wordModel.setLessonId(lessonId);
        wordModel.setNativeWord(nativeWord.toLowerCase());
        wordModel.setTranslatedWord(translateWord.toLowerCase());
        wordModel.setExampleSentence(sentence);
        realm.commitTransaction();
        realm.close();
        return wordModel;
    }

    private void setWordModelId(final WordDbModel wordModel, int id) {

        try {
            wordModel.setWordId(id);
        } catch (final RealmPrimaryKeyConstraintException keyExist) {
            setWordModelId(wordModel, ++id);
        }
    }

    public void deleteLessonFromDb(final int lessonId) {

        realm = Realm.getDefaultInstance();
        realm.beginTransaction();
        realm.where(WordDbModel.class).equalTo(Constants.LESSON_ID_COLUMN, lessonId).findAll().deleteAllFromRealm();
        realm.where(LessonDbModel.class).equalTo(Constants.LESSON_ID_COLUMN, lessonId).findFirst().deleteFromRealm();
        realm.commitTransaction();
        realm.close();
    }

    public List<LessonDbModel> getLessons() {
        return realm.where(LessonDbModel.class).findAll();
    }

    public OrderedRealmCollection<WordDbModel> getLessonWords(final int lessonId) {
        return realm.where(WordDbModel.class).equalTo(Constants.LESSON_ID_COLUMN, lessonId).findAll();
    }

    public WordDbModel getRandomWord() {
        final RealmResults<WordDbModel> listOfWords = realm.where(WordDbModel.class).findAll();
        return listOfWords.get(new Random().nextInt(listOfWords.size()));
    }

    public LessonDbModel getLesson(final int lessonId) {
        return realm.where(LessonDbModel.class).equalTo(Constants.LESSON_ID_COLUMN, lessonId).findFirst();
    }

    public void deleteWordFromDb(final int wordId) {
        realm = Realm.getDefaultInstance();
        realm.beginTransaction();
        realm.where(WordDbModel.class).equalTo(Constants.WORD_ID, wordId).findFirst().deleteFromRealm();
        realm.commitTransaction();
        realm.close();
    }

    public void updateWordInDb(final String nativeWord, final String translateWord, final String sentence, final int wordId) {
        realm = Realm.getDefaultInstance();
        realm.beginTransaction();
        final WordDbModel word = realm.where(WordDbModel.class).equalTo(Constants.WORD_ID, wordId).findFirst();
        word.setNativeWord(nativeWord);
        word.setTranslatedWord(translateWord);
        word.setExampleSentence(sentence);
        realm.commitTransaction();
        realm.close();
    }

    public void setWordEditMode(WordDbModel word, boolean editMode) {
        realm = Realm.getDefaultInstance();
        realm.beginTransaction();
        final WordDbModel wordDbModel = realm.where(WordDbModel.class).equalTo(Constants.WORD_ID, word.getWordId()).findFirst();
        wordDbModel.setEditMode(editMode);
        realm.commitTransaction();
        realm.close();
    }

    public void updateLessonData(final String lessonName, final int lessonImageResId, final int lessonId) {
        realm = Realm.getDefaultInstance();
        realm.beginTransaction();
        final LessonDbModel lesson = realm.where(LessonDbModel.class).equalTo(Constants.LESSON_ID_COLUMN, lessonId).findFirst();
        lesson.setLessonName(lessonName);
        lesson.setLessonImageResId(lessonImageResId);
        realm.commitTransaction();
        realm.close();
    }

    public List<WordDbModel> getTranslationFromPolish(final String polishWord) {
        return realm.where(WordDbModel.class).equalTo(Constants.NATIVE_WORD_KEY, polishWord).findAll();
    }

    public List<WordDbModel> getTranslationFromEnglish(final String englishWord) {
        return realm.where(WordDbModel.class).equalTo(Constants.TRANSLATE_WORD_KEY, englishWord).findAll();
    }
}