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

    @Override
    public void onMapReady(GoogleMap googleMap) {

        //Variável do mapa
        mMap = googleMap;

        /* Latitude e Longitude - Casa Samuel: -22.960772, -47.193555
           Latitude e Longitude - Casa Rodrigo: -22.928239, -47.095333 */

        //Mudar tipo de exibição do mapa
        mMap.setMapType(GoogleMap.MAP_TYPE_NORMAL);

        LatLng casaRodrigo = new LatLng(-22.928239, -47.095333);

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