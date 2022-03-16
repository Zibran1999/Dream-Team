package com.again.dreamteam.activities.ui.main;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import com.again.dreamteam.R;
import com.again.dreamteam.activities.MatchDetailsActivity;
import com.again.dreamteam.activities.NewsActivity;
import com.again.dreamteam.adapters.CricketLiveScoreAdapter;
import com.again.dreamteam.adapters.FootBallAdapter;
import com.again.dreamteam.adapters.NewsAdapter;
import com.again.dreamteam.databinding.FragmentMyHomeBinding;
import com.again.dreamteam.models.AdsViewModel;
import com.again.dreamteam.models.FootballDataModels.FootBallData;
import com.again.dreamteam.models.MatchDetailModels.Datum;
import com.again.dreamteam.models.MatchDetailModels.MatchViewModel;
import com.again.dreamteam.models.MatchNewsModels.NewsDatum;
import com.again.dreamteam.utils.MyApp;
import com.airbnb.lottie.LottieAnimationView;
import com.google.android.gms.ads.MobileAds;
import com.google.firebase.analytics.FirebaseAnalytics;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * A placeholder fragment containing a simple view.
 */
public class PlaceholderFragment extends Fragment {
    private static final String ARG_SECTION_NUMBER = "section_number";
    public static boolean bannerAds;
    // Interstitial Add
    int index = 1;
    List<Datum> cricketLiveScoreModelList = new ArrayList<>();
    List<NewsDatum> newsModelList = new ArrayList<>();
    List<FootBallData> footBallModelList = new ArrayList<>();
    MatchViewModel matchViewModel;
    SwipeRefreshLayout swipeRefreshLayout;
    LottieAnimationView lottieAnimationView;
    RecyclerView recyclerView;
    RelativeLayout adView;
    private FragmentMyHomeBinding binding;
    AdsViewModel adsViewModel;

    public static PlaceholderFragment newInstance(int index) {
        PlaceholderFragment fragment = new PlaceholderFragment();
        Bundle bundle = new Bundle();
        bundle.putInt(ARG_SECTION_NUMBER, index);
        fragment.setArguments(bundle);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        MobileAds.initialize(requireActivity());
        if (getArguments() != null) {
            index = getArguments().getInt(ARG_SECTION_NUMBER);
        }
    }


    @Override
    public View onCreateView(
            @NonNull LayoutInflater inflater, ViewGroup container,
            Bundle savedInstanceState) {

        binding = FragmentMyHomeBinding.inflate(inflater, container, false);
        View root = binding.getRoot();
        recyclerView = binding.homeRecyclerView;
        lottieAnimationView = binding.lottieRV;
        swipeRefreshLayout = binding.swipeRefresh;
        adView = binding.adView;

        LinearLayoutManager layoutManager = new LinearLayoutManager(root.getContext());
        layoutManager.setOrientation(RecyclerView.VERTICAL);
        recyclerView.setLayoutManager(layoutManager);

        matchViewModel = new ViewModelProvider(requireActivity()).get(MatchViewModel.class);

//        adsViewModel = new AdsViewModel(requireActivity(),adView);
//        getLifecycle().addObserver(adsViewModel);

        if (index == 1) {
            MyApp.showAdxBannerAd(requireActivity(),binding.adView);

            lottieAnimationView.setAnimation(R.raw.loding_dot);
            lottieAnimationView.playAnimation();
            swipeRefreshLayout.setOnRefreshListener(() -> {
                MyApp.showAdxBannerAd(requireActivity(),binding.adView);
                setMatchData(root.getContext());
                swipeRefreshLayout.setRefreshing(false);
            });
            setMatchData(root.getContext());

        } else if (index == 2) {
            MyApp.showAdxBannerAd(requireActivity(),binding.adView);

            lottieAnimationView.setAnimation(R.raw.loding_dot);
            lottieAnimationView.playAnimation();
            swipeRefreshLayout.setOnRefreshListener(() -> {
                setNewsData(root.getContext());
                MyApp.showAdxBannerAd(requireActivity(),binding.adView);

                swipeRefreshLayout.setRefreshing(false);
            });
            setNewsData(root.getContext());

        } else if (index == 3) {
            lottieAnimationView.setAnimation(R.raw.loding_dot);
            lottieAnimationView.playAnimation();
            MyApp.showAdxBannerAd(requireActivity(),binding.adView);

            swipeRefreshLayout.setOnRefreshListener(() -> {
                setFootBallData(root.getContext());
                MyApp.showAdxBannerAd(requireActivity(),binding.adView);

                swipeRefreshLayout.setRefreshing(false);

            });
            setFootBallData(root.getContext());

        }

        return root;
    }

