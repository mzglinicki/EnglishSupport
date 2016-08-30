package com.project.mzglinicki.yourowndictionary.adapters;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.TextView;

import com.project.mzglinicki.yourowndictionary.R;
import com.project.mzglinicki.yourowndictionary.realmDb.LessonDbModel;

import java.util.List;

/**
 * Created by mzglinicki.96 on 27.08.2016.
 */
public class LessonsListAdapter extends RecyclerView.Adapter<LessonsViewHolder> {

    private final LayoutInflater inflater;
    private final List<LessonDbModel> lessons;
    private final OnClickListener onClickListener;

    public LessonsListAdapter(final Context context, final List<LessonDbModel> lessons, final OnClickListener onClickListener) {
        this.inflater = LayoutInflater.from(context);
        this.lessons = lessons;
        this.onClickListener = onClickListener;
    }

    @Override
    public LessonsViewHolder onCreateViewHolder(final ViewGroup parent, final int viewType) {
        final View view = inflater.inflate(R.layout.lesson_model, parent, false);
        return new LessonsViewHolder(view);
    }

    @Override
    public void onBindViewHolder(final LessonsViewHolder holder, final int position) {
        final LessonDbModel model = lessons.get(position);
        final ImageButton deleteImageBtn = holder.getDeleteImageBtn();
        holder.getLessonNameTextView().setText(model.getLessonName());
        holder.getImageView().setImageResource(model.getLessonImageResId());
        holder.getLessonModelCardView().setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(final View v) {
                deleteImageBtn.setVisibility(deleteImageBtn.getVisibility() == View.VISIBLE ? View.GONE : View.VISIBLE);
                return true;
            }
        });
        setupClickListeners(holder.getBtnLearn(), model, position);
        setupClickListeners(holder.getBtnTest(), model, position);
        setupClickListeners(holder.getBtnEdit(), model, position);
        setupClickListeners(deleteImageBtn, model, position);
    }

    private void setupClickListeners(final View view, final LessonDbModel lesson, final int position) {

        if (onClickListener == null) {
            return;
        }

        view.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(final View v) {
                switch (view.getId()) {
                    case R.id.learnTextView:
                        onClickListener.onLearnBtnClick(lesson);
                        break;
                    case R.id.testTextView:
                        onClickListener.onTestBtnClick(lesson);
                        break;
                    case R.id.editTextView:
                        onClickListener.onEditBtnClick(lesson);
                        break;
                    case R.id.deleteImageBtn:
                        onClickListener.onDeleteImageBtnClick(lesson, position);
                        break;
                    default:
                        break;
                }
            }
        });
    }

    @Override
    public int getItemCount() {
        return lessons.size();
    }

    public interface OnClickListener {
        void onLearnBtnClick(final LessonDbModel lesson);

        void onTestBtnClick(final LessonDbModel lesson);

        void onEditBtnClick(final LessonDbModel lesson);

        void onDeleteImageBtnClick(final LessonDbModel lesson, final int position);
    }
}