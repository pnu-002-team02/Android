package com.pnuproject.travellog.Mypage.Edit;

import com.android.volley.Response;
import com.android.volley.toolbox.StringRequest;

import java.util.HashMap;
import java.util.Map;

public class EditPasswordRequest extends StringRequest {
    final static private String URL = "";
    private Map<String, String> parameters;

    public EditPasswordRequest(String usrName, String usrID, String usrPW, String usrDOB, Response.Listener<String> listener) {
        super(Method.POST, URL, listener, null);
        parameters = new HashMap<>();
        parameters.put("userName", usrName);
        parameters.put("userID", usrID);
        parameters.put("userPW", usrPW);
        parameters.put("userDOB", usrDOB);
    }

    @Override
    public Map<String, String> getParams() {
        return parameters;
    }
}