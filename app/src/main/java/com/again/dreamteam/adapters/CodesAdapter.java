package com.again.dreamteam.adapters;


import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.RecyclerView;

import com.again.dreamteam.BuildConfig;
import com.again.dreamteam.R;
import com.again.dreamteam.models.ContestCodeModels.CodesModel;
import com.again.dreamteam.utils.MyApp;
import com.bumptech.glide.Glide;

import java.util.ArrayList;
import java.util.List;

import de.hdodenhof.circleimageview.CircleImageView;

public class CodesAdapter extends RecyclerView.Adapter<CodesAdapter.ViewHolder> {
    List<CodesModel> codesModelList = new ArrayList<>();
    Context context;

    public CodesAdapter(Context context) {
        this.context = context;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new ViewHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.codes_layout, parent, false));
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {


        holder.userName.setText(codesModelList.get(position).getUserName());
        holder.winningAmount.setText(" ₹ " + codesModelList.get(position).getWinnerAmount());
        holder.entryFees.setText(" ₹ " + codesModelList.get(position).getEntryFees());
        holder.totalWinner.setText(codesModelList.get(position).getTotalWinner());
        holder.totalTeam.setText(codesModelList.get(position).getTotalTeam());
        holder.contestCode.setText(codesModelList.get(position).getContestCode());
        Glide.with(context).load("https://softwaresreviewguides.com/dreamteam11/APIs/Contest_Images/" + codesModelList.get(position).getImages()).into(holder.profileImg);
        holder.showFullProfileImage(codesModelList.get(position).getImages());

        holder.profileImg.setOnClickListener(v -> {
            MyApp.showInterstitialAd((Activity) context);
            holder.dialog.show();

//            if (MyApp.mInterstitialAd != null) {
//                MyApp.mInterstitialAd.show(((Activity)context));
//                MyApp.mInterstitialAd.setFullScreenContentCallback(new FullScreenContentCallback() {
//                    @Override
//                    public void onAdDismissedFullScreenContent() {
//                        // Called when fullscreen content is dismissed.
//                        holder.dialog.show();
//
//                    }
//
//                    @Override
//                    public void onAdFailedToShowFullScreenContent(AdError adError) {
//                        // Called when fullscreen content failed to show.
//                        Log.d("TAG", "The ad failed to show.");
//                    }
//
//                    @Override
//                    public void onAdShowedFullScreenContent() {
//                        // Called when fullscreen content is shown.
//                        // Make sure to set your reference to null so you don't
//                        // show it a second time.
//                        MyApp.mInterstitialAd = null;
//                        Log.d("TAG", "The ad was shown.");
//                    }
//                });
//            } else {
////                MyApp.showInterstitialAd(((Activity) context), intent);
//                holder.dialog.show();
//
//                Log.d("TAG", "The interstitial ad wasn't ready yet.");
//
//            }
        });
    }

    @Override
    public int getItemCount() {
        return codesModelList.size();
    }

    public void updateCodeModelList(List<CodesModel> codesModelList) {
        this.codesModelList.clear();
        this.codesModelList.addAll(codesModelList);
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        TextView userName, winningAmount, entryFees, totalWinner, totalTeam, contestCode;
        Button copyBtn;
        CircleImageView profileImg;
        Dialog dialog;


        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            userName = itemView.findViewById(R.id.user_name);
            winningAmount = itemView.findViewById(R.id.tv_winning_amount);
            entryFees = itemView.findViewById(R.id.tv_entry_fees);
            totalWinner = itemView.findViewById(R.id.tv_total_winner);
            totalTeam = itemView.findViewById(R.id.tv_total_team);
            contestCode = itemView.findViewById(R.id.contest_code);
            copyBtn = itemView.findViewById(R.id.tv_copy);
            profileImg = itemView.findViewById(R.id.profileImg);
            copyBtn.setOnClickListener(v -> {
//                ClipboardManager clipboardManager = (ClipboardManager) itemView.getContext().getSystemService(Context.CLIPBOARD_SERVICE);
//                ClipData clipData = ClipData.newPlainText("text", contestCode.getText());
//                clipboardManager.setPrimaryClip(clipData);
//                Toast.makeText(itemView.getContext(), "Copied to clipboard", Toast.LENGTH_SHORT).show();
                shareApp();
            });

        }

        private void shareApp() {
            try {
                Intent shareIntent = new Intent(Intent.ACTION_SEND);
                shareIntent.setType("text/plain");
                shareIntent.putExtra(Intent.EXTRA_SUBJECT, R.string.app_name);
                String shareMessage = "\nLet me recommend you this application\n\n";
                shareMessage = shareMessage + "https://play.google.com/store/apps/details?id=" + BuildConfig.APPLICATION_ID + "\n\n";
                shareIntent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                shareIntent.putExtra(Intent.EXTRA_TEXT, shareMessage);
                ((Activity) itemView.getContext()).startActivity(Intent.createChooser(shareIntent, "choose one"));
            } catch (Exception e) {
                e.printStackTrace();
            }
        }

        private void showFullProfileImage(String images) {
            dialog = new Dialog(itemView.getContext());
            dialog.setContentView(R.layout.show_full_image);
            dialog.getWindow().setLayout(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT);
            dialog.getWindow().setBackgroundDrawable(ContextCompat.getDrawable(itemView.getContext(), R.drawable.item_bg));
            dialog.setCancelable(true);
            ImageView imageView = dialog.findViewById(R.id.fullImage);
            Glide.with(itemView.getContext()).load("https://softwaresreviewguides.com/dreamteam11/APIs/Contest_Images/" + images).into(imageView);


        }

    }
}
