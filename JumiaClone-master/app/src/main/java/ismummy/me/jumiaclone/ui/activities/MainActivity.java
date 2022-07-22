package ismummy.me.jumiaclone.ui.activities;

import android.os.Bundle;
import android.support.design.widget.TabLayout;
import android.support.v4.view.ViewPager;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBar;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.widget.Toolbar;

import butterknife.BindView;
import butterknife.OnClick;
import ismummy.me.jumiaclone.R;
import ismummy.me.jumiaclone.core.EndPoints;
import ismummy.me.jumiaclone.ui.adapters.MainActivityAdapter;
import ismummy.me.jumiaclone.ui.base.BaseActivity;
import ismummy.me.jumiaclone.ui.fragments.CartFragment;
import ismummy.me.jumiaclone.ui.fragments.HomeFragment;
import ismummy.me.jumiaclone.ui.fragments.SavedFragment;

public class MainActivity extends BaseActivity {

    @BindView(R.id.tab_layout)
    TabLayout tabLayout;
    @BindView(R.id.viewpager)
    ViewPager viewPager;
    @BindView(R.id.toolbar)
    Toolbar toolbar;
    @BindView(R.id.drawer_layout_main)
    DrawerLayout mDrawerLayout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        setupViewPager();
        setupDrawer();
    }

    private void setupDrawer() {
        if (getSupportActionBar() != null)
            getSupportActionBar().setDisplayShowTitleEnabled(true);
        ActionBarDrawerToggle mDrawerToggle = new ActionBarDrawerToggle(this,
                mDrawerLayout, toolbar, R.string.app_name, R.string.app_name);

        mDrawerLayout.addDrawerListener(mDrawerToggle);
        mDrawerToggle.syncState();
    }

    private void setupViewPager() {
        MainActivityAdapter adapter = new MainActivityAdapter(getSupportFragmentManager());
        adapter.addFragment(HomeFragment.newInstance());
        adapter.addFragment(SavedFragment.newInstance());
        adapter.addFragment(CartFragment.newInstance());
        viewPager.setAdapter(adapter);
        tabLayout.setupWithViewPager(viewPager);
        tabLayout.getTabAt(0).setIcon(R.drawable.ic_action_home);
        tabLayout.getTabAt(1).setIcon(R.drawable.ic_action_saved);
        tabLayout.getTabAt(2).setIcon(R.drawable.ic_action_cart);
    }

    @OnClick(R.id.layout_drawer_travel)
    void travelDrawerClicked() {
        openInternalWebView(EndPoints.JUMIA_TRAVEL);
        mDrawerLayout.closeDrawers();
    }

    @OnClick(R.id.layout_drawer_food)
    void foodDrawerClicked() {
        openInternalWebView(EndPoints.JUMIA_FOOD);
        mDrawerLayout.closeDrawers();
    }

    @OnClick(R.id.layout_drawer_deals)
    void dealDrawerClicked() {
        openInternalWebView(EndPoints.JUMIA_DEAL);
        mDrawerLayout.closeDrawers();
    }

    @OnClick(R.id.layout_drawer_house)
    void houseDrawerClicked() {
        openInternalWebView(EndPoints.JUMIA_HOUSE);
        mDrawerLayout.closeDrawers();
    }

    @OnClick(R.id.layout_drawer_car)
    void carDrawerClicked() {
        openInternalWebView(EndPoints.JUMIA_CAR);
        mDrawerLayout.closeDrawers();
    }

    @OnClick(R.id.layout_drawer_one)
    void oneDrawerClicked() {
        openInternalWebView(EndPoints.JUMIA_ONE);
        mDrawerLayout.closeDrawers();
    }

    @OnClick(R.id.layout_drawer_market)
    void marketDrawerClicked() {
        openInternalWebView(EndPoints.JUMIA_MARKET);
        mDrawerLayout.closeDrawers();
    }

    @OnClick(R.id.layout_drawer_flight)
    void flightDrawerClicked() {
        openInternalWebView(EndPoints.JUMIA_FLIGHT);
        mDrawerLayout.closeDrawers();
    }

    //method to move change the current page another page from other fragment
    public void setViewPagerIndex(int viewPagerIndex) {
        viewPager.setCurrentItem(viewPagerIndex);
    }
}
