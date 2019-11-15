package com.example.cjambo.utils;

import android.content.Context;
import android.content.SharedPreferences;

import com.example.cjambo.R;

public class SharedPreferencesConfig {
    private SharedPreferences sharedPreferences;
    private Context context;
    public SharedPreferencesConfig(Context context){
        this.context = context;
        sharedPreferences = context.getSharedPreferences(context.getResources().getString(R.string.SHARED_PREFERENCES),Context.MODE_PRIVATE);
    }
    public void saveAuthenticationInformation(String accessToken,String name,String status){
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putString(context.getResources().getString(R.string.CLIENT_NAME),name);
        editor.putString(context.getResources().getString(R.string.CLIENT_TOKEN),accessToken);
        editor.putString(context.getResources().getString(R.string.CLIENT_STATUS),status);
        editor.commit();
    }
    public String readClientName(){
        String name;
        name = sharedPreferences.getString(context.getResources().getString(R.string.CLIENT_NAME),"");
        return  name;
    }
    public String readClientToken(){
        String accessToken;
        accessToken = sharedPreferences.getString(context.getResources().getString(R.string.CLIENT_TOKEN),"");
        return  accessToken;
    }
    public String readClientStatus(){
        String status;
        status = sharedPreferences.getString(context.getResources().getString(R.string.CLIENT_STATUS),"");
        return  status;
    }
    public  boolean isloggedIn(){
        return readClientStatus().equals(Constants.ACTIVE_CONSTANT);
    }
}
