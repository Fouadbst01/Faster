package com.example.faster;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.Toast;

import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.budiyev.android.codescanner.AutoFocusMode;
import com.budiyev.android.codescanner.CodeScanner;
import com.budiyev.android.codescanner.CodeScannerView;
import com.budiyev.android.codescanner.DecodeCallback;
import com.budiyev.android.codescanner.ScanMode;
import com.example.faster.Data.Classe;
import com.example.faster.Data.Element;
import com.example.faster.Data.Etudiant;
import com.example.faster.Data.Professeur;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.android.material.progressindicator.CircularProgressIndicator;
import com.google.zxing.Result;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.net.Socket;
import java.sql.Date;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


public class ScannerviewActivity extends AppCompatActivity {
    private CircularProgressIndicator cr;
    private CodeScanner mCodeScanner;
    private List<String> list;
    private Button fin;
    public int a =0;
    private String lastValue;

    private Element e =null;
    private Classe c= null;

    private boolean flag = false;

    ArrayList<String> cnes;
    List<Etudiant> etudiants;
    @Override
    public void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        getSupportActionBar().hide();
        setContentView(R.layout.scannerview);

        Bundle extras = getIntent().getExtras();
        ObjectMapper mapper = new ObjectMapper();
        try {
            e = mapper.readValue(extras.get("element").toString(),
                    new TypeReference<Element>() {});
            c = mapper.readValue(extras.get("classe").toString(),
                    new TypeReference<Classe>() {});
        } catch (JsonProcessingException e) {
            e.printStackTrace();
        }
        cr = findViewById(R.id.progressc);


        cr.setProgress(100,true);
        fin = findViewById(R.id.fin);

        CodeScannerView ScannerView =(CodeScannerView) findViewById(R.id.codeScannerView);

        list=new ArrayList<>();

        mCodeScanner = new CodeScanner(this,ScannerView);

        mCodeScanner.setDecodeCallback(new DecodeCallback() {
            @Override
            public void onDecoded(@NonNull Result result) {
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        String res = result.getText().toLowerCase();
                        if (cnes.contains(res)) {
                            flag=true;
                            Toast.makeText(ScannerviewActivity.this, "Done !!", Toast.LENGTH_SHORT).show();
                            a++;
                            cr.setProgress(a,true);
                            etudiants.remove(cnes.indexOf(res));
                            cnes.remove(res);
                        }
                    }
                });
            }
        });

        loadStudent();

        mCodeScanner.setFlashEnabled(true);
        mCodeScanner.setScanMode(ScanMode.CONTINUOUS);
        mCodeScanner.setAutoFocusEnabled(true);
        mCodeScanner.setAutoFocusMode(AutoFocusMode.SAFE);


        mCodeScanner.startPreview();

        cr.setProgress(0);
        fin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                try {
                    Intent I = new Intent(view.getContext(),ShowEtActivity.class);
                    ObjectMapper mapper = new ObjectMapper();
                    String list = mapper.writeValueAsString(etudiants);
                    I.putExtra("students",list);
                    String el = mapper.writeValueAsString(e);
                    I.putExtra("element",el);
                    finish();
                    startActivity(I);
                }catch (Exception e){
                    System.out.println(e.getStackTrace());
                }
            }
        });


    }

    @Override
    protected void onResume() {
        super.onResume();
        mCodeScanner.startPreview();
    }

    @Override
    protected void onPause() {
        mCodeScanner.releaseResources();
        super.onPause();
    }

    private void loadStudent(){
        ObjectMapper mapper = new ObjectMapper();
        Socket socket = null;
        Map<String, List<Etudiant>> m;
        cnes=new ArrayList<>();
        try {
            socket = new Socket("192.168.0.109",8083);
            DataOutputStream out = new DataOutputStream(socket.getOutputStream());
            DataInputStream din = new DataInputStream(socket.getInputStream());
            Request request= new Request("ETUDIANT");
            request.findAll().Where(new HashMap<String,String>(){
                {
                    put("id_class",String.valueOf(c.getId_class()));
                }
            });
            out.writeUTF(mapper.writeValueAsString(request));
            m = mapper.readValue(din.readUTF(),
                    new TypeReference<Map<String, List<Etudiant>>>() {});
            List<String> classes= new ArrayList<>();
            etudiants=m.get("body");
            for (Etudiant et:etudiants){
                cnes.add(et.getCne().toLowerCase());
            }
            cr.setMax(etudiants.size());
            out.close();
            din.close();
            socket.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }



}
