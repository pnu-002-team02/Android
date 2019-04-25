package com.pnuproject.travellog.Signup.Controller;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.TextInputLayout;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.AppCompatEditText;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.Button;

import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.toolbox.Volley;
import com.pnuproject.travellog.Login.Controller.LoginActivity;
import com.pnuproject.travellog.R;

import org.json.JSONException;
import org.json.JSONObject;

public class SignupActivity extends AppCompatActivity {
    TextInputLayout inputID;
    TextInputLayout inputPW;
    TextInputLayout inputPW_check;

    private AlertDialog dialog;
    private boolean validate = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_signup);

        inputID = (TextInputLayout)findViewById(R.id.signup_ti_ID);
        inputPW = (TextInputLayout)findViewById(R.id.signup_ti_PW);
        inputPW_check = (TextInputLayout)findViewById(R.id.signup_ti_PW_check);

        final AppCompatEditText editName = (AppCompatEditText)findViewById(R.id.signup_te_Name);
        final AppCompatEditText editID = (AppCompatEditText)findViewById(R.id.signup_te_ID);
        final AppCompatEditText editPW = (AppCompatEditText)findViewById(R.id.signup_te_PW);
        final AppCompatEditText editPW_check = (AppCompatEditText)findViewById(R.id.signup_te_PW_check);
        final AppCompatEditText editDOB = (AppCompatEditText)findViewById(R.id.signup_te_DOB);

        final Button btn_ID_check = (Button) findViewById(R.id.btn_IDcheck);
        final Button btn_signup_OK = (Button) findViewById(R.id.btn_signup_OK);
        final Button btn_signup_cancle = (Button) findViewById(R.id.btn_signup_cancle);

        //ID 이메일 형식 체크
        editID.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) { }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) { }

            @Override
            public void afterTextChanged(Editable s) {
                if(!s.toString().matches("^[A-z|0-9]([A-z|0-9]*)(@)([A-z]*)(\\.)([A-z]*)$")){
                    inputID.setError("이메일 형식으로 입력해주세요.");
                }
                else {
                    inputID.setError(null);
                }
            }
        });

        //패스워드 문자길이 체크
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

        //ID 중복확인
        btn_ID_check.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final String usrID = editID.getText().toString();
                if(validate){
                    return;
                }
                if(usrID.equals("")){
                    AlertDialog.Builder builder = new AlertDialog.Builder(SignupActivity.this);
                    builder.setMessage("아이디를 입력해주십시오.")
                            .setPositiveButton("OK", null)
                            .create()
                            .show();
                    return;
                }

                Response.Listener<String> responseListener = new Response.Listener<String>(){
                    @Override
                    public void onResponse(String response) {
                        try{
                            JSONObject jsonResponse = new JSONObject(response);
                            boolean success = jsonResponse.getBoolean("success");
                            if(success){//사용할 수 있는 아이디라면
                                AlertDialog.Builder builder = new AlertDialog.Builder(SignupActivity.this);
                                builder.setMessage("사용할 수 있는 ID입니다.")
                                        .setPositiveButton("확인", null)
                                        .create()
                                        .show();
                                editID.setEnabled(false);
                                validate = true;
                            }
                            else
                            {
                                AlertDialog.Builder builder = new AlertDialog.Builder(SignupActivity.this);
                                builder.setMessage("이미 존재하는 ID입니다.")
                                        .setNegativeButton("확인", null)
                                        .create()
                                        .show();
                            }
                        }
                        catch(Exception e){
                            e.printStackTrace();
                        }
                    }
                };
                ValidateRequest validateRequest = new ValidateRequest(usrID, responseListener);
                RequestQueue queue = Volley.newRequestQueue(SignupActivity.this);
                queue.add(validateRequest);
            }
        });

        //회원가입 등록
        btn_signup_OK.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String usrName = editName.getText().toString();
                String usrID = editID.getText().toString();
                String usrPW = editPW.getText().toString();
                String usrPW_check = editPW_check.getText().toString();
                String usrDOB = editDOB.getText().toString();

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
                SignupRequest signupRequest = new SignupRequest(usrName, usrID, usrPW, usrDOB, responseListener);
                RequestQueue queue = Volley.newRequestQueue(SignupActivity.this);
                queue.add(signupRequest);

                if(usrName.equals("")||usrID.equals("")||usrPW.equals("")||usrPW_check.equals("")||usrDOB.equals("")){
                    AlertDialog.Builder builder = new AlertDialog.Builder(SignupActivity.this);
                    builder.setMessage("입력하지 않은 항목이 존재합니다.")
                            .setNegativeButton("확인", null)
                            .create()
                            .show();
                    return;
                }
                if(!validate){
                    AlertDialog.Builder builder = new AlertDialog.Builder(SignupActivity.this);
                    builder.setMessage("ID 중복확인을 해주세요.")
                            .setNegativeButton("확인", null)
                            .create()
                            .show();
                    return;
                }

                Intent intent = new Intent(SignupActivity.this, LoginActivity.class);
                SignupActivity.this.startActivity(intent);
            }
        });

        btn_signup_cancle.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
    }
}
