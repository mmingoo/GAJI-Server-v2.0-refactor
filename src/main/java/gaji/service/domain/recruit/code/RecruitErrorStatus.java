package gaji.service.domain.recruit.code;

import gaji.service.global.exception.code.BaseCodeDto;
import gaji.service.global.exception.code.BaseErrorCodeInterface;
import lombok.AllArgsConstructor;
import lombok.Getter;
import org.springframework.http.HttpStatus;


@Getter
@AllArgsConstructor
public enum RecruitErrorStatus implements BaseErrorCodeInterface {
    _RECRUIT_POST_NOT_FOUND(HttpStatus.BAD_REQUEST, "RECRUIT_4001", "모집 게시글을 찾을 수 없습니다."),
    _RECRUIT_POST_ALREADY_COMPLETE(HttpStatus.BAD_REQUEST, "RECRUIT4002", "이미 모집 완료된 게시글 입니다."),

    _COMMENT_NOT_FOUND(HttpStatus.BAD_REQUEST, "COMMENT_4001", "존재하지 않는 댓글입니다."),
    _COMMENT_ALREADY_DELETE(HttpStatus.BAD_REQUEST, "COMMENT_4002", "이미 삭제된 댓글입니다."),
    _COMMENT_NOT_OWNER(HttpStatus.BAD_REQUEST, "COMMENT_4003", "댓글 작성자가 아닙니다."),

    _ROOM_ALREADY_LIKE(HttpStatus.BAD_REQUEST, "LIKE_4001", "이미 좋아요 된 게시글 입니다."),
    _ROOM_ALREADY_NO_LIKE(HttpStatus.BAD_REQUEST, "LIKE_4002", "이미 좋아요 취소된 게시글 입니다."),

    _ROOM_ALREADY_BOOKMARK(HttpStatus.BAD_REQUEST, "BOOKMARK_4001", "이미 북마크 된 게시글 입니다."),
    _ROOM_ALREADY_NO_BOOKMARK(HttpStatus.BAD_REQUEST, "BOOKMARK_4002", "이미 북마크 취소된 게시글 입니다."),

    _QUERY_SO_SHORT(HttpStatus.BAD_REQUEST, "SEARCH_4001", "검색어가 너무 짧습니다."),
    _QUERY_SO_LONG(HttpStatus.BAD_REQUEST, "SEARCH_4002", "검색어가 너무 깁니다.")
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
