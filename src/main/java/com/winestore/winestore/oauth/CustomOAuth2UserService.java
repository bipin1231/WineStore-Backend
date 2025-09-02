package com.winestore.winestore.oauth;

import com.winestore.winestore.entity.User;
import com.winestore.winestore.repository.UserRepo;
import com.winestore.winestore.service.CustomOAuth2UserDetails;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.oauth2.client.userinfo.DefaultOAuth2UserService;
import org.springframework.security.oauth2.client.userinfo.OAuth2UserRequest;
import org.springframework.security.oauth2.core.OAuth2AuthenticationException;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.stereotype.Service;

import java.util.Map;
import java.util.Optional;


@Service
public class CustomOAuth2UserService extends DefaultOAuth2UserService {

    @Autowired
    private UserRepo userRepo;

    @Override
    public OAuth2User loadUser(OAuth2UserRequest userRequest) {
        OAuth2User oAuth2User = super.loadUser(userRequest);

        System.out.println("google information"+oAuth2User);

        Map<String, Object> attributes = oAuth2User.getAttributes();
        String provider = userRequest.getClientRegistration().getRegistrationId();
        // ✅ Defensive checks
        String email = (String) attributes.get("email");
        if (email == null || email.isEmpty()) {
            throw new OAuth2AuthenticationException("Email not found from provider " + provider);
        }

        // Extract optional attributes
        String firstName = (String) attributes.get("given_name");
        String lastName = (String) attributes.get("family_name");
        String imageUrl = (String) attributes.get("picture");

        // Find or create user
        Optional<User> existingUser = userRepo.findByEmailAndAuthProvider(email, provider);

        User user;
        if (existingUser.isPresent()) {
            user = existingUser.get();
            //  Update info if it changed
            user.setFirstName(firstName);
            user.setLastName(lastName);
            user.setImageUrl(imageUrl);

        } else {
            //  Create new user
            user = User.builder()
                    .email(email)
                    .firstName(firstName)
                    .lastName(lastName)
                    .imageUrl(imageUrl)
                    .authProvider(provider)
                    .roles("user")
                    .build();
        }
        userRepo.save(user);

        return new CustomOAuth2UserDetails(user,attributes);
    }
}
