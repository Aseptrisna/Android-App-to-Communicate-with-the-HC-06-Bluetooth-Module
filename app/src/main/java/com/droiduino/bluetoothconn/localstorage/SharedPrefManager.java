package com.droiduino.bluetoothconn.localstorage;

import android.content.Context;
import android.content.SharedPreferences;

public class SharedPrefManager {
    public static final String SP_LOGIN_APP = "Apps";
    public static final String SP_ID = "ID";
    public static final String USER= "User";
    public static final String SP_NAMA = "spNama";
    public static final String SP_EMAIL = "spEmail";
    public static final String SP_TELPON= "spTelpon";
    public static final String SP_ALAMAT = "spAlamat";
    public static final String SP_Mac = "mac";
    public static final String SP_Prov = "prov";
    public static final String SP_Kab = "kab";
    public static final String SP_max = "max";
    public static final String SP_min = "min";
    public static final String SP_device_channel ="device_channel";
    public static final String SP_Status = "status";
    public static final String SP_Role = "Role";
    public static final String SP_SUDAH_LOGIN = "SudahLogin";
    public static final String SP_Jadwal_1 = "Jawal_1";
    public static final String SP_Jadwal_2 = "Jawal_2";
    public static final String SP_Jadwal_3= "Jawal_3";
    public static final String SP_Jam= "jam";
//    public static List<Model_User> user;

    SharedPreferences sp;
    SharedPreferences.Editor spEditor;
    public SharedPrefManager(Context context){
        sp = context.getSharedPreferences(SP_LOGIN_APP, Context.MODE_PRIVATE);
        spEditor = sp.edit();
    }

    public void saveSPString(String keySP, String value){
        spEditor.putString(keySP, value);
        spEditor.commit();
    }

    public void saveSPInt(String keySP, int value){
        spEditor.putInt(keySP, value);
        spEditor.commit();
    }

    public void saveSPBoolean(String keySP, boolean value){
        spEditor.putBoolean(keySP, value);
        spEditor.commit();
    }

    public String getSPNama(){
        return sp.getString(SP_NAMA, "");
    }
    public String getSPRole(){
        return sp.getString(SP_Role, "");
    }
    public String getUser(){
        return sp.getString(USER, "");
    }
    public String getSpId(){
        return sp.getString(SP_ID, "");
    }
    public String getSPEmail(){
        return sp.getString(SP_EMAIL, "");
    }
    public String getSPAlamat(){
        return sp.getString(SP_ALAMAT, "");
    }
    public String getSPTelpon(){
        return sp.getString(SP_TELPON, "");
    }
    public String getSP_Mac(){
        return sp.getString(SP_Mac, "");
    }
    public String getSP_Prov(){
        return sp.getString(SP_Prov, "");
    }
    public String getSP_Kab(){
        return sp.getString(SP_Kab, "");
    }
    public String getSP_Status(){
        return sp.getString(SP_Status, "");
    }
    public String getSP_max(){
        return sp.getString(SP_max, "");
    }
    public String getSP_min(){
        return sp.getString(SP_min, "");
    }
    public String getSP_device_channel(){
        return sp.getString(SP_device_channel, "");
    }
    public Boolean getSudahLogin(){
        return sp.getBoolean(SP_SUDAH_LOGIN, false);
    }

    public String getSP_Jadwal_1(){
        return sp.getString(SP_Jadwal_1, "");
    }
    public String getSP_Jadwal_2(){
        return sp.getString(SP_Jadwal_2, "");
    }
    public String getSP_Jadwal_3(){
        return sp.getString(SP_Jadwal_3, "");
    }
    public String getSP_Jam(){
        return sp.getString(SP_Jam, "");
    }

}
