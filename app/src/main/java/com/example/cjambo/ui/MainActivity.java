package com.example.cjambo.ui;

import android.content.Intent;
import android.os.Bundle;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.example.cjambo.R;

import androidx.appcompat.app.AlertDialog;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.core.view.GravityCompat;
import androidx.appcompat.app.ActionBarDrawerToggle;

import android.view.MenuItem;

import com.example.cjambo.auth.LoginActivity;
import com.google.android.material.navigation.NavigationView;

import androidx.drawerlayout.widget.DrawerLayout;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.RecyclerView;

import android.view.Menu;
import android.view.View;
import android.widget.Adapter;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RelativeLayout;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import okhttp3.OkHttpClient;

public class MainActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener {
    Spinner numbers,groups;
    EditText subject,message;
    Button send;
    RelativeLayout progressLyt;
    private static final String contacts = "http://cjambo.ampleshelter.com/dashboard/databaseFiles/contacts.php";
    private static final String group = "http://cjambo.ampleshelter.com/dashboard/databaseFiles/groups.php";
    private List<String>numbersArray;
    private ArrayAdapter<String> numbersAdapter;
    private List<String>groupsArray;
    private ArrayAdapter<String>groupsAdapter;
    public static final String JSON_ARRAY = "result";
    TextView chooseRecipient,chooseRecipients;
    ConstraintLayout toNumbers,toGroups;
    private JSONArray result;
    String nn;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        DrawerLayout drawer = findViewById(R.id.drawer_layout);
        numbers = findViewById(R.id.numbers);
        groups = findViewById(R.id.groups);
        chooseRecipient = findViewById(R.id.choose_recipient);
        chooseRecipients = findViewById(R.id.choose_recipients);
        toGroups = findViewById(R.id.togroups);
        toNumbers = findViewById(R.id.tonumbers);
        send = findViewById(R.id.send_message);
        progressLyt = findViewById(R.id.progressLoad);
        subject = findViewById(R.id.edit_subject);
        message = findViewById(R.id.enter_message);
        NavigationView navigationView = findViewById(R.id.nav_view);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.addDrawerListener(toggle);
        toggle.syncState();
        navigationView.setNavigationItemSelectedListener(this);
        numbersArray = new ArrayList<>();

        numbersAdapter = new ArrayAdapter<>(
                this, android.R.layout.simple_spinner_item, numbersArray);

        numbersAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

        numbers.setAdapter(numbersAdapter);
        groupsArray = new ArrayList<>();

        groupsAdapter = new ArrayAdapter<>(
                this, android.R.layout.simple_spinner_item, groupsArray);

        groupsAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

