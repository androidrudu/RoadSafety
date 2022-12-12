package com.montbleu.Utils;


import okhttp3.Call;
import okhttp3.OkHttpClient;
import okhttp3.Request;

/**
 * Created by ardiya on 4/19/2017.
 */
public class OpenWeatherSingleton {
    private String appid = "8819d02b2f03bec5b4cd1e402f39cca8";
    //for public
    private String api_server = "http://api.openweathermap.org/data/2.5/";
    //for testing
    //private String api_server = "http://samples.openweathermap.org/data/2.5/";
    private OkHttpClient client = new OkHttpClient();

    private static OpenWeatherSingleton instance = null;

    public OpenWeatherSingleton() {
    }

    public static OpenWeatherSingleton getInstance(){
        if(instance == null){
            instance = new OpenWeatherSingleton();
        }
        return instance;
    }

    public Call getCurrentWeatherByCity(String cityName){
        return getCurrentWeatherByCity(cityName, "metric");
    }

    public Call getCurrentWeatherByCity(String cityName, String unit){
        Request request = new Request.Builder()
                .url(api_server + String.format("weather?q=%s&units=%s&appid=%s", cityName, unit, appid))
                .build();

        return client.newCall(request);
    }



    public Call getForecastByCity(String lat,String lng){
        Request request = new Request.Builder()
                .url(api_server + String.format("weather?units=metric&appid=%s&lat=%s&lon=%s",appid,lat,lng))
                .build();
        return client.newCall(request);
    }


}
