package com.shahim.themovieapp;

import android.app.DownloadManager;
import android.net.Uri;
import android.os.Bundle;

import com.kennyc.view.MultiStateView;
import com.shahim.themovieapp.api.APIClient;
import com.shahim.themovieapp.api.APIInterface;
import com.shahim.themovieapp.api.VideoItem;
import com.shahim.themovieapp.helper.MultiStateHelper;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.Menu;
import android.view.MenuItem;
import android.widget.Toast;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class MainActivity extends AppCompatActivity {

    @BindView(R.id.multiStateView)
    MultiStateView mStateView;
    MultiStateHelper mStateHelper;

    @BindView(R.id.recyclerView)
    RecyclerView mRecyclerView;

    VideoListQuickAdapter mAdapter;

    ArrayList<VideoItem> mVideos;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ButterKnife.bind(this);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        init();
        loadData();
    }

    void init() {
        mStateHelper = new MultiStateHelper(mStateView);
        mVideos = new ArrayList<>();
        initAdapter();
    }

    void initAdapter() {
        mRecyclerView.setLayoutManager(new LinearLayoutManager(this));
        mRecyclerView.setHasFixedSize(true);
        mAdapter = new VideoListQuickAdapter(new ArrayList<VideoItem>());
        mAdapter.openLoadAnimation();
        mAdapter.setOnItemChildClickListener((adapter, view, position) -> {
            if (view.getId()==R.id.downloadButton) {
                triggerDownload(mVideos.get(position));
            }
        });
        mRecyclerView.setAdapter(mAdapter);
    }

    private void triggerDownload(VideoItem it) {
        if (it.getSources().size()==0) {
            Toast.makeText(this,"Uh oh! This video has no sources.",Toast.LENGTH_SHORT).show();
            return;
        }
        DownloadManager.Request request=new DownloadManager.Request(Uri.parse(it.getSources().get(0)))
                .setTitle(it.getTitle())// Title of the Download Notification
                .setDescription("Downloading Video")// Description of the Download Notification
                .setNotificationVisibility(DownloadManager.Request.VISIBILITY_VISIBLE);// Visibility of the download Notification

        DownloadManager downloadManager= (DownloadManager) getSystemService(DOWNLOAD_SERVICE);
        long downloadID = downloadManager.enqueue(request);

        Toast.makeText(this,"Video has started downloading. Check Notification for details",Toast.LENGTH_SHORT).show();
    }

    private void loadData() {
        mStateHelper.showloading();

        APIInterface mAPI = APIClient.getClient().create(APIInterface.class);
        Call<ArrayList<VideoItem>> mCall = mAPI.getVideoList();
        mCall.enqueue(new Callback<ArrayList<VideoItem>>() {
            @Override
            public void onResponse(Call<ArrayList<VideoItem>> call, Response<ArrayList<VideoItem>> response) {
                if (response.isSuccessful()) {
                    ArrayList<VideoItem> lr = response.body();
                    if (lr==null) {
                        lr = new ArrayList<>();
                    }
                    mVideos = lr;
                    refreshView();
                }
                else {
                    mStateHelper.showError(R.string.list_error_msg, view -> loadData());
                }
            }

            @Override
            public void onFailure(Call<ArrayList<VideoItem>> call, Throwable t) {
                mStateHelper.showError(R.string.error_generic_network, view -> loadData());
            }
        });
    }

    void refreshView() {
        if (mVideos.size()==0) {
            mStateHelper.showEmpty(R.string.list_empty_msg);
        }
        else {
            if (mAdapter==null) {
                return;
            }
            mStateHelper.showContent();
            mAdapter.updateItems(mVideos);
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }
}
