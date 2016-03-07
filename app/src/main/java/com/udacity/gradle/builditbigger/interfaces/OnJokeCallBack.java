package com.udacity.gradle.builditbigger.interfaces;

/**
 * Created by Danihelsan
 */
public interface OnJokeCallBack {
    void onJokeRetrieved(String joke);

    void onErrorRetrieved(Exception joke);
}
