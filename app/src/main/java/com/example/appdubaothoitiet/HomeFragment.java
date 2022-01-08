package com.example.appdubaothoitiet;

import android.Manifest;
import android.content.Context;
import android.content.pm.PackageManager;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.location.LocationManager;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.core.app.ActivityCompat;
import androidx.databinding.DataBindingUtil;
import androidx.fragment.app.Fragment;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.example.appdubaothoitiet.R;
import com.example.appdubaothoitiet.databinding.FragmentHomeBinding;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationServices;
import com.squareup.picasso.Picasso;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Locale;

public class HomeFragment extends Fragment {
    FragmentHomeBinding binding;
    WeatherAdapter weatherAdapter;
    ArrayList<WeatherModel> weatherModelArrayList;
    private LocationManager locationManager;
    private int PEMISSION_CODE = 1;
    private String nameCity;

    private ISendDataFragment mIsendDataFragment;

    public interface ISendDataFragment{
        void sendData(String sendCity);
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        //khoi tao interface
        mIsendDataFragment = (ISendDataFragment) getActivity();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        getActivity().getWindow().setFlags(WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS, WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS);
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_home, container, false);
        weatherModelArrayList = new ArrayList<>();
        weatherAdapter = new WeatherAdapter(getActivity(), weatherModelArrayList);
        binding.recylerWeather.setAdapter(weatherAdapter);
        locationManager = (LocationManager) getActivity().getSystemService(Context.LOCATION_SERVICE);
        //kiem tra quyền trong phân đoạn
        if (ActivityCompat.checkSelfPermission(getActivity(), Manifest.permission.ACCESS_FINE_LOCATION)!= PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(getActivity(), Manifest.permission.ACCESS_COARSE_LOCATION)!=PackageManager.PERMISSION_GRANTED){

            ActivityCompat.requestPermissions(getActivity(),new String[]{Manifest.permission.ACCESS_FINE_LOCATION,Manifest.permission.ACCESS_COARSE_LOCATION}, PEMISSION_CODE);
        }
        //lấy vị trí người dùng hiện tại
        Location location = locationManager.getLastKnownLocation(LocationManager.NETWORK_PROVIDER);
        if (location != null){
            nameCity = getnameCity(location.getLongitude(),location.getLatitude());
        }
        getWeatherInfo(nameCity);
        binding.imgSearch.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String city = binding.edtCity.getText().toString();
                if (city.isEmpty()){
                    Toast.makeText(getActivity(), "Please enter city name !", Toast.LENGTH_SHORT).show();
                }else {
                    binding.txtNameCity.setText(nameCity);
                    getWeatherInfo(city);
                    //send city to Nextdayfragment
                    mIsendDataFragment.sendData(city);
                }
            }
        });
        return binding.getRoot();
    }

    //Yêu cầu truy cập quyền khi cần thiết
    @Override
    public void onRequestPermissionsResult(int requestCode,String[] permissions,int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode==PEMISSION_CODE){
            if (grantResults.length>0 && grantResults[0] == PackageManager.PERMISSION_GRANTED){
                Toast.makeText(getActivity(), "Permision granted.....", Toast.LENGTH_SHORT).show();
            }else {
                Toast.makeText(getActivity(), "Please provide the permisions !", Toast.LENGTH_SHORT).show();
            }
        }
    }

    private String getnameCity(double longitude , double latitude){
        String nameCity = "Not found";
        //hiển thị tìm kiếm vị trí theo tọa độ
        Geocoder geocoder = new Geocoder(getActivity(), Locale.getDefault());
        try {
            //lấy tên thành phố từ tọa độ và vĩ độ
            List<Address> addresses = geocoder.getFromLocation(latitude,longitude,10);
            for (Address adr : addresses){
                if (adr != null){
                    String city = adr.getLocality();
                    if (city!=null && !city.equals("")){
                        nameCity = city;
                    }else {
                        Log.d("TAG","CITY NOT FOUND");
                        Toast.makeText(getActivity(), "User city not found...", Toast.LENGTH_SHORT).show();
                    }
                }
            }
        }catch (Exception e){
            e.printStackTrace();
        }
        return nameCity;
    }
    private void getWeatherInfo(String nameCity){
        String url = "http://api.weatherapi.com/v1/forecast.json?key=dca892fc2d534382a1461857211207&q=" + nameCity + "&days=1&aqi=yes&alerts=yes";
        binding.txtNameCity.setText(nameCity);
        //dùng volley đọc dữ liệu json từ API
        RequestQueue requestQueue = Volley.newRequestQueue(getActivity());
        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.GET, url, null, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                binding.idLoading.setVisibility(View.GONE);
                binding.idHome.setVisibility(View.VISIBLE);
                weatherModelArrayList.clear();
                try {
                    String teamperature =response.getJSONObject("current").getString("temp_c");
                    binding.txtNhietdo.setText(teamperature + " °C ");
                    String ngay = response.getJSONObject("current").getString("last_updated");
                    binding.txtNgayThang.setText(ngay);
                    int isDay = response.getJSONObject("current").getInt("is_day");
                    String condition = response.getJSONObject("current").getJSONObject("condition").getString("text");
                    String conditionIcon = response.getJSONObject("current").getJSONObject("condition").getString("icon");
                    Picasso.get().load("http:".concat(conditionIcon)).into(binding.imgIcon);
                    binding.txtCodition.setText(condition);
                    if (isDay==1){
                        //morning
                        Picasso.get().load("https://thuthuatnhanh.com/wp-content/uploads/2020/01/anh-bau-troi-xanh-lam-background-powerpoint.jpg").into(binding.imgBackgroup);
                    }else {
                        //night
                        Picasso.get().load("https://www.howtogeek.com/wp-content/uploads/2017/10/2badstarphoto.jpg").into(binding.imgBackgroup);
                    }
                    JSONObject forecastObj = response.getJSONObject("forecast");
                    JSONObject forecast0 = forecastObj.getJSONArray("forecastday").getJSONObject(0);
                    JSONArray hourArray = forecast0.getJSONArray("hour");
                    for (int i=0;i<hourArray.length();i++){
                        JSONObject hourObj = hourArray.getJSONObject(i);
                        String time = hourObj.getString("time");
                        String temperature = hourObj.getString("temp_c");
                        String imgIcon = hourObj.getJSONObject("condition").getString("icon");
                        String wind = hourObj.getString("wind_kph");
                        weatherModelArrayList.add(new WeatherModel(time,temperature,imgIcon,wind));
                    }
                    weatherAdapter.notifyDataSetChanged();

                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError volleyError) {
                Toast.makeText(getActivity(), "Please enter valid city name !", Toast.LENGTH_SHORT).show();
            }
        });
        requestQueue.add(jsonObjectRequest);
    }
}