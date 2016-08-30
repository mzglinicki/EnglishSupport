package com.project.mzglinicki.yourowndictionary.adapters;

import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import com.project.mzglinicki.yourowndictionary.R;

import butterknife.Bind;
import butterknife.ButterKnife;

/**
 * Created by mzglinicki.96 on 27.08.2016.
 */
public class LessonsViewHolder extends RecyclerView.ViewHolder {

    @Bind(R.id.lessonName)
    TextView lessonNameTextView;
    @Bind(R.id.learnTextView)
    TextView btnLearn;
    @Bind(R.id.testTextView)
    TextView btnTest;
    @Bind(R.id.editTextView)
    TextView btnEdit;
    @Bind(R.id.lessonImage)
    ImageView imageView;
    @Bind(R.id.lessonModelCardView)
    CardView lessonModelCardView;
    @Bind(R.id.deleteImageBtn)
    ImageButton deleteImageBtn;

    public LessonsViewHolder(final View itemView) {
        super(itemView);
        ButterKnife.bind(this, itemView);
    }

    public TextView getLessonNameTextView() {
        return lessonNameTextView;
    }

    public TextView getBtnLearn() {
        return btnLearn;
    }

    public TextView getBtnTest() {
        return btnTest;
    }

    public TextView getBtnEdit() {
        return btnEdit;
    }

    public ImageView getImageView() {
        return imageView;
    }

    public CardView getLessonModelCardView() {
        return lessonModelCardView;
    }

    public ImageButton getDeleteImageBtn() {
        return deleteImageBtn;
    }
}
