package com.technopradyumn.evstationmap;

import static com.technopradyumn.evstationmap.model.Constants.API_KEY;

import androidx.core.app.ActivityCompat;
import androidx.fragment.app.FragmentActivity;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.CameraPosition;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.LatLngBounds;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.maps.model.Polyline;
import com.google.android.gms.maps.model.PolylineOptions;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.maps.DirectionsApi;
import com.google.maps.GeoApiContext;
import com.google.maps.model.DirectionsResult;
import com.google.maps.model.TravelMode;
import com.technopradyumn.evstationmap.databinding.ActivitySingleStationMapsBinding;

import java.io.IOException;
import java.util.List;
import java.util.Objects;

public class SingleStationMapsActivity extends FragmentActivity implements OnMapReadyCallback {

    private GoogleMap mMap;
    private ActivitySingleStationMapsBinding binding;
    private Polyline pathPolyline;
    private FirebaseFirestore firestore;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        binding = ActivitySingleStationMapsBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        firestore = FirebaseFirestore.getInstance();

        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        assert mapFragment != null;
        mapFragment.getMapAsync(this);
    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;

        // Get the station details passed from the previous activity
        String name = getIntent().getStringExtra("stationName");
        double latitude = getIntent().getDoubleExtra("stationLatitude", 0);
        double longitude = getIntent().getDoubleExtra("stationLongitude", 0);
        LatLng stationLatLng = new LatLng(latitude, longitude);

        // Add a marker for the station
        View customMarkerView = getLayoutInflater().inflate(R.layout.custom_marker_layout, null);
        TextView titleTextView = customMarkerView.findViewById(R.id.titleTextView);
        ImageView markerImageView = customMarkerView.findViewById(R.id.markerImageView);
        titleTextView.setText(name);
        markerImageView.setImageResource(R.drawable.circular_marker_icon);

        MarkerOptions markerOp = new MarkerOptions()
                .position(stationLatLng)
                .icon(BitmapDescriptorFactory.fromBitmap(createDrawableFromView(customMarkerView)));

        markerOp.infoWindowAnchor(0.5f, 0.5f);
        markerOp.anchor(0.9f, 0.9f);
        Objects.requireNonNull(mMap.addMarker(markerOp)).showInfoWindow();


        // Move camera to the station location
        mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(stationLatLng, 10.5f));

        if (ActivityCompat.checkSelfPermission(this, android.Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED) {
            mMap.setMyLocationEnabled(true);

            LocationRequest locationRequest = LocationRequest.create();
            locationRequest.setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY);
            LocationServices.getFusedLocationProviderClient(this)
                    .getLastLocation()
                    .addOnSuccessListener(this, location -> {
                        if (location != null) {
                            LatLng userLocation = new LatLng(location.getLatitude(), location.getLongitude());
                            MarkerOptions markerOptions = new MarkerOptions()
                                    .position(userLocation)
                                    .title("Your Location");
                            mMap.addMarker(markerOptions);

                            mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(userLocation, 10.5f));

                            CameraPosition cameraPosition = new CameraPosition.Builder()
                                    .target(userLocation)
                                    .zoom(10.5f)
                                    .build();

                            mMap.animateCamera(CameraUpdateFactory.newCameraPosition(cameraPosition));
                            mMap.setOnMarkerClickListener(marker -> {
                                LatLng destination = marker.getPosition();
                                drawPath(userLocation, destination);
//                               drawRealPath(userLocation, destination);
                                return false;
                            });

                        }
                    });
        } else {
            Toast.makeText(this, "Location permission not granted", Toast.LENGTH_SHORT).show();
        }

    }


    private Bitmap createDrawableFromView(View view) {
        view.measure(View.MeasureSpec.UNSPECIFIED, View.MeasureSpec.UNSPECIFIED);
        view.layout(0, 0, view.getMeasuredWidth(), view.getMeasuredHeight());
        Bitmap bitmap = Bitmap.createBitmap(view.getMeasuredWidth(), view.getMeasuredHeight(), Bitmap.Config.ARGB_8888);
        Canvas canvas = new Canvas(bitmap);
        view.draw(canvas);
        return bitmap;
    }

    private void drawPath(LatLng origin, LatLng destination) {
        if (pathPolyline != null) {
            pathPolyline.remove();
        }

        pathPolyline = mMap.addPolyline(new PolylineOptions()
                .add(origin, destination)
                .width(5)
                .color(Color.BLUE));

        LatLngBounds.Builder builder = new LatLngBounds.Builder();
        builder.include(origin);
        builder.include(destination);
        LatLngBounds bounds = builder.build();
        mMap.animateCamera(CameraUpdateFactory.newLatLngBounds(bounds, 50));
    }



//    private void drawRealPath(LatLng origin, LatLng destination) {
//        if (pathPolyline != null) {
//            pathPolyline.remove();
//        }
//
//        // Create a new thread to perform network operations (Directions API calls)
//        new Thread(() -> {
//            try {
//                GeoApiContext geoApiContext = new GeoApiContext.Builder()
//                        .apiKey(API_KEY)
//                        .build();
//
//                DirectionsResult directionsResult = DirectionsApi.newRequest(geoApiContext)
//                        .mode(TravelMode.DRIVING)
//                        .origin(new com.google.maps.model.LatLng(origin.latitude, origin.longitude))
//                        .destination(new com.google.maps.model.LatLng(destination.latitude, destination.longitude))
//                        .await();
//
//                if (directionsResult.routes.length > 0) {
//                    List<LatLng> path = decodePolyline(directionsResult.routes[0].overviewPolyline.getEncodedPath());
//
//                    runOnUiThread(() -> {
//                        // Add the Polyline to the map
//                        pathPolyline = mMap.addPolyline(new PolylineOptions()
//                                .addAll(path)
//                                .width(5)
//                                .color(Color.BLUE));
//
//                        // Move the camera to show the entire path
//                        LatLngBounds.Builder builder = new LatLngBounds.Builder();
//                        builder.include(origin);
//                        builder.include(destination);
//                        LatLngBounds bounds = builder.build();
//                        mMap.animateCamera(CameraUpdateFactory.newLatLngBounds(bounds, 50));
//                    });
//                }
//            } catch (InterruptedException | IOException |
//                     com.google.maps.errors.ApiException e) {
//                e.printStackTrace();
//            }
//        }).start();
//    }


}