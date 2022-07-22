package com.projets.heven.taxijaune.model;

/**
 * Created by Woumtana Pingdiwindé Youssouf 03/2019
 * Tel: +226 63 86 22 46 - 73 35 41 41
 * Email: issoufwoumtana@gmail.com
 **/

import android.app.ProgressDialog;
import android.content.Context;
import android.content.SharedPreferences;
import android.util.Log;

import com.projets.heven.taxijaune.R;

public class M {
    public static ProgressDialog pDialog;
    private static SharedPreferences mSharedPreferences;
    private static String pref_name="settings_odv";

    public static void showLoadingDialog(Context mContext) {
        pDialog = new ProgressDialog(mContext);
        pDialog.setMessage(mContext.getString(R.string.please_wait));
        pDialog.setIndeterminate(true);
        pDialog.setCancelable(false);
        pDialog.show();
    }

    public static void hideLoadingDialog() {
        if (pDialog.isShowing()) {
            pDialog.dismiss();
        }
    }

    public static void showToast(Context mContext, String message) {
        M.showToast(mContext, message);
    }

    public static void T(Context mContext, String Message) {
        M.showToast(mContext, Message);
    }

    public static void L(String Message) {
        Log.e("Vidoo", Message);
    }

    public static boolean setUsername(String username, Context mContext) {
        mSharedPreferences = mContext.getSharedPreferences(pref_name, 0);
        SharedPreferences.Editor editor = mSharedPreferences.edit();
        editor.putString("username", username);
        return editor.commit();
    }

    public static String getUsername(Context mContext) {
        mSharedPreferences = mContext.getSharedPreferences(pref_name, 0);
        return mSharedPreferences.getString("username", null);
    }

    public static boolean setNom(String nom, Context mContext) {
        mSharedPreferences = mContext.getSharedPreferences(pref_name, 0);
        SharedPreferences.Editor editor = mSharedPreferences.edit();
        editor.putString("nom", nom);
        return editor.commit();
    }

    public static String getNom(Context mContext) {
        mSharedPreferences = mContext.getSharedPreferences(pref_name, 0);
        return mSharedPreferences.getString("nom", null);
    }

    public static boolean setPrenom(String prenom, Context mContext) {
        mSharedPreferences = mContext.getSharedPreferences(pref_name, 0);
        SharedPreferences.Editor editor = mSharedPreferences.edit();
        editor.putString("prenom", prenom);
        return editor.commit();
    }

    public static String getPrenom(Context mContext) {
        mSharedPreferences = mContext.getSharedPreferences(pref_name, 0);
        return mSharedPreferences.getString("prenom", null);
    }

    public static boolean setRouteDistance(String prenom, Context mContext) {
        mSharedPreferences = mContext.getSharedPreferences(pref_name, 0);
        SharedPreferences.Editor editor = mSharedPreferences.edit();
        editor.putString("routedistance", prenom);
        return editor.commit();
    }

    public static String getRouteDistance(Context mContext) {
        mSharedPreferences = mContext.getSharedPreferences(pref_name, 0);
        return mSharedPreferences.getString("routedistance", null);
    }

    public static boolean setRouteDuration(String prenom, Context mContext) {
        mSharedPreferences = mContext.getSharedPreferences(pref_name, 0);
        SharedPreferences.Editor editor = mSharedPreferences.edit();
        editor.putString("routeduration", prenom);
        return editor.commit();
    }

    public static String getRouteDuration(Context mContext) {
        mSharedPreferences = mContext.getSharedPreferences(pref_name, 0);
        return mSharedPreferences.getString("routeduration", null);
    }

    public static boolean setStatutConducteur(String prenom, Context mContext) {
        mSharedPreferences = mContext.getSharedPreferences(pref_name, 0);
        SharedPreferences.Editor editor = mSharedPreferences.edit();
        editor.putString("statut_conducteur", prenom);
        return editor.commit();
    }

    public static String getCurrentFragment(Context mContext) {
        mSharedPreferences = mContext.getSharedPreferences(pref_name, 0);
        return mSharedPreferences.getString("current_fragment", null);
    }

    public static boolean setCurrentFragment(String prenom, Context mContext) {
        mSharedPreferences = mContext.getSharedPreferences(pref_name, 0);
        SharedPreferences.Editor editor = mSharedPreferences.edit();
        editor.putString("current_fragment", prenom);
        return editor.commit();
    }

