package com.projectandroid.kevinjimezb.appcrtour;

import android.content.Context;
import android.content.Intent;
import android.support.v4.app.FragmentActivity;
import android.os.Bundle;
import android.text.Html;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptor;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

public class CRMap extends FragmentActivity implements OnMapReadyCallback, GoogleMap.InfoWindowAdapter {

    //Context & API
    Context context;
    private GoogleMap googleMap;

    //JsonCoordenate
    private RequestQueue requestQueue;
    private static final String url = "http://192.168.1.10:8080/APICrTour/empCoordenada.php";
    private StringRequest request;

    //Others
    private double latitude;
    private double longitude;
    private int idEmpresa;
    private String nombre;
    private static int MARKERS_COUNTER = 0;

    public CRMap() {
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_crmap);
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);

        context = this.getApplicationContext();

        requestQueue = Volley.newRequestQueue(this);

    }

    @Override
    public void onMapReady(GoogleMap mMap) {
        googleMap = mMap;
        mMap.setMapType(GoogleMap.MAP_TYPE_HYBRID);
        mMap.getUiSettings().setZoomControlsEnabled(true);
//      if (ContextCompat.checkSelfPermission(this, android.Manifest.permission.ACCESS_FINE_LOCATION) ==
//                PackageManager.PERMISSION_GRANTED &&
//                ContextCompat.checkSelfPermission(this, android.Manifest.permission.ACCESS_COARSE_LOCATION) ==
//                        PackageManager.PERMISSION_GRANTED) {
//            googleMap.setMyLocationEnabled(true);
//            googleMap.getUiSettings().setMyLocationButtonEnabled(true);
//
//        } else {
//            Toast.makeText(this, "The location permission is not enabled right now!", Toast.LENGTH_LONG).show();
//        }
        getCoordenateJson();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.Salir:
                finish();
                break;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public View getInfoWindow(Marker marker) {
        return null;
    }

    @Override
    public View getInfoContents(Marker marker) {

        View myContentView = getLayoutInflater().inflate(R.layout.popupmaps, null);

        TextView tv = (TextView) myContentView.findViewById(R.id.Lat);
        tv.setText(Html.fromHtml("Lat:" + "<b><font color=\"black\">" + rint((latitude-Integer.parseInt(marker.getTitle())),1000) + "</b></font>"));
        tv = (TextView) myContentView.findViewById(R.id.title);
        tv.setText(marker.getTitle());
        tv = (TextView) myContentView.findViewById(R.id.Lon);
        tv.setText(Html.fromHtml("Lon:" + "<b><font color=\"black\">" + rint((longitude-Integer.parseInt(marker.getTitle())),1000) + "</b></font>"));

        return myContentView;
    }

    public double rint(double number, int significantDigit) {
        return Math.rint(number * significantDigit) / significantDigit;
    }

    //Se obtiene el id del boton presionado para cambiar de icono
    public BitmapDescriptor getTypeIcon(){
        BitmapDescriptor icon = null;
        Intent myIntent = getIntent(); // gets the previously created intent
        String markerIcon = myIntent.getStringExtra("MarkerType"); // will return "FirstKeyValue"
         switch (markerIcon) {
             case "3":
                 icon = BitmapDescriptorFactory.fromResource(R.drawable.mk_market);
                 break;
             case "1":
                 icon = BitmapDescriptorFactory.fromResource(R.drawable.mk_hotel);
                 break;
             case "2":
                 icon = BitmapDescriptorFactory.fromResource(R.drawable.mk_leaf);
                 break;
             case "5":
                 icon = BitmapDescriptorFactory.fromResource(R.drawable.mk_boat);
                 break;
             case "4":
                 icon = BitmapDescriptorFactory.fromResource(R.drawable.mk_food);
                 break;
             default:
                 icon = BitmapDescriptorFactory.fromResource(R.drawable.mk_other);
                 break;
         }

        return icon;
    }

    //Se obtiene el id del boton presionado
    public String getTypeId(){
        String id;
        Intent myIntentId = getIntent(); // gets the previously created intent
        String markerIcon = myIntentId.getStringExtra("MarkerType"); // will return "FirstKeyValue"
        switch (markerIcon) {
            case "3":
                id = "3";
                break;
            case "1":
                id = "1";
                break;
            case "2":
                id = "2";
                break;
            case "5":
                id = "5";
                break;
            case "4":
                id = "4";
                break;
            default:
                id = "6";
                break;
        }

        return id;
    }

    //metodo utilizado para obtener web services y crear marker
    public void getCoordenateJson(){

        request = new StringRequest(Request.Method.POST, url, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                try {
                    JSONObject jsonObject = new JSONObject(response);
                    if (jsonObject.names().get(0).equals("geo")) {
                        MarkerOptions markerOptions = new MarkerOptions();
                        BitmapDescriptor myIcon = getTypeIcon();
                        JSONArray parentArray = jsonObject.getJSONArray("geo");

                        for(int i=0; i < parentArray.length(); i++)
                        {
                            JSONObject finalObject = parentArray.getJSONObject(i);
                            idEmpresa = finalObject.getInt("IdEmpresa");
                            nombre = finalObject.getString("Nombre");
                            latitude = finalObject.getDouble("Latitud");
                            longitude = finalObject.getDouble("Longitud");
                            if ((finalObject.length() != 0)) {
                                markerOptions.position(new LatLng(latitude, longitude))
                                            .snippet(String.format("Marker: " + (idEmpresa-MARKERS_COUNTER)))
                                            .icon(myIcon)
                                            .title(nombre)
                                            .flat(true);
                                    googleMap.addMarker(markerOptions);
                                    MARKERS_COUNTER++;
                                }
                            MARKERS_COUNTER = 0;
                        }

                    } else {
                        Toast.makeText(getApplicationContext(), "Error message: " + jsonObject.getString("error"), Toast.LENGTH_SHORT).show();
                    }

                } catch (JSONException e) {
                    e.printStackTrace();
                }

            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {

            }

        })
        {
            @Override
            protected Map<String,String> getParams() throws AuthFailureError {
                HashMap<String, String> hashMap = new HashMap<String, String>();
                hashMap.put("id", getTypeId());
                return hashMap;
            }

        };
        requestQueue.add(request);
    }

}
