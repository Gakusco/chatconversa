package com.jcortiz.chatconversa.Activities;

import androidx.annotation.NonNull;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.FragmentActivity;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Location;
import android.os.Bundle;
import android.widget.Toast;

import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationCallback;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationResult;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.location.LocationSettingsRequest;
import com.google.android.gms.maps.CameraUpdate;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.jcortiz.chatconversa.Constantes;
import com.jcortiz.chatconversa.R;

public class MapsActivity extends FragmentActivity implements OnMapReadyCallback, GoogleMap.OnMapLongClickListener, GoogleMap.OnMarkerClickListener {

    private GoogleMap mMap;


    private static final int LOCATION_CODE = 40;
    private static final String[] PERMISSIONS = new String[]{Manifest.permission.ACCESS_FINE_LOCATION};
    private FusedLocationProviderClient fusedLocationProviderClient;
    private LocationRequest locationRequest;
    private LocationCallback locationCallback;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_maps);
        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);

        fusedLocationProviderClient = LocationServices.getFusedLocationProviderClient(this);

        locationCallback = new LocationCallback() {
            @Override
            public void onLocationResult(LocationResult locationResult) {
                if(locationResult == null ) {
                    return;
                }
                for (Location location : locationResult.getLocations()) {
                    CameraUpdate pos = CameraUpdateFactory.newLatLng(new LatLng(location.getLatitude(),location.getLongitude()));
                    mMap.moveCamera(pos);
                }
            }
        };


    }

    private void inicializarUbicacionPorRastreo() {
        locationRequest = new LocationRequest();
        locationRequest.setInterval(2000); // 2 segundos
        locationRequest.setFastestInterval(1000); // 1 segundo
        locationRequest.setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY);
        LocationSettingsRequest.Builder builder = new LocationSettingsRequest.Builder()
                .addLocationRequest(locationRequest);
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            // TODO: Consider calling
            //    ActivityCompat#requestPermissions
            // here to request the missing permissions, and then overriding
            //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
            //                                          int[] grantResults)
            // to handle the case where the user grants the permission. See the documentation
            // for ActivityCompat#requestPermissions for more details.
            return;
        }
        fusedLocationProviderClient.requestLocationUpdates(locationRequest, locationCallback, null);
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
        mMap.getUiSettings().setCompassEnabled(true);
        mMap.getUiSettings().setZoomControlsEnabled(true);
        Bundle ubicacion = getIntent().getExtras();
        if ( ubicacion != null ){
            String latitud = ubicacion.getString(Constantes.ENVIAR_LATITUD);
            String longitud = ubicacion.getString(Constantes.ENVIAR_LONGITUD);
            LatLng latLng = new LatLng(Double.parseDouble(latitud), Double.parseDouble(longitud));
            mMap.addMarker(new MarkerOptions().position(latLng).title("Ubicación"));
            CameraUpdate pos = CameraUpdateFactory.newLatLng(latLng);
            CameraUpdate zoom = CameraUpdateFactory.zoomTo(17);
            mMap.moveCamera(zoom);
            mMap.moveCamera(pos);
        } else {
            mMap.setOnMapLongClickListener(this);
            mMap.setOnMarkerClickListener(this);
            CameraUpdate zoom = CameraUpdateFactory.zoomTo(17);
            mMap.moveCamera(zoom);
            habilitarLocalizacion();
        }

    }

    private void habilitarLocalizacion() {
        if(ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED) {
            if (mMap != null) {
                mMap.setMyLocationEnabled(true);
                inicializarUbicacionPorRastreo();
            }
        } else {
            ActivityCompat.requestPermissions(this, PERMISSIONS, LOCATION_CODE);
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        if (requestCode != LOCATION_CODE) {
            return;
        }

        if (ContextCompat.checkSelfPermission(this,Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED) {
            habilitarLocalizacion();
        }
    }

    @Override
    public void onMapLongClick(LatLng latLng) {
        mMap.addMarker(new MarkerOptions().position(latLng).title("Marcador"));
    }

    @Override
    public boolean onMarkerClick(Marker marker) {
        Intent i = new Intent(MapsActivity.this, Principal.class);
        i.putExtra(Constantes.ENVIAR_LATITUD, String.valueOf(marker.getPosition().latitude));
        i.putExtra(Constantes.ENVIAR_LONGITUD, String.valueOf(marker.getPosition().longitude));
        startActivity(i);
        finish();
        return false;
    }
}