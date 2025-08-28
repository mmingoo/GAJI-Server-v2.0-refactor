package gaji.service.oauth2.service;


import gaji.service.domain.enums.ServiceRole;
import gaji.service.domain.enums.SocialType;
import gaji.service.domain.enums.UserActive;
import gaji.service.domain.user.entity.User;
import gaji.service.domain.user.service.UserCommandService;
import gaji.service.domain.user.service.UserQueryService;
import gaji.service.global.exception.RestApiException;
import gaji.service.oauth2.dto.CustomOAuth2User;
import lombok.RequiredArgsConstructor;
import org.springframework.security.oauth2.client.userinfo.DefaultOAuth2UserService;
import org.springframework.security.oauth2.client.userinfo.OAuth2UserRequest;
import org.springframework.security.oauth2.core.OAuth2AuthenticationException;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.stereotype.Service;

import static gaji.service.global.exception.code.status.GlobalErrorStatus._INVALID_LOGIN_TYPE;

@Service
@RequiredArgsConstructor
public class CustomOAuth2UserService extends DefaultOAuth2UserService {
    private final UserQueryService userQueryService;
    private final UserCommandService userCommandService;


    @Override
    public OAuth2User loadUser(OAuth2UserRequest userRequest) throws OAuth2AuthenticationException {

        // 🔥 OAuth2 API에서 유저 정보 가져오기
        OAuth2User oAuth2User = super.loadUser(userRequest);

        // 인증 공급자에서 사용자 정보를 가져옴
        String registrationId = userRequest.getClientRegistration().getRegistrationId();

        // 소셜 로그인 타입 설정
        SocialType socialType = setSocialType(registrationId);

        String providerId = extractProviderId(socialType, oAuth2User);

        //리소스 서버에서 발급 받은 정보로 사용자를 특정할 아이디값을 만듬
        String usernameId = socialType+" "+providerId;

        // 유저 존재 여부 확인
        User existData = userQueryService.findByUsernameId(usernameId);


        if (existData == null) {

            // 처음 로그인하는 경우, 기본값을 가진 User 엔티티 생성
            User newUser = User.builder()
                    .usernameId(usernameId)
                    .socialType(socialType)
                    .role(ServiceRole.ROLE_USER)
                    .build();
            userCommandService.save(newUser);
        }
        return new CustomOAuth2User(usernameId, ServiceRole.ROLE_USER, existData == null);
    }

    private String extractProviderId(SocialType socialType, OAuth2User oAuth2User) {
        if (socialType == SocialType.KAKAO) {
            return oAuth2User.getAttributes().get("id").toString();
        } else if (socialType == SocialType.GOOGLE) {
            return oAuth2User.getAttributes().get("sub").toString(); // Google은 "sub" 필드가 ID
        }
        throw new RestApiException(_INVALID_LOGIN_TYPE);
    }

    public SocialType setSocialType(String social){
        if(social.equals("kakao")){
            return SocialType.KAKAO;
        }else if (social.equals("google")){
            return SocialType.GOOGLE;
        }else{
            throw new RestApiException(_INVALID_LOGIN_TYPE);
        }
    }
}