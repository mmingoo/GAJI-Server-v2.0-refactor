package gaji.service.domain.user.service;

import gaji.service.domain.user.entity.User;
import gaji.service.domain.user.web.dto.UserRequestDTO;

public interface UserCommandService {
    public User cancleUser(Long userId);
    public User updateUserNickname(Long userIdFromToken, Long userIdFromPathVariable, UserRequestDTO.UpdateNicknameDTO request);
    public void hardDeleteInactiveUsers();
    void save(User user);

}
