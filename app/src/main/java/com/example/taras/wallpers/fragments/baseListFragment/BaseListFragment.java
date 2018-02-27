package com.example.taras.wallpers.fragments.baseListFragment;

import android.os.Bundle;
import android.support.design.widget.Snackbar;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import com.example.taras.wallpers.R;
import com.example.taras.wallpers.api.ModelsOfResponse.photo.PhotoItem;
import com.example.taras.wallpers.fragments.EndlessRecyclerOnScrollListener;
import com.example.taras.wallpers.fragments.ListFragmentContract;
import com.example.taras.wallpers.fragments.PhotosRecyclerAdapter;
import com.hannesdorfmann.mosby3.mvp.MvpFragment;
import java.util.List;

public abstract class BaseListFragment extends MvpFragment<ListFragmentContract.FragmentView, BaseListPresenter>
        implements ListFragmentContract.FragmentView {
    private RecyclerView recyclerView;
    private PhotosRecyclerAdapter photosRecyclerAdapter;
    private EndlessRecyclerOnScrollListener endlessRecyclerOnScrollListener;
    @Override
    public abstract BaseListPresenter createPresenter();

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_random_photos, container, false);
        recyclerView = view.findViewById(R.id.list);

        getPresenter().getNextData();
        return view;
    }

    @Override
    public void showMessage(String message) {
        Snackbar.make(recyclerView, message, Snackbar.LENGTH_LONG).show();
    }

    @Override
    public void showContent(List<PhotoItem> list) {
        if(photosRecyclerAdapter == null) {
            photosRecyclerAdapter = new PhotosRecyclerAdapter(list, getPresenter(), getContext());
            LinearLayoutManager layoutManager = new LinearLayoutManager(getContext());
            recyclerView.setLayoutManager(layoutManager);
            endlessRecyclerOnScrollListener = new EndlessRecyclerOnScrollListener(layoutManager) {
                @Override
                public void onLoadMore(int current_page) {
                    getPresenter().getNextData();
                }
            };
            recyclerView.addOnScrollListener(endlessRecyclerOnScrollListener);
            recyclerView.setAdapter(photosRecyclerAdapter);
        }else {
            photosRecyclerAdapter.addNewData(list);
        }
    }

}


