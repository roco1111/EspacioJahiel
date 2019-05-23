package com.rosario.hp.espaciojahiel.Fragment;

import android.app.DatePickerDialog;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.rosario.hp.espaciojahiel.Adaptadores.arcangelAdapter;
import com.rosario.hp.espaciojahiel.Adaptadores.imagenAdapter;
import com.rosario.hp.espaciojahiel.Adaptadores.mensajeCanalizadoAdapter;
import com.rosario.hp.espaciojahiel.Entidades.arcangel;
import com.rosario.hp.espaciojahiel.R;
import com.rosario.hp.espaciojahiel.include.Constantes;
import com.rosario.hp.espaciojahiel.include.VolleySingleton;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;

public class arcangelGeneralFragment extends Fragment {
    private static final String TAG = arcangelGeneralFragment.class.getSimpleName();
    public static final String ARG_ARTICLES_NUMBER = "arcangel";
    private ArrayList<arcangel> datos;
    private ImageView imageViewArcangel;
    private arcangelAdapter ArcangelAdapter;
    private TextView mensaje_arcangel;
    private TextView titulo_arcangel;
    Calendar c1 = Calendar.getInstance();
    private JsonObjectRequest myRequest;
    private String ls_cod_usuario;
    private String ls_fecha;
    private Button buttonVer;
    private ImageButton buttonFecha;
    private TextView fecha_nac;
    DatePickerDialog datePickerDialog;
    Calendar c;
    private RecyclerView arcangel;
    private RecyclerView.LayoutManager lManager;
    private Integer dia;

    public arcangelGeneralFragment(){}

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.activity_arcangel_general, container, false);

        mensaje_arcangel = v.findViewById(R.id.textViewtexto);
        titulo_arcangel = v.findViewById(R.id.textViewTitulo);
        imageViewArcangel = v.findViewById(R.id.imageViewArcangel);
        buttonVer = v.findViewById(R.id.buttonVer);
        fecha_nac = v.findViewById(R.id.editTextFechaNac);
        buttonFecha = v.findViewById(R.id.buttonFecha);
        arcangel = v.findViewById(R.id.reciclador);
        arcangel.setHasFixedSize(true);
        // Usar un administrador para LinearLayout
        lManager = new LinearLayoutManager(getActivity());
        arcangel.setLayoutManager(lManager);
        datos = new ArrayList<>();

        datos.clear();
        arcangel arc = new arcangel("","Atención","Debe ingresar una fecha de nacimiento","");
        datos.add(arc);

        ArcangelAdapter = new arcangelAdapter(datos,getContext(),0);
        // Setear adaptador a la lista
        arcangel.setAdapter(ArcangelAdapter);

        buttonVer.setOnClickListener(new View.OnClickListener() {
                                         @Override
                                         public void onClick(View v) {
                                             cargarAdaptador();
                                         }
                                     });

        buttonFecha.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String ls_fecha = fecha_nac.getText().toString();
                Date datetxt1 = null;

                if(ls_fecha.equals("")) {
                    datetxt1 = Calendar.getInstance().getTime();
                }else {
                    SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy");

                    try {
                        datetxt1 = sdf.parse(ls_fecha);
                    } catch (ParseException e) {
                        e.printStackTrace();
                    }
                }
                c = Calendar.getInstance();
                c.setTime(datetxt1);
                int mYear = c.get(Calendar.YEAR); // current year
                int mMonth = c.get(Calendar.MONTH); // current month
                int mDay = c.get(Calendar.DAY_OF_MONTH); // current day

                // date picker dialog
                datePickerDialog = new DatePickerDialog(getContext(),
                        new DatePickerDialog.OnDateSetListener() {

                            @Override
                            public void onDateSet(DatePicker view, int year,
                                                  int monthOfYear, int dayOfMonth) {
                                // set day of month , month and year value in the edit text
                                fecha_nac.setText(dayOfMonth + "/"
                                        + (monthOfYear + 1) + "/" + year);

                            }
                        }, mYear, mMonth, mDay);
                datePickerDialog.show();
            }
        });

        cargarAdaptador();

        return v;
    }
    public void cargarAdaptador() {
        // Petición GET


        ls_fecha = fecha_nac.getText().toString();

        if (ls_fecha.equals("") || ls_fecha.equals("0000-00-00")){
            datos.clear();
            arcangel arc = new arcangel("","Atención","Debe ingresar una fecha de nacimiento","");
            datos.add(arc);

            ArcangelAdapter = new arcangelAdapter(datos,getContext(),0);
            // Setear adaptador a la lista
            arcangel.setAdapter(ArcangelAdapter);
        }else {

            SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy");
            Date datetxt1 = null;
            Calendar selectedDate1 = Calendar.getInstance();
            try {
                datetxt1 = sdf.parse(ls_fecha);
            } catch (ParseException e) {
                e.printStackTrace();
            }
            selectedDate1.setTime(datetxt1);

            dia = selectedDate1.get(Calendar.DAY_OF_WEEK);

            String newURL = Constantes.GET_ARCANGEL_FECHA + "?id=" + String.valueOf(dia);
            VolleySingleton.
                    getInstance(getActivity()).
                    addToRequestQueue(
                            new JsonObjectRequest(
                                    Request.Method.GET,
                                    newURL,
                                    null,
                                    new Response.Listener<JSONObject>() {

                                        @Override
                                        public void onResponse(JSONObject response) {
                                            // Procesar la respuesta Json
                                            procesarRespuesta(response);
                                        }
                                    },
                                    new Response.ErrorListener() {
                                        @Override
                                        public void onErrorResponse(VolleyError error) {
                                            Log.d(TAG, "Error Volley: " + error.toString());
                                        }
                                    }

                            )
                    );
        }
    }
    private void procesarRespuesta(JSONObject response) {
        try {
            // Obtener atributo "estado"
            String estado = response.getString("estado");

            switch (estado) {
                case "1": // EXITO

                    JSONArray mensaje = response.getJSONArray("arcangel");

                    datos.clear();

                    for(int i = 0; i < mensaje.length(); i++)
                    {JSONObject object = mensaje.getJSONObject(i);
                        arcangel arc = new arcangel("","","","");

                        String id = object.getString("id");

                        arc.setId(id);

                        String titulo = object.getString("titulo");

                        arc.setTitulo(titulo);

                        String mensaje_dia = object.getString("mensaje");

                        arc.setMensaje(mensaje_dia);

                        datos.add(arc);

                    }

                    ArcangelAdapter = new arcangelAdapter(datos,getContext(),dia);
                    // Setear adaptador a la lista
                    arcangel.setAdapter(ArcangelAdapter);


                    break;
                case "2": // FALLIDO


                    break;
            }

        } catch (JSONException e) {
            Log.d(TAG, e.getMessage());
        }

    }


}
