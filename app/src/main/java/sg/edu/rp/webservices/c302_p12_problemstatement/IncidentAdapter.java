package sg.edu.rp.webservices.c302_p12_problemstatement;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import java.util.ArrayList;

public class IncidentAdapter extends ArrayAdapter<Incident> {
    private Context context;
    private ArrayList<Incident> al;
    private TextView tv1, tv2;

    public IncidentAdapter(Context context, int resource, ArrayList<Incident> objects) {
        super(context, resource, objects);
        this.context = context;
        al = objects;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        LayoutInflater inflater = (LayoutInflater) context
                .getSystemService(Context.LAYOUT_INFLATER_SERVICE);

        View rowView = inflater.inflate(R.layout.row, parent, false);
        tv1 = rowView.findViewById(R.id.textView);
        tv2 = rowView.findViewById(R.id.textView2);

        Incident incident = al.get(position);
        tv1.setText(incident.getType());
        tv2.setText(incident.getMessage());
        return rowView;
    }
}
