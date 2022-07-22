package com.projets.heven.taxijaune.settings;

import android.content.Context;

public class Progressdialog {
    private Context mContext;
    private android.app.ProgressDialog pDialog;

    public Progressdialog(Context mContext) {
        this.mContext = mContext;
    }

    public Progressdialog() {

    }

    public void init(){
        pDialog = new android.app.ProgressDialog(mContext);
        pDialog.setMessage("Connexion...");
        pDialog.setCancelable(false);
    }

    public void show(){
        pDialog.show();
    }

    public void dismiss(){
        pDialog.dismiss();
    }

    public Boolean isShowing(){
        if(pDialog.isShowing())
            return true;
        else return false;
    }
}
