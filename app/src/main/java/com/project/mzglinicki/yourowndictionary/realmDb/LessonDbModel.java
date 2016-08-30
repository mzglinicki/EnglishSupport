package com.project.mzglinicki.yourowndictionary.realmDb;

import java.util.ArrayList;

import io.realm.RealmList;
import io.realm.RealmObject;
import io.realm.annotations.PrimaryKey;

/**
 * Created by Marcin Zglinicki on 18.05.16.
 */
public class LessonDbModel extends RealmObject {

    @PrimaryKey
    private int lessonId;
    private String lessonName;
    private int lessonImageResId;

    public int getLessonId() {
        return lessonId;
    }

    public void setLessonId(final int lessonId) {
        this.lessonId = lessonId;
    }

    public String getLessonName() {
        return lessonName;
    }

    public void setLessonName(final String lessonName) {
        this.lessonName = lessonName;
    }

    public int getLessonImageResId() {
        return lessonImageResId;
    }

    public void setLessonImageResId(final int lessonImageResId) {
        this.lessonImageResId = lessonImageResId;
    }
}