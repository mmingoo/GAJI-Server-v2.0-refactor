package gaji.service.domain.message.service;

import gaji.service.domain.message.entity.Message;
import gaji.service.domain.message.web.dto.MessageRequestDTO;

import java.util.List;

public interface MessageCommandService {
    public List<Message> createMessage(Long myId, Long otherId, MessageRequestDTO.CreateMessageDTO request);
}
