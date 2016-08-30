package com.project.mzglinicki.yourowndictionary.adapters;

import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ImageButton;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.project.mzglinicki.yourowndictionary.R;

import butterknife.Bind;
import butterknife.ButterKnife;

/**
 * Created by mzglinicki.96 on 11.08.2016.
 */
public class WordListViewHolder extends RecyclerView.ViewHolder {

    @Bind(R.id.nativeWordTextView)
    TextView nativeWordTextView;
    @Bind(R.id.translatedWordEditText)
    TextView translatedWordTextView;
    @Bind(R.id.record)
    RelativeLayout record;
    @Bind(R.id.volumeImageBtn)
    ImageButton volumeImageButton;
    @Bind(R.id.deleteImageButton)
    ImageButton deleteImageButton;
    @Bind(R.id.editImageButton)
    ImageButton editImageButton;

    public WordListViewHolder(final View itemView) {
        super(itemView);
        ButterKnife.bind(this, itemView);
    }

    public RelativeLayout getRecord() {
        return record;
    }

    public TextView getNativeWordTextView() {
        return nativeWordTextView;
    }

    public TextView getTranslatedWordTextView() {
        return translatedWordTextView;
    }

    public void setupModelView(final boolean enable) {

        volumeImageButton.setVisibility(enable ? View.GONE : View.VISIBLE);
        deleteImageButton.setVisibility(enable ? View.VISIBLE : View.GONE);
        editImageButton.setVisibility(enable ? View.VISIBLE : View.GONE);
    }

    public ImageButton getVolumeImageButton() {
        return volumeImageButton;
    }

    public ImageButton getEditImageButton() {
        return editImageButton;
    }

    public ImageButton getDeleteImageButton() {
        return deleteImageButton;
    }
}