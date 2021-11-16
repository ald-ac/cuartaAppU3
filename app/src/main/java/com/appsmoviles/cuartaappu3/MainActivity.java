package com.appsmoviles.cuartaappu3;

import androidx.appcompat.app.AppCompatActivity;

import android.os.AsyncTask;
import android.os.Bundle;
import android.widget.ListView;

import com.appsmoviles.cuartaappu3.adaptadores.MyAdaptadorListView;
import com.appsmoviles.cuartaappu3.modelos.Contacto;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {
    //GUI
    ListView lvContactos;
    ArrayList<Contacto> contactos = new ArrayList<Contacto>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        lvContactos = findViewById(R.id.lv_contactos);

        //Consultar contactos a API y llenar arraylist -> listview
        Tarea t = new Tarea();
        t.execute("http://huasteco.tiburcio.mx/~dam17090994/consultar.php");
    }

    class Tarea extends AsyncTask<String, Void, String> {
        @Override
        protected String doInBackground(String... strings) {
            //Enviar direccion http de consulta
            return ConexionWeb(strings[0]);
        }

        @Override
        protected void onPostExecute(String s) {
            //Llenar arraylist contactos
            try {
                //Obtener arrayjson del string
                JSONArray arrayContactos = new JSONArray(s);
                for(int i=0; i < arrayContactos.length(); i++) {
                    JSONObject con = arrayContactos.getJSONObject(i);
                    contactos.add(new Contacto(con.getString("id_tel"), con.getString("nombre"), con.getString("ciudad")));
                }
            } catch (JSONException e) {
                e.printStackTrace();
            }

            //Establecer adaptador
            MyAdaptadorListView adaptadorContactos = new MyAdaptadorListView(getApplicationContext(), contactos);
            //Llenar listView
            lvContactos.setAdapter(adaptadorContactos);

            super.onPostExecute(s);
        }
    }

    String ConexionWeb(String direccion) {
        String pagina="";
        try {
            URL url = new URL(direccion);
            HttpURLConnection conexion = (HttpURLConnection) url.openConnection();

            if (conexion.getResponseCode() == HttpURLConnection.HTTP_OK) {
                BufferedReader reader = new BufferedReader(new
                        InputStreamReader(conexion.getInputStream()));
                String linea = reader.readLine();
                while (linea != null) {
                    pagina += linea + "\n";
                    linea = reader.readLine();
                }
                reader.close();

            } else {
                pagina += "ERROR: " + conexion.getResponseMessage() + "\n";
            }
            conexion.disconnect();
        }
        catch (Exception e){
            pagina+=e.getMessage();
        }
        return pagina;
    }
}