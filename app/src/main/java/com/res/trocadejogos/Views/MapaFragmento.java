package com.res.trocadejogos.Views;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.core.app.ActivityCompat;

import android.Manifest;
import android.app.Activity;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Address;
import android.location.Geocoder;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.ValueEventListener;
import com.res.trocadejogos.Adapter.CustomInfoWindowAdapter;
import com.res.trocadejogos.Classes.Game;
import com.res.trocadejogos.Config.ConfigFirebase;
import com.res.trocadejogos.Config.FirebaseUser;
import com.res.trocadejogos.Config.Permission;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

public class MapaFragmento extends SupportMapFragment implements OnMapReadyCallback {

    private List<Game> gameMarker = new ArrayList<>();
    private GoogleMap mMap;
    private Context context;
    private String selectedGame;
    private String identificadorUsuario;
    private String cepNumber;
    private DatabaseReference databaseReference;
    private LocationManager locationManager;
    private LocationListener locationListener;
    private String[] permissoes = new String[]{
            Manifest.permission.ACCESS_FINE_LOCATION
    };

    public MapaFragmento(Context context, String selectedGame) {
        this.context = context;
        this.selectedGame = selectedGame;
    }


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        identificadorUsuario = FirebaseUser.getIdentificadorUsuario();
        databaseReference = ConfigFirebase.getFirebaseDatabase();

        databaseReference.child("gameOwners").child(selectedGame).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                gameMarker.clear();
                for(DataSnapshot gameSnapShot:dataSnapshot.getChildren()){
                    gameMarker.add(gameSnapShot.getValue(Game.class));
                }
                pegarDados(gameMarker);

            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

        databaseReference.child("users").child(identificadorUsuario).child("cep").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
               cepNumber = dataSnapshot.getValue(String.class);
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
        mMap.setInfoWindowAdapter(new CustomInfoWindowAdapter(context));

        Geocoder geocoder = new Geocoder(context, Locale.getDefault());

                try {

                    List<Address> listaEndereco = geocoder.getFromLocationName(cepNumber, 1);

                    while (listaEndereco.size()==0) {
                        listaEndereco = geocoder.getFromLocationName(cepNumber, 1);
                    }
                        Address endereco = listaEndereco.get(0);

                        Double lat = endereco.getLatitude();
                        Double lon = endereco.getLongitude();

                        LatLng localUsuario = new LatLng(lat, lon);

                        mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(localUsuario, 10));

                        mMap.setOnInfoWindowClickListener(new GoogleMap.OnInfoWindowClickListener() {
                            @Override
                            public void onInfoWindowClick(Marker marker) {
                                Intent it = new Intent(context, Conversas.class);
                                startActivity(it);
                            }
                        });


                } catch (IOException e) {
                    e.printStackTrace();
                }
    }

    private void pegarDados(List<Game> gameMarker){

        for(final Game game:gameMarker){

            databaseReference.child("users").child(game.getIdOwner()).addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                     String nome = dataSnapshot.child("nome").getValue(String.class);
                     String cep = dataSnapshot.child("cep").getValue(String.class);

                     mark(nome,cep,game.getVenda(),game.getTroca());
                }

                @Override
                public void onCancelled(@NonNull DatabaseError databaseError) {

                }
            });
        }
    }

    private void mark(String nome, String cep, String vender, String trocar)  {

        String venda;
        String troca;

        if(vender.equals("1")){
            venda = "SIM";
        }else {
            venda = "NÃO";
        }

        if(trocar.equals("1")){
            troca = "SIM";
        }else {
            troca = "NÃO";
        }

        Geocoder geocoder = new Geocoder(context, Locale.getDefault());

        try {

            List<Address> listaEndereco = geocoder.getFromLocationName(cep, 1);
            while (listaEndereco.size()==0) {
                listaEndereco = geocoder.getFromLocationName(cep, 1);
            }
                Address endereco = listaEndereco.get(0);

                Double lat = endereco.getLatitude();
                Double lon = endereco.getLongitude();

                LatLng local = new LatLng(lat, lon);

                mMap.addMarker(
                        new MarkerOptions()
                                .position(local)
                                .title(nome)
                                .snippet("Venda: " + venda + " Troca: " + troca)
                                .icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_GREEN))
                );

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