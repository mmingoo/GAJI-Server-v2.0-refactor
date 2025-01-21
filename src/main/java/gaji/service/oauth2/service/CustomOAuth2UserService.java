package gaji.service.oauth2.service;


import gaji.service.domain.enums.Gender;
import gaji.service.domain.enums.ServiceRole;
import gaji.service.domain.enums.SocialType;
import gaji.service.domain.enums.UserActive;
import gaji.service.domain.user.entity.User;
import gaji.service.domain.user.service.UserCommandService;
import gaji.service.domain.user.service.UserQueryService;
import gaji.service.oauth2.dto.CustomOAuth2User;
import gaji.service.oauth2.dto.OAuthUserDTO;
import gaji.service.oauth2.dto.TransferUserDTO;
import gaji.service.oauth2.response.GoogleResponse;
import gaji.service.oauth2.response.NaverResponse;
import gaji.service.oauth2.response.OAuth2Response;
import lombok.RequiredArgsConstructor;
import org.springframework.security.oauth2.client.userinfo.DefaultOAuth2UserService;
import org.springframework.security.oauth2.client.userinfo.OAuth2UserRequest;
import org.springframework.security.oauth2.core.OAuth2AuthenticationException;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;

@Service
@RequiredArgsConstructor
public class CustomOAuth2UserService extends DefaultOAuth2UserService {
    private final UserQueryService userQueryService;
    private final UserCommandService userCommandService;


    @Override
    public OAuth2User loadUser(OAuth2UserRequest userRequest) throws OAuth2AuthenticationException {

        OAuth2User oAuth2User = super.loadUser(userRequest);
        boolean isNewUser = false;
        String registrationId = userRequest.getClientRegistration().getRegistrationId();
        OAuth2Response oAuth2Response = null;
        if (registrationId.equals("naver")) {

            oAuth2Response = new NaverResponse(oAuth2User.getAttributes());
        }
        else if (registrationId.equals("google")) {

            oAuth2Response = new GoogleResponse(oAuth2User.getAttributes());
        }
        else {

            return null;
        }

        //리소스 서버에서 발급 받은 정보로 사용자를 특정할 아이디값을 만듬
        String usernameId = oAuth2Response.getProvider()+" "+oAuth2Response.getProviderId();
        User existData = userQueryService.findByUsernameId(usernameId);


        if (existData == null) {

            isNewUser = true;
            OAuthUserDTO oAuthuserDTO = new OAuthUserDTO();
            oAuthuserDTO.setUsernameId(usernameId);
            oAuthuserDTO.setRole(ServiceRole.ROLE_USER);
            oAuthuserDTO.setNewUser(isNewUser);

            if (registrationId.equals("naver")) {

                TransferUserDTO transferUserDTO =new TransferUserDTO();
                transferUserDTO.setUsernameId(usernameId);
                transferUserDTO.setEmail(oAuth2Response.getEmail());
                transferUserDTO.setNickname(extractNickname(oAuth2Response.getEmail()));
                transferUserDTO.setName(oAuth2Response.getName());
                transferUserDTO.setGender(toEnumGender(oAuth2Response.getGender()));
                transferUserDTO.setBirthday(formatDate(oAuth2Response.getBirthyear(), oAuth2Response.getBirthday()));
                transferUserDTO.setUserActive(UserActive.ACTIVE);
                transferUserDTO.setSocialType(setSocialType(registrationId));
                transferUserDTO.setRole(ServiceRole.ROLE_USER);

                User user = User.createUser(transferUserDTO); // 정적 팩토리 메서드 사용
                userCommandService.save(user);
            }
            else if (registrationId.equals("google")) {
                TransferUserDTO transferUserDTO =new TransferUserDTO();

                transferUserDTO.setUsernameId(usernameId);
                transferUserDTO.setEmail(oAuth2Response.getEmail());
                transferUserDTO.setName(oAuth2Response.getName());
                transferUserDTO.setGender(toEnumGender(oAuth2Response.getGender()));
                transferUserDTO.setNickname(extractNickname(oAuth2Response.getEmail()));
//                transferUserDTO.setBirthday(formatDate(oAuth2Response.getBirthyear(), oAuth2Response.getBirthday()));
                transferUserDTO.setUserActive(UserActive.ACTIVE);
                transferUserDTO.setSocialType(setSocialType(registrationId));
                transferUserDTO.setRole(ServiceRole.ROLE_USER);

                User user = User.createUser(transferUserDTO); // 정적 팩토리 메서드 사용
                System.out.println(user.getGender());
                userCommandService.save(user);
            }

            return new CustomOAuth2User(oAuthuserDTO);

        }else{
            existData.setEmail(oAuth2Response.getEmail());
            existData.setName(oAuth2Response.getName());

            userCommandService.save(existData);

            OAuthUserDTO oAuthuserDTO = new OAuthUserDTO();
            oAuthuserDTO.setUsernameId(usernameId);
            oAuthuserDTO.setRole(ServiceRole.ROLE_USER);
            oAuthuserDTO.setNewUser(isNewUser);


            return new CustomOAuth2User(oAuthuserDTO);
        }
    }


    public LocalDate formatDate(String birthyear, String birthday){
        if(birthday == null){
            return null;
        }else{
            try {
                // birthday (MM-dd 형식)와 birthyear를 결합
                String fullDateString = birthyear + "-" + birthday;

                // 결합된 문자열을 파싱할 DateTimeFormatter 생성
                DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");

                // 문자열을 LocalDate로 파싱
                return LocalDate.parse(fullDateString, formatter);
            } catch (DateTimeParseException e) {
                System.out.println("Error parsing date: " + e.getMessage());
                return null;
            }
        }

    }


    public Gender toEnumGender(String gender) {
        if (gender == null) {
            return Gender.UNKNOWN;
        }
        switch (gender.toUpperCase()) {
            case "W":
            case "F":
                return Gender.FEMALE;
            case "M":
                return Gender.MALE;
            default:
                return Gender.UNKNOWN;
        }
    }
    public SocialType setSocialType(String social){
        if(social.equals("naver")){
            return SocialType.NAVER;
        }else{
            return SocialType.GOOGLE;
        }
    }

    public static String extractNickname(String email) {
        if (email == null || email.isEmpty()) {
            return null;
        }

        int atIndex = email.indexOf('@');
        if (atIndex == -1) {
            return null; // '@'가 없는 경우 유효하지 않은 이메일로 간주
        }

        return email.substring(0, atIndex);
    }

}