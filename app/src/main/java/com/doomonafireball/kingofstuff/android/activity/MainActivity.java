package com.doomonafireball.kingofstuff.android.activity;


import android.animation.ObjectAnimator;
import android.app.Activity;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.PixelFormat;
import android.os.Bundle;
import android.os.Handler;
import android.text.Spannable;
import android.text.SpannableString;
import android.text.style.AbsoluteSizeSpan;
import android.text.style.ForegroundColorSpan;
import android.util.DisplayMetrics;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.Window;
import android.view.animation.DecelerateInterpolator;

import com.doomonafireball.kingofstuff.android.Datastore;
import com.doomonafireball.kingofstuff.android.MainApp;
import com.doomonafireball.kingofstuff.android.R;
import com.doomonafireball.kingofstuff.android.api.WikipediaApiService;
import com.doomonafireball.kingofstuff.android.api.model.Page;
import com.doomonafireball.kingofstuff.android.otto.BusProvider;
import com.doomonafireball.kingofstuff.android.otto.event.PageEvent;
import com.doomonafireball.kingofstuff.android.util.LogoGlyphs;
import com.doomonafireball.kingofstuff.android.util.SwipeDismissTouchListener;
import com.doomonafireball.kingofstuff.android.widget.PageContainer;
import com.squareup.otto.Subscribe;

import java.util.Collections;
import java.util.LinkedList;

import javax.inject.Inject;

import butterknife.ButterKnife;
import butterknife.InjectView;
import oak.svg.AnimatedSvgView;
import oak.util.FontTypefaceSpan;

public class MainActivity extends Activity {

    @Inject
    protected Datastore mDatastore;
    @InjectView(R.id.red)
    PageContainer red;
    @InjectView(R.id.blue)
    PageContainer blue;
    @InjectView(R.id.orange)
    PageContainer orange;
    @InjectView(R.id.purple)
    PageContainer purple;
    @InjectView(R.id.green)
    PageContainer green;
    @InjectView(R.id.animated_svg_view)
    AnimatedSvgView animatedSvgView;
    private Handler mHandler = new Handler();
    private LinkedList<PageContainer> mEmptyPageContainers = new LinkedList<PageContainer>();
    private float mScreenWidth;
    private DecelerateInterpolator mInterpolator = new DecelerateInterpolator();

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ButterKnife.inject(this);
        MainApp.getObjectGraph().inject(this);

        SpannableString s = new SpannableString(getString(R.string.app_name));
        s.setSpan(new FontTypefaceSpan(this, getString(R.string.action_bar_font)), 0, s.length(),
                Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
        s.setSpan(new AbsoluteSizeSpan(32, true), 0, s.length(), Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
        s.setSpan(new ForegroundColorSpan(getResources().getColor(R.color.action_bar_title_color)), 0, s.length(), Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
        getActionBar().setTitle(s);

        DisplayMetrics displaymetrics = new DisplayMetrics();
        getWindowManager().getDefaultDisplay().getMetrics(displaymetrics);
        mScreenWidth = displaymetrics.widthPixels;

        mEmptyPageContainers.add(red);
        mEmptyPageContainers.add(blue);
        mEmptyPageContainers.add(orange);
        mEmptyPageContainers.add(purple);
        mEmptyPageContainers.add(green);
        Collections.shuffle(mEmptyPageContainers);

        for (final PageContainer pc : mEmptyPageContainers) {
            if (pc.animateFromLeft) {
                pc.setX(-mScreenWidth);
            } else {
                pc.setX(mScreenWidth);
            }
            pc.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    pc.toggle();
                }
            });
        }

