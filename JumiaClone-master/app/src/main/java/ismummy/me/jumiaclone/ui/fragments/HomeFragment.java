package ismummy.me.jumiaclone.ui.fragments;


import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import butterknife.OnClick;
import ismummy.me.jumiaclone.R;
import ismummy.me.jumiaclone.core.EndPoints;
import ismummy.me.jumiaclone.ui.base.BaseFragment;

/**
 * A simple {@link Fragment} subclass.
 */
public class HomeFragment extends BaseFragment {


    public static HomeFragment newInstance(){
        return  new HomeFragment();
    }
    public HomeFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_home, container, false);
    }

    @OnClick(R.id.iv_banner)
    void bannerClicked(){
        openInternalWebView(EndPoints.HOME_BANNER);
    }
    @OnClick(R.id.layout_express)
    void expressClicked(){
        openInternalWebView(EndPoints.JUMIA_EXPRESS);
    }

    @OnClick(R.id.layout_entertainment)
    void entertainmentClicked()
    {
        openInternalWebView(EndPoints.JUMIA_ENTERTAINMENT);
    }
    @OnClick(R.id.layout_phones)
    void phonesClicked(){
        openInternalWebView(EndPoints.JUMIA_PHONES);
    }
    @OnClick(R.id.layout_power)
    void powerClicked(){
        openInternalWebView(EndPoints.JUMIA_POWERUP);
    }
    @OnClick(R.id.layout_deal)
    void dealClicked(){
        openInternalWebView(EndPoints.DEALS_OF_THE_DAY);
    }
    @OnClick(R.id.layout_hair)
    void hairClicked(){
        openInternalWebView(EndPoints.JUMIA_HAIR);
    }
    @OnClick(R.id.layout_household)
    void householdClicked(){
        openInternalWebView(EndPoints.JUMIA_HOME);
    }
    @OnClick(R.id.layout_formen)
    void formenClicked()
    {
        openInternalWebView(EndPoints.JUMIA_FOR_MEN);
    }
    @OnClick(R.id.iv_hp_shop)
    void hpShopClicked(){
        openInternalWebView(EndPoints.JUMIA_HP);
    }
    @OnClick(R.id.iv_intel_shop)
    void intelShopClicked(){
        openInternalWebView(EndPoints.JUMIA_INTEL);
    }
    @OnClick(R.id.iv_molfix_shop)
    void molfixShopClicked(){
        openInternalWebView(EndPoints.JUMIA_MOLFIX);
    }
    @OnClick(R.id.iv_canon_shop)
    void canonShopClicked(){
        openInternalWebView(EndPoints.JUMIA_CANON);
    }
    @OnClick(R.id.iv_infinix_shop)
    void infinixShopClicked(){
        openInternalWebView(EndPoints.JUMIA_INFINIX);
    }
    @OnClick(R.id.iv_innjo_shop)
    void innjoShopClicked(){
        openInternalWebView(EndPoints.JUMIA_INNJOO);
    }
    @OnClick(R.id.iv_tecno_shop)
    void tecnoShopClicked(){
        openInternalWebView(EndPoints.JUMIA_TECNO);
    }
    @OnClick(R.id.iv_home_deal)
    void homeDealClicked(){
        openInternalWebView(EndPoints.JUMIA_HOME_DEAL);
    }
    @OnClick(R.id.iv_voucher_deal)
    void voucherDealClicked()
    {
        openInternalWebView(EndPoints.JUMIA_VOUCHER_DEAL);
    }

    @OnClick(R.id.layout_phones_store)
    void phonesStoreClicked(){
        openInternalWebView(EndPoints.JUMIA_STORE_PHONES);
    }
    @OnClick(R.id.layout_baby_store)
    void babyStoreClicked(){
        openInternalWebView(EndPoints.JUMIA_STORE_BABY);
    }
    @OnClick(R.id.layout_computing_store)
    void computingStoreClicked()
    {
        openInternalWebView(EndPoints.JUMIA_STORE_COMPUTING);
    }
    @OnClick(R.id.layout_electronic_store)
    void electronicStoreClicked(){
        openInternalWebView(EndPoints.JUMIA_STORE_ELECTRONICS);
    }
    @OnClick(R.id.layout_home_store)
    void homeStoreClicked(){
        openInternalWebView(EndPoints.JUMIA_STORE_HOME);
    }
    @OnClick(R.id.layout_men_clothing_store)
    void menClothingStoreClicked()
    {
        openInternalWebView(EndPoints.JUMIA_STORE_MEN_CLOTHING);
    }
    @OnClick(R.id.layout_women_clothing_store)
    void womenClothingStoreClicked()
    {
        openInternalWebView(EndPoints.JUMIA_STORE_WOMEN_CLOTHING);
    }
    @OnClick(R.id.layout_men_shoes_store)
    void menShoesStoreClicked(){
        openInternalWebView(EndPoints.JUMIA_STORE_MEN_SHOES);
    }
    @OnClick(R.id.layout_women_shoes_store)
    void womenShoesStoreClicked(){
        openInternalWebView(EndPoints.JUMIA_STORE_WOMEN_SHOES);
    }
    @OnClick(R.id.layout_health_store)
    void healthStoreClicked(){
        openInternalWebView(EndPoints.JUMIA_STORE_BEAUTY);
    }
}
