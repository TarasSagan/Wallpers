package com.example.taras.wallpapers.activity.main;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.Snackbar;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.taras.wallpapers.R;
import com.example.taras.wallpapers.activity.search.SearchActivity;
import com.example.taras.wallpapers.api.ModelsOfResponse.profile.ProfileResponse;
import com.example.taras.wallpapers.fragments.listAllPhotos.AllPhotosFragment;
import com.example.taras.wallpapers.fragments.listRandomPhotos.RandomPhotosFragment;
import com.example.taras.wallpapers.fragments.listCuratedPhotos.CuratedPhotosFragment;
import com.hannesdorfmann.mosby3.mvp.MvpActivity;
import com.squareup.picasso.Picasso;

public class MainActivity extends MvpActivity<MainActivityContract.View, MainActivityPresenter>
        implements NavigationView.OnNavigationItemSelectedListener, MainActivityContract.View{
    private FragmentManager fragmentManager;
    private FragmentTransaction fragmentTransaction;
    private AllPhotosFragment allPhotosFragment;
    private CuratedPhotosFragment curatedPhotosFragment;
    private RandomPhotosFragment randomPhotosFragment;
    private Toolbar toolbar;
    private DrawerLayout drawer;
    private NavigationView navigationView;
    private View header;
    private TextView textLogin, textNavHeaderUsername;
    private ImageView imageProfilePhoto;
    private Button btnLogin;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        toolbar = (Toolbar) findViewById(R.id.toolbar);
        drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        navigationView = (NavigationView) findViewById(R.id.nav_view);

        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.addDrawerListener(toggle);
        toggle.syncState();


        navigationView.setNavigationItemSelectedListener(this);
        fragmentManager = getSupportFragmentManager();

//        try {this.getSupportActionBar().hide();
//        } catch (NullPointerException e){}

        bindView();
        setPublicUser();
        getPresenter().initPresenter();

        if(randomPhotosFragment == null){
            randomPhotosFragment = new RandomPhotosFragment();
        }
        showFragment(randomPhotosFragment);
        navigationView.setCheckedItem(R.id.nav_random);
    }

    private void bindView(){
        header = navigationView.getHeaderView(0);
        textLogin = header.findViewById(R.id.textLogin);
        textNavHeaderUsername = header.findViewById(R.id.textNavHeaderUsername);
        imageProfilePhoto = header.findViewById(R.id.imageProfilePhoto);
        btnLogin = header.findViewById(R.id.btnLogin);
        textNavHeaderUsername.setOnClickListener(v -> getPresenter().onShowCurrentUserProfile());
        imageProfilePhoto.setOnClickListener(v -> getPresenter().onShowCurrentUserProfile());
        btnLogin.setOnClickListener(v -> getPresenter().onLoginLogout());
        textLogin.setOnClickListener(v -> getPresenter().onLoginLogout());
    }

    @Override
    public MainActivityPresenter createPresenter() {
        return new MainActivityPresenter(this);
    }

    @Override
    public void onBackPressed() {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }
    boolean need = true;
//    @Override
//    public boolean onCreateOptionsMenu(Menu menu) {
//        // Inflate the menu; this adds items to the action bar if it is present.
//        getMenuInflater().inflate(R.menu.main, menu);
//
//        return true;
//    }
//
//    @Override
//    public boolean onOptionsItemSelected(MenuItem item) {
//        int id = item.getItemId();
//
//        if (id == R.id.menu_sort) {
//
//        } else if (id == R.id.menu_sort_latest) {
//
//        } else if (id == R.id.menu_sort_oldest) {
//
//        } else if (id == R.id.menu_sort_popular) {
//
//        }
//
//        return true;
//    }

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        int id = item.getItemId();

        if (id == R.id.nav_random) {
            if(randomPhotosFragment == null){
                randomPhotosFragment = new RandomPhotosFragment();

            }
            showFragment(randomPhotosFragment);

        } else if (id == R.id.nav_photos) {
            if(allPhotosFragment == null){
                allPhotosFragment = new AllPhotosFragment();
                need = false;
            }
            showFragment(allPhotosFragment);

        } else if (id == R.id.nav_curated) {
            if(curatedPhotosFragment == null){
                curatedPhotosFragment = new CuratedPhotosFragment();
            }
            showFragment(curatedPhotosFragment);

        } else if (id == R.id.nav_search) {
            Intent intent = new Intent(this, SearchActivity.class);
            startActivity(intent);
        }

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }

    private void showFragment(Fragment fragment){
        fragmentTransaction = fragmentManager.beginTransaction();
        fragmentTransaction.replace(R.id.container, fragment);
        fragmentTransaction.commit();
    }

    @Override
    public void showMessage(String message) {
        Snackbar.make(drawer, message, Snackbar.LENGTH_LONG).show();
    }

    @Override
    public void setAuthUser(ProfileResponse user) {
        btnLogin.setBackground(getDrawable(R.drawable.ic_log_out));
        textLogin.setVisibility(View.INVISIBLE);
        textNavHeaderUsername.setVisibility(View.VISIBLE);
        textNavHeaderUsername.setText(TextUtils.isEmpty(user.getLastName())
                ? user.getFirstName()
                : user.getFirstName()
                + " "
                + user.getLastName());
        imageProfilePhoto.setVisibility(View.VISIBLE);
        Picasso.with(this).load(user.getProfileImage().getMedium())
                .placeholder(getDrawable(R.drawable.ic_user_black_24dp))
                .error(getDrawable(R.drawable.ic_error))
                .into(imageProfilePhoto);
    }

    @Override
    public void setPublicUser() {
        textNavHeaderUsername.setVisibility(View.INVISIBLE);
        imageProfilePhoto.setVisibility(View.INVISIBLE);
        textLogin.setText(getString(R.string.login));
        btnLogin.setBackground(getDrawable(R.drawable.ic_login));
    }
}
