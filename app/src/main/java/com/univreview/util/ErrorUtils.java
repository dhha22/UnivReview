package com.univreview.util;

import android.support.v7.app.AlertDialog;

import com.google.gson.JsonSyntaxException;
import com.univreview.App;
import com.univreview.Navigator;
import com.univreview.activity.LoginActivity;
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
                //Logger.e(responseBody);
                if (response.code() == 500) {
                    Util.toast("서버 에러");
                }else if(response.code() == 401){
                    App.setUserToken(null);
                }else if(response.code() == 412){   // 강제 업데이트
                    if(App.getCurrentActivity() instanceof LoginActivity){
                        new AlertDialog.Builder(App.getCurrentActivity())
                                .setMessage("대학리뷰 최신 버전이 출시 되었습니다\n업데이트 후 이용해 주세요")
                                .setPositiveButton("확인", (dialogInterface, i) -> Navigator.goGooglePlayStore())
                                .setNegativeButton("취소", null)
                                .setCancelable(false)
                                .show();
                    }else{
                       Navigator.goLogin(App.getCurrentActivity());
                    }

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

    public static String getErrorBody(Throwable throwable){
        if (throwable instanceof HttpException) {
            Response response = ((HttpException) throwable).response();
            try {
                Logger.e(response.code());
                Logger.e(response.message());
                String responseBody = response.errorBody().string();
                Logger.e(responseBody);
                return responseBody;
            }catch (IOException e){
                Logger.e(e);
            }
        }
        return null;
    }
}
