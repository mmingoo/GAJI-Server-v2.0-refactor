package gaji.service.domain.user.service;

import gaji.service.domain.enums.UserActive;
import gaji.service.domain.user.code.UserErrorStatus;
import gaji.service.domain.user.entity.User;
import gaji.service.domain.user.enums.UserDeletePeriod;
import gaji.service.domain.user.repository.UserRepository;
import gaji.service.domain.user.web.dto.UserRequestDTO;
import gaji.service.global.exception.RestApiException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;

@Service
@RequiredArgsConstructor
public class UserCommandServiceImpl implements UserCommandService{

    private final UserRepository userRepository;
    private final UserQueryService userQueryService;

    @Override
    @Transactional
    public User cancleUser(Long userId) {
        User user = updateUserStatus(userId, UserActive.IN_ACTIVE);

        return user;
    }

    public User updateUserStatus(Long userId, UserActive status) {
        User user = userQueryService.findUserById(userId);
        user.updateStatus(status);

        return user;
    }

    @Override
    @Transactional
    public User updateUserNickname(Long userIdFromToken, Long userIdFromPathVariable, UserRequestDTO.UpdateNicknameDTO request) {
        User user = userQueryService.findUserById(userIdFromToken);

        if(!user.equals(userQueryService.findUserById(userIdFromPathVariable))){
            throw new RestApiException(UserErrorStatus._USER_IS_NOT_SAME_);
        }

        String newNickname = request.getNickname();

        if (user.getNickname()!=null && user.getNickname().equals(newNickname)) {
            throw new RestApiException(UserErrorStatus._NICKNAME_IS_SAME_);
        }

        user.updateNickname(newNickname);

        return user;
    }

    @Override
    @Transactional
    public void hardDeleteInactiveUsers() {
        LocalDateTime cutoffDate = LocalDateTime.now()
                .minusDays(UserDeletePeriod.ONE_MONTH.getDays());
        userRepository.deleteAllByInactiveTimeBefore(cutoffDate);
    }

    @Override
    public void save(User user) {
        userRepository.save(user);
    }
}
