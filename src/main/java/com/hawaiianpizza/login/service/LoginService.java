package com.hawaiianpizza.login.service;

import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.hawaiianpizza.login.dao.LoginDao;
import com.hawaiianpizza.login.model.GoogleUser;
import com.hawaiianpizza.login.model.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.io.*;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.ProtocolException;
import java.net.URL;
import java.util.HashMap;
import java.util.List;
import java.util.Optional;
import java.util.StringTokenizer;

@Service
public class LoginService {
    @Autowired
    private LoginDao loginDao;
    @Autowired
    private PasswordEncoder passwordEncoder;
    @Autowired
    private JavaMailSender mailSender;
    private static final String FROM_ADDRESS = "ssafystudy@gmail.com";


    public String getToken(final String authorize_code) {
        String access_Token = "";
        String refresh_Token = "";
        String reqURL = "https://kauth.kakao.com/oauth/token";

        try {
            URL url = new URL(reqURL);

            HttpURLConnection conn = (HttpURLConnection) url.openConnection();

            //    POST 요청을 위해 기본값이 false인 setDoOutput을 true로

            conn.setRequestMethod("POST");
            conn.setDoOutput(true);

            //    POST 요청에 필요로 요구하는 파라미터 스트림을 통해 전송
            BufferedWriter bw = new BufferedWriter(new OutputStreamWriter(conn.getOutputStream()));
            StringBuilder sb = new StringBuilder();
            sb.append("grant_type=authorization_code");
            sb.append("&client_id=8d49647c4738cb1c7919b1734a1e2121");  //본인이 발급받은 key
            sb.append("&redirect_uri=http://10.8.0.14:3000/kakao_login");     // 본인이 설정해 놓은 경로
            sb.append("&code=" + authorize_code);
            bw.write(sb.toString());
            bw.flush();

            //    결과 코드가 200이라면 성공
            int responseCode = conn.getResponseCode();
            System.out.println("responseCode : " + responseCode);

            //    요청을 통해 얻은 JSON타입의 Response 메세지 읽어오기
            BufferedReader br = new BufferedReader(new InputStreamReader(conn.getInputStream()));
            String line = "";
            String result = "";

            while ((line = br.readLine()) != null) {
                result += line;
            }
            System.out.println("response body : " + result);

            //    Gson 라이브러리에 포함된 클래스로 JSON파싱 객체 생성
            JsonParser parser = new JsonParser();
            JsonElement element = parser.parse(result);

            access_Token = element.getAsJsonObject().get("access_token").getAsString();
            refresh_Token = element.getAsJsonObject().get("refresh_token").getAsString();

            System.out.println("access_token : " + access_Token);
            System.out.println("refresh_token : " + refresh_Token);

            br.close();
            bw.close();
        } catch (IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }

        return access_Token;

    }
    public HashMap<String, Object> getUserInfo (final String access_Token) {

        //    요청하는 클라이언트마다 가진 정보가 다를 수 있기에 HashMap타입으로 선언
        HashMap<String, Object> userInfo = new HashMap<>();
        String reqURL = "https://kapi.kakao.com/v2/user/me";
        try {
            URL url = new URL(reqURL);
            HttpURLConnection conn = (HttpURLConnection) url.openConnection();
            conn.setRequestMethod("GET");

            //    요청에 필요한 Header에 포함될 내용
            conn.setRequestProperty("Authorization", "Bearer " + access_Token);

            int responseCode = conn.getResponseCode();
            System.out.println("responseCode : " + responseCode);

            BufferedReader br = new BufferedReader(new InputStreamReader(conn.getInputStream()));

            String line = "";
            String result = "";

            while ((line = br.readLine()) != null) {
                result += line;
            }
            System.out.println("response body : " + result);

            JsonParser parser = new JsonParser();
            JsonElement element = parser.parse(result);

            JsonObject properties = element.getAsJsonObject().get("properties").getAsJsonObject();
            JsonObject kakao_account = element.getAsJsonObject().get("kakao_account").getAsJsonObject();
            String nickname = properties.getAsJsonObject().get("nickname").getAsString();
            String profile_image = properties.getAsJsonObject().get("profile_image").getAsString();
            String email = kakao_account.getAsJsonObject().get("email").getAsString();
            String id = kakao_account.getAsJsonObject().get("id").getAsString();
            userInfo.put("nickname", nickname);
            userInfo.put("email", email);
            userInfo.put("profile_image", profile_image);
            userInfo.put("id",id);
        } catch (IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }

        return userInfo;
    }

