package com.example.faster;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.os.StrictMode;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.example.faster.Data.Compte;
import com.example.faster.Data.Professeur;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.Serializable;
import java.net.Socket;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


public class MainActivity extends AppCompatActivity{
    transient Button logButton;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        getSupportActionBar().hide();
        setContentView(R.layout.activity_main);

        EditText login =(EditText) findViewById(R.id.InputEmail);
        EditText passwordt = (EditText) findViewById(R.id.InputPassword);

        logButton = (Button) findViewById(R.id.button_login);

        EditText finalLogin = login;
        logButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
                StrictMode.setThreadPolicy(policy);


                String username = login.getText().toString();
                String password = passwordt.getText().toString();
                Map<String, List<Compte>> m;

                try {
                    ObjectMapper mapper = new ObjectMapper();
                    Socket socket = new Socket("192.168.0.109",8083);
                    DataOutputStream out = new DataOutputStream(socket.getOutputStream());
                    DataInputStream din = new DataInputStream(socket.getInputStream());
                    Request request= new Request("compte");
                    request.findAll().Where(new HashMap<String,String>(){
                        {
                            put("username","'"+username+"'");
                            put("pswd","'"+password+"'");
                        }
                    });

                    out.writeUTF(mapper.writeValueAsString(request));
                    m = mapper.readValue(din.readUTF(),
                            new TypeReference<Map<String, List<Compte>>>() {});

                    if(m.get("body").isEmpty()){
                        Toast.makeText(MainActivity.this,"incorecte usernam or password",Toast.LENGTH_SHORT).show();
                    }else{
                        Socket socket2 = new Socket("192.168.0.109",8083);
                        DataOutputStream out2 = new DataOutputStream(socket2.getOutputStream());
                        DataInputStream din2 = new DataInputStream(socket2.getInputStream());
                        Request request2= new Request("professeur");
                        request2.findAll().Where(new HashMap<String,String>(){
                            {
                                put("Id_cmpt", String.valueOf(m.get("body").get(0).getId_cmpt()));
                            }
                        });
                        out2.writeUTF(mapper.writeValueAsString(request2));
                        Map<String, List<Professeur>> m2= mapper.readValue(din2.readUTF(),
                                new TypeReference<Map<String, List<Professeur>>>() {});
                        Professeur p = m2.get("body").get(0);
                        System.out.println(p);
                        out.flush();
                        socket.close();
                        Intent intent = new Intent(view.getContext(), HomeActivity.class);
                        String ps = mapper.writeValueAsString(p);
                        intent.putExtra("prof", ps);
                        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                        startActivity(intent);
                    }



                } catch (Exception e) {
                    e.printStackTrace();
                }


                //respond = (Respond) ois.readObject();


                //Respond respond = Request.Send(request);
                //Toast.makeText(MainActivity.this,respond.getStatus(),Toast.LENGTH_SHORT).show();
            }
        });


    }


}