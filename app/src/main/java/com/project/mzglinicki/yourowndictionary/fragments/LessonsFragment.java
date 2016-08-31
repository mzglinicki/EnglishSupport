package com.project.mzglinicki.yourowndictionary.fragments;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.Snackbar;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.project.mzglinicki.yourowndictionary.Constants;
import com.project.mzglinicki.yourowndictionary.R;
import com.project.mzglinicki.yourowndictionary.activities.LearnActivity;
import com.project.mzglinicki.yourowndictionary.activities.LessonCreator;
import com.project.mzglinicki.yourowndictionary.activities.MainActivity;
import com.project.mzglinicki.yourowndictionary.adapters.LessonsListAdapter;
import com.project.mzglinicki.yourowndictionary.adapters.LessonsViewHolder;
import com.project.mzglinicki.yourowndictionary.realmDb.LessonDbModel;
import com.project.mzglinicki.yourowndictionary.realmDb.RealmDbHelper;

import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;

/**
 * Created by mzglinicki.96 on 27.08.2016.
 */
public class LessonsFragment extends Fragment implements LessonsListAdapter.OnClickListener {

    @Bind(R.id.lessonsRecycleView)
    RecyclerView recycleView;

    private RealmDbHelper dbHelper;
    private List<LessonDbModel> lessonDbModels;
    private LessonsListAdapter listAdapter;

    @Nullable
    @Override
    public View onCreateView(final LayoutInflater inflater, final ViewGroup container, @Nullable Bundle savedInstanceState) {
        final View view = inflater.inflate(R.layout.fragment_lessons_main, container, false);
        ButterKnife.bind(this, view);
        dbHelper = RealmDbHelper.getInstance(getContext());
        lessonDbModels = dbHelper.getLessons();

        setupRecycleView();
        return view;
    }

    @Override
    public void onStart() {
        super.onStart();
        ((MainActivity) getActivity()).toggleFABVisibility();
        ((MainActivity) getActivity()).setToolbarTitle(getString(R.string.lessonsFragmentTitle));
        listAdapter.notifyDataSetChanged();
    }

    @Override
    public void onStop() {
        super.onStop();
        ((MainActivity) getActivity()).toggleFABVisibility();
    }

    private void setupRecycleView() {
        listAdapter = new LessonsListAdapter(getContext(), lessonDbModels, this);
        recycleView.setHasFixedSize(true);
        recycleView.setAdapter(listAdapter);
        recycleView.setLayoutManager(new LinearLayoutManager(getContext()));
    }

    @Override
    public void onLearnBtnClick(final LessonDbModel lesson) {
        final Intent intent = new Intent(getContext(), LearnActivity.class);
        intent.putExtra(Constants.LESSON_ID_KEY, lesson.getLessonId());
        startActivity(intent);
    }

    @Override
    public void onTestBtnClick(final LessonDbModel lesson) {
        Toast.makeText(getContext(), "onTestBtnClick", Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onEditBtnClick(final LessonDbModel lesson) {
        final Intent intent = new Intent(getContext(), LessonCreator.class);
        intent.putExtra(Constants.LESSON_ID_KEY, lesson.getLessonId());
        startActivity(intent);
    }

    @Override
    public void onDeleteImageBtnClick(final LessonDbModel lesson, final int position, final LessonsViewHolder holder) {

        showSnackbar(lesson, position, holder);
    }

    private void showSnackbar(final LessonDbModel lesson, final int position, final LessonsViewHolder holder) {
        final Snackbar snackbar = Snackbar
                .make(recycleView, getString(R.string.areYouSureToDelete), Snackbar.LENGTH_LONG)
                .setAction(R.string.yes, new View.OnClickListener() {
                    @Override
                    public void onClick(final View view) {
                        dbHelper.deleteLessonFromDb(lesson.getLessonId());
//                        listAdapter.notifyItemRangeChanged(0, listAdapter.getItemCount());
                        listAdapter.notifyItemRemoved(holder.getAdapterPosition());
//                        listAdapter.notifyItemSetChanged(holder.getAdapterPosition());
                    }
                });
        snackbar.show();
    }
}