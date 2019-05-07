package com.pnuproject.travellog.etc;
import android.os.AsyncTask;
import android.util.Log;
import java.net.URL;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;


public class RetrofitTask  {
    private RetrofitExecutionHandler executionHandler;
    private AsyncTaskManager asyncTaskManager;
    private String targetURL;

    public RetrofitTask(RetrofitExecutionHandler executionHandler, String targetURL) {
        this.targetURL =  targetURL;
        this.executionHandler = executionHandler;
    }

    public void execute(RetrofitRequestParam... request) {
        if(request == null)
            return;

        asyncTaskManager = new AsyncTaskManager();
        asyncTaskManager.execute(request);
    }

    class AsyncTaskManager extends AsyncTask <RetrofitTask.RetrofitRequestParam,Object,RetrofitTask.RetrofitResponseParam> {
        @Override
        protected RetrofitResponseParam doInBackground(RetrofitRequestParam... request) {
            Retrofit retrofit = new Retrofit.Builder()
                    .baseUrl(targetURL).addConverterFactory(GsonConverterFactory.create())
                    .build();

            Object response = executionHandler.onBeforeAyncExcute(retrofit, request[0]);
            boolean bError = request[0].isError();
            request[0].setError(false);
            return new RetrofitResponseParam(request[0].getTaskNum(),response,bError);
        }
        @Override
        protected void onPreExecute() {
            super.onPreExecute();
        }

        @Override
        protected void onPostExecute(RetrofitResponseParam response) {
            super.onPreExecute();
            executionHandler.onAfterAyncExcute(response);
        }
    }


    public interface RetrofitExecutionHandler {
        void onAfterAyncExcute( RetrofitResponseParam response);
        Object onBeforeAyncExcute(Retrofit retrofit, RetrofitRequestParam paramRequest);
    }

    public static class RetrofitRequestParam {
        private int taskNum;
        private boolean bError;
        private Object paramRequest;

        public RetrofitRequestParam(int taskNum , Object paramRequest  ) {
            this.taskNum = taskNum;
            this.paramRequest = paramRequest;
            bError = false;
        }

        public void setError(boolean bError) {
            this.bError = bError;
        }

        public boolean isError() {
            return bError;
        }

        public Object getParamRequest() { return paramRequest; }
        public int getTaskNum() { return taskNum;}
        public void setTaskNum(int taskNum) { this.taskNum = taskNum; }
        public void setParamRequest(Object paramRequest) {
            this.paramRequest = paramRequest;
        }
    }

    public static class RetrofitResponseParam {
        private int taskNum;
        private Object response;
        private boolean bError;

        public void setError(boolean bError) {
            this.bError = bError;
        }

        public boolean isError() {
            return bError;
        }

        public RetrofitResponseParam(int taskNum , Object response, boolean bError) {
            this.taskNum = taskNum;
            this.response = response;
            this.bError = bError;
        }

        public Object getResponse() { return response;}
        public int getTaskNum() { return taskNum;}
    }

    public static class RetrofitTaskError {
        private int taskNum;
        private String msg;

        public RetrofitTaskError(int taskNum, String msg){
            this.taskNum = taskNum;
            this.msg = msg;
        }
        public int getTaskNum() {
            return taskNum;
        }

        public String getMsg() {
            return msg;
        }
    }
}
