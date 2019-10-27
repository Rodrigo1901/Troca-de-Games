package com.res.trocadejogos.Views;

import androidx.fragment.app.FragmentActivity;

import android.graphics.Color;
import android.os.Bundle;
import android.widget.Toast;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.CircleOptions;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.maps.model.PolygonOptions;
import com.google.android.gms.maps.model.PolylineOptions;
import com.res.trocadejogos.R;

public class Mapa extends FragmentActivity implements OnMapReadyCallback {

    private GoogleMap mMap;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_mapa);

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

        //Variável do mapa
        mMap = googleMap;

        /* Latitude e Longitude - Casa Samuel: -22.960772, -47.193555
           Latitude e Longitude - Casa Rodrigo: -22.928239, -47.095333 */

        //Mudar tipo de exibição do mapa
        mMap.setMapType(GoogleMap.MAP_TYPE_NORMAL);

        final LatLng casaRodrigo = new LatLng(-22.928239, -47.095333);

        /* Adicionando formas (Círculo e Poligono) ao redor de um ponto:

        CircleOptions circleOptions = new CircleOptions();
        circleOptions.center(casaRodrigo);
        circleOptions.radius(500);//em metros
        circleOptions.strokeWidth(10);
        circleOptions.strokeColor(Color.GREEN);
        circleOptions.fillColor(Color.argb(64,255,153,0));
        mMap.addCircle(circleOptions);

        PolygonOptions polygonOptions = new PolygonOptions();
        polygonOptions.add(new LatLng(-22.925894, -47.095611));
        polygonOptions.add(new LatLng(-22.928071, -47.096670));
        polygonOptions.add(new LatLng(-22.929019, -47.094492));
        polygonOptions.add(new LatLng(-22.927695, -47.093484));
        polygonOptions.strokeWidth(10);
        polygonOptions.strokeColor(Color.GREEN);
        polygonOptions.fillColor(Color.argb(64,255,153,0));
        mMap.addPolygon(polygonOptions);
         */

        /* Adicionando evento de clique no mapa:

        mMap.setOnMapClickListener(new GoogleMap.OnMapClickListener() {
            @Override
            public void onMapClick(LatLng latLng) {

                Double latitude = latLng.latitude;
                Double longitude = latLng.longitude;

                Toast.makeText(Mapa.this, "Lat: " + latitude + "\nLong: " + longitude, Toast.LENGTH_SHORT).show();

                PolylineOptions polylineOptions = new PolylineOptions();
                polylineOptions.add(casaRodrigo);
                polylineOptions.add(latLng);
                polylineOptions.color(Color.BLUE);
                polylineOptions.width(20);

                mMap.addPolyline(polylineOptions);
            }
        });
         */

        //Adicionando marcador no mapa
        mMap.addMarker(
                new MarkerOptions()
                        .position(casaRodrigo)
                        .title("Iniciar conversa com Rodrigo")
                        .snippet("God of War - Troca: SIM - Vende: NÃO")
                        .icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_GREEN))
                //.icon(BitmapDescriptorFactory.fromResource(R.drawable.icon_chat_48px))
        );
        //Definindo zoom ao abrir mapa
        mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(casaRodrigo, 15));
    }
}