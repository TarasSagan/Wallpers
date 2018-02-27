package com.example.taras.wallpers.fragments;


import com.example.taras.wallpers.api.ModelsOfResponse.photo.PhotoItem;
import com.hannesdorfmann.mosby3.mvp.MvpView;

import java.util.List;

public interface ListFragmentContract {
    public interface FragmentView extends MvpView {
        void showMessage(String message);
        void showContent(List<PhotoItem> list);
    }
    public interface FragmentPresenter {
        void onLike(PhotoItem item, int position);
        void onShowUser(PhotoItem item);
        void onDownload(PhotoItem item);
        void onShowPhotoDetails(PhotoItem item);
        void getNextData();
    }
}
