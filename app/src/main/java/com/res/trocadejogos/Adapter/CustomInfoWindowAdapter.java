package com.res.trocadejogos.Adapter;

import android.view.View;
import android.widget.Button;

import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.model.Marker;
import com.res.trocadejogos.R;

public class CustomInfoWindowAdapter implements GoogleMap.InfoWindowAdapter {




    private void rendowWindowText(Marker marker,View view){

        Button conversar = (Button) view.findViewById(R.id.conversar);

    }


    @Override
    public View getInfoWindow(Marker marker) {
        return null;
    }

    @Override
    public View getInfoContents(Marker marker) {
        return null;
    }
}
