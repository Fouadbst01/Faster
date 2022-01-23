package com.example.faster;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;

import androidx.appcompat.app.AppCompatActivity;

import com.example.faster.Data.Classe;
import com.example.faster.Data.Element;
import com.example.faster.Data.Etudiant;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.net.Socket;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class ShowEtActivity extends AppCompatActivity {
    private Button done;
    private Button cancel;
    private ListView studenlist;
    private List<Etudiant> litEtu;
    private Element element;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getSupportActionBar().setTitle("Étudiants absents");
        setContentView(R.layout.show_student);

        studenlist = findViewById(R.id.studenlist);
        done = findViewById(R.id.done);
        cancel = findViewById(R.id.cancel);

        Bundle extras = getIntent().getExtras();
        ObjectMapper mapper = new ObjectMapper();
        try {
            litEtu = mapper.readValue(extras.get("students").toString(),
                    new TypeReference<List<Etudiant>>() {});
            element = mapper.readValue(extras.get("element").toString(),new TypeReference<Element>(){});
        } catch (JsonProcessingException e) {
            e.printStackTrace();
        }

        ArrayAdapter arrayAdapter = new ArrayAdapter(this,R.layout.support_simple_spinner_dropdown_item,litEtu);

        studenlist.setAdapter(arrayAdapter);


        done.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                try {
                for (int j= 0; j < litEtu.size(); j++) {
                    ObjectMapper mapper = new ObjectMapper();
                    Socket socket = new Socket("192.168.0.109", 8083);
                    DataOutputStream out = new DataOutputStream(socket.getOutputStream());
                    DataInputStream din = new DataInputStream(socket.getInputStream());
                    DateTimeFormatter dtf = DateTimeFormatter.ofPattern("yyyy/MM/dd");
                    LocalDateTime now = LocalDateTime.now();
                    Log.e("j : ",String.valueOf(j));
                    Request request = new Request("ABSENCE");
                    Map<String,String> map = new HashMap();
                    map.put("id_etu", String.valueOf(litEtu.get(j).getId_etu()));
                    map.put("id_element", String.valueOf(element.getId_element()));
                    map.put("date_abs", "'" + dtf.format(now) + "'");
                    request.POST(map);
                    out.writeUTF(mapper.writeValueAsString(request));
                    din.readUTF();
                    out.close();
                    din.close();
                    socket.close();
                }
                ObjectMapper mapper = new ObjectMapper();
                Socket socket = new Socket("192.168.0.109", 8083);
                DataOutputStream out = new DataOutputStream(socket.getOutputStream());
                DataInputStream din = new DataInputStream(socket.getInputStream());
                Request request= new Request("ELEMENT");
                request.PUT(new HashMap<String, String>(){
                    {
                        put("id_element",String.valueOf(element.getId_element()));
                        put("nbHeur",String.valueOf(element.getNbHeur()+2));
                    }
                });
                out.writeUTF(mapper.writeValueAsString(request));
                din.readUTF();
                out.close();
                din.close();
                socket.close();
                }catch (Exception e){
                    System.out.println(e.getStackTrace());
                }

                finish();
            }
        });

        cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });

    }
}
