package com.res.trocadejogos.Views;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.core.app.ActivityCompat;
import androidx.fragment.app.FragmentActivity;

import android.Manifest;
import android.app.Activity;
import android.content.Context;
import android.content.DialogInterface;
import android.content.pm.PackageManager;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.util.Log;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.res.trocadejogos.Config.Permission;
import com.res.trocadejogos.R;

import java.io.IOException;
import java.util.List;
import java.util.Locale;

public class MapaFragmento extends SupportMapFragment implements OnMapReadyCallback {

    private GoogleMap mMap;
    private Context context;
    private LocationManager locationManager;
    private LocationListener locationListener;
    private String[] permissoes = new String[]{
            Manifest.permission.ACCESS_FINE_LOCATION
    };


    public MapaFragmento(Context context) {
        this.context = context;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        //Validar Permissões
        Permission.validarPermissoes(permissoes, (Activity) context, 1);
        getMapAsync(this);
    }

    @Override
    public void onMapReady(GoogleMap googleMap) {

        //Variável do mapa
        mMap = googleMap;

        //Mudar tipo de exibição do mapa
        mMap.setMapType(GoogleMap.MAP_TYPE_NORMAL);
        /* Latitude e Longitude - Casa Samuel: -22.960772, -47.193555
           Latitude e Longitude - Casa Rodrigo: -22.928239, -47.095333 */

        //Objeto responsável por gerenciar a localização do usuário
        locationManager = (LocationManager) context.getSystemService(Context.LOCATION_SERVICE);

        locationListener = new LocationListener() {

            @Override
            public void onLocationChanged(Location location) {
                Log.d("localização", "onLocationChanged: " + location.toString());

                Double latitude = location.getLatitude();
                Double longitude = location.getLongitude();

                //Limpando marcadores antes de adicionar
                mMap.clear();

                LatLng localUsuario = new LatLng(latitude, longitude);

                //Adicionando marcador no mapa
                mMap.addMarker(
                        new MarkerOptions()
                                .position(localUsuario)
                                .title("Local Usuário")
                                .snippet("Meu local")
                                .icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_GREEN))
                        //.icon(BitmapDescriptorFactory.fromResource(R.drawable.icon_chat_48px))
                );
                //Definindo zoom ao abrir mapa
                mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(localUsuario, 15));



            }

            @Override
            public void onStatusChanged(String provider, int status, Bundle extras) {
            }

            @Override
            public void onProviderEnabled(String provider) {
            }

            @Override
            public void onProviderDisabled(String provider) {
            }
        };

        /*
         * 1) Provedor da localização
         * 2) Tempo mínimo entre atualizações de localização (milissegundos)
         * 3) Distância mínima entre as atualizações de localização (metros)
         * 4) Location listener (para recebermos as atualizações)
         * */
        if (ActivityCompat.checkSelfPermission(context, Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED) {
            locationManager.requestLocationUpdates(
                    LocationManager.GPS_PROVIDER,
                    0,
                    0,
                    locationListener
            );
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);

        for (int permissaoResultado : grantResults) {

            //Permissão negada (denied) X Permissão concedida (granted)
            if (permissaoResultado == PackageManager.PERMISSION_DENIED) {
                //Alerta
                alertaValidarPermissao();
            } else if (permissaoResultado == PackageManager.PERMISSION_GRANTED) {
                //Recuperar localização do usuário

                /*
                 * 1) Provedor da localização
                 * 2) Tempo mínimo entre atualizações de localização (milissegundos)
                 * 3) Distância mínima entre as atualizações de localização (metros)
                 * 4) Location listener (para recebermos as atualizações)
                 * */
                if (ActivityCompat.checkSelfPermission(context, Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED) {
                    locationManager.requestLocationUpdates(
                            LocationManager.GPS_PROVIDER,
                            0,
                            0,
                            locationListener
                    );
                }
            }
        }
    }

    private void alertaValidarPermissao() {

        AlertDialog.Builder builder = new AlertDialog.Builder(context);
        builder.setTitle("Permissões Negadas");
        builder.setMessage("Para utilizar o app é necessário aceitar as permissões!");
        builder.setCancelable(false);
        builder.setPositiveButton("CONFIRMAR", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {

            }
        });

        AlertDialog dialog = builder.create();
        dialog.show();
    }

}