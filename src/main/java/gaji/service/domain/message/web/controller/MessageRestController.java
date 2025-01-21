package gaji.service.domain.message.web.controller;

import gaji.service.domain.message.converter.MessageConverter;
import gaji.service.domain.message.entity.Message;
import gaji.service.domain.message.service.MessageCommandService;
import gaji.service.domain.message.service.MessageQueryService;
import gaji.service.domain.message.web.dto.MessageRequestDTO;
import gaji.service.domain.message.web.dto.MessageResponseDTO;
import gaji.service.global.base.BaseResponse;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/messages")
@RequiredArgsConstructor
public class MessageRestController {
    private final MessageCommandService messageCommandService;
    private final MessageQueryService messageQueryService;

    @PostMapping("/{otherId}")
    public BaseResponse<MessageResponseDTO.CreateResultDTO> create(Long myId/*하드 코딩용 추후 수정.*/,
                                                                   //@RequestHeader("Authorization") String token,
                                                                   @PathVariable Long otherId,
                                                                   @RequestBody @Valid MessageRequestDTO.CreateMessageDTO request) {
        //Long myId = getUserIdFromToken(token);
        List<Message> messageList = messageCommandService.createMessage(myId, otherId, request);
        return BaseResponse.onSuccess(MessageConverter.toCreateResultDTO(messageList));
    }
}
