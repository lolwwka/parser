package com.xyz.exceptions;

public class IllegalParamsException extends IllegalArgumentException{

    private int paramNum;

    public IllegalParamsException(String s, int number) {
        super(s);
        paramNum = number;
    }
    public int getParamNum() {
        return paramNum;
    }
}
