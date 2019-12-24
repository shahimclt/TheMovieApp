package com.shahim.themovieapp;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.content.ContextCompat;
import androidx.palette.graphics.Palette;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.google.android.material.appbar.CollapsingToolbarLayout;
import com.shahim.themovieapp.api.APIClient;
import com.shahim.themovieapp.api.APIInterface;
import com.shahim.themovieapp.api.Pojo.Movie;
import com.shahim.themovieapp.api.Pojo.MovieDetail;
import com.shahim.themovieapp.api.Pojo.SearchResult;
import com.squareup.picasso.Picasso;
import com.squareup.picasso.Target;

import butterknife.BindView;
import butterknife.ButterKnife;
import jp.wasabeef.picasso.transformations.BlurTransformation;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class MovieDetailActivity extends AppCompatActivity {

    @BindView(R.id.toolbar)
    Toolbar mToolbar;
    @BindView(R.id.collapsingToolbar)
    CollapsingToolbarLayout mCollapsingToolbar;

    @BindView(R.id.toolbarImage)
    ImageView bgPoster;
    @BindView(R.id.poster)
    ImageView mainPoster;
    @BindView(R.id.bgSubtitle)
    ViewGroup genreHolder;
    @BindView(R.id.show_type)
    ImageView showTypeIcon;
    @BindView(R.id.item_desc)
    TextView mYear;

    private static final String EXTRA_KEY_MOVIE = "omdbmovie";

    private Movie mMovie;
    private MovieDetail mMovieDetail;

    public static Intent craftIntent(Context c, Movie movie) {
        Intent detail = new Intent(c, MovieDetailActivity.class);
        detail.putExtra(EXTRA_KEY_MOVIE,movie);
        return detail;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_movie_detail);
        ButterKnife.bind(this);
        setSupportActionBar(mToolbar);
        init();
        displayDetail();
        extractColors();
        loadData();
    }

    private void init() {
        Intent myIntent = getIntent();
        mMovie = myIntent.getParcelableExtra(EXTRA_KEY_MOVIE);
        if (mMovie== null) {
            finish();
        }
    }

    void extractColors() {
        Picasso.with(this)
            .load(mMovie.getPoster())
            .into(new Target() {
                @Override
                public void onBitmapLoaded(Bitmap bitmap, Picasso.LoadedFrom from) {
                    Palette.from(bitmap)
                            .generate(palette -> {
                                Palette.Swatch textSwatch = palette.getVibrantSwatch();
                                if (textSwatch == null) {
                                    return;
                                }

                                mCollapsingToolbar.setContentScrimColor(textSwatch.getRgb());
                                mCollapsingToolbar.setStatusBarScrimColor(textSwatch.getRgb());
//                                    backgroundGroup.setBackgroundColor(textSwatch.getRgb());
//                                    titleColorText.setTextColor(textSwatch.getTitleTextColor());
//                                    bodyColorText.setTextColor(textSwatch.getBodyTextColor());
                            });
                }

                @Override
                public void onBitmapFailed(Drawable errorDrawable) {

                }

                @Override
                public void onPrepareLoad(Drawable placeHolderDrawable) {

                }
            });
    }

    void displayDetail() {
        mCollapsingToolbar.setTitle(mMovie.getTitle());

        showTypeIcon.setImageDrawable(ContextCompat.getDrawable(this,mMovie.getTypeIcon()));
        mYear.setText(mMovie.getYear());

        Picasso.with(this)
                .load(mMovie.getPoster())
                .transform(new BlurTransformation(this, 50, 1))
                .error(R.drawable.im_movie_poster)
                .into(bgPoster);

        Picasso.with(this)
                .load(mMovie.getPoster())
                .error(R.drawable.im_movie_poster)
                .into(mainPoster);
    }

    void loadData() {
        APIInterface mAPI = APIClient.getClient().create(APIInterface.class);
        Call<MovieDetail> mCall = mAPI.getMovieDetails(mMovie.getImdbID());
        mCall.enqueue(new Callback<MovieDetail>() {
            @Override
            public void onResponse(Call<MovieDetail> call, Response<MovieDetail> response) {
                if (response.isSuccessful()) {
                    MovieDetail res = response.body();
                    mMovieDetail = res;

                    refreshView();
                }
                else {
                    //TODO show something
                }
            }

            @Override
            public void onFailure(Call<MovieDetail> call, Throwable t) {
                //TODO do something
            }
        });
    }

    void refreshView() {
        LayoutInflater inflater = getLayoutInflater();
        for (String g : mMovieDetail.getGenre().split(",")) {
            TextView gText = (TextView) inflater.inflate(R.layout.stub_movie_genre,genreHolder,false);
            gText.setText(g.trim());
            genreHolder.addView(gText);
        }
    }
}
