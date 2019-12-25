package com.shahim.themovieapp;

import androidx.annotation.ColorRes;
import androidx.annotation.StringRes;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.content.ContextCompat;
import androidx.palette.graphics.Palette;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.text.Spannable;
import android.text.SpannableString;
import android.text.SpannableStringBuilder;
import android.text.style.ForegroundColorSpan;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.github.florent37.fiftyshadesof.FiftyShadesOf;
import com.google.android.material.appbar.CollapsingToolbarLayout;
import com.google.android.material.snackbar.Snackbar;
import com.shahim.themovieapp.api.APIClient;
import com.shahim.themovieapp.api.APIInterface;
import com.shahim.themovieapp.api.Pojo.Movie;
import com.shahim.themovieapp.api.Pojo.MovieDetail;
import com.shahim.themovieapp.api.Pojo.Rating;
import com.shahim.themovieapp.helper.BookmarksManagerSingleton;
import com.squareup.picasso.Picasso;
import com.squareup.picasso.Target;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import jp.wasabeef.picasso.transformations.BlurTransformation;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import xyz.hanks.library.bang.SmallBangView;

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
        displayInitial();
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

    void displayInitial() {
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

        BookmarksManagerSingleton bms = BookmarksManagerSingleton.sharedInstance(this);
        if(bms.isBookmarked(this,mMovie)) {
            mBookmarkToggle.setSelected(true);
            mBookmarkImage.setImageDrawable(ContextCompat.getDrawable(this,R.drawable.ic_bookmark_24dp));
        }
        else {
            mBookmarkToggle.setSelected(false);
            mBookmarkImage.setImageDrawable(ContextCompat.getDrawable(this,R.drawable.ic_bookmark_border_24dp));
        }
    }

    void loadData() {
        final FiftyShadesOf fiftyShadesOf = FiftyShadesOf.with(this)
                .on(mDetailHolder)
                .start();
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
                fiftyShadesOf.stop();
            }

            @Override
            public void onFailure(Call<MovieDetail> call, Throwable t) {
                //TODO do something
                fiftyShadesOf.stop();
            }
        });
    }

    @BindView(R.id.movie_detail_holder)
    View mDetailHolder;
    @BindView(R.id.movie_rating)
    TextView mRating;
    @BindView(R.id.movie_runtime)
    TextView mRuntime;
    @BindView(R.id.movie_synopsis)
    TextView mPlot;

    @BindView(R.id.external_ratings_holder)
    ViewGroup mRatingsHolder;

    @BindView(R.id.movie_director)
    TextView mDirector;
    @BindView(R.id.movie_writer)
    TextView mWriter;
    @BindView(R.id.movie_cast)
    TextView mCast;

    void refreshView() {
        LayoutInflater inflater = getLayoutInflater();
        for (String g : mMovieDetail.getGenre().split(",")) {
            TextView gText = (TextView) inflater.inflate(R.layout.stub_movie_genre,genreHolder,false);
            gText.setText(g.trim());
            genreHolder.addView(gText);
        }

        mRating.setText(mMovieDetail.getRated());
        mRuntime.setText(mMovieDetail.getRuntime());

        for (Rating rating : mMovieDetail.getRatings()) {
            View ratingView = inflater.inflate(R.layout.stub_movie_rating_ext,mRatingsHolder,false);
            ((ImageView)ratingView.findViewById(R.id.rater_image)).setImageDrawable(ContextCompat.getDrawable(this,rating.getSourceIcon()));
            ((TextView)ratingView.findViewById(R.id.rater_name)).setText(rating.getSource());
            ((TextView)ratingView.findViewById(R.id.rater_val)).setText(rating.getValue());
            mRatingsHolder.addView(ratingView);
        }

        mPlot.setText(mMovieDetail.getPlot());

        //Cast n Crew

        SpannableStringBuilder ssb;

        ssb = new SpannableStringBuilder("");
        ssb.append(getSpannable(R.string.movie_detail_director,R.color.textColorSecondary));
        ssb.append(getSpannable(mMovieDetail.getDirector(),R.color.textColorPrimary));
        mDirector.setText(ssb);

        mWriter.setText(getCommaFormattedSpannable(R.string.movie_detail_writer,mMovieDetail.getWriter()));
        mCast.setText(getCommaFormattedSpannable(R.string.movie_detail_cast,mMovieDetail.getActors()));

    }

    CharSequence getCommaFormattedSpannable(@StringRes int label, String value) {
        SpannableStringBuilder ssb = new SpannableStringBuilder("");
        ssb.append(getSpannable(label,R.color.textColorSecondary));
        ssb.append(" ");
        for (String section : value.split(",")) {
            String[] parts = section.split("\\(");
            ssb.append(getSpannable(parts[0],R.color.textColorPrimary));
            if (parts.length>1) {
                ssb.append(getSpannable("(" + parts[1], R.color.textColorSecondary));
            }
            ssb.append(getSpannable(",", R.color.textColorSecondary));
        }

        return ssb.subSequence(0,ssb.length()-1);
    }

    SpannableString getSpannable(@StringRes int value, @ColorRes int color) {
        return getSpannable(getString(value),color);
    }
    SpannableString getSpannable(String value, @ColorRes int color) {
        SpannableString sp = new SpannableString(value);
        sp.setSpan(new ForegroundColorSpan(ContextCompat.getColor(this,color)),0,sp.length(),Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
        return sp;
    }

    @BindView(R.id.movie_bookmark)
    SmallBangView mBookmarkToggle;
    @BindView(R.id.movie_bookmark_image)
    ImageView mBookmarkImage;

    @OnClick(R.id.movie_bookmark)
    void bookmarkClicked() {
        if (mBookmarkToggle.isSelected()) {
            mBookmarkToggle.setSelected(false);
            mBookmarkImage.setImageDrawable(ContextCompat.getDrawable(this,R.drawable.ic_bookmark_border_24dp));
            Snackbar.make(mBookmarkToggle, R.string.movie_bookmark_removed, Snackbar.LENGTH_SHORT).show();
            BookmarksManagerSingleton.sharedInstance(this).removeBookmark(this,mMovie);
        } else {
            mBookmarkToggle.setSelected(true);
            mBookmarkImage.setImageDrawable(ContextCompat.getDrawable(this,R.drawable.ic_bookmark_24dp));
            mBookmarkToggle.likeAnimation(new AnimatorListenerAdapter() {
                @Override
                public void onAnimationEnd(Animator animation) {
                    super.onAnimationEnd(animation);
                }
            });
            Snackbar.make(mBookmarkToggle, R.string.movie_bookmark_added, Snackbar.LENGTH_SHORT).show();
            BookmarksManagerSingleton.sharedInstance(this).addBookmark(this,mMovie);
        }
    }
}
