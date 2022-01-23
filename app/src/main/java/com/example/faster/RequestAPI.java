package com.example.faster;

import java.io.ObjectOutputStream;
import java.io.Serializable;
import java.net.Socket;

public class RequestAPI implements Serializable {
    public static Request request= new Request();

    public static Respond send(){
        Respond respond=null;
        Socket socket = null;
        try {
            socket = new Socket("192.168.0.109",8083);
            ObjectOutputStream out = new ObjectOutputStream(socket.getOutputStream());
            //ObjectInputStream ois = new ObjectInputStream(socket.getInputStream());
            out.writeObject(request);
            //respond = (Respond) ois.readObject();
            out.flush();
            socket.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return respond;
    }
}
