package com.shahim.themovieapp;

import android.widget.ImageView;

import androidx.annotation.NonNull;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.DiffUtil;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.shahim.themovieapp.api.Pojo.Movie;
import com.shahim.themovieapp.helper.BookmarksManagerSingleton;
import com.squareup.picasso.Picasso;

import java.util.List;

import xyz.hanks.library.bang.SmallBangView;

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

        ((ImageView)helper.getView(R.id.show_type)).setImageDrawable(ContextCompat.getDrawable(mContext,item.getTypeIcon()));

        if(BookmarksManagerSingleton.sharedInstance(mContext).isBookmarked(mContext,item)) {
            ((ImageView) helper.getView(R.id.bookmarker_image)).setImageDrawable(ContextCompat.getDrawable(mContext,R.drawable.ic_bookmark_24dp));
            ((SmallBangView) helper.getView(R.id.bookmarker)).setBackground(ContextCompat.getDrawable(mContext,R.drawable.bg_thumb_bookmark_active));
            ((SmallBangView) helper.getView(R.id.bookmarker)).setSelected(true);
        }
        else {
            ((ImageView) helper.getView(R.id.bookmarker_image)).setImageDrawable(ContextCompat.getDrawable(mContext,R.drawable.ic_bookmark_border_white_24dp));
            ((SmallBangView) helper.getView(R.id.bookmarker)).setBackground(ContextCompat.getDrawable(mContext,R.drawable.bg_thumb_bookmark));
            ((SmallBangView) helper.getView(R.id.bookmarker)).setSelected(false);
        }
        helper.addOnClickListener(R.id.bookmarker);

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