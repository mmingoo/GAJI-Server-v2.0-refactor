package gaji.service.domain.post.code;

import gaji.service.global.exception.code.BaseCodeDto;
import gaji.service.global.exception.code.BaseErrorCodeInterface;
import lombok.AllArgsConstructor;
import lombok.Getter;
import org.springframework.http.HttpStatus;


@Getter
@AllArgsConstructor
public enum CommunityPostErrorStatus implements BaseErrorCodeInterface {

    _POST_NOT_FOUND(HttpStatus.BAD_REQUEST, "POST_4001", "존재하지 않는 게시글입니다."),
    _INVALID_POST_TYPE(HttpStatus.BAD_REQUEST, "POST_4002", "유효하지 않은 게시글 유형입니다."),
    _INVALID_POST_STATUS(HttpStatus.BAD_REQUEST, "POST_4003", "유효하지 않은 게시글 상태값입니다."),
    _ALREADY_EXIST_POST_LIKES(HttpStatus.BAD_REQUEST, "POST_4004", "이미 좋아요한 게시글입니다."),
    _ALREADY_EXIST_POST_BOOKMARK(HttpStatus.BAD_REQUEST, "POST_4005", "이미 북마크한 게시글입니다."),
    _BOOKMARK_CNT_NEGATIVE(HttpStatus.BAD_REQUEST, "POST_4006", "북마크 수는 0보다 작을 수 없습니다."),
    _LIKE_CNT_NEGATIVE(HttpStatus.BAD_REQUEST, "POST_4007", "좋아요 수는 0보다 작을 수 없습니다."),
    _POP_SCORE_NEGATIVE(HttpStatus.BAD_REQUEST, "POST_4007", "인기점수는 0보다 작을 수 없습니다."),

    _NOT_AUTHORIZED(HttpStatus.FORBIDDEN, "POST_4031", "해당 게시글에 접근 권한이 없습니다.")
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
