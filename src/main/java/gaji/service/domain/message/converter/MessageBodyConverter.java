package gaji.service.domain.message.converter;

import gaji.service.domain.message.entity.MessageBody;
import gaji.service.domain.message.web.dto.MessageRequestDTO;

public class MessageBodyConverter {
    public static MessageBody toMessageBody(MessageRequestDTO.CreateMessageDTO messageRequestDTO){
        return MessageBody.builder()
                .body(messageRequestDTO.getBody())
                .build();
    }
}
