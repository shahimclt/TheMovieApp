package com.shahim.themovieapp;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.app.ActivityOptions;
import android.content.Context;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.EditorInfo;
import android.view.inputmethod.InputMethodManager;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.google.android.material.snackbar.Snackbar;
import com.google.android.material.textfield.TextInputEditText;
import com.shahim.themovieapp.api.APIClient;
import com.shahim.themovieapp.api.APIInterface;
import com.shahim.themovieapp.api.Pojo.Movie;
import com.shahim.themovieapp.api.Pojo.SearchResult;
import com.shahim.themovieapp.helper.BookmarksManagerSingleton;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.Unbinder;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import xyz.hanks.library.bang.SmallBangView;

public class BookmarksFragment extends Fragment {

    @BindView(R.id.recyclerView)
    RecyclerView mRecyclerView;

    MovieListQuickAdapter mAdapter;

    BookmarksManagerSingleton mBMS;

    private Unbinder unbinder;
    @Override public void onDestroyView() {
        super.onDestroyView();
        unbinder.unbind();
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_home, container, false);
        unbinder = ButterKnife.bind(this, v);
        if (savedInstanceState==null) {
            init();
        }
        return v;
    }

    @Override
    public void onResume() {
        super.onResume();
        mAdapter.setNewData(mBMS.getBookmarks());
    }

    void init() {
//        mStateHelper = new MultiStateHelper(mStateView);
        mBMS = BookmarksManagerSingleton.sharedInstance(getContext());
        initAdapter();
        refreshView();
    }

    void initAdapter() {
        GridLayoutManager layoutManager = new GridLayoutManager(getContext(),3);
        mRecyclerView.setLayoutManager(layoutManager);
        mRecyclerView.setHasFixedSize(true);
        mAdapter = new MovieListQuickAdapter();
        mAdapter.openLoadAnimation(BaseQuickAdapter.SCALEIN);
        mAdapter.setOnItemChildClickListener((adapter, view, position) -> {
            if (view.getId()==R.id.bookmarker) {
                if (view.isSelected()) {
                    view.setSelected(false);
                    view.setBackground(ContextCompat.getDrawable(getContext(),R.drawable.bg_thumb_bookmark));
                    ((ImageView)view.findViewById(R.id.bookmarker_image)).setImageDrawable(ContextCompat.getDrawable(getContext(),R.drawable.ic_bookmark_border_white_24dp));
                    Snackbar.make(mRecyclerView, R.string.movie_bookmark_removed, Snackbar.LENGTH_SHORT).show();
                    BookmarksManagerSingleton.sharedInstance(getContext()).removeBookmark(getContext(),mAdapter.getItem(position));
                    mAdapter.updateItems(mBMS.getBookmarks());
                } else {
                    view.setSelected(true);
                    view.setBackground(ContextCompat.getDrawable(getContext(),R.drawable.bg_thumb_bookmark_active));
                    ((ImageView)view.findViewById(R.id.bookmarker_image)).setImageDrawable(ContextCompat.getDrawable(getContext(),R.drawable.ic_bookmark_24dp));
                    Snackbar.make(mRecyclerView, R.string.movie_bookmark_added, Snackbar.LENGTH_SHORT).show();
                    BookmarksManagerSingleton.sharedInstance(getContext()).addBookmark(getContext(),mAdapter.getItem(position));
                    ((SmallBangView)view).likeAnimation(new AnimatorListenerAdapter() {
                        @Override
                        public void onAnimationEnd(Animator animation) {
                            super.onAnimationEnd(animation);
                        }
                    });
                    mAdapter.updateItems(mBMS.getBookmarks());
                }
            }
        });
        mAdapter.setOnItemClickListener((adapter, view, position) -> {
            Movie movie = mAdapter.getData().get(position);
            showMovieDetail(movie,view);
        });
        mRecyclerView.setAdapter(mAdapter);
    }

    void showMovieDetail(Movie movie, View view) {
        ActivityOptions options = ActivityOptions.makeSceneTransitionAnimation(getActivity(),view.findViewById(R.id.item_image),getResources().getString(R.string.moviePosterTransitionName));
        startActivity(MovieDetailActivity.craftIntent(getActivity(),movie),options.toBundle());
    }

    void refreshView() {
        mAdapter.setNewData(mBMS.getBookmarks());
    }

}
