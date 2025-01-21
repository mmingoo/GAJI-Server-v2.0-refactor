package gaji.service.domain.message.entity;

import gaji.service.domain.message.enums.MessageTypeEnum;
import gaji.service.domain.user.entity.User;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;



@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Message {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "my_id", referencedColumnName = "user_id")
    private User self;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "other_id", referencedColumnName = "user_id")
    private User other;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "message_body_id")
    private MessageBody messageBody;

    private MessageTypeEnum type;
    private LocalDateTime readDate;
    private LocalDateTime sendDate;

    @Builder
    public Message(User mine, User other, MessageTypeEnum type, LocalDateTime sendDate) {
        this.self = mine;
        this.other = other;
        this.type = type;
        this.sendDate = sendDate;
    }

    public void setMessageBody(MessageBody messageBody) {
        this.messageBody = messageBody;
        messageBody.getMessageList().add(this);
    }

}
