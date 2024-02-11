package com.technopradyumn.evstationmap;

import static com.technopradyumn.evstationmap.model.Constants.API_KEY;

import android.content.Context;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.location.Location;
import android.location.LocationManager;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.core.app.ActivityCompat;
import androidx.fragment.app.FragmentActivity;
import androidx.lifecycle.ViewModelProvider;

import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.GoogleMapOptions;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.CameraPosition;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.LatLngBounds;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.maps.model.Polyline;
import com.google.android.gms.maps.model.PolylineOptions;
import com.google.maps.DirectionsApi;
import com.google.maps.GeoApiContext;
import com.google.maps.model.DirectionsResult;
import com.google.maps.model.TravelMode;
import com.technopradyumn.evstationmap.databinding.ActivityMapsBinding;
import com.technopradyumn.evstationmap.model.StationModel;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class MapsActivity extends FragmentActivity implements OnMapReadyCallback {
    private GoogleMap mMap;
    private ActivityMapsBinding binding;
    private StationViewModel stationViewModel;

    private Polyline pathPolyline;

    GoogleMapOptions options = new GoogleMapOptions();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityMapsBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        stationViewModel = new ViewModelProvider(this).get(StationViewModel.class);

        ImageView changeMapView = findViewById(R.id.changeMapView);

        changeMapView.setOnClickListener(view -> {
            if (mMap.getMapType() == GoogleMap.MAP_TYPE_HYBRID) {
                mMap.setMapType(GoogleMap.MAP_TYPE_NORMAL);
            } else {
                mMap.setMapType(GoogleMap.MAP_TYPE_HYBRID);
            }
        });

        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        assert mapFragment != null;
        mapFragment.getMapAsync(this);

    }

    @Override
    public void onMapReady(@NonNull GoogleMap googleMap) {
        mMap = googleMap;

        if (ActivityCompat.checkSelfPermission(this, android.Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED) {
            mMap.setMyLocationEnabled(true);

            // Get the user's last known location using Fused Location Provider
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
                            mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(userLocation, 15));

                            CameraPosition cameraPosition = new CameraPosition.Builder()
                                    .target(userLocation)
                                    .build();

                            loadStationsFromViewModel();
                            mMap.animateCamera(CameraUpdateFactory.newCameraPosition(cameraPosition));
                            mMap.addMarker(markerOptions);

                            mMap.setOnMarkerClickListener(marker -> {
                                LatLng destination = marker.getPosition();
                                drawPath(userLocation, destination);
//                                drawRealPath(userLocation, destination);
                                return false;
                            });

                        }
                    });
        } else {
            Toast.makeText(this, "Location permission not granted", Toast.LENGTH_SHORT).show();
        }
    }


    private void loadStationsFromViewModel() {
        stationViewModel.getStations().observe(this, stations -> {
            for (StationModel station : stations) {
                addMarkerToMap(station);
            }
        });
    }

    private void addMarkerToMap(StationModel station) {
        LatLng stationLocation = new LatLng(station.getLatitude(), station.getLongitude());

        // Inflate custom marker layout
        View customMarkerView = getLayoutInflater().inflate(R.layout.custom_marker_layout, null);

        // Set title and distance on the custom marker view
        TextView titleTextView = customMarkerView.findViewById(R.id.titleTextView);
        titleTextView.setText(station.getName() + "\nAvailable Point - " + station.getAvailableChargingPoints());

        MarkerOptions markerOptions = new MarkerOptions()
                .position(stationLocation)
                .icon(BitmapDescriptorFactory.fromBitmap(createBitmapFromView(customMarkerView)));

        markerOptions.infoWindowAnchor(0.5f, 0.5f);
        markerOptions.anchor(0.5f, 0.5f);
        mMap.addMarker(markerOptions).showInfoWindow();
    }

    private Bitmap createBitmapFromView(View view) {
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

        // Perform any necessary calculations to obtain the route (e.g., using Directions API)

        // For demonstration purposes, let's create a Polyline with a few intermediate points
        LatLng intermediate1 = new LatLng((origin.latitude + destination.latitude) / 2, (origin.longitude + destination.longitude) / 2);
        LatLng intermediate2 = new LatLng((intermediate1.latitude + destination.latitude) / 2, (intermediate1.longitude + destination.longitude) / 2);

        // Add the Polyline to the map
        pathPolyline = mMap.addPolyline(new PolylineOptions()
                .add(origin, intermediate1, intermediate2, destination)
                .width(5)
                .color(Color.BLUE));

        // Move the camera to show the entire path
        LatLngBounds.Builder builder = new LatLngBounds.Builder();
        builder.include(origin);
        builder.include(destination);
        LatLngBounds bounds = builder.build();
        mMap.animateCamera(CameraUpdateFactory.newLatLngBounds(bounds, 50));
    }

    private void drawRealPath(LatLng origin, LatLng destination) {
        if (pathPolyline != null) {
            pathPolyline.remove();
        }

        // Create a new thread to perform network operations (Directions API calls)
        new Thread(() -> {
            try {
                GeoApiContext geoApiContext = new GeoApiContext.Builder()
                        .apiKey(API_KEY)
                        .build();

                DirectionsResult directionsResult = DirectionsApi.newRequest(geoApiContext)
                        .mode(TravelMode.DRIVING)
                        .origin(new com.google.maps.model.LatLng(origin.latitude, origin.longitude))
                        .destination(new com.google.maps.model.LatLng(destination.latitude, destination.longitude))
                        .await();

                if (directionsResult.routes.length > 0) {
                    List<LatLng> path = decodePolyline(directionsResult.routes[0].overviewPolyline.getEncodedPath());

                    runOnUiThread(() -> {
                        // Add the Polyline to the map
                        pathPolyline = mMap.addPolyline(new PolylineOptions()
                                .addAll(path)
                                .width(5)
                                .color(Color.BLUE));

                        // Move the camera to show the entire path
                        LatLngBounds.Builder builder = new LatLngBounds.Builder();
                        builder.include(origin);
                        builder.include(destination);
                        LatLngBounds bounds = builder.build();
                        mMap.animateCamera(CameraUpdateFactory.newLatLngBounds(bounds, 50));
                    });
                }
            } catch (InterruptedException | IOException |
                     com.google.maps.errors.ApiException e) {
                e.printStackTrace();
            }
        }).start();
    }

    private List<LatLng> decodePolyline(String encodedPolyline) {
        List<LatLng> polyline = new ArrayList<>();
        int index = 0, len = encodedPolyline.length();
        int lat = 0, lng = 0;

        while (index < len) {
            int b, shift = 0, result = 0;
            do {
                b = encodedPolyline.charAt(index++) - 63;
                result |= (b & 0x1F) << shift;
                shift += 5;
            } while (b >= 0x20);

            int dlat = ((result & 1) != 0 ? ~(result >> 1) : (result >> 1));
            lat += dlat;

            shift = 0;
            result = 0;
            do {
                b = encodedPolyline.charAt(index++) - 63;
                result |= (b & 0x1F) << shift;
                shift += 5;
            } while (b >= 0x20);

            int dlng = ((result & 1) != 0 ? ~(result >> 1) : (result >> 1));
            lng += dlng;

            LatLng p = new LatLng((double) lat / 1E5, (double) lng / 1E5);
            polyline.add(p);
        }

        return polyline;
    }


}