package com.example.taras.wallpers.fragments.listNewPhotos;


import com.example.taras.wallpers.fragments.baseListFragment.BaseListFragment;
import com.example.taras.wallpers.fragments.baseListFragment.BaseListPresenter;

public class NewPhotosFragment extends BaseListFragment{
    @Override
    public BaseListPresenter createPresenter() {
        return new NewPhotosFragmentPresenter();
    }
}