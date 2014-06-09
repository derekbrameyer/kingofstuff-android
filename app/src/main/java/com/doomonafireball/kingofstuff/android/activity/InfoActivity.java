package com.doomonafireball.kingofstuff.android.activity;

import android.app.Activity;
import android.graphics.Color;
import android.os.Bundle;
import android.os.Handler;
import android.text.Spannable;
import android.text.SpannableString;
import android.text.style.AbsoluteSizeSpan;
import android.text.style.ForegroundColorSpan;
import android.view.MenuItem;
import android.widget.TextView;

import com.doomonafireball.kingofstuff.android.R;
import com.doomonafireball.kingofstuff.android.util.LogoGlyphs;

import butterknife.ButterKnife;
import butterknife.InjectView;
import oak.svg.AnimatedSvgView;
import oak.util.FontTypefaceSpan;
import oak.util.OakUtils;

/**
 * Created by derek on 6/6/14.
 */
public class InfoActivity extends Activity {

    @InjectView(R.id.version_text_view)
    TextView versionTextView;
    @InjectView(R.id.send_feedback_text_view)
    TextView sendFeedbackTextView;
    @InjectView(R.id.acknowledgments_text_view)
    TextView acknowledgmentsTextView;
    @InjectView(R.id.animated_svg_view)
    AnimatedSvgView animatedSvgView;
    private Handler mHandler = new Handler();

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_info);
        ButterKnife.inject(this);

        SpannableString s = new SpannableString(getString(R.string.about_king_of_stuff));
        s.setSpan(new FontTypefaceSpan(this, getString(R.string.action_bar_font)), 0, s.length(),
                Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
        s.setSpan(new AbsoluteSizeSpan(32, true), 0, s.length(), Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
        s.setSpan(new ForegroundColorSpan(getResources().getColor(R.color.action_bar_title_color)), 0, s.length(), Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
        getActionBar().setTitle(s);
        getActionBar().setDisplayHomeAsUpEnabled(true);

        String typefaceName = getString(R.string.default_font);
        versionTextView.setTypeface(OakUtils.getStaticTypeFace(this, typefaceName));
        sendFeedbackTextView.setTypeface(OakUtils.getStaticTypeFace(this, typefaceName));
        acknowledgmentsTextView.setTypeface(OakUtils.getStaticTypeFace(this, typefaceName));

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
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                onBackPressed();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }
}
