package com.univreview.util;

import com.google.gson.JsonSyntaxException;
import com.univreview.App;
import com.univreview.log.Logger;

import java.io.IOException;
import java.net.SocketTimeoutException;
import java.net.UnknownHostException;

import retrofit2.Response;
import retrofit2.adapter.rxjava.HttpException;

/**
 * Created by DavidHa on 2016. 9. 7..
 */
public class ErrorUtils {
    public static final int ERROR_400 = 400;
    public static final int ERROR_404 = 404;

    public static int parseError(Throwable throwable) {
        if (throwable instanceof HttpException) {
            Response response = ((HttpException) throwable).response();
            try {
                Logger.e(response.code());
                Logger.e(response.message());
                String responseBody = response.errorBody().string();
                Logger.e(responseBody);
                if (response.code() == 500) {
                    Util.toast("서버 에러");
                }else if(response.code() == 401){
                    App.setUserToken(null);
                }
            } catch (IOException e) {
                Logger.e(e.toString());
            }
            return response.code();
        } else if (throwable instanceof UnknownHostException) {
            Util.toast("네트워크 설정을 확인해주세요.");
        } else if (throwable instanceof SocketTimeoutException) {
            Util.toast("네트워크 설정을 확인해주세요.");
        } else if (throwable instanceof JsonSyntaxException) {
            Logger.e(throwable);
        }
        Logger.e(throwable);
        return 0;
    }
}
