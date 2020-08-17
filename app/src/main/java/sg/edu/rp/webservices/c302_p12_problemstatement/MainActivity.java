package sg.edu.rp.webservices.c302_p12_problemstatement;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;

import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.JsonHttpResponseHandler;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

import cz.msebera.android.httpclient.Header;

public class MainActivity extends AppCompatActivity {

    ListView lv;
    IncidentAdapter ia;
    ArrayList<Incident> al;
    private AsyncHttpClient client;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        lv = findViewById(R.id.lv);
        client = new AsyncHttpClient();
        al = new ArrayList<Incident>();
        ia = new IncidentAdapter(getBaseContext(), R.layout.row, al);
        lv.setAdapter(ia);

        client.addHeader("AccountKey", "cYsiznKuReChgmNVjkun9Q==");
        client.get("http://datamall2.mytransport.sg/ltaodataservice/TrafficIncidents", new JsonHttpResponseHandler() {
            @Override
            public void onSuccess(int statusCode, Header[] headers, JSONObject response) {
                try {
                    Log.i("JSON Results: ", response.toString());
                    JSONArray value = response.getJSONArray("value");
                    for (int i = 0; i < value.length(); i++) {
                        JSONObject jsonObject = value.getJSONObject(i);
                        Incident incident = new Incident(jsonObject.getString("Type"), jsonObject.getDouble("Latitude"), jsonObject.getDouble("Longitude"), jsonObject.getString("Message"));
                        al.add(incident);
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
                ia.notifyDataSetChanged();
            }
        });

        lv.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Intent intent = new Intent(MainActivity.this, MapsActivity.class);
                intent.putExtra("coords", al.get(position));
                startActivity(intent);
            }
        });
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.main, menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId()) {
            case R.id.action_menu:
                Intent i = new Intent(MainActivity.this, MapsActivity.class);
                startActivity(i);
                break;
            case R.id.action_add:
                AlertDialog.Builder builder = new AlertDialog.Builder(MainActivity.this);
                builder.setTitle("Upload to Firestore");
                builder.setMessage("Proceed to upload to Firestore?");
                builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        CollectionReference cr = FirebaseFirestore.getInstance().collection("incidents");
                        for (Incident i:al) {
                            cr.add(i);
                        }
                    }
                });
                builder.setNegativeButton("CANCEL", null);
                AlertDialog dialog = builder.create();
                dialog.show();
                break;
        }
        return super.onOptionsItemSelected(item);
    }
}