    public String getGithubToken(final String authorize_code) {
        String access_Token = "";
        String refresh_Token = "";
        String reqURL = "https://github.com/login/oauth/access_token?";
        try {
            URL url = new URL(reqURL);

            HttpURLConnection conn = (HttpURLConnection) url.openConnection();

            //    POST 요청을 위해 기본값이 false인 setDoOutput을 true로

            conn.setRequestMethod("POST");
            conn.setDoOutput(true);
            conn.setRequestProperty("Accept","application/json");

            //    POST 요청에 필요로 요구하는 파라미터 스트림을 통해 전송
            BufferedWriter bw = new BufferedWriter(new OutputStreamWriter(conn.getOutputStream()));
            StringBuilder sb = new StringBuilder();
            sb.append("&client_id=fd299d60e9868bcaf31f");  //본인이 발급받은 key
            sb.append("&client_secret=98d6e0d9d70fb6262ad1e9f1eae79c90c47d43e7");     //
            sb.append("&code=" + authorize_code);
            bw.write(sb.toString());
            bw.flush();

            //    결과 코드가 200이라면 성공
            int responseCode = conn.getResponseCode();
            System.out.println("responseCode : " + responseCode);

            //    요청을 통해 얻은 JSON타입의 Response 메세지 읽어오기
            BufferedReader br = new BufferedReader(new InputStreamReader(conn.getInputStream()));
            String line = "";
            String result = "";

            while ((line = br.readLine()) != null) {
                result += line;
            }
            System.out.println("response body : " + result);

            //    Gson 라이브러리에 포함된 클래스로 JSON파싱 객체 생성
            String t[] = result.split("&");
            for(int i = 0;i<t.length;i++){

            }
            JsonParser parser = new JsonParser();
            JsonElement element = parser.parse(result);
            access_Token = element.getAsJsonObject().get("access_token").getAsString();

            System.out.println("access_token : " + access_Token);
            br.close();
            bw.close();
        } catch (IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }

        return access_Token;

    }
    public HashMap<String, Object> getGithubUserInfo (final String access_Token) {
        System.out.println("getGithubUserInfo");
        //    요청하는 클라이언트마다 가진 정보가 다를 수 있기에 HashMap타입으로 선언
        HashMap<String, Object> userInfo = new HashMap<>();
        String reqURL = "https://api.github.com/user";
        try {
            URL url = new URL(reqURL);
            HttpURLConnection conn = (HttpURLConnection) url.openConnection();
            conn.setRequestMethod("GET");

            //    요청에 필요한 Header에 포함될 내용
            conn.setRequestProperty("Authorization", "token " + access_Token);

            int responseCode = conn.getResponseCode();
            System.out.println("responseCode : " + responseCode);

            BufferedReader br = new BufferedReader(new InputStreamReader(conn.getInputStream()));

            String line = "";
            String result = "";

            while ((line = br.readLine()) != null) {
                result += line;
            }
            System.out.println("response body : " + result);

            JsonParser parser = new JsonParser();
            JsonElement element = parser.parse(result);

            String nickname = element.getAsJsonObject().get("login").getAsString();
            String id = element.getAsJsonObject().get("id").getAsString();
            String email = element.getAsJsonObject().get("html_url").getAsString();
            String profile_image = element.getAsJsonObject().get("avatar_url").getAsString();
            System.out.println("nickname : " + nickname);
            System.out.println("id : " + id);
            System.out.println("email : " + email);
            System.out.println("profile_image : " +profile_image);
            userInfo.put("nickname",nickname);
            userInfo.put("id",id);
            userInfo.put("email",email);
            userInfo.put("profile_image",profile_image);


        } catch (IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }

        return userInfo;
    }
    public User googleLogin(GoogleUser guser){

        //todo :  user 가입 여부 확인, 없을시 가입

        try{
            Optional<User> user = loginDao.findById(guser.getId());
            if(loginDao.findById(guser.getId()).isPresent()){
                System.out.println("user login");
            }
            else
            {
                System.out.println("test");
                loginDao.save(new User());
                System.out.println("test success");
                User suser = new User();
                System.out.println("save");
                suser.setId(guser.getId());
                suser.setEmail(guser.getEmail());
                suser.setLogintype("google");
                suser.setName(guser.getName());
                suser.setToken(guser.getAccess_token());
                suser.setProfileimage(guser.getProfile_image());
                System.out.println(suser);
                loginDao.save(suser);
                System.out.println("save");
            }
            return user.get();
        }
        catch (Exception e)
        {
            User suser = new User();
            System.out.println("user join");
            suser.setId(guser.getId());
            suser.setEmail(guser.getEmail());
            suser.setLogintype("google");
            suser.setName(guser.getName());
            suser.setToken(guser.getAccess_token());
            suser.setProfileimage(guser.getProfile_image());
            loginDao.save(suser);
            return suser;
        }

    }

