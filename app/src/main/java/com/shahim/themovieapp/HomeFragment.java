package com.shahim.themovieapp;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.shahim.themovieapp.api.APIClient;
import com.shahim.themovieapp.api.APIInterface;
import com.shahim.themovieapp.api.Pojo.Movie;
import com.shahim.themovieapp.api.Pojo.SearchResult;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.Unbinder;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class HomeFragment extends Fragment {

    @BindView(R.id.recyclerView)
    RecyclerView mRecyclerView;

    MovieListQuickAdapter mAdapter;

    SearchResult searchResult;

    String quesry = "Avengers";

    Integer mCurrentCounter = 0;

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
            loadData(1);
        }
        return v;
    }

    void init() {
//        mStateHelper = new MultiStateHelper(mStateView);
        initAdapter();
    }

    void initAdapter() {
        GridLayoutManager layoutManager = new GridLayoutManager(getContext(),3);
        mRecyclerView.setLayoutManager(layoutManager);
        mRecyclerView.setHasFixedSize(true);
        mAdapter = new MovieListQuickAdapter();
        mAdapter.openLoadAnimation(BaseQuickAdapter.SCALEIN);
        mAdapter.setOnItemChildClickListener((adapter, view, position) -> {
//            if (view.getId()==R.id.downloadButton) {
//            }
        });
        mRecyclerView.setAdapter(mAdapter);
    }

    void initLoadMore() {
        mAdapter.setEnableLoadMore(true);
        mAdapter.setOnLoadMoreListener(() -> mRecyclerView.postDelayed(() -> {
            if (mCurrentCounter >= searchResult.getTotalResults()) {
                //Data are all loaded.
                mAdapter.loadMoreEnd();
            } else {
                loadData(mCurrentCounter/10 + 1);
            }
        }, 1000), mRecyclerView);
    }

    private void loadData(Integer page) {
//        mStateHelper.showloading();

        APIInterface mAPI = APIClient.getClient().create(APIInterface.class);
        Call<SearchResult> mCall = mAPI.searchMovie(quesry,page);
        mCall.enqueue(new Callback<SearchResult>() {
            @Override
            public void onResponse(Call<SearchResult> call, Response<SearchResult> response) {
                if (response.isSuccessful()) {
                    SearchResult res = response.body();
                    searchResult = res;
                    if(page>1) {
                        mAdapter.addData(searchResult.getSearch());
                        mCurrentCounter = mAdapter.getData().size();
                        mAdapter.loadMoreComplete();
                    }
                    else {
                        refreshView();
                        initLoadMore();
                    }
                }
                else {
                    if (page>1) {
                        mAdapter.loadMoreFail();
                    }
                }
            }

            @Override
            public void onFailure(Call<SearchResult> call, Throwable t) {
                if (page>1) {
                    mAdapter.loadMoreFail();
                }
            }
        });
    }

    void refreshView() {
        if (searchResult.getResponse()) {
            if (mAdapter==null) {
                return;
            }
//            mStateHelper.showContent();
            mAdapter.updateItems(searchResult.getSearch());
            mCurrentCounter = mAdapter.getData().size();
//            mStateHelper.showEmpty(R.string.list_empty_msg);
        }
        else {

        }
    }

}
