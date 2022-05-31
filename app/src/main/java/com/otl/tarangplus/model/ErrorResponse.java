package com.otl.tarangplus.model;

import java.io.Serializable;

public class ErrorResponse implements Serializable
{
    public Error error;

    public static class Error {

        public String message;
        public String code;


        public String getCode() {
            return code;
        }

        public void setCode(String code) {
            this.code = code;
        }


        public String getMessage() {
            return message;
        }

        public void setMessage(String message) {
            this.message = message;
        }


    }

}
