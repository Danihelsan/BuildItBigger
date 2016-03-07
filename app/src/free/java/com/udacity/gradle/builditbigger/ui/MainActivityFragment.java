package com.udacity.gradle.builditbigger.ui;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;

import com.google.android.gms.ads.AdListener;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdView;
import com.google.android.gms.ads.InterstitialAd;
import com.udacity.gradle.builditbigger.R;
import com.udacity.gradle.builditbigger.asynctasks.EndpointsAsyncTask;
import com.udacity.gradle.builditbigger.interfaces.OnJokeCallBack;
import com.udacity.gradle.builditbigger.interfaces.OnLoadingListener;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;
import pe.asomapps.jokeviewer.JokeViewerActivity;


/**
 * A placeholder fragment containing a simple view.
 */
public class MainActivityFragment extends Fragment implements OnJokeCallBack, OnLoadingListener {
    private InterstitialAd mInterstitialAd;
    private String jokeToShow;

    @Bind(R.id.progressBar)
    ProgressBar bar;

    @OnClick(R.id.tellJoke) void tellJoke(){
        new EndpointsAsyncTask(this.getContext(),this, this).execute();
        if (mInterstitialAd.isLoaded()) {
            mInterstitialAd.show();
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View root = inflater.inflate(R.layout.fragment_main, container, false);
        ButterKnife.bind(this,root);

        loadInterSitialAd();

        AdView mAdView = (AdView) root.findViewById(R.id.adView);
        // Create an ad request. Check logcat output for the hashed device ID to
        // get test ads on a physical device. e.g.
        // "Use AdRequest.Builder.addTestDevice("ABCDEF012345") to get test ads on this device."
        AdRequest adRequest = new AdRequest.Builder()
                .addTestDevice(AdRequest.DEVICE_ID_EMULATOR)
                .build();
        mAdView.loadAd(adRequest);

        return root;
    }

    private void loadInterSitialAd() {
        mInterstitialAd = new InterstitialAd(this.getContext());
        mInterstitialAd.setAdUnitId(getString(R.string.intersitial_ad_unit_id));

        mInterstitialAd.setAdListener(new AdListener() {
            @Override
            public void onAdClosed() {
                requestNewInterstitial();
                if (jokeToShow!=null){
                    openJokeViewer(jokeToShow);
                }
            }
        });

        requestNewInterstitial();
    }

    private void requestNewInterstitial() {
        AdRequest adRequest = new AdRequest.Builder()
                .addTestDevice(AdRequest.DEVICE_ID_EMULATOR)
                .build();

        mInterstitialAd.loadAd(adRequest);
    }

    @Override
    public void onJokeRetrieved(String joke) {
        jokeToShow = joke;
        if (mInterstitialAd.isLoaded()){
            mInterstitialAd.show();
        } else {
            openJokeViewer(jokeToShow);
        }
    }

    @Override
    public void onErrorRetrieved(Exception joke) {
        //TODO do something with this exception
    }

    @Override
    public void beforeLoading() {
        if (this.isAdded()){
            bar.setVisibility(View.VISIBLE);
        }
    }

    @Override
    public void afterLoading() {
        if (this.isAdded()){
            bar.setVisibility(View.GONE);
        }
    }

    private void openJokeViewer(String jokeToShow) {
        Bundle bundle = new Bundle();
        bundle.putString(JokeViewerActivity.KEY_JOKE, jokeToShow);

        Intent intent = new Intent(MainActivityFragment.this.getActivity(),JokeViewerActivity.class);
        intent.putExtras(bundle);
        startActivity(intent);
        jokeToShow = null;
    }
}
