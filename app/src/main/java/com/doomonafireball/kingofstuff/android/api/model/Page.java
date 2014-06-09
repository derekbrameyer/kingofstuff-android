package com.doomonafireball.kingofstuff.android.api.model;

import com.google.gson.annotations.SerializedName;

/**
 * Created by derek on 6/5/14.
 */
public class Page {

    @SerializedName("pageid")
    public String pageId;
    @SerializedName("ns")
    public String ns;
    @SerializedName("title")
    public String title;
    @SerializedName("extract")
    public String extract;
}
