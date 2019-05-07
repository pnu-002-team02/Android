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
import android.widget.Toast;
import com.pnuproject.travellog.Login.Controller.LoginActivity;
import com.pnuproject.travellog.R;
import com.pnuproject.travellog.Signup.Model.RequestDataSignup;
import com.pnuproject.travellog.Signup.Model.ResponseDataCheckID;
import com.pnuproject.travellog.Signup.Model.ResponseDataSignup;
import com.pnuproject.travellog.Signup.Model.SignupRetrofitInterface;
import com.pnuproject.travellog.etc.RetrofitTask;

import java.net.ConnectException;
import java.net.UnknownHostException;

import retrofit2.Retrofit;

public class SignupActivity extends AppCompatActivity implements RetrofitTask.RetrofitExecutionHandler {
    TextInputLayout inputID;
    TextInputLayout inputPW;
    TextInputLayout inputPW_check;
    AppCompatEditText editName;
    AppCompatEditText editID;
    AppCompatEditText editPW;
    AppCompatEditText editPW_check;
    AppCompatEditText editDOB;

    private final int RETROFIT_TASK_ERROR = 0x00;
    private final int RETROFIT_TASK_CHECKID = 0x01;
    private final int RETROFIT_TASK_SIGNUP = 0x02;

    private AlertDialog dialog;
    private boolean validate = false;

    private RetrofitTask retrofitTask;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_signup);
        retrofitTask = new RetrofitTask(this, getResources().getString(R.string.server_address));
        inputID = (TextInputLayout)findViewById(R.id.signup_ti_ID);
        inputPW = (TextInputLayout)findViewById(R.id.signup_ti_PW);
        inputPW_check = (TextInputLayout)findViewById(R.id.signup_ti_PW_check);

        editName = (AppCompatEditText)findViewById(R.id.signup_te_Name);
        editID = (AppCompatEditText)findViewById(R.id.signup_te_ID);
        editPW = (AppCompatEditText)findViewById(R.id.signup_te_PW);
        editPW_check = (AppCompatEditText)findViewById(R.id.signup_te_PW_check);
        editDOB = (AppCompatEditText)findViewById(R.id.signup_te_DOB);

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
                //if (validate) {
                //    return;
                //}
                if (usrID.equals("")) {
                    AlertDialog.Builder builder = new AlertDialog.Builder(SignupActivity.this);
                    builder.setMessage("아이디를 입력해주십시오.")
                            .setPositiveButton("OK", null)
                            .create()
                            .show();
                    return;
                }
                RetrofitTask.RetrofitRequestParam requestParam = new RetrofitTask.RetrofitRequestParam(RETROFIT_TASK_CHECKID, usrID);
                retrofitTask.execute(requestParam);
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

                RequestDataSignup dataSignup = new RequestDataSignup(usrName, usrID, usrPW,usrPW_check);
                RetrofitTask.RetrofitRequestParam requestParam = new RetrofitTask.RetrofitRequestParam(RETROFIT_TASK_SIGNUP, dataSignup);
                retrofitTask.execute(requestParam);
                if (usrName.equals("") || usrID.equals("") || usrPW.equals("") || usrPW_check.equals("") || usrDOB.equals("")) {
                    AlertDialog.Builder builder = new AlertDialog.Builder(SignupActivity.this);
                    builder.setMessage("입력하지 않은 항목이 존재합니다.")
                            .setNegativeButton("확인", null)
                            .create()
                            .show();
                    return;
                }
                if (!validate) {
                    AlertDialog.Builder builder = new AlertDialog.Builder(SignupActivity.this);
                    builder.setMessage("ID 중복확인을 해주세요.")
                            .setNegativeButton("확인", null)
                            .create()
                            .show();
                    return;
                }
            }
        });

        btn_signup_cancle.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }

        });
    }

    @Override
    public void onAfterAyncExcute(RetrofitTask.RetrofitResponseParam response) {
        if (response == null || response.getResponse() == null) {
            runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    Toast.makeText(getBaseContext(), getResources().getString(R.string.errmsg_retrofit_unknown), Toast.LENGTH_SHORT).show();
                }
            });

            return;
        } else if( response.getTaskNum() == RETROFIT_TASK_ERROR) {
            final String errMsg = (String)response.getResponse();
            runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    Toast.makeText(getBaseContext(), errMsg, Toast.LENGTH_SHORT).show();
                }
            });

            return;
        }

        int taskNum = response.getTaskNum();
        Object responseData = response.getResponse();

        switch (taskNum) {
            case RETROFIT_TASK_SIGNUP: {
                final ResponseDataSignup res = (ResponseDataSignup) responseData;
                if (res.getSuccess() != 0) {
                    Toast.makeText(getBaseContext(), res.getMessage(), Toast.LENGTH_SHORT).show();
                } else {
                    Toast.makeText(getBaseContext(), res.getMessage(), Toast.LENGTH_SHORT).show();
                    Intent intent = new Intent(SignupActivity.this, LoginActivity.class);
                    startActivity(intent);
                }
            }
                break;
            case RETROFIT_TASK_CHECKID: {
                final ResponseDataCheckID res = (ResponseDataCheckID) responseData;
                if (res.getSuccess() != 0) {
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            Toast.makeText(getBaseContext(), res.getMessage(), Toast.LENGTH_SHORT).show();
                            //editID.setEnabled(true);
                            validate = false;
                        }
                    });
                    return;
                } else {
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            Toast.makeText(getBaseContext(), res.getMessage(), Toast.LENGTH_SHORT).show();
                            //editID.setEnabled(false);
                            validate = true;
                        }
                    });
                }
            }
                break;
            default:
                break;
        }
    }

    @Override
    public Object onBeforeAyncExcute(Retrofit retrofit, RetrofitTask.RetrofitRequestParam paramRequest) {

        Object response = null;
        int taskNum = paramRequest.getTaskNum();
        Object requestParam = paramRequest.getParamRequest();
        SignupRetrofitInterface signupRetrofit = retrofit.create(SignupRetrofitInterface.class);

        try {
            switch (taskNum) {
                case RETROFIT_TASK_CHECKID:
                    response = signupRetrofit.checkEmail((String) requestParam).execute().body();
                    break;
                case RETROFIT_TASK_SIGNUP:
                    response = signupRetrofit.doSignup((RequestDataSignup) requestParam).execute().body();
                    break;
                default:
                    break;
            }
        }
        catch (UnknownHostException ex) {
            paramRequest.setTaskNum(RETROFIT_TASK_ERROR);
            response = new String(getResources().getString(R.string.errmsg_retrofitbefore_ownernetwork));
        }
        catch (ConnectException ex) {
            paramRequest.setTaskNum(RETROFIT_TASK_ERROR);
            response = new String(getResources().getString(R.string.errmsg_retrofitbefore_servernetwork));
        }
        catch (Exception ex) {
            paramRequest.setTaskNum(RETROFIT_TASK_ERROR);
            response = new String(getResources().getString(R.string.errmsg_retrofit_unknown));
        }

        return response;
    }
}
