package com.doomonafireball.kingofstuff.android.api;

import android.app.IntentService;
import android.content.Intent;
import android.os.Bundle;

import com.doomonafireball.kingofstuff.android.api.model.Page;
import com.doomonafireball.kingofstuff.android.otto.BusProvider;
import com.doomonafireball.kingofstuff.android.otto.event.PageEvent;
import com.google.gson.Gson;
import com.squareup.okhttp.OkHttpClient;
import com.squareup.okhttp.Request;
import com.squareup.okhttp.Response;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;

/**
 * Created by derek on 6/5/14.
 */
public class WikipediaApiService extends IntentService {

    public static final String URL_GET_PAGE = "http://en.wikipedia.org/w/api.php?action=query&prop=extracts&format=json&exsentences=1&exlimit=10&exintro=&explaintext=&generator=random&grnnamespace=0&grnlimit=1";
    public static final String URL_GET_PAGES = "http://en.wikipedia.org/w/api.php?action=query&prop=extracts&format=json&exsentences=1&exlimit=10&exintro=&explaintext=&generator=random&grnnamespace=0&grnlimit=5";
    public static final String API_CALL = "WikipediaApiService_ApiCall";
    public static final int GET_NEW_PAGE = 0x00;
    public static final int GET_NEW_PAGES = 0x01;

    public WikipediaApiService() {
        super("WikipediaApiService");
    }

    @Override
    protected void onHandleIntent(Intent intent) {
        Bundle extras = intent.getExtras();
        switch (extras.getInt(API_CALL)) {
            case GET_NEW_PAGE:
                getNewPage();
                break;
            case GET_NEW_PAGES:
                getNewPages();
                break;
        }
    }

    private void getNewPage() {
        OkHttpClient client = new OkHttpClient();
        Request request = new Request.Builder()
                .url(URL_GET_PAGE)
                .build();
        try {
            Gson gson = new Gson();
            Response response = client.newCall(request).execute();
            BufferedReader reader = new BufferedReader(new InputStreamReader(response.body().byteStream()));
            WikipediaResponse wikipediaResponse = gson.fromJson(reader, WikipediaResponse.class);
            Page[] pages = wikipediaResponse.query.pages.values().toArray(new Page[0]);
            BusProvider.getInstance().post(new PageEvent(pages[0]));
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void getNewPages() {
        OkHttpClient client = new OkHttpClient();
        Request request = new Request.Builder()
                .url(URL_GET_PAGES)
                .build();
        try {
            Gson gson = new Gson();
            Response response = client.newCall(request).execute();
            BufferedReader reader = new BufferedReader(new InputStreamReader(response.body().byteStream()));
            WikipediaResponse wikipediaResponse = gson.fromJson(reader, WikipediaResponse.class);
            for (Page page : wikipediaResponse.query.pages.values().toArray(new Page[0])) {
                BusProvider.getInstance().post(new PageEvent(page));
                Thread.sleep(100);
            }
        } catch (IOException e) {
            e.printStackTrace();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }
}
