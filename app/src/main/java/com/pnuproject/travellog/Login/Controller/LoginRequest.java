package com.pnuproject.travellog.Login.Controller;

import com.android.volley.Response;
import com.android.volley.toolbox.StringRequest;

import java.util.HashMap;
import java.util.Map;

public class LoginRequest extends StringRequest {
    final static private String URL = "";
    private Map<String, String> parameters;

    public LoginRequest(String usrID, String usrPW, Response.Listener<String> listener) {
        super(Method.POST, URL, listener, null);
        parameters = new HashMap<>();
        parameters.put("userID", usrID);
        parameters.put("userPW", usrPW);
    }

    @Override
    public Map<String, String> getParams() {
        return parameters;
    }
}