    public static String getStatutConducteur(Context mContext) {
        mSharedPreferences = mContext.getSharedPreferences(pref_name, 0);
        return mSharedPreferences.getString("statut_conducteur", null);
    }


//    public static boolean setEmail(String email, Context mContext) {
//        mSharedPreferences = mContext.getSharedPreferences(pref_name, 0);
//        SharedPreferences.Editor editor = mSharedPreferences.edit();
//        editor.putString("email", email);
//        return editor.commit();
//    }

    public static String getEmail(Context mContext) {
        mSharedPreferences = mContext.getSharedPreferences(pref_name, 0);
        return mSharedPreferences.getString("email", null);
    }

    public static boolean setEmail(String email, Context mContext) {
        mSharedPreferences = mContext.getSharedPreferences(pref_name, 0);
        SharedPreferences.Editor editor = mSharedPreferences.edit();
        editor.putString("email", email);
        return editor.commit();
    }

    public static String getUserCategorie(Context mContext) {
        mSharedPreferences = mContext.getSharedPreferences(pref_name, 0);
        return mSharedPreferences.getString("user_cat", null);
    }

    public static boolean setUserCategorie(String user_cat, Context mContext) {
        mSharedPreferences = mContext.getSharedPreferences(pref_name, 0);
        SharedPreferences.Editor editor = mSharedPreferences.edit();
        editor.putString("user_cat", user_cat);
        return editor.commit();
    }

    public static String getCoutByKm(Context mContext) {
        mSharedPreferences = mContext.getSharedPreferences(pref_name, 0);
        return mSharedPreferences.getString("cout_km", null);
    }

    public static boolean setCoutByKm(String cout_km, Context mContext) {
        mSharedPreferences = mContext.getSharedPreferences(pref_name, 0);
        SharedPreferences.Editor editor = mSharedPreferences.edit();
        editor.putString("cout_km", cout_km);
        return editor.commit();
    }

    public static boolean setPhone(String phone, Context mContext) {
        mSharedPreferences = mContext.getSharedPreferences(pref_name, 0);
        SharedPreferences.Editor editor = mSharedPreferences.edit();
        editor.putString("phone", phone);
        return editor.commit();
    }

    public static String getPhone(Context mContext) {
        mSharedPreferences = mContext.getSharedPreferences(pref_name, 0);
        return mSharedPreferences.getString("phone", null);
    }

    public static boolean setlogintype(String logintype, Context mContext) {
        mSharedPreferences = mContext.getSharedPreferences(pref_name, 0);
        SharedPreferences.Editor editor = mSharedPreferences.edit();
        editor.putString("logintype", logintype);
        return editor.commit();
    }

    public static String getlogintype(Context mContext) {
        mSharedPreferences = mContext.getSharedPreferences(pref_name, 0);
        return mSharedPreferences.getString("logintype", null);
    }

    public static boolean setPushNotification(Boolean pn, Context mContext) {
        mSharedPreferences = mContext.getSharedPreferences(pref_name, 0);
        SharedPreferences.Editor editor = mSharedPreferences.edit();

        editor.putBoolean("pushnotify", pn);
        return editor.commit();
    }

    public static Boolean isPushNotify(Context mContext) {
        mSharedPreferences = mContext.getSharedPreferences(pref_name, 0);
        return mSharedPreferences.getBoolean("pushnotify", true);
    }

    public static void logOut(Context mContext) {
        setID(null,mContext);
        setNom(null, mContext);
        setPrenom(null,mContext);
        setPhone(null,mContext);
        setEmail(null,mContext);
        setUserCategorie(null,mContext);
        setCoutByKm(null,mContext);
//        setPhoto(null,mContext);
        setlogintype(null,mContext);
        setUsername(null,mContext);
        setPushNotification(true,mContext);
        mSharedPreferences.getAll().clear();
    }

    public static boolean setID(String ID, Context mContext) {
        mSharedPreferences = mContext.getSharedPreferences(pref_name, 0);
        SharedPreferences.Editor editor = mSharedPreferences.edit();

        editor.putString("userid", ID);
        return editor.commit();
    }

    public static String getID(Context mContext) {
        mSharedPreferences = mContext.getSharedPreferences(pref_name, 0);
        return mSharedPreferences.getString("userid", "");
    }
}
