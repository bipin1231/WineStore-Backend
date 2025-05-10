package com.winestore.winestore.oauth;

import com.winestore.winestore.entity.User;
import com.winestore.winestore.repository.UserRepo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.oauth2.client.userinfo.DefaultOAuth2UserService;
import org.springframework.security.oauth2.client.userinfo.OAuth2UserRequest;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.stereotype.Service;



@Service
public class CustomOAuth2UserService extends DefaultOAuth2UserService {

    @Autowired
    private UserRepo userRepo;

    @Override
    public OAuth2User loadUser(OAuth2UserRequest userRequest) {
        OAuth2User oAuth2User = super.loadUser(userRequest);

        System.out.println("google information"+oAuth2User);
        // Here you can save or update user in DB if you want
        // For now, just return the oAuth2User
        String email = oAuth2User.getAttribute("email");
        String authProvider= userRequest.getClientRegistration().getRegistrationId();
        System.out.println("email is ........."+email);
        System.out.println("authprovider="+authProvider);

        if(userRepo.findByEmailAndAuthProvider(email,authProvider).isEmpty()) {

            User user = new User();
            user.setEmail(email);
            user.setAuthProvider(authProvider);
            userRepo.save(user);
        }

        return oAuth2User;
    }
}