    // 로컬 로그인 부분
    public boolean overLapCheck(String id){
        try{
            if(loginDao.findById(id).isPresent()){
                return true;
            };
            return false;
        }
        catch (Exception e){

            return false;
        }
    }
    public User localSignUp(User user){

        try{
            String createCode = ramdom(10);
            String encodePwd = passwordEncoder.encode(user.getPwd());

            user.setPwd(encodePwd);
            user.setCertified("false");
            user.setCertifycode(createCode.toString());
            System.out.println(user);
            User ret = loginDao.save(user);
            loginDao.save(user);
            //인증 메일 전송
            sendMail("하와이안피자 서비스 인증번호",user.getEmail(),createCode.toString());

            return ret;
        }
        catch(Exception exception) {
            System.out.println(exception);
        }
        return user;
    }


    public boolean localSignIn(String id,String pwd){

        try{
            Optional<User> ret = loginDao.findById(id);
            if(ret.isPresent()){
                if(passwordEncoder.matches(pwd,ret.get().getPwd()))
                    return true;
                else
                    System.out.println(pwd+": password fail");
            }
            else
                System.out.println(id+": id fail");
            return false;
        }
        catch(Exception exception) {
            System.out.println(exception);
        }
        return false;
    }
    // 비밀번호 찾기
    public boolean localResetPwd(String id,String email){

        try{
            Optional<User> ret = loginDao.findByIdAndEmail(id,email);
            if(ret.isPresent()){
                User user = ret.get();
                String randomPwd = ramdom(8);
                user.setPwd(passwordEncoder.encode(randomPwd));
                loginDao.save(user);
                //메일전송
                sendMail("하와이안피자 서비스 비밀번호 초기화",user.getEmail(),"초기화된 비밀번호 : "+randomPwd);
                System.out.println("reset sucess");
                return true;
            }
            else
                System.out.println("reset fail");
            return false;
        }
        catch(Exception exception) {
            System.out.println(exception);
        }
        return false;
    }

    // 인증 시도, 성공시 재로그인
    public boolean localCertify(String id,String code){
        Optional<User> ret = loginDao.findByIdAndCertifycode(id,code);
        try{
            if(ret.isPresent()){
                User user = ret.get();
                user.setCertified("true");
                loginDao.save(user);
                //메일전송
                return true;
            }
        }
        catch (Exception e){
            System.out.println(e);
            return false;
        }
        return false;
    }


    // 랜덤코드 및 패스워드 생성
    public String ramdom(int num){
        String codechar = "0123456789abcdefghijklmnopqrstuvwxyzABCDEFGHIJKLMNOPQRSTUVWXYZ";
        StringBuilder createCode = new StringBuilder();
        for(int i = 0;i<num;i++) {
            createCode.append(codechar.charAt((int) (Math.random() * codechar.length())));
        }
        return createCode.toString();
    }

    // 이메일 전송
    public void sendMail(String title, String toEmail, String text){
        System.out.println("메일전송");
        SimpleMailMessage message = new SimpleMailMessage();
        message.setTo(toEmail);
        message.setFrom(FROM_ADDRESS);
        message.setSubject(title);
        message.setText(text);

        mailSender.send(message);
        System.out.println(toEmail+"로 메일 전송 성공");
    }
}