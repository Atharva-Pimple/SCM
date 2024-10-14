package com.scm.helper;

import org.springframework.security.core.Authentication;
import org.springframework.security.oauth2.client.authentication.OAuth2AuthenticationToken;
import org.springframework.security.oauth2.core.user.OAuth2User;

public class Helper {
    public static String getLoggedInUserEmail(Authentication authentication){
        if(authentication instanceof OAuth2AuthenticationToken){
            var oAuth2AuthenticationToken=(OAuth2AuthenticationToken)authentication;
            var clientId=oAuth2AuthenticationToken.getAuthorizedClientRegistrationId();

            var user=(OAuth2User)authentication.getPrincipal();
            String userEmail="";
            if(clientId.equalsIgnoreCase("google")){
                userEmail= user.getAttribute("email").toString();
            }else if(clientId.equalsIgnoreCase("github")){
                userEmail= user.getAttribute("email")!=null ?user.getAttribute("email").toString() :user.getAttribute("login").toString()+"@gmail.com";
            }
            return userEmail;
        }else{
            return authentication.getName();
        }
    }

    public static String getEmailVerificationLink(String emailToken){
        String link="http://localhost:8081/auth/verify-email?token="+emailToken;

        return link;
    }
}
