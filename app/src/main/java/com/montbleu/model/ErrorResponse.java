package com.montbleu.model;

import java.util.ArrayList;

public class ErrorResponse {

        private String code;

        private ArrayList<Messages> messages;

        public String getCode ()
        {
            return code;
        }

        public void setCode (String code)
        {
            this.code = code;
        }

        public ArrayList<Messages> getMessages ()
        {
            return messages;
        }

        public void setMessages (ArrayList<Messages> messages)
        {
            this.messages = messages;
        }

        @Override
        public String toString()
        {
            return "ClassPojo [code = "+code+", messages = "+messages+"]";
        }



    public class Messages
    {
        private String fieldName;

        private String message;

        public String getFieldName ()
        {
            return fieldName;
        }

        public void setFieldName (String fieldName)
        {
            this.fieldName = fieldName;
        }

        public String getMessage ()
        {
            return message;
        }

        public void setMessage (String message)
        {
            this.message = message;
        }

        @Override
        public String toString()
        {
            return "ClassPojo [fieldName = "+fieldName+", message = "+message+"]";
        }
    }


}
