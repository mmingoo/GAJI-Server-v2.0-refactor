package gaji.service.domain.post.code;

import gaji.service.global.exception.code.BaseCodeDto;
import gaji.service.global.exception.code.BaseErrorCodeInterface;
import lombok.AllArgsConstructor;
import lombok.Getter;
import org.springframework.http.HttpStatus;


@Getter
@AllArgsConstructor
public enum CommunityCommentErrorStatus implements BaseErrorCodeInterface {

    _COMMENT_NOT_FOUND(HttpStatus.BAD_REQUEST, "COMMENT_4001", "존재하지 않는 댓글입니다."),
    _NOT_AUTHORIZED(HttpStatus.FORBIDDEN, "COMMENT_4031", "해당 댓글에 접근 권한이 없습니다.")
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
