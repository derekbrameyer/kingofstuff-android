package com.doomonafireball.kingofbs.android.api;

import com.doomonafireball.kingofbs.android.api.model.Query;
import com.google.gson.annotations.SerializedName;

/**
 * Created by derek on 6/5/14.
 */
public class WikipediaResponse {

    @SerializedName("query")
    public Query query;
}
