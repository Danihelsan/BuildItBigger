package com.udacity.gradle.builditbigger.ui;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;

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
public class MainActivityFragment extends Fragment implements OnJokeCallBack, OnLoadingListener{
    @Bind(R.id.progressBar)
    ProgressBar bar;

    @OnClick(R.id.tellJoke) void tellJoke(){
        new EndpointsAsyncTask(this.getContext(),this, this).execute();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View root = inflater.inflate(R.layout.fragment_main, container, false);
        ButterKnife.bind(this,root);


        return root;
    }
    @Override
    public void onJokeRetrieved(String joke) {
        openJokeViewer(joke);
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
