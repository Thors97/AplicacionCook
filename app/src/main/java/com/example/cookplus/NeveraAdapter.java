package com.example.cookplus;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.CheckBox;

import androidx.annotation.NonNull;

import com.example.cookplus.R;

import java.util.List;

/* Para el desarrollo de este adapter me he basado en el siguiente vídeo
 * Fuente: https://www.youtube.com/watch?v=742V81aJ75o
 */

public class NeveraAdapter extends ArrayAdapter<NeveraItem> {
    public NeveraAdapter(Context context, int resource, List objects) {
        super(context, resource, objects);
    }

    /* Funcion que llama el listView cuando le quiere pedir al adaptador una
     * de las "pastillas" de la lista
     */
    @NonNull
    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        View resultado = convertView;

        //Si no hay "pastillas" previas, las creamos
        if(resultado == null){
            LayoutInflater inflater = LayoutInflater.from(getContext());
            //el inflater devuelve un view
            resultado = inflater.inflate(R.layout.nevera_item, null);
        }

        CheckBox checkBox = (CheckBox) resultado.findViewById(R.id.neveraItem);
        NeveraItem item = getItem(position);

        checkBox.setText(item.getText());
        checkBox.setChecked(item.isChecked());

        return resultado;
    }
}
