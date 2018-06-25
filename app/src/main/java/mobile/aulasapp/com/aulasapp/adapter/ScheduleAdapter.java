package mobile.aulasapp.com.aulasapp.adapter;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import java.util.ArrayList;

import mobile.aulasapp.com.aulasapp.R;
import mobile.aulasapp.com.aulasapp.model.Schedule;

public class ScheduleAdapter extends ArrayAdapter<Schedule> {
    private final Context context;

    public ScheduleAdapter(@NonNull Context context, ArrayList<Schedule> schedules) {
        super(context, 0, schedules);
        this.context = context;
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        // Get the data item for this position
        Schedule schedule = getItem(position);
        // Check if an existing view is being reused, otherwise inflate the view
        if (convertView == null) {
            convertView = LayoutInflater.from(getContext()).inflate(R.layout.item_schedule, parent, false);
        }
        // Lookup view for data population
        TextView tvName = (TextView) convertView.findViewById(R.id.tvDay);
        TextView tvHome = (TextView) convertView.findViewById(R.id.tvPeriod);
        // Populate the data into the template view using the data object
        tvName.setText(context.getResources().getStringArray(R.array.days)[schedule.getDay()]);
        tvHome.setText(schedule.toString());
        // Return the completed view to render on screen
        return convertView;
    }
}
