package com.pnuproject.travellog.Login.Controller;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.Toast;
import com.kakao.auth.ISessionCallback;
import com.kakao.auth.Session;
import com.kakao.util.exception.KakaoException;
import com.kakao.util.helper.log.Logger;
import com.pnuproject.travellog.Login.Model.LoginRetrofitInterface;
import com.pnuproject.travellog.Login.Model.RequestDataLogin;
import com.pnuproject.travellog.Login.Model.ResponseDataLogin;
import com.pnuproject.travellog.Main.MainActivity.Controller.MainActivity;
import com.pnuproject.travellog.R;
import com.pnuproject.travellog.Signup.Controller.SignupActivity;
import com.pnuproject.travellog.etc.RetrofitTask;
import com.pnuproject.travellog.etc.TLApp;
import java.net.ConnectException;
import java.net.UnknownHostException;
import retrofit2.Retrofit;
import static android.content.Intent.FLAG_ACTIVITY_NO_HISTORY;

public class LoginActivity extends Activity implements RetrofitTask.RetrofitExecutionHandler{
    private ISessionCallback callback;
    private RetrofitTask retrofitTask;

    final static private int RETROFIT_TASK_ERROR = 0x00;
    final static private int RETROFIT_TASK_LOGIN = 0x01;
    @Override
    protected void onCreate(final Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        retrofitTask = new RetrofitTask(this, getResources().getString(R.string.server_address));
        setContentView(R.layout.activity_login);

        final EditText inputID = (EditText) findViewById(R.id.login_te_ID);
        final EditText inputPW = (EditText) findViewById(R.id.login_te_PW);
        LinearLayout btn_signup = (LinearLayout) findViewById(R.id.btn_goto_signup);
        Button btn_login = (Button) findViewById(R.id.btn_login_ok);

        //회원가입 액티비티 이동
        btn_signup.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent signupIntent = new Intent(LoginActivity.this, SignupActivity.class);
                signupIntent.addFlags(FLAG_ACTIVITY_NO_HISTORY);
                startActivity(signupIntent);
            }
        });

        //로그인 구현
        btn_login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                final String usrID = inputID.getText().toString();
                final String usrPW = inputPW.getText().toString();
                RequestDataLogin dataSignup = new RequestDataLogin(usrID, usrPW);

                RetrofitTask.RetrofitRequestParam requestParam = new RetrofitTask.RetrofitRequestParam(RETROFIT_TASK_LOGIN, dataSignup);
                retrofitTask.execute(requestParam);
            }
        });

        callback = new SessionCallback();
        Session.getCurrentSession().addCallback(callback);
        Session.getCurrentSession().checkAndImplicitOpen();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (Session.getCurrentSession().handleActivityResult(requestCode, resultCode, data)) {
            return;
        }
        super.onActivityResult(requestCode, resultCode, data);
    }
    @Override
    protected void onDestroy() {
        super.onDestroy();
        Session.getCurrentSession().removeCallback(callback);
    }

    @Override
    public void onAfterAyncExcute(RetrofitTask.RetrofitResponseParam response7) {
        if (response7 == null || response7.getResponse() == null) {
            runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    Toast.makeText(getBaseContext(), getResources().getString(R.string.errmsg_retrofit_unknown), Toast.LENGTH_SHORT).show();
                }
            });

            return;
        } else if( response7.getTaskNum() == RETROFIT_TASK_ERROR) {
            final String errMsg = (String)response7.getResponse();
            runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    Toast.makeText(getBaseContext(), errMsg, Toast.LENGTH_SHORT).show();
                }
            });

            return;
        }

        int taskNum = response7.getTaskNum();
        Object responseData = response7.getResponse();

        switch (taskNum) {
            case RETROFIT_TASK_LOGIN: {
                final ResponseDataLogin res = (ResponseDataLogin) responseData;
                if (res.getSuccess() != 0) {
                    Toast.makeText(getBaseContext(), res.getMessage(), Toast.LENGTH_SHORT).show();

                } else {
                    Toast.makeText(getBaseContext(), res.getUsername() + "님 반갑습니다", Toast.LENGTH_SHORT).show();
                    TLApp.setUserInfo(new TLApp.UserInfo(res.getUsername(),res.getUserID()));
                    Intent intent = new Intent(LoginActivity.this, MainActivity.class);
                    intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
                    startActivity(intent);
                }
            }
                break;
            default:
                break;
        }
    }

    @Override
    public Object onBeforeAyncExcute(Retrofit retrofit, RetrofitTask.RetrofitRequestParam paramRequest7) {
        Object response = null;
        int taskNum = paramRequest7.getTaskNum();
        Object requestParam = paramRequest7.getParamRequest();
        LoginRetrofitInterface loginRetrofit = retrofit.create(LoginRetrofitInterface.class);

        try {
            switch (taskNum) {
                case RETROFIT_TASK_LOGIN:
                    response = loginRetrofit.doLogin((RequestDataLogin)requestParam).execute().body();
                    break;
                default:
                    break;
            }
        }
        catch (UnknownHostException ex) {
            paramRequest7.setTaskNum(RETROFIT_TASK_ERROR);
            response = new String(getResources().getString(R.string.errmsg_retrofitbefore_ownernetwork));
        }
        catch (ConnectException ex) {
            paramRequest7.setTaskNum(RETROFIT_TASK_ERROR);
            response = new String(getResources().getString(R.string.errmsg_retrofitbefore_servernetwork));
        }
        catch (Exception ex) {
            paramRequest7.setTaskNum(RETROFIT_TASK_ERROR);
            response = new String(getResources().getString(R.string.errmsg_retrofit_unknown));
        }

        return response;
    }

    private class SessionCallback implements ISessionCallback {
        @Override
        public void onSessionOpened() {
            redirectSignupActivity();
        }
        @Override
        public void onSessionOpenFailed(KakaoException exception) {
            if (exception != null) {
                Logger.e(exception);
            }
        }
    }
    protected void redirectSignupActivity() {
        final Intent intent = new Intent(this, LoginActivity.class);
        startActivity(intent);
        finish();
    }
}
