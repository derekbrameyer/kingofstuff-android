package com.doomonafireball.kingofbs.android.widget;

import android.content.Context;
import android.text.Html;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.doomonafireball.kingofstuff.android.R;

import java.util.Random;

/**
 * Created by derek on 6/5/14.
 */
public class PageContainer extends RelativeLayout {

    public boolean animateFromLeft;
    private TextView titleTextView;
    private TextView extractTextView;
    private boolean mIsTitleShowing = true;

    public PageContainer(Context context) {
        this(context, null, 0);
    }

    public PageContainer(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public PageContainer(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        init(context, attrs);
    }

    private void init(Context context, AttributeSet attrs) {
        setClickable(true);
        animateFromLeft = new Random().nextBoolean();

        LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        inflater.inflate(R.layout.page_container, this, true);

        titleTextView = (TextView) findViewById(R.id.title);
        extractTextView = (TextView) findViewById(R.id.extract);

        extractTextView.setAlpha(0.0f);
    }

    public void setTitle(String title) {
        titleTextView.setText(title);
    }

    public void setExtract(String extract) {
        extractTextView.setText(Html.fromHtml(extract));
    }

    public void toggle() {
        if (mIsTitleShowing) {
            mIsTitleShowing = false;
            titleTextView.animate().alpha(0.0f).setDuration(100).start();
            extractTextView.animate().alpha(1.0f).setDuration(100).start();
        } else {
            mIsTitleShowing = true;
            titleTextView.animate().alpha(1.0f).setDuration(100).start();
            extractTextView.animate().alpha(0.0f).setDuration(100).start();
        }
    }

    public void showTitle() {
        mIsTitleShowing = true;
        titleTextView.animate().alpha(1.0f).setDuration(100).start();
        extractTextView.animate().alpha(0.0f).setDuration(100).start();
    }
}
