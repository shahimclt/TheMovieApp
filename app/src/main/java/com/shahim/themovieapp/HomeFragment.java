package com.shahim.themovieapp;

import android.content.Context;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.EditorInfo;
import android.view.inputmethod.InputMethodManager;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.google.android.material.textfield.TextInputEditText;
import com.shahim.themovieapp.api.APIClient;
import com.shahim.themovieapp.api.APIInterface;
import com.shahim.themovieapp.api.Pojo.SearchResult;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.Unbinder;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class HomeFragment extends Fragment {

    @BindView(R.id.recyclerView)
    RecyclerView mRecyclerView;

    @BindView(R.id.search_input)
    TextInputEditText searchInput;

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
        searchInput.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView textView, int i, KeyEvent keyEvent) {
                if(i== EditorInfo.IME_ACTION_SEARCH) {
                    InputMethodManager inputManager = (InputMethodManager) getActivity().getSystemService(Context.INPUT_METHOD_SERVICE);
                    inputManager.hideSoftInputFromWindow(getActivity().getCurrentFocus().getWindowToken(),InputMethodManager.HIDE_NOT_ALWAYS);
                    quesry = textView.getText().toString();
                    mCurrentCounter = 0;
                    mAdapter.setNewData(null);
                    loadData(1);
                    return true;
                }
                return false;
            }
        });
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
        mAdapter.setPreLoadNumber(2);
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
        if (mCurrentCounter==0) {
            mAdapter.setEmptyView(R.layout.msv_layout_loadingview,mRecyclerView);
        }
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
                    else {
                        View errorView = getLayoutInflater().inflate(R.layout.msv_layout_errorview, mRecyclerView, false);
                        errorView.findViewById(R.id.retry_btn).setOnClickListener(v -> loadData(page));
                        mAdapter.setEmptyView(errorView);
                    }
                }
            }

            @Override
            public void onFailure(Call<SearchResult> call, Throwable t) {
                if (page>1) {
                    mAdapter.loadMoreFail();
                }
                else {
                    View errorView = getLayoutInflater().inflate(R.layout.msv_layout_errorview, mRecyclerView, false);
                    errorView.findViewById(R.id.retry_btn).setOnClickListener(v -> loadData(page));
                    mAdapter.setEmptyView(errorView);
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
            mAdapter.setNewData(searchResult.getSearch());
            mCurrentCounter = mAdapter.getData().size();
//            mStateHelper.showEmpty(R.string.list_empty_msg);
        }
        else {
            View errorView = getLayoutInflater().inflate(R.layout.msv_layout_errorview, mRecyclerView, false);
            ((TextView)errorView.findViewById(R.id.msv_error_desc)).setText(searchResult.getError());
            ((TextView)errorView.findViewById(R.id.msv_error_desc)).setVisibility(View.VISIBLE);
            errorView.findViewById(R.id.retry_btn).setOnClickListener(v -> loadData(1));
            mAdapter.setEmptyView(errorView);
        }
    }

}
