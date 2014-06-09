package com.doomonafireball.kingofbs.android.api.model;

import com.google.gson.annotations.SerializedName;

import java.util.Map;

/**
 * Created by derek on 6/5/14.
 */
public class Query {

    @SerializedName("pages")
    public Map<String, Page> pages;

}
