package com.example.faster;

import android.os.StrictMode;

import androidx.annotation.NonNull;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.Serializable;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.atomic.AtomicInteger;

public class Request{
    private String type;
    private String Table;
    private Map<String,Map<String,String>> body;

    public Request() {
        body=new HashMap<>();
    }
    public Request(String tableName) {
        body=new HashMap<>();
        Table = tableName;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getTable() {
        return Table;
    }

    public void setTable(String table) {
        Table = table;
    }

    public Map<String,Map<String,String>> getBody() {
        return body;
    }

    public void setBody(Map<String,Map<String,String>> body) {
        this.body = body;
    }

    //FOR CLIENT SIDE
    public Request findAll(){
        this.type="GET";
        return this;
    }


    public Request Where(Map<String,String> map){
        body.put("WHERE",map);
        return this;
    }

    public Request Select(Map<String,String> map){
        body.put("Filed",map);
        return this;
    }

    public void POST(Map<String,String> map){
        this.type="POST";
        body.put("DATA",map);
    }

    public Request PUT(Map<String,String> map){
        this.type="PUT";
        body.put("DATA",map);
        return this;
    }

    public Request DELETE(){
        this.type="DELETE";
        return this;
    }

}
