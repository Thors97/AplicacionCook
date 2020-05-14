package com.example.myapplication;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.WebView;
import android.widget.ArrayAdapter;
import android.widget.CheckBox;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;

import androidx.annotation.NonNull;

import java.util.ArrayList;
import java.util.List;

/**
 * Para el desarrollo de este adapter me he basado en lo comentado en la siguiente entrada de StackOverflkow
 * Fuente: https://stackoverflow.com/questions/21833181/arrayadapter-text-and-image
 */
public class BuscarRecetasAdapter extends ArrayAdapter<Receta> {

    Context context;
    List<Receta> items;

    public BuscarRecetasAdapter(Context context, int resourceId, List<Receta> items) {
        super(context, resourceId, items);
        this.context = context;
        this.items = items;
    }

    private class ViewHolder {
        ImageView imageView;
        TextView txtTitle;
        TextView txtCalorias;
    }

    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder holder = null;
        Receta receta = getItem(position);

        LayoutInflater mInflater = (LayoutInflater) context
                .getSystemService(Activity.LAYOUT_INFLATER_SERVICE);
        if (convertView == null) {
            convertView = mInflater.inflate(R.layout.lista_recetas, null);
            holder = new ViewHolder();
            holder.txtTitle = (TextView) convertView.findViewById(R.id.listText);
            holder.imageView = (ImageView) convertView.findViewById(R.id.listImage);
            holder.txtCalorias = (TextView) convertView.findViewById(R.id.listCalorias);
            convertView.setTag(holder);
        } else
            holder = (ViewHolder) convertView.getTag();


        holder.txtTitle.setText(receta.getNombre());
        holder.imageView.setImageBitmap(receta.getImagen());
        holder.txtCalorias.setText("CALORIAS:  " + receta.getCalorias());
        return convertView;
    }

    public String getLink(int position) {
        return items.get(position).getUrl();
    }
}



