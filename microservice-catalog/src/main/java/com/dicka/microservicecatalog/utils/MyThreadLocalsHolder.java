package com.dicka.microservicecatalog.utils;

public class MyThreadLocalsHolder {

    private static final ThreadLocal<String> CORELATION_ID = new ThreadLocal<>();

    public static void setCorelationId(String correlationId){
        CORELATION_ID.set(correlationId);
    }

    public static String getCorrelationId(){
        return CORELATION_ID.get();
    }
}
