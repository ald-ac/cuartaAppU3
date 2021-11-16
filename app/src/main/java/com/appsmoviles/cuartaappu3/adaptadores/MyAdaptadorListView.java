package com.appsmoviles.cuartaappu3.adaptadores;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.appsmoviles.cuartaappu3.R;
import com.appsmoviles.cuartaappu3.modelos.Contacto;

import java.util.ArrayList;
//Establecer que extiende de un ArrayAdapter<Paciente>
public class MyAdaptadorListView extends ArrayAdapter<Contacto> {
    Context context;
    ArrayList<Contacto> Contactos;

    public MyAdaptadorListView(Context c, ArrayList<Contacto> contactos) {
        super(c, R.layout.row_list_view, R.id.textView1, contactos);
        this.context = c;
        this.Contactos = contactos;
    }

    @NonNull
    @Override //Renderizando vista con row_list_view.xml
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        LayoutInflater layoutInflater = (LayoutInflater)context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View row = layoutInflater.inflate(R.layout.row_list_view, parent, false);
        TextView miTelefono = row.findViewById(R.id.textView1);
        TextView miNombre = row.findViewById(R.id.textView2);
        TextView miCiudad = row.findViewById(R.id.textView3);

        miTelefono.setText(Contactos.get(position).getTelefono());
        miNombre.setText(Contactos.get(position).getNombre());
        miCiudad.setText(Contactos.get(position).getCiudad());

        return row;
    }
}