        groups.setAdapter(groupsAdapter);
        chooseRecipients.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                recipients();
            }
        });
        chooseRecipient.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                recipients();
            }
        });
        getContacts();
        getGroups();
        send.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (toNumbers.isShown() || toGroups.isShown()) {
                    String spin = tt();
                    String mess = message.getText().toString();
                    String sub = subject.getText().toString();
                    Toast.makeText(MainActivity.this, spin, Toast.LENGTH_LONG).show();
                    sendMessage(spin, mess, sub);
                }else if (toNumbers.isShown() && toGroups.isShown()){
                    String num,grp,sub,msg;
                    num = numbers.getSelectedItem().toString();
                    grp = groups.getSelectedItem().toString();
                    sub = subject.getText().toString();
                    msg = message.getText().toString();
                    Toast.makeText(MainActivity.this, num + grp, Toast.LENGTH_LONG).show();
                    sendSms(num,grp,sub,msg);
                }
            }
        });

    }

    private String tt() {
        String ss = "";
        if (toNumbers.isShown()) {
            ss = numbers.getSelectedItem().toString();
        }else if (toGroups.isShown()) {
            ss = groups.getSelectedItem().toString();
        }
        return ss;
    }

    private void recipients() {
        chooseRecipients.setVisibility(View.GONE);
        chooseRecipient.setVisibility(View.VISIBLE);
        alert();
    }

    private void alert() {
        TextView picknumbers,pickgroups,both;
        AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(MainActivity.this);
        View view = getLayoutInflater().inflate(R.layout.recipients,null);
        picknumbers = view.findViewById(R.id.pickNumbers);
        pickgroups = view.findViewById(R.id.pickGroups);
        both = view.findViewById(R.id.both);
        alertDialogBuilder.setView(view);

        final AlertDialog alertDialog = alertDialogBuilder.create();
        alertDialog.show();
        picknumbers.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                alertDialog.dismiss();
                chooseRecipient.setVisibility(View.VISIBLE);
                chooseRecipients.setVisibility(View.GONE);
                toNumbers.setVisibility(View.VISIBLE);
                toGroups.setVisibility(View.GONE);

            }
        });
        pickgroups.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                alertDialog.dismiss();
                chooseRecipient.setVisibility(View.VISIBLE);
                chooseRecipients.setVisibility(View.GONE);
                toNumbers.setVisibility(View.GONE);
                toGroups.setVisibility(View.VISIBLE);
            }
        });
        both.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                alertDialog.dismiss();
                chooseRecipient.setVisibility(View.VISIBLE);
                chooseRecipients.setVisibility(View.GONE);
                toGroups.setVisibility(View.VISIBLE);
                toNumbers.setVisibility(View.VISIBLE);
            }
        });

    }

    private void getContacts() {
        showProgress();
        StringRequest stringRequest = new StringRequest(Request.Method.POST, contacts, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                hideProgress();
                ArrayList<String> clist=new ArrayList<String>();
                JSONArray jsonArray = null;
                try {
                    jsonArray = new JSONArray(response);
                    if(jsonArray!=null){
                        for (int index=0;index<jsonArray.length();index++){
                            numbersArray.add(jsonArray.getJSONObject(index).getString("cname")+" "+jsonArray.getJSONObject(index).getString("mobile"));
                        }

                    }

                    numbersAdapter.notifyDataSetChanged();



                } catch (JSONException e) {
                    e.printStackTrace();
                    Toast.makeText(MainActivity.this,e.getMessage(),Toast.LENGTH_SHORT).show();
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                hideProgress();
                Toast.makeText(MainActivity.this,"damn",Toast.LENGTH_SHORT).show();
            }
        })
        {

            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String>params = new HashMap<>();
                params.put("email","st");
                params.put("password","gg");
                return params;
            }
        };
        RequestQueue requestQueue = Volley.newRequestQueue(this);
        requestQueue.add(stringRequest);
    }
    private void getGroups() {
        showProgress();
        StringRequest stringRequest = new StringRequest(Request.Method.POST, group, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                hideProgress();
                ArrayList<String> clist=new ArrayList<String>();
                JSONArray jsonArray = null;
                try {
                    jsonArray = new JSONArray(response);
                    if(jsonArray!=null){
                        for (int index=0;index<jsonArray.length();index++){
                            groupsArray.add(jsonArray.getJSONObject(index).getString("gname"));
                        }

                    }

                    groupsAdapter.notifyDataSetChanged();



                } catch (JSONException e) {
                    e.printStackTrace();
                    Toast.makeText(MainActivity.this,e.getMessage(),Toast.LENGTH_SHORT).show();
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                hideProgress();
                Toast.makeText(MainActivity.this,"damn",Toast.LENGTH_SHORT).show();
            }
        })
        {

            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String>params = new HashMap<>();
                params.put("email","st");
                params.put("password","gg");
                return params;
            }
        };
        RequestQueue requestQueue = Volley.newRequestQueue(this);
        requestQueue.add(stringRequest);
    }
    @Override
    public void onBackPressed() {
        DrawerLayout drawer = findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_loyalties) {
            return true;
        }else if (id == R.id.action_payments){

        }else if (id == R.id.action_help){

        }

        return super.onOptionsItemSelected(item);
    }

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();

        if (id == R.id.nav_compose) {
            // Handle the camera action
        } else if (id == R.id.nav_contacts) {

        } else if (id == R.id.nav_groups) {

        } else if (id == R.id.nav_inbox) {

        }else if (id == R.id.nav_transactions) {

        }else if (id == R.id.nav_scheduling) {
            Intent intent = new Intent(MainActivity.this, SchedulingActivity.class);
            startActivity(intent);

        } else if (id == R.id.nav_sentbox) {

        }else if (id == R.id.nav_outbox) {

        }else if (id == R.id.nav_share) {

        } else if (id == R.id.nav_send) {

        }

        DrawerLayout drawer = findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }
    private void showProgress(){
        progressLyt.setVisibility(View.VISIBLE);
    }
    private void hideProgress(){
        progressLyt.setVisibility(View.GONE);
    }
    private void sendMessage(final String spinnerText,final String subjectS, final String messageM) {
        showProgress();
        StringRequest stringRequest = new StringRequest(Request.Method.POST, contacts, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                hideProgress();
                try {
                    JSONObject jsonObject = new JSONObject(response);
                    String success = jsonObject.getString("success");
                    String message = jsonObject.getString("message");
                    if (success.equals("1")){

                        Toast.makeText(MainActivity.this,message,Toast.LENGTH_SHORT).show();
                        Intent intent = new Intent(MainActivity.this, MainActivity.class);
                        startActivity(intent);
                        finish();
                    }else{
                        Toast.makeText(MainActivity.this,message,Toast.LENGTH_SHORT).show();
                    }


                } catch (JSONException e) {
                    e.printStackTrace();
                    Toast.makeText(MainActivity.this,"catch",Toast.LENGTH_SHORT).show();
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                hideProgress();
                Toast.makeText(MainActivity.this,"damn",Toast.LENGTH_SHORT).show();
            }
        })
        {
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String>params = new HashMap<>();
                params.put("recipient",spinnerText);
                params.put("subject",subjectS);
                params.put("message",messageM);
                return params;
            }
        };
        RequestQueue requestQueue = Volley.newRequestQueue(this);
        requestQueue.add(stringRequest);
    }
    private void sendSms(final String groupP,final String numberN, final String subjectS, final String messageM) {
        showProgress();
        StringRequest stringRequest = new StringRequest(Request.Method.POST, contacts, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                hideProgress();
                try {
                    JSONObject jsonObject = new JSONObject(response);
                    String success = jsonObject.getString("success");
                    String message = jsonObject.getString("message");
                    if (success.equals("1")){

                        Toast.makeText(MainActivity.this,message,Toast.LENGTH_SHORT).show();
                        Intent intent = new Intent(MainActivity.this, MainActivity.class);
                        startActivity(intent);
                        finish();
                    }else{
                        Toast.makeText(MainActivity.this,message,Toast.LENGTH_SHORT).show();
                    }


                } catch (JSONException e) {
                    e.printStackTrace();
                    Toast.makeText(MainActivity.this,"catch",Toast.LENGTH_SHORT).show();
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                hideProgress();
                Toast.makeText(MainActivity.this,"damn",Toast.LENGTH_SHORT).show();
            }
        })
        {
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String>params = new HashMap<>();
                params.put("number",numberN);
                params.put("groups",groupP);
                params.put("subject",subjectS);
                params.put("message",messageM);
                return params;
            }
        };
        RequestQueue requestQueue = Volley.newRequestQueue(this);
        requestQueue.add(stringRequest);
    }
}
