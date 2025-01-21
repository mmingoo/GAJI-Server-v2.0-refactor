package gaji.service.domain.user.repository;

import gaji.service.domain.user.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.time.LocalDateTime;
import java.util.Optional;

public interface UserRepository extends JpaRepository<User, Long> {
    Optional<User> findById(Long id);
    User findByUsernameId(String username);
    void deleteAllByInactiveTimeBefore(LocalDateTime time);
}
