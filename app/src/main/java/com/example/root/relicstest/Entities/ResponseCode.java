package com.example.root.relicstest.Entities;

public class ResponseCode {


    public String code;

    public String msg;

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        code = code;
    }

    public String getMsg() {
        return msg;
    }

    public void setMsg(String msg) {
        this.msg = msg;
    }

    @Override
    public String toString() {
        return "Code:" +
                code +
                "\n" + "Msg:" + msg;
    }
}
