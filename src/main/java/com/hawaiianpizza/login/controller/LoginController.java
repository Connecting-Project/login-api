package com.hawaiianpizza.login.controller;

import com.hawaiianpizza.login.model.GoogleUser;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import com.hawaiianpizza.login.model.User;
import com.hawaiianpizza.login.service.LoginService;

import java.time.Clock;
import java.util.HashMap;

@CrossOrigin(origins = "*")
@RestController
@RequestMapping("/login")
public class LoginController {

    private final LoginService loginService;

    public LoginController(LoginService loginService) {
        this.loginService = loginService;
    }

    @GetMapping("/code")
    public ResponseEntity<?> kakao(@RequestParam(value = "code", required = false) String code) throws Exception {
        System.out.println("#########" + code);
        System.out.println("kakao Login Controller");
        try {
            System.out.println(code);
            String token = loginService.getToken(code);
            System.out.println("info");
            HashMap<String, Object> ret = loginService.getUserInfo(token);
            ret.put("token", token);
            return new ResponseEntity<>(ret, HttpStatus.OK);
        } catch (Exception e) {
            return new ResponseEntity<>(null, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }
//    @PostMapping(value = "/kakao")
//    public ResponseEntity<?> kakao(@RequestParam String code) {
//        System.out.println("kakao Login Controller");
//        try {
//            System.out.println(code);
//            String token = loginService.getToken(code);
//            System.out.println("info");
//            HashMap<String, Object> ret = loginService.getUserInfo(token);
//            return new ResponseEntity<>(token, HttpStatus.OK);
//        } catch (Exception e) {
//            return new ResponseEntity<>(null, HttpStatus.INTERNAL_SERVER_ERROR);
//        }
//
//    }

    @PostMapping(value = "/google")
    public ResponseEntity<?> google(@RequestBody GoogleUser user) {
        System.out.println("google Login Controller");
        try {
            System.out.println(user);
            System.out.println("info");
            loginService.googleLogin(user);
            return new ResponseEntity<>("login Success", HttpStatus.OK);
        } catch (Exception e) {
            return new ResponseEntity<>("login fail", HttpStatus.INTERNAL_SERVER_ERROR);
        }

    }

    @PostMapping(value = "/github")
    public ResponseEntity<?> github(@RequestParam String code) {
        System.out.println("github Login Controller");
        try {
            System.out.println(code);
            String token = loginService.getGithubToken(code);
            System.out.println("info");
            HashMap<String, Object> ret = loginService.getGithubUserInfo(token);
            ret.put("token", token);
            System.out.println(ret);
            return new ResponseEntity<>(ret, HttpStatus.OK);
        } catch (Exception e) {
            return new ResponseEntity<>(null, HttpStatus.INTERNAL_SERVER_ERROR);
        }

    }


    @PostMapping(value = "/overLapCheck")
    public ResponseEntity<?> overLapCheck(@RequestParam String id) {
        System.out.println("overLapCheck Controller");
        try {
            if (loginService.overLapCheck(id)) {
                return new ResponseEntity<>("sucess", HttpStatus.OK);
            } else {
                return new ResponseEntity<>("fail", HttpStatus.OK);
            }
        } catch (Exception e) {
            return new ResponseEntity<>(null, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @PostMapping(value = "/localSignup")
    public ResponseEntity<?> localSignup(@RequestBody User user) {
        System.out.println("localSignup Controller");
        try {
                System.out.println(user);
                User ret = loginService.localSignUp(user);
                return new ResponseEntity<>(ret, HttpStatus.OK);
            }
        catch (Exception e) {
            return new ResponseEntity<>(null, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @PostMapping(value = "/localSignin")
    public ResponseEntity<?> localSignin(@RequestParam String id,@RequestParam String password) {
        System.out.println("localSignin Controller");
        try {
            User user = loginService.localSignIn(id,password);
            if(user!=null){
                return new ResponseEntity<>(user, HttpStatus.OK);
            }
            else{
                return new ResponseEntity<>("login fail", HttpStatus.OK);
            }
        } catch (Exception e) {
            return new ResponseEntity<>(null, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @PostMapping(value = "/localReset")
    public ResponseEntity<?> localReset(@RequestParam String id,@RequestParam String email) {
        System.out.println("localReset Controller");
        try {
            if(loginService.localResetPwd(id,email)){

                return new ResponseEntity<>("reset sucess", HttpStatus.OK);
            }
            else{
                return new ResponseEntity<>("reset fail", HttpStatus.OK);
            }
        } catch (Exception e) {
            return new ResponseEntity<>(null, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @PostMapping(value = "/localCertify")
    public ResponseEntity<?> localCertify(@RequestParam String id,@RequestParam String CertifyCode) {
        System.out.println("localCertify Controller");
        try {
            if(loginService.localCertify(id,CertifyCode)){
                return new ResponseEntity<>("localCertify sucess", HttpStatus.OK);
            }
            else{
                return new ResponseEntity<>("localCertify fail", HttpStatus.OK);
            }
        } catch (Exception e) {
            return new ResponseEntity<>(null, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

}
