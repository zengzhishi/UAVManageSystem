package com.zlion.controller;

import java.util.HashMap;

/**
 * Created by zzs on 2016/9/4.
 */
public class Result extends HashMap<String, Object> {
    public Result(){
        put("Code", 100);
        put("Msg", "OK");
    }

    public Result okForList(){
        put("Code", 200);
        return this;
    }

    public Result argError(){
        put("Code", 102);
        put("Msg", "Illegal Arguments");
        return this;
    }

    public Result passError(){
        put("Code","103");
        put("Msg","User or Password Error");
        return this;
    }

    public Result illegalMethod(){
        put("Code", 104);
        put("Msg", "Illegal Action Parameters");
        return this;
    }

    public Result needAuth(){
        put("Code", 101);
        put("Msg", "Need Auth");
        return this;
    }

}

