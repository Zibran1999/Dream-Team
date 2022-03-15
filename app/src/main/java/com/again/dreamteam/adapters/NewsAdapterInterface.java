package com.again.dreamteam.adapters;


import com.again.dreamteam.models.MatchNewsModels.NewsDatum;

public interface NewsAdapterInterface {
    void onItemClicked(NewsDatum newsDatum, int position);
}
