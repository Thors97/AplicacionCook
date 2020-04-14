package com.example.myapplication;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.CheckBox;
import android.widget.LinearLayout;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;


public class BuscarRecetasAdapter extends ArrayAdapter<Receta> {

    public BuscarRecetasAdapter(Context context, int resource, List objects) {
        super(context, resource, objects);
    }

    public View getView(int position, View convertView, ViewGroup parent) {

        // Get the data item for this position
        View resultado = convertView;
        Receta receta = getItem(position);
        // Check if an existing view is being reused, otherwise inflate the view
        if (resultado == null) {
            resultado = LayoutInflater.from(getContext()).inflate(R.layout.lista_recetas, parent, false);
        }
        // Lookup view for data population
        TextView listText = (TextView) convertView.findViewById(R.id.listText);

        // Populate the data into the template view using the data object
        listText.setText(receta.getNombre());

        // Return the completed view to render on screen
        return resultado;
    }

}
