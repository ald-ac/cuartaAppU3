package com.appsmoviles.cuartaappu3;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.Toast;

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

    //solicitar permisos de llamada primera vez
    final int MY_PERMISSIONS_REQUEST_CALL_PHONE = 10;
    String primeraLlamada;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        lvContactos = findViewById(R.id.lv_contactos);

        //Consultar contactos a API y llenar arraylist -> listview
        Tarea t = new Tarea();
        t.execute("http://huasteco.tiburcio.mx/~dam17090994/consultar.php");

        //Programar click
        lvContactos.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Intent llamadaIntent = new Intent(Intent.ACTION_CALL);
                llamadaIntent.setData(Uri.parse("tel:"+contactos.get(position).getTelefono()));
                //Si ya se tienen permisos, iniciar llamada
                if (ActivityCompat.checkSelfPermission(MainActivity.this,
                        Manifest.permission.CALL_PHONE) == PackageManager.PERMISSION_GRANTED) {
                    startActivity(llamadaIntent);
                } else{ //Sino se tienen permisos, solicitarlos
                    //Guardar numero a marcar despues del permiso
                    primeraLlamada = contactos.get(position).getTelefono();
                    //Dialogo para permiso
                    ActivityCompat.requestPermissions(MainActivity.this, new String[]{ Manifest.permission.CALL_PHONE}, MY_PERMISSIONS_REQUEST_CALL_PHONE);
                }
            }
        });
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
            //Establecer adaptador con arrayContacto ya lleno
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

    @Override
    public void onRequestPermissionsResult(int requestCode, String permissions[], int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        switch (requestCode) { //Cuando se pida permiso
            case MY_PERMISSIONS_REQUEST_CALL_PHONE: { //y sea de llamada entonces
                if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    // PERMISO CONCEDIDO
                    Toast.makeText(getApplicationContext(), "PERMISO CONCEDIDO", Toast.LENGTH_SHORT).show();
                    Intent llamadaIntent = new Intent(Intent.ACTION_CALL);
                    llamadaIntent.setData(Uri.parse("tel:"+primeraLlamada));
                    primeraLlamada = null;
                    startActivity(llamadaIntent);
                } else { // PERMISO DENEGADO
                    Toast.makeText(getApplicationContext(), "PERMISO DENEGADO", Toast.LENGTH_SHORT).show();
                }
                return;
            }
        }
    }
}