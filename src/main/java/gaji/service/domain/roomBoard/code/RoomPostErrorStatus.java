package gaji.service.domain.roomBoard.code;

import gaji.service.global.exception.code.BaseCodeDto;
import gaji.service.global.exception.code.BaseErrorCodeInterface;
import lombok.AllArgsConstructor;
import lombok.Getter;
import org.springframework.http.HttpStatus;


@Getter
@AllArgsConstructor
public enum RoomPostErrorStatus implements BaseErrorCodeInterface {
    _ROOM_BOARD_NOT_FOUND(HttpStatus.BAD_REQUEST, "ROOM_4001", "존재하지 않는 게시판입니다."),


    //권한
    _USER_NOT_DELETE_AUTH(HttpStatus.BAD_REQUEST, "AUTH_4001", "게시글을 삭제할 권한이 없습니다."),
    _USER_NOT_UPDATE_AUTH(HttpStatus.BAD_REQUEST, "AUTH_4002", "게시글을 수정할 권한이 없습니다."),

    //댓글
    _USER_NOT_COMMENT_UPDATE_AUTH(HttpStatus.BAD_REQUEST, "AUTH_4003", "댓글을 수정할 권한이 없습니다."),
    _USER_NOT_COMMENT_DELETE_AUTH(HttpStatus.BAD_REQUEST, "AUTH_4004", "댓글을 삭제할 권한이 없습니다."),
    _NOT_FOUND_COMMENT(HttpStatus.BAD_REQUEST, "COMMENT_4001", "존재하지 않는 댓글입니다."),

    // 게시글 관련 에러
    _POST_NOT_FOUND(HttpStatus.BAD_REQUEST, "POST_4001", "존재하지 않는 게시글입니다."),
    _POST_ALREADY_LIKED(HttpStatus.BAD_REQUEST, "POST_4002", "이미 좋아요를 누른 게시글입니다."),
    _POST_LIKE_NOT_FOUND(HttpStatus.BAD_REQUEST, "POST_4003", "게시글에 좋아요를 누르지 않은 상태입니다."),
    _POST_ALREADY_BOOKMARKED(HttpStatus.BAD_REQUEST, "POST_4004", "이미 북마크를 누른 게시글입니다."),
    _POST_BOOKMARKED_NOT_FOUND(HttpStatus.BAD_REQUEST, "POST_4005", "북마크를 누르지 않은 상태입니다.");












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
