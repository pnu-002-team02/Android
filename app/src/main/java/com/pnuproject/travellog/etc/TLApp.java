package com.pnuproject.travellog.etc;

import android.app.Application;
import android.content.Context;

import com.kakao.auth.ApprovalType;
import com.kakao.auth.AuthType;
import com.kakao.auth.IApplicationConfig;
import com.kakao.auth.ISessionConfig;
import com.kakao.auth.KakaoAdapter;
import com.kakao.auth.KakaoSDK;
import com.pnuproject.travellog.Login.Controller.GlobalApplication;

public class TLApp  extends Application {
    private static TLApp obj = null;
    private static UserInfo userinfo;

    public static UserInfo getUserInfo() {
        return userinfo;
    }
    public static void setUserInfo(UserInfo userinfo) {
        TLApp.userinfo = userinfo;
    }

    @Override
    public void onCreate() {
        super.onCreate();
        obj = this;

        //카카오 연동 구현
        KakaoSDK.init(new TLApp.KakaoSDKAdapter());
    }

    public static TLApp getGlobalApplicationContext() {
        return obj;
    }

    private static class KakaoSDKAdapter extends KakaoAdapter {
        @Override
        public IApplicationConfig getApplicationConfig() {
            return new IApplicationConfig() {
                @Override
                public Context getApplicationContext() {
                    return TLApp.getGlobalApplicationContext();
                }
            };
        }

        /**
         * Session Config에 대해서는 default값들이 존재한다.
         * 필요한 상황에서만 override해서 사용하면 됨.
         *
         * @return Session의 설정값.
         */
        @Override
        public ISessionConfig getSessionConfig() {
            return new ISessionConfig() {
                @Override
                public AuthType[] getAuthTypes() {
                    return new AuthType[]{AuthType.KAKAO_TALK};
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
    }


    public static class UserInfo {
        private String name;
        public UserInfo(String name) {
            this.name = name;

        }

        public String getName() {
            return name;
        }
    }
}
