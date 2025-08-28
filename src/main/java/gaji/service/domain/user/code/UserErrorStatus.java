package gaji.service.domain.user.code;

import gaji.service.global.exception.code.BaseCodeDto;
import gaji.service.global.exception.code.BaseErrorCodeInterface;
import lombok.AllArgsConstructor;
import lombok.Getter;
import org.springframework.http.HttpStatus;

@Getter
@AllArgsConstructor
public enum UserErrorStatus implements BaseErrorCodeInterface {

    _USER_NOT_FOUND(HttpStatus.BAD_REQUEST, "USER_4001", "존재하지 않는 회원입니다."),

    //nickname 관련 Error
    _NICKNAME_IS_SAME_(HttpStatus.BAD_REQUEST, "USER_4002", "전과 동일한 닉네임으로 수정할 수 없습니다."),
    _USER_IS_NOT_SAME_(HttpStatus.BAD_REQUEST, "USER_4003", "다른 회원의 닉네임을 수정할 수 없습니다."),
    ;

    private final HttpStatus httpStatus;
    private final boolean isSuccess = false;
    private final String code;
    private final String message;

    @Override
    public BaseCodeDto getErrorCode() {
        return BaseCodeDto.builder()
                .httpStatus(httpStatus)
                .isSuccess(isSuccess)
                .code(code)
                .message(message)
                .build();
    }
}
