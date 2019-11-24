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
import android.widget.Toast;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.ValueEventListener;
import com.res.trocadejogos.Config.ConfigFirebase;
import com.res.trocadejogos.Config.FirebaseUser;
import com.res.trocadejogos.Config.Permission;
import com.res.trocadejogos.R;

import java.io.IOException;
import java.util.List;
import java.util.Locale;

public class MapaFragmento extends SupportMapFragment implements OnMapReadyCallback {

    private GoogleMap mMap;
    private Context context;
    private String identificadorUsuario;
    private String cepNumber;
    private DatabaseReference databaseReference;
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

        identificadorUsuario = FirebaseUser.getIdentificadorUsuario();
        databaseReference = ConfigFirebase.getFirebaseDatabase();

        DatabaseReference refCep = databaseReference.child("users").child(identificadorUsuario).child("cep");
        refCep.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                String cep = dataSnapshot.getValue(String.class);
                cepNumber = cep;
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

        //Validar Permissões
        Permission.validarPermissoes(permissoes, (Activity) context, 1);
        getMapAsync(this);
    }

    @Override
    public void onMapReady(GoogleMap googleMap) {

        mMap = googleMap;
        mMap.setMapType(GoogleMap.MAP_TYPE_NORMAL);

        Geocoder geocoder = new Geocoder(context, Locale.getDefault());

        try {
            String stringEndereco = cepNumber;
            List<Address> listaEndereco = geocoder.getFromLocationName(stringEndereco, 1);

            if (listaEndereco != null && listaEndereco.size() > 0) {
                Address endereco = listaEndereco.get(0);

                Log.d("local", "onLocationChanged: " + endereco.toString());

                Double lat = endereco.getLatitude();
                Double lon = endereco.getLongitude();

                LatLng localUsuario = new LatLng(lat, lon);

                mMap.addMarker(
                        new MarkerOptions()
                                .position(localUsuario)
                                .title("Local Usuário")
                                .snippet("Casa Rod")
                                .icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_GREEN))
                );
                mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(localUsuario, 15));
            }
        } catch (IOException e) {
            e.printStackTrace();
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