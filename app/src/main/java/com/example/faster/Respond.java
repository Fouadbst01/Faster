package com.example.faster;

import java.io.Serializable;

public class Respond implements Serializable {
    static int SUCCESSFUL =200;
    static int NOT_FOUND =404;
    static int DATA_BASE_PROBLEM = 502;

    private int status;
    private Object body;

    public Respond(int status){
        this.status = status;
        this.body = null;
    }

    public Respond(int status, Object body){
        this.status = status;
        this.body = body;
    }

    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }

    public Object getBody() {
        return body;
    }

    public void setBody(Object body) {
        this.body = body;
    }
}
