package com.example.appdubaothoitiet;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.squareup.picasso.Picasso;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

public class WeatherNextDayAdapter extends RecyclerView.Adapter<WeatherNextDayAdapter.WeatherViewHolder> {
    Context context;
    List<WeatherNextDay> weatherNextDayList;

    public WeatherNextDayAdapter(Context context, List<WeatherNextDay> weatherNextDayList) {
        this.context = context;
        this.weatherNextDayList = weatherNextDayList;
    }

    @Override
    public WeatherViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.weather7days_item,parent,false);
        return new WeatherViewHolder(view);
    }

    @Override
    public void onBindViewHolder(WeatherNextDayAdapter.WeatherViewHolder holder, int position) {
        SimpleDateFormat input = new SimpleDateFormat("yyyy-MM-dd");
        try {
            Date date = input.parse(weatherNextDayList.get(position).getDays());
            holder.txtDays.setText(input.format(date));
        } catch (ParseException e) {
            e.printStackTrace();
        }
        holder.txtTrangThai.setText(weatherNextDayList.get(position).getTrangThai());
        holder.txtMaxTemp.setText(weatherNextDayList.get(position).getMaxTemp()+ "°C ");
        holder.txtMinTemp.setText(weatherNextDayList.get(position).getMinTemp()+ "°C - ");
        Picasso.get().load("http:".concat(weatherNextDayList.get(position).getImageIcon())).into(holder.imageIcon);
    }

    @Override
    public int getItemCount() {
        return weatherNextDayList.size();
    }

    public class WeatherViewHolder extends RecyclerView.ViewHolder{
        TextView txtDays , txtTrangThai,txtMaxTemp,txtMinTemp;
        ImageView imageIcon;
        public WeatherViewHolder(View itemView) {
            super(itemView);
            txtDays = itemView.findViewById(R.id.txtDays);
            txtTrangThai = itemView.findViewById(R.id.txtTrangThai);
            txtMaxTemp = itemView.findViewById(R.id.maxTemperature);
            txtMinTemp = itemView.findViewById(R.id.minTemperature);
            imageIcon = itemView.findViewById(R.id.imageIcon);
        }
    }
}
