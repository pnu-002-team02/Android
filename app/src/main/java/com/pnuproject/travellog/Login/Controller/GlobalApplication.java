package com.pnuproject.travellog.Login.Controller;

import android.app.Application;
import android.content.Context;

import com.kakao.auth.ApprovalType;
import com.kakao.auth.AuthType;
import com.kakao.auth.IApplicationConfig;
import com.kakao.auth.ISessionConfig;
import com.kakao.auth.KakaoAdapter;

public class GlobalApplication extends Application {
    private static GlobalApplication instance;
    public static final GlobalApplication getGlobalApplicationContext() {
        if (instance == null)
            throw new IllegalStateException("this application does not inherit com.kakao.GlobalApplication");
        return instance;
    }
    public static class KakaoSDKAdapter extends KakaoAdapter {

        @Override
        public ISessionConfig getSessionConfig() {
            return new ISessionConfig() {
                @Override
                public AuthType[] getAuthTypes() {
                    return new AuthType[]{AuthType.KAKAO_LOGIN_ALL};
                }
                @Override
                public boolean isUsingWebviewTimer() {
                    return false;
                }
                @Override
                public boolean isSecureMode() {
                    return false;
                }
                @Override
                public ApprovalType getApprovalType() {
                    return ApprovalType.INDIVIDUAL;
                }
                @Override
                public boolean isSaveFormData() {
                    return true;
                }
            };
        }
        @Override
        public IApplicationConfig getApplicationConfig() {
            return new IApplicationConfig() {
                @Override
                public Context getApplicationContext() {
                    return GlobalApplication.getGlobalApplicationContext();
                }
            };
        }
    }
    @Override
    public void onCreate() {
        super.onCreate();
        instance = this;
    }
}
