package com.udacity.gradle.builditbigger;

import android.test.AndroidTestCase;

import com.udacity.gradle.builditbigger.asynctasks.EndpointsAsyncTask;
import com.udacity.gradle.builditbigger.interfaces.OnJokeCallBack;

import java.util.concurrent.CountDownLatch;

/**
 * Created by Danihelsan
 */
public class EndpointAsynctaskTest extends AndroidTestCase {
    @Override
    protected void setUp() throws Exception {
        super.setUp();
    }

    public void testVerifyJokeServerResponse() {
        // keep execution going only if the response of the server is a joke
        final CountDownLatch countDownLatch = new CountDownLatch(1);

        OnJokeCallBack callBack = new OnJokeCallBack() {
            @Override
            public void onJokeRetrieved(String joke) {
                assertNotNull("Joke is null", joke);
                assertTrue("Joke is empty", joke.length() > 0);
                countDownLatch.countDown();
            }

            @Override
            public void onErrorRetrieved(Exception joke) {
                assertTrue("There was no response of the server", false);
            }
        };
        assertNotNull("callBack null", callBack);

        EndpointsAsyncTask asyncTask = new EndpointsAsyncTask(getContext(), callBack, null);
        assertNotNull("asyncTask null", asyncTask);
        asyncTask.execute();
        try {
            countDownLatch.await();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    @Override
    protected void tearDown() throws Exception {
        super.tearDown();
    }
}
