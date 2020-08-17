package sg.edu.rp.webservices.c302_p12_problemstatement;

import androidx.annotation.Nullable;
import androidx.fragment.app.FragmentActivity;

import android.content.Intent;
import android.os.Bundle;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

public class MapsActivity extends FragmentActivity implements OnMapReadyCallback {

    private GoogleMap mMap;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_maps);
        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);
    }

    /**
     * Manipulates the map once available.
     * This callback is triggered when the map is ready to be used.
     * This is where we can add markers or lines, add listeners or move the camera. In this case,
     * we just add a marker near Sydney, Australia.
     * If Google Play services is not installed on the device, the user will be prompted to install
     * it inside the SupportMapFragment. This method will only be triggered once the user has
     * installed Google Play services and returned to the app.
     */
    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;
        Intent intent = getIntent();
        Incident incident = (Incident) intent.getSerializableExtra("coords");
        if (incident != null) {
            LatLng location = new LatLng(incident.getLatitude(), incident.getLongitude());
            mMap.addMarker(new MarkerOptions()
                    .position(location)
                    .title(incident.getMessage())
            );
        }
        else {
            CollectionReference cr = FirebaseFirestore.getInstance().collection("incidents");
            cr.addSnapshotListener(new EventListener<QuerySnapshot>() {
                @Override
                public void onEvent(@Nullable QuerySnapshot value, @Nullable FirebaseFirestoreException error) {
                    for (QueryDocumentSnapshot doc: value) {
                        if (doc.getDouble("latitude") != null && doc.getDouble("longitude") != null) {
                            LatLng point = new LatLng(doc.getDouble("latitude"), doc.getDouble("longitude"));
                            mMap.addMarker(new MarkerOptions()
                                    .position(point)
                                    .title(doc.getString("message"))
                            );
                        }
                    }
                }
            });
        }
        LatLng singapore = new LatLng(1.3437449, 103.7540047);
        mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(singapore, 11));
    }
}