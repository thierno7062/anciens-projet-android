package com.projets.heven.taxijaune.settings;

import android.content.Context;
import android.graphics.Typeface;

/**
 * Created by Woumtana on 01/01/2019.
 * Tel : 63 86 22 46
 * issoufwoumtana@gmail.com
 */

public class AppConst {

//    public static final String Server_url ="http://192.168.137.1/taxi_jaune_mobile/";
    public static final String Server_url ="http://localhost/yellow_taxi_webservices/";
//    public static final String Server_urlMain ="http://192.168.137.1/taxi_jaune/";
    public static final String Server_urlMain ="http://localhost/yellow_taxi_admin/";
    public static final String MAIN = Server_url;
//    public static final String imgurl = Server_url+"upload/videos/";

    public static String fcm_id;

    public static final String ADMOB_ID = "ca-app-pub-6192865524332826~8608785476"; // app id

    public static final String INTERSTIAL_ID = "ca-app-pub-6192865524332826/1391397163"; // app id

//    public static Typeface font_regular(Context context){
//        Typeface typeface=Typeface.createFromAsset(context.getAssets(), "fonts/OxygenRegular.ttf");
//        return typeface;
//    }
//
//    public static Typeface font_medium(Context context){
//        Typeface typeface=Typeface.createFromAsset(context.getAssets(), "fonts/OxygenMedium.ttf");
//        return typeface;
//    }
//
//    public static Typeface font_light(Context context){
//        Typeface typeface=Typeface.createFromAsset(context.getAssets(), "fonts/OxygenLight.ttf");
//        return typeface;
//    }
//
//    public static Typeface font_montserrat_light(Context context){
//        Typeface typeface=Typeface.createFromAsset(context.getAssets(), "fonts/MontserratLight.ttf");
//        return typeface;
//    }
//
//    public static Typeface font_montserrat_medium(Context context){
//        Typeface typeface=Typeface.createFromAsset(context.getAssets(), "fonts/MontserratMedium.ttf");
//        return typeface;
//    }
//
//    public static Typeface font_montserrat_regular(Context context){
//        Typeface typeface=Typeface.createFromAsset(context.getAssets(), "fonts/MontserratRegular.ttf");
//        return typeface;
//    }
//
//    public static Typeface font_notosans_regular(Context context){
//        Typeface typeface=Typeface.createFromAsset(context.getAssets(), "fonts/NotoSansRegular.ttf");
//        return typeface;
//    }
//
//    public static Typeface font_quicksand_light(Context context){
//        Typeface typeface=Typeface.createFromAsset(context.getAssets(), "fonts/QuicksandLight.ttf");
//        return typeface;
//    }

    public static Typeface font_quicksand_medium(Context context){
        Typeface typeface= Typeface.createFromAsset(context.getAssets(), "fonts/QuicksandMedium.ttf");
        return typeface;
    }

    public static Typeface font_quicksand_regular(Context context){
        Typeface typeface= Typeface.createFromAsset(context.getAssets(), "fonts/QuicksandRegular.ttf");
        return typeface;
    }

    public static Typeface font_quicksand_semibold(Context context){
        Typeface typeface= Typeface.createFromAsset(context.getAssets(), "fonts/QuicksandSemiBold.ttf");
        return typeface;
    }

}
