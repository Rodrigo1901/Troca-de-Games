package com.res.trocadejogos.Views;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;

import android.Manifest;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.widget.Toast;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.res.trocadejogos.Config.Permission;
import com.res.trocadejogos.R;

import java.io.IOException;
import java.util.List;
import java.util.Locale;

public class Mapa extends AppCompatActivity implements OnMapReadyCallback {

    private long backPressedTime;
    private Toast backToast;
    private BottomNavigationView bottonNav;
    private GoogleMap mMap;
    private String[] permissoes = new String[]{
            Manifest.permission.ACCESS_FINE_LOCATION
    };
    private LocationManager locationManager;
    private LocationListener locationListener;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_mapa);

        androidx.appcompat.widget.Toolbar toolbar = findViewById(R.id.toolbar);
        toolbar.setTitle("Mapa");
        setSupportActionBar(toolbar);

        //Validar Permissões
        Permission.validarPermissoes(permissoes, this, 1);

        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);

        bottonNav = findViewById(R.id.bottom_navigation);
        bottonNav.setSelectedItemId(R.id.menu_map);
        bottonNav.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem menuItem) {
                switch (menuItem.getItemId()){
                    case R.id.menu_library:
                        Intent it1 = new Intent(Mapa.this, Biblioteca.class);
                        startActivity(it1);
                        break;
                    case R.id.menu_chat:
                        Intent it2 = new Intent(Mapa.this, Conversas.class);
                        startActivity(it2);
                        break;
                }
                return true;
            }
        });

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {

        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.menu_mapa, menu);

        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public void onBackPressed() {

        if (backPressedTime + 2000 > System.currentTimeMillis()) {
            backToast.cancel();
            moveTaskToBack(true);
            return;

        } else {
            backToast = Toast.makeText(Mapa.this, "Aperte novamente para sair", Toast.LENGTH_SHORT);
            backToast.show();
        }
        backPressedTime = System.currentTimeMillis();
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {

        switch (item.getItemId()) {

            case R.id.action_pesquisar:
                finish();
                break;
        }
        return super.onOptionsItemSelected(item);
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
        locationManager = (LocationManager) this.getSystemService(Context.LOCATION_SERVICE);

        locationListener = new LocationListener() {

            @Override
            public void onLocationChanged(Location location) {
                Log.d("localização", "onLocationChanged: " + location.toString());

                Double latitude = location.getLatitude();
                Double longitude = location.getLongitude();

                //Limpando marcadores antes de adicionar
                mMap.clear();

                /*
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
                 */

                /*
                Geocoding -> processo de transformar um endereço ou descrição de um local em latitude/longitude
                Reverse Geocoding -> processo de transformar latitude/longitude em um endereço
                */
                Geocoder geocoder = new Geocoder(getApplicationContext(), Locale.getDefault());

                try {
                    /* Reverse Geocoding */
                    // List<Address> listaEndereco = geocoder.getFromLocation(latitude, longitude, 1);

                    /* Geocoding */
                    String stringEndereco = "R. Dr. Silvio Carvalhaes, 150 - Jardim Pauliceia, Campinas - SP";
                    List<Address> listaEndereco = geocoder.getFromLocationName(stringEndereco, 1);

                    if (listaEndereco != null && listaEndereco.size() > 0) {
                        Address endereco = listaEndereco.get(0);

                        Log.d("local", "onLocationChanged: " + endereco.toString());
                        /* RETORNO DO LOG:
                         *
                         * onLocationChanged:
                         * Address[
                         *   addressLines=[0:"R. Dr. Silvio Carvalhaes, 150 - Jardim Pauliceia, Campinas - SP, 13060, Brazil"],
                         *   feature=150,
                         *   admin=São Paulo,
                         *   sub-admin=Campinas,
                         *   locality=null,
                         *   thoroughfare=Rua Doutor Silvio Carvalhaes,
                         *   postalCode=13060,
                         *   countryCode=BR,
                         *   countryName=Brazil,
                         *   hasLatitude=true,
                         *   latitude=-22.9282583,
                         *   hasLongitude=true,
                         *   longitude=-47.0953078,
                         *   phone=null,
                         *   url=null,
                         *   extras=null]
                         */

                        Double lat = endereco.getLatitude();
                        Double lon = endereco.getLongitude();

                        LatLng localUsuario = new LatLng(lat, lon);

                        //Adicionando marcador no mapa
                        mMap.addMarker(
                                new MarkerOptions()
                                        .position(localUsuario)
                                        .title("Local Usuário")
                                        .snippet("Casa Rod")
                                        .icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_GREEN))
                                //.icon(BitmapDescriptorFactory.fromResource(R.drawable.icon_chat_48px))
                        );
                        //Definindo zoom ao abrir mapa
                        mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(localUsuario, 15));
                    }
                } catch (IOException e) {
                    e.printStackTrace();
                }
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
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED) {
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
                if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED) {
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

        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Permissões Negadas");
        builder.setMessage("Para utilizar o app é necessário aceitar as permissões!");
        builder.setCancelable(false);
        builder.setPositiveButton("CONFIRMAR", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                finish();
            }
        });

        AlertDialog dialog = builder.create();
        dialog.show();
    }
}
