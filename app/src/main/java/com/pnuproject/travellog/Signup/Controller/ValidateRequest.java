package com.pnuproject.travellog.Signup.Controller;

import com.android.volley.Response;
import com.android.volley.toolbox.StringRequest;

import java.util.HashMap;
import java.util.Map;

public class ValidateRequest extends StringRequest {
    final static private String URL = "";
    private Map<String, String> parameters;

    public ValidateRequest(String usrID, Response.Listener<String> listener) {
        super(Method.POST, URL, listener, null);
        parameters = new HashMap<>();
        parameters.put("userID", usrID);
    }

    @Override
    public Map<String, String> getParams() {
        return parameters;
    }
}