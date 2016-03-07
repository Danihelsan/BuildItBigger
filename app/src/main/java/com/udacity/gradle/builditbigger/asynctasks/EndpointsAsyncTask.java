package com.udacity.gradle.builditbigger.asynctasks;

import android.content.Context;
import android.os.AsyncTask;
import android.widget.Toast;

import com.asomapps.backend.joker.myApi.MyApi;
import com.google.api.client.extensions.android.http.AndroidHttp;
import com.google.api.client.extensions.android.json.AndroidJsonFactory;
import com.google.api.client.googleapis.services.AbstractGoogleClientRequest;
import com.google.api.client.googleapis.services.GoogleClientRequestInitializer;
import com.udacity.gradle.builditbigger.data.JokeResponse;
import com.udacity.gradle.builditbigger.interfaces.OnJokeCallBack;
import com.udacity.gradle.builditbigger.interfaces.OnLoadingListener;

import java.io.IOException;

/**
 * Created by Danihelsan
 */
public class EndpointsAsyncTask extends AsyncTask<Void, Void, JokeResponse> {
    private static MyApi myApiService = null;
    private Context context;
    private OnJokeCallBack callBack;
    private OnLoadingListener loadingListener;

    public EndpointsAsyncTask(Context context, OnJokeCallBack callBack, OnLoadingListener loadingListener){
        this.context = context;
        this.callBack = callBack;
        this.loadingListener = loadingListener;
    }

    @Override
    protected void onPreExecute() {
        super.onPreExecute();
        if (loadingListener!=null){
            loadingListener.beforeLoading();
        }
    }

    @Override
    protected JokeResponse doInBackground(Void... params) {

        if(myApiService == null) {  // Only do this once

            String fullRoot = "http://10.0.2.2:8080/_ah/api/";
            MyApi.Builder builder = new MyApi.Builder(AndroidHttp.newCompatibleTransport(), new AndroidJsonFactory(), null)
                    // options for running against local devappserver
                    // - 10.0.2.2 is localhost's IP address in Android emulator
                    // - turn off compression when running against local devappserver
                    .setRootUrl(fullRoot).setGoogleClientRequestInitializer(new GoogleClientRequestInitializer() {
                        @Override
                        public void initialize(AbstractGoogleClientRequest<?> abstractGoogleClientRequest) throws IOException {
                            abstractGoogleClientRequest.setDisableGZipContent(true);
                        }
                    });
            // end options for devappserver
            myApiService = builder.build();

        }

        // MOCKING LOADING
        try {
            Thread.sleep(2000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        // MOCKING LOADING

        try {
            String joke = myApiService.tellJoke().execute().getJoke();
            return new JokeResponse(joke);
        } catch (IOException e) {
            JokeResponse response = new JokeResponse(true,e);
            return response;
        }
    }

    @Override
    protected void onPostExecute(JokeResponse result) {
        if (loadingListener!=null){
            loadingListener.afterLoading();
        }
        if (result!=null){
            if(callBack!=null){
                if (!result.isError()){
                    callBack.onJokeRetrieved((String)result.getResponse());
                } else {
                    callBack.onErrorRetrieved((Exception) result.getResponse());
                }
            } else {
                Toast.makeText(context, "CALLBACK NULL", Toast.LENGTH_LONG).show();
            }
        } else{
            Toast.makeText(context, "RESPONSE NULL", Toast.LENGTH_LONG).show();
        }
    }
}