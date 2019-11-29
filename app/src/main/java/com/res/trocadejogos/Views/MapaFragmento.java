package com.res.trocadejogos.Views;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import android.Manifest;
import android.app.Activity;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;

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
    private DatabaseReference databaseReference;
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

        //Validar Permissões

        Permission.validarPermissoes(permissoes, (Activity) context, 1);
        getMapAsync(this);

        //identificadorUsuario = FirebaseUser.getIdentificadorUsuario();
        databaseReference = ConfigFirebase.getFirebaseDatabase();
        identificadorUsuario = FirebaseUser.getIdentificadorUsuario();


    }

    @Override
    public void onMapReady(GoogleMap googleMap) {


        mMap = googleMap;
        mMap.setMapType(GoogleMap.MAP_TYPE_NORMAL);
        mMap.setInfoWindowAdapter(new CustomInfoWindowAdapter(context));
        mMap.setMyLocationEnabled(true);

        mMap.setOnInfoWindowClickListener(new GoogleMap.OnInfoWindowClickListener() {
            @Override
            public void onInfoWindowClick(Marker marker) {

                String tag = marker.getTag().toString();
                String nome = marker.getTitle();
                Intent it = new Intent(context, Chat.class);
                it.putExtra("id", tag);
                it.putExtra("nome", nome);
                startActivity(it);

            }
        });


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
    }

    private void pegarDados(final List<Game> gameMarker){

        new Thread(new Runnable() {
            @Override
            public void run() {

        for(final Game game:gameMarker){

                    databaseReference.child("users").child(game.getIdOwner()).addListenerForSingleValueEvent(new ValueEventListener() {
                        @Override
                        public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                            String nome = dataSnapshot.child("nome").getValue(String.class);
                            String cep = dataSnapshot.child("cep").getValue(String.class);

                            getLatLon(nome,cep,game.getVenda(),game.getTroca(),game.getIdOwner());
                        }

                        @Override
                        public void onCancelled(@NonNull DatabaseError databaseError) {

                        }
                    });

                }
            }
        }).start();
        }


    private void getLatLon(final String nome, final String cep, final String vender, final String trocar, final String id) {

        new Thread(new Runnable() {
            @Override
            public void run() {

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


                try {

                    Geocoder geocoder = new Geocoder(context, Locale.getDefault());

                    List<Address> listaEndereco = geocoder.getFromLocationName(cep, 1);
                    while (listaEndereco.size()==0) {
                        listaEndereco = geocoder.getFromLocationName(cep, 1);
                    }

                    Address endereco = listaEndereco.get(0);

                    Double lat = endereco.getLatitude();
                    Double lon = endereco.getLongitude();

                    LatLng local = new LatLng(lat, lon);

                    mark(local, venda, troca, nome, id);


                } catch (IOException e) {
                    e.printStackTrace();
                }

            }
        }).start();
    }

    private void mark(final LatLng lalo, final String ven, final String tro, final String nome, final String id){

        Handler mainHandler = new Handler(context.getMainLooper());
        Runnable myRunnable = new Runnable() {
            @Override
            public void run(){

                if(!id.equals(identificadorUsuario)) {

                    Marker mark = mMap.addMarker(
                            new MarkerOptions()
                                    .position(lalo)
                                    .title(nome)
                                    .snippet("Venda: " + ven + " Troca: " + tro)
                                    .icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_GREEN))
                    );

                    mark.setTag(id);
                }
            }
        };
        mainHandler.post(myRunnable);

    }

}