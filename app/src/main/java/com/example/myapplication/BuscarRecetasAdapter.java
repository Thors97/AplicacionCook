package com.example.myapplication;

import android.app.Activity;
import android.content.Context;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.Arrays;
import java.util.List;

/**
 * Esta clase es la encargada de configurar el XML en el que se van a mostrar las recetas.
 * Para el desarrollo de este adapter me he basado en lo comentado en la siguiente entrada de StackOverflkow
 * Fuente: https://stackoverflow.com/questions/21833181/arrayadapter-text-and-image
 *
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
        TextView txtIngredientes;
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
            holder.txtIngredientes = (TextView) convertView.findViewById(R.id.listIngredientes);
            convertView.setTag(holder);
        } else
            holder = (ViewHolder) convertView.getTag();


        holder.txtTitle.setText(receta.getNombre());
        holder.imageView.setImageBitmap(receta.getImagen());
        holder.txtCalorias.setText("CALORIAS:" + "\n" + receta.getCalorias().substring(0, 7));
        String ingredientes = receta.getIngredientes().toString().substring(1, receta.getIngredientes().toString().length() - 1);
        holder.txtIngredientes.setText("INGREDIENTES: " + "\n" + ingredientes);

        return convertView;
    }

    /**
     * Esta funcion es la encargada de devolver la URL al Activity para que se pueda
     * abrir la receta en el navegador.
     *
     * @param position
     * @return String URL
     * Esta pagina me ha ayudado a implementar esta función:
     * https://stackoverflow.com/questions/36810210/listview-custom-adapter-onclick-launch-url-link
     */
    public String getLink(int position) {
        return items.get(position).getUrl();
    }
}



