package com.xyz.exceptions;

public class UnknownParamException extends UnsupportedOperationException{
    private char unknownParam;

    public UnknownParamException(String message, char unknownParam) {
        super(message);
        this.unknownParam = unknownParam;
    }

    public char getUnknownParam() {
        return unknownParam;
    }
}
