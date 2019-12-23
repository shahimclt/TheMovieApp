package com.shahim.themovieapp;

import android.widget.ImageView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.recyclerview.widget.DiffUtil;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.shahim.themovieapp.api.VideoItem;
import com.squareup.picasso.Picasso;

import java.util.List;

public class VideoListQuickAdapter extends BaseQuickAdapter<VideoItem, BaseViewHolder> {

    public VideoListQuickAdapter(@Nullable List<VideoItem> data) {
        super(R.layout.list_video_item, data);
    }

    @Override
    protected void convert(@NonNull BaseViewHolder helper, VideoItem item) {
        Picasso.with(mContext)
                .load(item.getThumb())
                .placeholder(R.drawable.ic_video_generic)
                .error(R.drawable.ic_video_generic)
                .into((ImageView) helper.getView(R.id.item_image));

        helper.setText(R.id.item_name,item.getTitle())
                .setText(R.id.item_desc,item.getDescription())
        .addOnClickListener(R.id.downloadButton);

    }

    public void updateItems(List<VideoItem> newItems) {
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
                return mData.get(oldItemPosition).getTitle().equals(newItems.get(newItemPosition).getTitle());
            }

            @Override
            public boolean areContentsTheSame(int oldItemPosition, int newItemPosition) {
                VideoItem oldData = mData.get(oldItemPosition);
                VideoItem newData = newItems.get(newItemPosition);

                boolean result = true;

                return result;
            }
        });
        diffResult.dispatchUpdatesTo(this);
        this.mData.clear();
        this.mData.addAll(newItems);
    }
}