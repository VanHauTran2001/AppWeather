package com.example.appdubaothoitiet;

import android.os.Bundle;

import androidx.databinding.DataBindingUtil;
import androidx.fragment.app.Fragment;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.example.appdubaothoitiet.R;
import com.example.appdubaothoitiet.databinding.FragmentNextDayBinding;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

public class NextDayFragment extends Fragment {
    FragmentNextDayBinding binding;
    WeatherNextDayAdapter weatherNextDayAdapter;
    ArrayList<WeatherNextDay> weatherNextDayArrayList;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,Bundle savedInstanceState) {
        binding = DataBindingUtil.inflate(inflater,R.layout.fragment_next_day,container,false);
        weatherNextDayArrayList = new ArrayList<>();
        weatherNextDayAdapter = new WeatherNextDayAdapter(getActivity(),weatherNextDayArrayList);
        binding.recyler7days.setAdapter(weatherNextDayAdapter);
        return binding.getRoot();
    }
    public void receiveDataFragment(String dataCity){
        binding.txtCity.setText(dataCity);
        getData7days(dataCity);
    }
    private void getData7days(String data){
        String url = "https://api.weatherapi.com/v1/forecast.json?key=dca892fc2d534382a1461857211207&q="+ data +"&days=7&aqi=yes&alerts=yes";
        //dùng volley đọc dữ liệu json từ API
        RequestQueue requestQueue = Volley.newRequestQueue(getActivity());
        StringRequest stringRequest = new StringRequest(Request.Method.GET, url,
                new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                weatherNextDayArrayList.clear();
                try {
                    JSONObject jsonObject = new JSONObject(response);
                    JSONObject forecastObj = jsonObject.getJSONObject("forecast");
                    JSONArray forecastArray = forecastObj.getJSONArray("forecastday");
                    for (int i=0;i<forecastArray.length();i++){
                        JSONObject object = forecastArray.getJSONObject(i);
                        String day = object.getString("date");
                        JSONObject dayObj = object.getJSONObject("day");
                        String maxTemp = dayObj.getString("maxtemp_c");
                        String minYTemp = dayObj.getString("mintemp_c");
                        String trangThai = dayObj.getJSONObject("condition").getString("text");
                        String imageIcon = dayObj.getJSONObject("condition").getString("icon");
                        weatherNextDayArrayList.add(new WeatherNextDay(day,trangThai,imageIcon,maxTemp,minYTemp));
                    }
                    weatherNextDayAdapter.notifyDataSetChanged();
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError volleyError) {

                    }
                });
        requestQueue.add(stringRequest);
    }
}