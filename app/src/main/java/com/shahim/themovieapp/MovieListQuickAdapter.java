package com.shahim.themovieapp;

import android.widget.ImageView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.recyclerview.widget.DiffUtil;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.shahim.themovieapp.api.Pojo.Movie;
import com.squareup.picasso.Picasso;

import java.util.List;

public class MovieListQuickAdapter extends BaseQuickAdapter<Movie, BaseViewHolder> {

    public MovieListQuickAdapter() {
        super(R.layout.list_movie_item);
    }

    @Override
    protected void convert(@NonNull BaseViewHolder helper, Movie item) {
        Picasso.with(mContext)
                .load(item.getPoster())
                .placeholder(R.drawable.im_movie_poster)
                .error(R.drawable.im_movie_poster)
                .into((ImageView) helper.getView(R.id.item_image));

        helper.setText(R.id.item_name,item.getTitle())
                .setText(R.id.item_desc,item.getYear());
//        .addOnClickListener(R.id.downloadButton);

    }

    public void updateItems(List<Movie> newItems) {
        DiffUtil.DiffResult diffResult = DiffUtil.calculateDiff(new DiffUtil.Callback() {
            @Override
            public int getOldListSize() {
                return mData.size();
            }

            @Override
            public int getNewListSize() {
                return newItems.size();
            }

            @Override
            public boolean areItemsTheSame(int oldItemPosition, int newItemPosition) {
                return mData.get(oldItemPosition).getImdbID().equals(newItems.get(newItemPosition).getImdbID());
            }

            @Override
            public boolean areContentsTheSame(int oldItemPosition, int newItemPosition) {
                Movie oldData = mData.get(oldItemPosition);
                Movie newData = newItems.get(newItemPosition);

                boolean result = true;

                return result;
            }
        });
        diffResult.dispatchUpdatesTo(this);
        this.mData.clear();
        this.mData.addAll(newItems);
    }
}