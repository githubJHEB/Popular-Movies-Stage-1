package com.example.android.moviesapp;
import android.content.Intent;
import android.support.v4.app.Fragment;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RatingBar;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

public class Detail_Activity extends AppCompatActivity {

    private static final String DEGBUG_TA = "TRAK";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail);

        if (savedInstanceState == null) {
            getSupportFragmentManager().beginTransaction()
                    .add(R.id.container, new DetailFragment())
                    .commit();
            ActionBar g = getSupportActionBar();
            g.setDisplayShowHomeEnabled(true);
            g.setIcon(R.drawable.camara);
        }

    }

    /**
     * A placeholder fragment containing a simple view.
     */
    public static class DetailFragment extends Fragment {
        String mDetaiDataStr;
        String detailTitle;
        String detailReleaeDate;
        String detailVote;
        String detailSypnopsis;
        String urlImage;

        public DetailFragment() {}

        @Override
        public View onCreateView(LayoutInflater inflater, ViewGroup container,
                                 Bundle savedInstanceState) {
            View rootView = inflater.inflate(R.layout.fragment_detail, container, false);
            Intent intent = getActivity().getIntent();
            Bundle extras = intent.getExtras();
            if (intent != null && extras != null) {
                mDetaiDataStr = extras.getString("moviesStrDataP");
                urlImage = mDetaiDataStr.substring(mDetaiDataStr.indexOf("#0##") + 4, mDetaiDataStr.indexOf("#1##"));
                detailTitle = mDetaiDataStr.substring(mDetaiDataStr.indexOf("#1##") + 4, mDetaiDataStr.indexOf("#2##"));
                detailReleaeDate = mDetaiDataStr.substring(mDetaiDataStr.indexOf("#2##") + 4, mDetaiDataStr.indexOf("#3##"));
                detailVote = mDetaiDataStr.substring(mDetaiDataStr.indexOf("#3##") + 4, mDetaiDataStr.indexOf("#4##"));
                detailSypnopsis = mDetaiDataStr.substring(mDetaiDataStr.indexOf("#4##") + 4, mDetaiDataStr.indexOf("#5##"));
                ImageView image = (ImageView) rootView.findViewById(R.id.detail_image_poster);
                Picasso
                        .with(getActivity())
                        .load(urlImage)
                        .placeholder(R.drawable.timer)
                        .fit()
                        .into(image);
                TextView titleDisplay = (TextView) rootView.findViewById(R.id.detail_title_textview);
                titleDisplay.setText(detailTitle);
                TextView releaseDate = (TextView) rootView.findViewById(R.id.detail_release_date_textview);
                releaseDate.setText(detailReleaeDate);
                RatingBar mRatingBar = (RatingBar) rootView.findViewById(R.id.rating);
                float number = Float.valueOf(detailVote) / 2;
                mRatingBar.setRating(Float.valueOf(number));
                TextView voteDisplay = (TextView) rootView.findViewById(R.id.detail_vote_average_textview);
                voteDisplay.setText(String.valueOf(number));
                TextView sypnopsisDate = (TextView) rootView.findViewById(R.id.detail_sypnopsis_textview);
                sypnopsisDate.setText(detailSypnopsis);
            }
            return rootView;
        }
    }
}
