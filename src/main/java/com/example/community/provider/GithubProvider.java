package com.example.community.provider;

import ch.qos.logback.core.net.SyslogOutputStream;
import com.alibaba.fastjson.JSON;
import com.example.community.dto.AccessTokenDto;
import com.example.community.dto.GithubUser;
import okhttp3.*;
import org.springframework.stereotype.Component;

import java.io.IOException;

@Component
public class GithubProvider {
    public String getAccessTokens(AccessTokenDto accessTokenDto){
       MediaType mediaType = MediaType.get("application/json; charset=utf-8");
        OkHttpClient client = new OkHttpClient();
            RequestBody body = RequestBody.create(mediaType, JSON.toJSONString(accessTokenDto));
            Request request = new Request.Builder()
                    .url("https://github.com/login/oauth/access_token")
                    .post(body)
                    .build();
            try (Response response = client.newCall(request).execute()) {
                String str = response.body().string();
                String[] split = str.split("&");
                String tokenstr = split[0];
                String token = tokenstr.split("=")[1];
                System.out.println(str);
                return token;
            } catch (Exception e) {
                e.printStackTrace();
            }
        return null;
    }

    public GithubUser getUser(String accssToken){
        OkHttpClient client = new OkHttpClient();
        System.out.println(accssToken);
        Request request = new Request.Builder()
                .url("https://api.github.com/user?access_token=" + accssToken)
                .build();
        try {
            Response response = client.newCall(request).execute();
            String str = response.body().string();
            GithubUser githubUser = JSON.parseObject(str,GithubUser.class);
            return githubUser;
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }
}

