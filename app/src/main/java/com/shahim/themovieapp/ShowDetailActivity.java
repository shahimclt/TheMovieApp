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
import android.widget.ImageView;

import com.google.android.material.appbar.CollapsingToolbarLayout;
import com.shahim.themovieapp.api.Pojo.OmdbShow;
import com.squareup.picasso.Picasso;
import com.squareup.picasso.Target;

import butterknife.BindView;
import butterknife.ButterKnife;
import jp.wasabeef.picasso.transformations.BlurTransformation;

public class ShowDetailActivity extends AppCompatActivity {

    @BindView(R.id.toolbar)
    Toolbar mToolbar;
    @BindView(R.id.collapsingToolbar)
    CollapsingToolbarLayout mCollapsingToolbar;

    @BindView(R.id.toolbarImage)
    ImageView bgPoster;
    @BindView(R.id.poster)
    ImageView mainPoster;

    private static final String EXTRA_KEY_MOVIE = "omdbmovie";

    private OmdbShow mMovie;

    public static Intent craftIntent(Context c, OmdbShow movie) {
        Intent detail = new Intent(c, ShowDetailActivity.class);
        detail.putExtra(EXTRA_KEY_MOVIE,movie);
        return detail;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_show_detail);
        ButterKnife.bind(this);
        setSupportActionBar(mToolbar);
        init();
        displayDetail();
        extractColors();
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
}
