package gaji.service.domain.message.converter;

import gaji.service.domain.message.entity.Message;
import gaji.service.domain.message.enums.MessageTypeEnum;
import gaji.service.domain.message.web.dto.MessageResponseDTO;
import gaji.service.domain.user.entity.User;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

public class MessageConverter {
    public static MessageResponseDTO.CreateResultDTO toCreateResultDTO(List<Message> messageList){
        List<Long> messageIds = messageList.stream()
                .map(Message::getId)
                .collect(Collectors.toList());

        return MessageResponseDTO.CreateResultDTO.builder()
                .messageIds(messageIds)
                .build();
    }

    public static Message toSendMessage(User mine, User other){
        return Message.builder()
                .mine(mine)
                .other(other)
                .type(MessageTypeEnum.Send)
                .sendDate(LocalDateTime.now())
                .build();
    }

    public static Message toReceiveMessage(User mine, User other){
        return Message.builder()
                .mine(other)
                .other(mine)
                .type(MessageTypeEnum.Receive)
                .sendDate(LocalDateTime.now()).
                build();
    }
}
