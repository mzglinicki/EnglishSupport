package com.project.mzglinicki.yourowndictionary.adapters;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;

import com.project.mzglinicki.yourowndictionary.R;
import com.project.mzglinicki.yourowndictionary.realmDb.WordDbModel;

import java.util.List;

/**
 * Created by mzglinicki.96 on 11.08.2016.
 */
public class WordsListRecycleAdapter extends RecyclerView.Adapter<WordListViewHolder> {

    private final LayoutInflater inflater;
    private final List<WordDbModel> listOfWords;
    private final OnClickLister onClickLister;
    private final Context context;

    public WordsListRecycleAdapter(final Context context, final List<WordDbModel> listOfWords, final OnClickLister onClickLister) {
        inflater = LayoutInflater.from(context);
        this.listOfWords = listOfWords;
        this.onClickLister = onClickLister;
        this.context = context;
    }

    @Override
    public WordListViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        final View view = inflater.inflate(R.layout.word_list_model, parent, false);
        return new WordListViewHolder(view);
    }

    @Override
    public void onBindViewHolder(final WordListViewHolder holder, final int position) {
        final WordDbModel model = listOfWords.get(position);
        final RelativeLayout itemLayout = holder.getRecord();
        holder.getNativeWordTextView().setText(model.getNativeWord());
        holder.getTranslatedWordTextView().setText(model.getTranslatedWord());
        holder.setupModelView(model.isEditEnabled());
        setupOnLongClickListener(itemLayout, model);
        setupClickListener(holder.getDeleteImageButton(), model, holder.getAdapterPosition());
        setupClickListener(holder.getVolumeImageButton(), model, holder.getAdapterPosition());
        setupClickListener(holder.getEditImageButton(), model, holder.getAdapterPosition());
        setupItemBackground(itemLayout, position);
    }

    private void setupItemBackground(final RelativeLayout itemLayout, final int position) {
        if (position % 2 == 0) {
            itemLayout.setBackgroundResource(R.color.white);
        } else {
            itemLayout.setBackgroundResource(R.color.blueGray_50);
        }
    }

    private void setupOnLongClickListener(final View view, final WordDbModel model) {
        view.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(final View record) {
                if (onClickLister != null) {
                    onClickLister.onLongClick(model);
                }
                return true;
            }
        });
    }

    private void setupClickListener(final View view, final WordDbModel model, final int position) {
        view.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(final View imageButton) {
                if (onClickLister != null) {
                    onClickLister.onClick(imageButton, model, position);
                }
            }
        });
    }

    @Override
    public int getItemCount() {
        return listOfWords.size();
    }

    public interface OnClickLister {
        void onClick(final View imageButton, final WordDbModel model, final int position);

        void onLongClick(final WordDbModel model);
    }
}