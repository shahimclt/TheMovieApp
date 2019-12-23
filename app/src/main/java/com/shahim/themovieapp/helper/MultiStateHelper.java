package com.shahim.themovieapp.helper;

import android.view.View;
import android.widget.TextView;

import androidx.annotation.StringRes;

import com.kennyc.view.MultiStateView;
import com.shahim.themovieapp.R;

import mehdi.sakout.fancybuttons.FancyButton;

import static com.kennyc.view.MultiStateView.VIEW_STATE_CONTENT;
import static com.kennyc.view.MultiStateView.VIEW_STATE_EMPTY;
import static com.kennyc.view.MultiStateView.VIEW_STATE_ERROR;
import static com.kennyc.view.MultiStateView.VIEW_STATE_LOADING;

/**
 * Created by Shahi on 22-08-2017.
 */

public class MultiStateHelper {

    private MultiStateView multiStateView;

    public MultiStateHelper(MultiStateView view) {
        multiStateView = view;
    }

    public void showContent() {
        multiStateView.setViewState(VIEW_STATE_CONTENT);
    }

    public void showloading() {
        multiStateView.setViewState(VIEW_STATE_LOADING);
    }

    public void showError(@StringRes int errorMsg, View.OnClickListener _lis) {
        multiStateView.setViewState(VIEW_STATE_ERROR);

        View errorView = multiStateView.getView(VIEW_STATE_ERROR);
        ((TextView)errorView.findViewById(R.id.msv_error_msg)).setText(errorMsg);
        FancyButton fBtn = errorView.findViewById(R.id.msv_error_btn);
        fBtn.setOnClickListener(_lis);
    }

    public void showEmpty(@StringRes int msg) {
        multiStateView.setViewState(VIEW_STATE_EMPTY);

        View errorView = multiStateView.getView(VIEW_STATE_EMPTY);
        ((TextView)errorView.findViewById(R.id.msv_empty_msg)).setText(msg);
        errorView.findViewById(R.id.msv_empty_btn).setVisibility(View.GONE);
    }

    public void showEmpty(@StringRes int msg, String btnTitle, String btnIcon, View.OnClickListener _lis) {
        showEmpty("",btnTitle,btnIcon,_lis);
        View errorView = multiStateView.getView(VIEW_STATE_EMPTY);
        ((TextView)errorView.findViewById(R.id.msv_empty_msg)).setText(msg);
    }
    public void showEmpty(CharSequence msg, String btnTitle, String btnIcon, View.OnClickListener _lis) {
        multiStateView.setViewState(VIEW_STATE_EMPTY);

        View errorView = multiStateView.getView(VIEW_STATE_EMPTY);
        ((TextView)errorView.findViewById(R.id.msv_empty_msg)).setText(msg);
        FancyButton fBtn = errorView.findViewById(R.id.msv_empty_btn);
        fBtn.setVisibility(View.VISIBLE);
        fBtn.setText(btnTitle);
        fBtn.setIconResource(btnIcon);
        fBtn.setOnClickListener(_lis);
    }
}
