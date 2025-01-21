package gaji.service.domain.message.service;

import gaji.service.domain.message.converter.MessageBodyConverter;
import gaji.service.domain.message.converter.MessageConverter;
import gaji.service.domain.message.entity.Message;
import gaji.service.domain.message.entity.MessageBody;
import gaji.service.domain.message.repository.MessageBodyRepository;
import gaji.service.domain.message.repository.MessageRepository;
import gaji.service.domain.message.web.dto.MessageRequestDTO;
import gaji.service.domain.user.code.UserErrorStatus;
import gaji.service.domain.user.entity.User;
import gaji.service.domain.user.repository.UserRepository;
import gaji.service.global.exception.RestApiException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Arrays;
import java.util.List;

@Service
@RequiredArgsConstructor
public class MessageCommandServiceImpl implements MessageCommandService{

    private final UserRepository userRepository;
    private final MessageRepository messageRepository;
    private final MessageBodyRepository messageBodyRepository;


    @Override
    @Transactional
    public List<Message> createMessage(Long myId, Long otherId, MessageRequestDTO.CreateMessageDTO request) {

        User mine = userRepository.findById(myId)
                .orElseThrow(()-> new RestApiException(UserErrorStatus._USER_NOT_FOUND));
        User other = userRepository.findById(otherId)
                .orElseThrow(()-> new RestApiException(UserErrorStatus._USER_NOT_FOUND));

        Message sendMessage = MessageConverter.toSendMessage(mine,other);
        Message receiveMessage = MessageConverter.toReceiveMessage(mine, other);

        MessageBody messageBody = MessageBodyConverter.toMessageBody(request);

        //연관관계 매핑을 위해 setter 사용
        sendMessage.setMessageBody(messageBody);
        receiveMessage.setMessageBody(messageBody);

        messageRepository.save(sendMessage);
        messageRepository.save(receiveMessage);
        messageBodyRepository.save(messageBody);

        return Arrays.asList(sendMessage, receiveMessage);
    }
}
