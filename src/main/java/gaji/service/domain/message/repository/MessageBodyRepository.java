package gaji.service.domain.message.repository;

import gaji.service.domain.message.entity.MessageBody;
import org.springframework.data.jpa.repository.JpaRepository;

public interface MessageBodyRepository extends JpaRepository<MessageBody, Long> {
}
