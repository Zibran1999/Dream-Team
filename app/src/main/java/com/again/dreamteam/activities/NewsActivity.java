package com.again.dreamteam.activities;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.again.dreamteam.R;
import com.again.dreamteam.models.AdsViewModel;
import com.again.dreamteam.utils.MyApp;
import com.bumptech.glide.Glide;

public class NewsActivity extends AppCompatActivity {

    int pos = -1;
    ImageView newsImg, backIcon;
    TextView newsTitle, newsDesc;
    String img, desc, title;
    RelativeLayout adView,adview2;

    AdsViewModel adsViewModel;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_news);
        newsImg = findViewById(R.id.newsImg);
        newsTitle = findViewById(R.id.newsTitle);
        newsDesc = findViewById(R.id.newsDesc);
        backIcon = findViewById(R.id.back_icon);
        adView = findViewById(R.id.adView);
        adview2 = findViewById(R.id.adView2);
        MyApp.showInterstitialAd(this);
        adsViewModel = new AdsViewModel(this,adView);
        getLifecycle().addObserver(adsViewModel);

        MyApp.showAdxBannerAd(this,adview2);
        img = getIntent().getStringExtra("img");
        desc = getIntent().getStringExtra("desc");
        title = getIntent().getStringExtra("title");
        pos = getIntent().getIntExtra("newsPos", 0);
//        if (pos%2==0){
//            new AppOpenManager(MyApp.mInstance, Paper.book().read(Prevalent.openAppAds), this);
//        }
//        if (pos != 0) {
//            new Handler().postDelayed(() -> {
//                MyApp.showInterstitialAd(this);
//            }, 1000);
//        }

        Glide.with(this).load("https://softwaresreviewguides.com/dreamteam11/APIs/Cricket_News_Images/" + img).into(newsImg);
        newsTitle.setText(title);
        newsDesc.setText(desc);
        backIcon.setOnClickListener(v -> onBackPressed());


    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        AdsViewModel.destroyBanner();
        overridePendingTransition(0, 0);
        finish();
        overridePendingTransition(0, 0);
    }

}