    @SuppressLint("NotifyDataSetChanged")
    public void setMatchData(Context context) {
        matchViewModel.getMatchData().observe(requireActivity(), matchDetailModel -> {
            List<Datum> matchDetailModelList = matchDetailModel.getData();
            Log.d("Added", String.valueOf(matchDetailModel.getData()));
            cricketLiveScoreModelList.clear();
            if (!matchDetailModelList.isEmpty()) {
                for (Datum datum : matchDetailModelList) {
                    String id = datum.getId();

                    String img1 = datum.getImage1();
                    String img2 = datum.getImage2();
                    String team1 = datum.getTeam1Name();
                    String team2 = datum.getTeam2Name();
                    String date = datum.getMatchDate();
                    String time = datum.getMatchTime();
                    String desc = datum.getMatchDesc();
                    cricketLiveScoreModelList.add(new Datum(id, img1, img2, team1, team2, date, desc, time));

                }
                CricketLiveScoreAdapter cricketLiveScoreAdapter = new CricketLiveScoreAdapter(cricketLiveScoreModelList, requireActivity(), bannerAds, (datum, position) -> {
                    AdsViewModel.destroyBanner();

                    Intent intent = new Intent(context, MatchDetailsActivity.class);
                    intent.putExtra("matchPos", position);
                    intent.putExtra("id", datum.getId());
                    intent.putExtra("pos", position);
                    startActivity(intent);

                    FirebaseAnalytics mFirebaseAnalytics = FirebaseAnalytics.getInstance(requireActivity());
                    Bundle bundle = new Bundle();
                    bundle.putString(FirebaseAnalytics.Param.ITEM_NAME, datum.getTeam1Name() + " VS " + datum.getTeam2Name());
                    bundle.putString(FirebaseAnalytics.Param.CONTENT_TYPE, "team Click");
                    mFirebaseAnalytics.logEvent("Selected_match_team_item", bundle);

                });
                recyclerView.setAdapter(cricketLiveScoreAdapter);
                Collections.reverse(cricketLiveScoreModelList);
                cricketLiveScoreAdapter.notifyDataSetChanged();
                lottieAnimationView.setVisibility(View.GONE);

            } else {
                lottieAnimationView.setAnimation(R.raw.empty);
                lottieAnimationView.playAnimation();
            }
        });
    }

    @SuppressLint("NotifyDataSetChanged")
    private void setNewsData(Context context) {
        matchViewModel.getMatchNewsData().observe(requireActivity(), matchNewsModel -> {
            List<NewsDatum> newsDatumList = matchNewsModel.getData();
            newsModelList.clear();
            if (!newsDatumList.isEmpty()) {
                for (NewsDatum news : newsDatumList) {
                    String id = news.getId();
                    String img = news.getNewsImg();
                    String title = news.getNewsTitle();
                    String desc = news.getNewsDesc();
                    newsModelList.add(new NewsDatum(id, img, title, desc));

                }
                NewsAdapter newsAdapter = new NewsAdapter(newsModelList, requireActivity(), bannerAds, (newsDatum, position) -> {
                    AdsViewModel.destroyBanner();

                    Intent intent = new Intent(context, NewsActivity.class);
                    intent.putExtra("newsPos", position);
                    intent.putExtra("img", newsDatum.getNewsImg());
                    intent.putExtra("desc", newsDatum.getNewsDesc());
                    intent.putExtra("title", newsDatum.getNewsTitle());
                    startActivity(intent);

                    FirebaseAnalytics mFirebaseAnalytics = FirebaseAnalytics.getInstance(requireActivity());
                    Bundle bundle = new Bundle();
                    bundle.putString(FirebaseAnalytics.Param.ITEM_NAME, newsDatum.getNewsTitle());
                    bundle.putString(FirebaseAnalytics.Param.CONTENT_TYPE, "News Click");
                    mFirebaseAnalytics.logEvent("Selected_news_item", bundle);

                });

                recyclerView.setAdapter(newsAdapter);
                Collections.reverse(newsModelList);
                newsAdapter.notifyDataSetChanged();
                lottieAnimationView.setVisibility(View.GONE);

            } else {
                lottieAnimationView.setAnimation(R.raw.empty);
                lottieAnimationView.playAnimation();
            }
        });

    }

    @SuppressLint("NotifyDataSetChanged")
    public void setFootBallData(Context context) {
        matchViewModel.getFootballData().observe(requireActivity(), footballDataModel -> {
            List<FootBallData> footBallData = footballDataModel.getData();
            footBallModelList.clear();

            if (!footBallData.isEmpty()) {
                for (FootBallData f : footBallData) {

                    String id = f.getId();
                    String img1 = f.getImage1();
                    String img2 = f.getImage2();
                    String team1 = f.getTeam1Name();
                    String team2 = f.getTeam2Name();
                    String date = f.getMatchDate();
                    String time = f.getMatchTime();
                    String desc = f.getMatchDesc();
                    footBallModelList.add(new FootBallData(id, img1, img2, team1, team2, date, desc, time));
                }
                FootBallAdapter footBallAdapter = new FootBallAdapter(footBallModelList, requireActivity(), bannerAds, (datum, position) -> {
                    AdsViewModel.destroyBanner();

                    Intent intent = new Intent(context, MatchDetailsActivity.class);
                    intent.putExtra("id", datum.getId());
                    intent.putExtra("pos", position);
                    startActivity(intent);

                    FirebaseAnalytics mFirebaseAnalytics = FirebaseAnalytics.getInstance(requireActivity());
                    Bundle bundle = new Bundle();
                    bundle.putString(FirebaseAnalytics.Param.ITEM_NAME, datum.getTeam1Name() + " VS " + datum.getTeam2Name());
                    bundle.putString(FirebaseAnalytics.Param.CONTENT_TYPE, "Football Click");
                    mFirebaseAnalytics.logEvent("Selected_Football_item", bundle);

                });
                recyclerView.setAdapter(footBallAdapter);
                Collections.reverse(footBallModelList);
                footBallAdapter.notifyDataSetChanged();
                lottieAnimationView.setVisibility(View.GONE);

            } else {
                lottieAnimationView.setAnimation(R.raw.empty);
                lottieAnimationView.playAnimation();
            }
        });


    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }

}