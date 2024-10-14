package com.scm.config;

import java.io.IOException;
import java.util.List;
import java.util.UUID;

import org.slf4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.oauth2.client.authentication.OAuth2AuthenticationToken;
import org.springframework.security.oauth2.core.user.DefaultOAuth2User;
import org.springframework.security.web.DefaultRedirectStrategy;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;
import org.springframework.stereotype.Component;

import com.scm.entities.Provider;
import com.scm.entities.User;
import com.scm.helper.AppConstants;
import com.scm.repositories.UserRepository;

import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

@Component
public class OAuthAuthenticationSuccessHandler implements AuthenticationSuccessHandler{

    Logger logger=org.slf4j.LoggerFactory.getLogger(OAuthAuthenticationSuccessHandler.class);

    @Autowired
    UserRepository userRepository;

    @Override
    public void onAuthenticationSuccess(HttpServletRequest request, HttpServletResponse response,
            Authentication authentication) throws IOException, ServletException {

                logger.info("OAuthAuthenticationSuccessHandle");

                var oAuth2AuthenticationToken=(OAuth2AuthenticationToken)authentication;

                String authorizedClientRegistrationId =oAuth2AuthenticationToken.getAuthorizedClientRegistrationId();

                var oauthUser=(DefaultOAuth2User)authentication.getPrincipal();

                User user=new User();
                user.setUserId(UUID.randomUUID().toString());
                user.setRoleList(List.of(AppConstants.ROLE_USER));
                user.setEmailVerified(true);
                user.setEnabled(true);
                user.setPassword("passsword");

                if(authorizedClientRegistrationId.equalsIgnoreCase("google")){
                    user.setEmail(oauthUser.getAttribute("email").toString());
                    user.setName(oauthUser.getAttribute("name").toString());
                    user.setProfilePic(oauthUser.getAttribute("picture").toString());
                    user.setProvider(Provider.GOOGLE);
                    user.setProviderUserId(oauthUser.getName());
                    user.setAbout("This account created using google");


                }else if(authorizedClientRegistrationId.equalsIgnoreCase("github")){
                    String email=oauthUser.getAttribute("email")!=null ?oauthUser.getAttribute("email").toString() :oauthUser.getAttribute("login").toString()+"@gmail.com";
                    String picture=oauthUser.getAttribute("avatar_url").toString();
                    String name=oauthUser.getAttribute("login").toString();
                    String providerId=oauthUser.getName();

                    user.setEmail(email);
                    user.setName(name);
                    user.setProfilePic(picture);
                    user.setProvider(Provider.GITHUB);
                    user.setProviderUserId(providerId);
                    user.setAbout("This account created using github");
                }
        
                // DefaultOAuth2User user=(DefaultOAuth2User)authentication.getPrincipal();
                // String email=user.getAttribute("email").toString();
                // String name=user.getAttribute("name").toString();
                // String picture=user.getAttribute("picture").toString();

                // User user1=new User();
                // user1.setName(name);
                // user1.setEmail(email);
                // user1.setProfilePic(picture);
                // user1.setPassword("password");
                // user1.setUserId(UUID.randomUUID().toString());
                // user1.setProvider(Provider.GOOGLE);
                // user1.setProviderUserId(user.getName());
                // user1.setRoleList(List.of(AppConstants.ROLE_USER));
                // user1.setAbout("This email is created using google...");
                // user1.setEmailVerified(true);

                User check=userRepository.findByEmail(user.getEmail()).orElse(null);
                if(check==null){
                    userRepository.save(user);
                    logger.info("User saved: "+user.getEmail());
                }


                // response.sendRedirect("/home");
                new DefaultRedirectStrategy().sendRedirect(request, response, "/user/dashboard");
    }

}
