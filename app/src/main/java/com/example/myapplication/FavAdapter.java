package com.example.myapplication;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.CheckBox;
import android.widget.ImageButton;
import android.widget.TextView;

import androidx.annotation.NonNull;

import java.util.List;

class FavAdapter extends ArrayAdapter<FavItem> {
    public FavAdapter(Context context, int resource, List objects) {
        super(context, resource, objects);
    }

    @NonNull
    @Override
    /*funcion que llama el listView cuando le quiere pedir al adaptador una de las "pastillas"
     * de la lista*/
    public View getView(int position, View convertView, ViewGroup parent) {

        View resultado = convertView;

        //Si no hay "pastillas" previas, las creamos
        if (resultado == null) {
            LayoutInflater inflater = LayoutInflater.from(getContext());
            //el inflater devuelve un view
            resultado = inflater.inflate(R.layout.fav_item, null);
        }

        // añado el texto al item de la lista
        FavItem item = getItem(position);
        TextView text = (TextView) resultado.findViewById(R.id.textItem);
        text.setText(item.getTitulo());

        //ImageButton btn = (ImageButton) resultado.findViewById(R.id.expand_button);

        return resultado;
    }
}