        animatedSvgView.setGlyphStrings(LogoGlyphs.FULL_LOGO_GLYPHS);
        int tc = Color.argb(255, 0, 0, 0);
        int rc = Color.argb(50, 0, 0, 0);
        int[] fillAlphas = new int[LogoGlyphs.FULL_LOGO_GLYPHS.length];
        int[] fillReds = new int[LogoGlyphs.FULL_LOGO_GLYPHS.length];
        int[] fillGreens = new int[LogoGlyphs.FULL_LOGO_GLYPHS.length];
        int[] fillBlues = new int[LogoGlyphs.FULL_LOGO_GLYPHS.length];
        int[] traceColors = new int[LogoGlyphs.FULL_LOGO_GLYPHS.length];
        int[] residueColors = new int[LogoGlyphs.FULL_LOGO_GLYPHS.length];
        for (int i = 0; i < LogoGlyphs.FULL_LOGO_GLYPHS.length; i++) {
            fillAlphas[i] = 255;
            fillReds[i] = 0;
            fillGreens[i] = 0;
            fillBlues[i] = 0;
            traceColors[i] = tc;
            residueColors[i] = rc;
        }
        animatedSvgView.setFillPaints(fillAlphas, fillReds, fillGreens, fillBlues);
        animatedSvgView.setTraceColors(traceColors);
        animatedSvgView.setTraceResidueColors(residueColors);
        animatedSvgView.setOnStateChangeListener(new AnimatedSvgView.OnStateChangeListener() {
            @Override
            public void onStateChange(int state) {
                if (state == AnimatedSvgView.STATE_FINISHED) {
                    if (mDatastore.getProvider() == Datastore.PROVIDER_WIKIPEDIA) {
                        Intent getPageIntent = new Intent(MainActivity.this, WikipediaApiService.class);
                        getPageIntent.putExtra(WikipediaApiService.API_CALL, WikipediaApiService.GET_NEW_PAGES);
                        startService(getPageIntent);
                    } else {
                        // TODO Wiktionary?
                    }

                    // Begin fading the svg view out
                    ObjectAnimator a3 = ObjectAnimator.ofFloat(animatedSvgView, "alpha", 0);
                    a3.setDuration(1250).start();
                }
            }
        });
    }

    @Override
    protected void onPostCreate(Bundle savedInstanceState) {
        super.onPostCreate(savedInstanceState);

        mHandler.postDelayed(new Runnable() {
            @Override
            public void run() {
                animatedSvgView.start();
            }
        }, 250);
    }

    @Override
    public void onResume() {
        super.onResume();
        BusProvider.getInstance().register(this);
    }

    @Override
    public void onPause() {
        super.onPause();
        BusProvider.getInstance().unregister(this);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.main_activity_menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.info:
                startActivity(new Intent(this, InfoActivity.class));
                return true;
            /*case R.id.wikipedia:
                item.setChecked(true);
                mDatastore.persistProvider(Datastore.PROVIDER_WIKIPEDIA);
                // TODO Set to wikipedia
                return true;
            case R.id.wiktionary:
                item.setChecked(true);
                mDatastore.persistProvider(Datastore.PROVIDER_WIKTIONARY);
                // TODO Set to wiktionary
                return true;*/
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    @Subscribe
    public void onPageEvent(PageEvent pageEvent) {
        Page page = pageEvent.page;
        if (mEmptyPageContainers.size() > 0) {
            final PageContainer pageContainerToPopulate = mEmptyPageContainers.pop();
            pageContainerToPopulate.showTitle();
            pageContainerToPopulate.setTitle(page.title);
            pageContainerToPopulate.setExtract(page.extract);
            pageContainerToPopulate.animate().setDuration(500).x(0).alpha(1.0f).setInterpolator(mInterpolator).start();
            pageContainerToPopulate.setOnTouchListener(new SwipeDismissTouchListener(
                    pageContainerToPopulate,
                    null,
                    new SwipeDismissTouchListener.DismissCallbacks() {
                        @Override
                        public boolean canDismiss(Object token) {
                            return true;
                        }

                        @Override
                        public void onDismiss(View view, Object token) {
                            // Get another one!
                            mEmptyPageContainers.add(pageContainerToPopulate);
                            Intent getPageIntent = new Intent(MainActivity.this, WikipediaApiService.class);
                            getPageIntent.putExtra(WikipediaApiService.API_CALL, WikipediaApiService.GET_NEW_PAGE);
                            startService(getPageIntent);
                            pageContainerToPopulate.setOnTouchListener(null);
                        }
                    }
            ));
        }
    }

    @Override
    public void onAttachedToWindow() {
        super.onAttachedToWindow();
        Window window = this.getWindow();
        window.setFormat(PixelFormat.RGBA_8888);
    }
}

