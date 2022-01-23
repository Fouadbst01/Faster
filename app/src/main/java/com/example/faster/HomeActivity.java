package com.example.faster;

import android.content.Intent;
import android.os.Bundle;
import android.os.PersistableBundle;
import android.view.View;
import android.view.Window;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.example.faster.Data.Classe;
import com.example.faster.Data.Compte;
import com.example.faster.Data.Element;
import com.example.faster.Data.Professeur;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.android.material.progressindicator.CircularProgressIndicator;
import com.google.android.material.textfield.TextInputLayout;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.Socket;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class HomeActivity extends AppCompatActivity {
    private TextInputLayout classe;
    private TextInputLayout matier;
    private AutoCompleteTextView classeauto;
    private AutoCompleteTextView elemnteauto;
    private Button gotoscanne;

    private TextView nbhtext;
    private CircularProgressIndicator bnhIndecatore;

    ArrayAdapter<String> adapter;
    List<Classe> classeList;

    List<Element> elementList;
    Professeur p=null;
    Classe c= null;
    Element e= null;
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //requestWindowFeature(Window.FEATURE_NO_TITLE);
        getSupportActionBar().setTitle("Home");
        setContentView(R.layout.homeview);
        Bundle extras = getIntent().getExtras();

        ObjectMapper mapper = new ObjectMapper();
        try {
            p = mapper.readValue(extras.get("prof").toString(),
                    new TypeReference<Professeur>() {});
        } catch (JsonProcessingException e) {
            e.printStackTrace();
        }

        classe = findViewById(R.id.classmenu);
        matier = findViewById(R.id.matiermenu);
        classeauto= findViewById(R.id.matiermenuautocom);
        elemnteauto = findViewById(R.id.elemntmenuautocom);
        gotoscanne = findViewById(R.id.startscanner);
        nbhtext = findViewById(R.id.nbHoursText);
        bnhIndecatore = findViewById(R.id.nbHoursProgeress);
        LoadClass();

        bnhIndecatore.setProgress(100,true);

        ((AutoCompleteTextView)classe.getEditText()).setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int position, long id) {
                c=classeList.get(position);
                elemnteauto.setText("");
                LoadMatier(p.getId_prof(),classeList.get(position).getId_class());
            }
        });

        ((AutoCompleteTextView)matier.getEditText()).setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int position, long id) {
                e=elementList.get(position);
                bnhIndecatore.setMax(e.getTotaleh());
                bnhIndecatore.setProgress(e.getNbHeur(),true);
                nbhtext.setText(e.getNbHeur()+" H");
            }
        });

        gotoscanne.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(e==null || c==null){
                    String msg = e==null?"u forget to element":"u forget to classe";
                    Toast.makeText(view.getContext(),msg,Toast.LENGTH_SHORT).show();
                }else{
                    ObjectMapper mapper = new ObjectMapper();
                    Intent intent =  new Intent(view.getContext(),ScannerviewActivity.class);
                    try {
                        String cs = mapper.writeValueAsString(c);
                        String es = mapper.writeValueAsString(e);
                        intent.putExtra("classe", cs);
                        intent.putExtra("element", es);
                        startActivity(intent);
                    } catch (JsonProcessingException jsonProcessingException) {
                        jsonProcessingException.printStackTrace();
                    }
                }
            }
        });

    }

    @Override
    protected void onResume() {
        super.onResume();
        classeauto.setText("");
        elemnteauto.setText("");
        classe.getEditText().clearFocus();
        matier.getEditText().clearFocus();
        nbhtext.setText("-- H");
        bnhIndecatore.setProgress(100,true);
    }

    void LoadClass(){
        ObjectMapper mapper = new ObjectMapper();
        Socket socket = null;
        Map<String, List<Classe>> m;
        try {
            socket = new Socket("192.168.0.109",8083);
            DataOutputStream out = new DataOutputStream(socket.getOutputStream());
            DataInputStream din = new DataInputStream(socket.getInputStream());
            Request request= new Request("CLASSEPROF");
            request.findAll().Where(new HashMap<String,String>(){
                {
                    put("id_prof","1");
                }
            });

            out.writeUTF(mapper.writeValueAsString(request));
            m = mapper.readValue(din.readUTF(),
                    new TypeReference<Map<String, List<Classe>>>() {});
            List<String> classes= new ArrayList<>();
            classeList=m.get("body");
            for (Classe c:classeList) {
                classes.add(c.getIntitule()+" "+c.getAnnee());
            }
            adapter =
                    new ArrayAdapter<String>(
                            this,
                            R.layout.support_simple_spinner_dropdown_item,
                            classes);
            classeauto.setAdapter(adapter);
        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    void LoadMatier(int idprof,int idclas){
        ObjectMapper mapper = new ObjectMapper();
        Socket socket = null;
        Map<String, List<Element>> m;
        try {
            socket = new Socket("192.168.0.109",8083);
            DataOutputStream out = new DataOutputStream(socket.getOutputStream());
            DataInputStream din = new DataInputStream(socket.getInputStream());
            Request request= new Request("CLASSEPROFElEMENT");
            request.findAll().Where(new HashMap<String,String>(){
                {
                    put("id_prof",String.valueOf(idprof));
                    put("id_class",String.valueOf(idclas));
                }
            });

            out.writeUTF(mapper.writeValueAsString(request));
            m = mapper.readValue(din.readUTF(),
                    new TypeReference<Map<String, List<Element>>>() {});

            List<String> elemnts= new ArrayList<>();
            elementList=(List<Element>) m.get("body");
            for (Element e:elementList) {
                elemnts.add(e.getIntitule());
            }
            adapter =
                    new ArrayAdapter<String>(
                            this,
                            R.layout.support_simple_spinner_dropdown_item,
                            elemnts);
            elemnteauto.setAdapter(adapter);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
