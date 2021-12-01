package com.example.weather;

import android.content.Context;
import android.os.Build;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;

import com.android.volley.Response;
import com.example.weather.ModalClass.Weather;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class ListAdaptor extends ArrayAdapter<Weather> {

    public ListAdaptor(@NonNull Context context, List<Weather> weatherList) {
        super(context, 0, weatherList);
    }

    @RequiresApi(api = Build.VERSION_CODES.N)
    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {

        //Getting Particular Weather Information
        Weather weather = getItem(position);

        if (convertView == null)
            //Attaching Layout File
            convertView = LayoutInflater.from(getContext()).inflate(R.layout.five_day_history_layout, parent, false);

        //Data Initialization and declaration
        TextView date = convertView.findViewById(R.id.Date);
        TextView cond = convertView.findViewById(R.id.weather_cond);
        TextView max = convertView.findViewById(R.id.max_temperature);
        TextView min = convertView.findViewById(R.id.min_temperature);

        //Formatting TimeStamp to required format of Date and Time using Built-in functions
        String dateStr = "";
        try {
            DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
            Date d = dateFormat.parse(weather.getDate().substring(0, 10));
            DateFormat formatter = new SimpleDateFormat("EEE,\nMMM d,''yy");
            dateStr = formatter.format(d);
        } catch (Exception e) {
            Toast.makeText(getContext(), e.getMessage(), Toast.LENGTH_SHORT).show();
        }

        //Setting Up Data
        date.setText(dateStr);
        cond.setText(weather.getDayPhrase()+"\n"+weather.getNightPhrase());
        max.setText(weather.getMaxTemp()+"° C");
        min.setText(weather.getMinTemp()+"° C");

        return convertView;
    }
}
