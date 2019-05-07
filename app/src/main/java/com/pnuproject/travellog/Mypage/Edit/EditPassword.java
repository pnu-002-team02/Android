package com.pnuproject.travellog.Mypage.Edit;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.TextInputLayout;
import android.support.v7.app.AlertDialog;
import android.support.v4.app.Fragment;
import android.support.v7.widget.AppCompatEditText;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.toolbox.Volley;
import com.pnuproject.travellog.R;

import org.json.JSONException;
import org.json.JSONObject;


import com.pnuproject.travellog.R;

public class EditPassword extends Fragment {

    public void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
    }

    TextInputLayout inputPW;
    TextInputLayout inputPW_check;


    private boolean validate = false;

    public View onCreateview(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        View view = inflater.inflate(R.layout.fragment_edit_password,container, false);

        inputPW = (TextInputLayout)view.findViewById(R.id.edit_password_ti_PW);
        inputPW_check = (TextInputLayout)view.findViewById(R.id.edit_password_ti_PW_check);

        final AppCompatEditText editPW = (AppCompatEditText)view.findViewById(R.id.edit_password_te_PW);
        final AppCompatEditText editPW_check = (AppCompatEditText)view.findViewById(R.id.edit_password_te_PW_check);


        final Button btn_ID_check = (Button) view.findViewById(R.id.btn_IDcheck);
        final Button btn_edit_password_OK_ = (Button) view.findViewById(R.id.btn_edit_password_OK);
        final Button btn_edit_password_cancle = (Button) view.findViewById(R.id.btn_edit_password_cancle);

        editPW.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) { }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) { }

            @Override
            public void afterTextChanged(Editable s) {
                if(!s.toString().matches("^[A-z|0-9|!@.#$%^&*?+-_~]*.{8,20}$")){
                    inputPW.setError("8-20자로 입력해주세요.");
                }
                else {
                    inputPW.setError(null);
                }
            }
        });

        //패스워드 일치 비교
        editPW_check.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) { }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if(!editPW.getText().toString().equals(editPW_check.getText().toString())) {
                    inputPW_check.setError("비밀번호가 일치하지 않습니다.");
                } else {
                    inputPW_check.setError(null);
                }
            }

            @Override
            public void afterTextChanged(Editable s) { }
        });

        btn_edit_password_OK_.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                String usrPW = editPW.getText().toString();
                String usrPW_check = editPW_check.getText().toString();

                Response.Listener<String> responseListener = new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        try {
                            JSONObject jsonResponse = new JSONObject(response);
                            boolean success = jsonResponse.getBoolean("success");
                            if(success) {
                                AlertDialog.Builder builder = new AlertDialog.Builder(SignupActivity.this);
                                builder.setMessage("회원 등록에 성공했습니다.")
                                        .setPositiveButton("확인", null)
                                        .create()
                                        .show();
                                Intent intent = new Intent(SignupActivity.this, LoginActivity.class);
                                SignupActivity.this.startActivity(intent);
                            }
                            else
                            {
                                AlertDialog.Builder builder = new AlertDialog.Builder(SignupActivity.this);
                                builder.setMessage("회원 등록에 실패했습니다.")
                                        .setNegativeButton("다시 시도", null)
                                        .create()
                                        .show();
                            }
                        }
                        catch(JSONException e)
                        {
                            e.printStackTrace();
                        }
                    }
                };
                EditPasswordRequest editPasswordRequest = new EditPasswordRequest(usrPW, responseListener);
                RequestQueue queue = Volley.newRequestQueue(SignupActivity.this);
                queue.add(editPasswordRequest);

                if(usrPW.equals("")||usrPW_check.equals("")){
                    AlertDialog.Builder builder = new AlertDialog.Builder(SignupActivity.this);
                    builder.setMessage("입력하지 않은 항목이 존재합니다.")
                            .setNegativeButton("확인", null)
                            .create()
                            .show();
                    return;
                }


                Intent intent = new Intent(SignupActivity.this, LoginActivity.class);
                SignupActivity.this.startActivity(intent);
            }
        });

        btn_edit_password_cancle.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
    }
}
