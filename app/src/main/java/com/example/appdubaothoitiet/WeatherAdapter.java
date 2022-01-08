package com.example.appdubaothoitiet;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import com.squareup.picasso.Picasso;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

public class WeatherAdapter extends RecyclerView.Adapter<WeatherAdapter.ViewHolder> {
    private Context context;
    private List<WeatherModel> weatherModelList;

    public WeatherAdapter(Context context, List<WeatherModel> weatherModelList) {
        this.context = context;
        this.weatherModelList = weatherModelList;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.weather_item,parent,false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(WeatherAdapter.ViewHolder holder, int position) {
        holder.txtTemperature.setText(weatherModelList.get(position).getTemperature() + " °C ");
        holder.txtWindSpeed.setText(weatherModelList.get(position).getWindSpeed() + "Km/h");
        Picasso.get().load("http:".concat(weatherModelList.get(position).getIcon())).into(holder.imgCodition);
        SimpleDateFormat input = new SimpleDateFormat("yyyy-MM-dd hh:mm");
        SimpleDateFormat output = new SimpleDateFormat("hh:mm aa");
        try {
            Date date = input.parse(weatherModelList.get(position).getTime());
            holder.txtTime.setText(output.format(date));
        } catch (ParseException e) {
            e.printStackTrace();
        }
    }

    @Override
    public int getItemCount() {
        return weatherModelList.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder{
        TextView txtTime,txtTemperature,txtWindSpeed;
        ImageView imgCodition;
        public ViewHolder(View itemView) {
            super(itemView);
            txtTime = itemView.findViewById(R.id.txtTime);
            txtTemperature = itemView.findViewById(R.id.txtTemperature);
            txtWindSpeed = itemView.findViewById(R.id.txtWindSpeed);
            imgCodition = itemView.findViewById(R.id.imgCodition);
        }
    }
}
