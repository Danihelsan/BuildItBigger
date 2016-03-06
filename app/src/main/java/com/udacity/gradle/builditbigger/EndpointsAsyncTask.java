package com.udacity.gradle.builditbigger;

import android.content.Context;
import android.os.AsyncTask;
import android.widget.Toast;

import com.asomapps.backend.joker.myApi.MyApi;
import com.google.api.client.extensions.android.http.AndroidHttp;
import com.google.api.client.extensions.android.json.AndroidJsonFactory;
import com.google.api.client.googleapis.services.AbstractGoogleClientRequest;
import com.google.api.client.googleapis.services.GoogleClientRequestInitializer;

import java.io.IOException;

/**
 * Created by Danihelsan
 */
public class EndpointsAsyncTask extends AsyncTask<Void, Void, String> {
    private static MyApi myApiService = null;
    private Context context;
    private OnJokeCallBack callBack;

    EndpointsAsyncTask(Context context, OnJokeCallBack callBack){
        this.context = context;
        this.callBack = callBack;
    }

    @Override
    protected String doInBackground(Void... params) {

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

            try {
                return myApiService.tellJoke().execute().getJoke();
            } catch (IOException e) {
                return e.getMessage();
            }
        }

        return null;
    }

    @Override
    protected void onPostExecute(String result) {
        if (result!=null){
            if(callBack!=null){
                callBack.onJokeRetreived(result);
            } else {
                Toast.makeText(context, result, Toast.LENGTH_LONG).show();
            }
        } else{
            Toast.makeText(context, "NO RESPONSE", Toast.LENGTH_LONG).show();
        }
    }
}