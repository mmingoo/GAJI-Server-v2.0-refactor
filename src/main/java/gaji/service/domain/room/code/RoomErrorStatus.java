package gaji.service.domain.room.code;

import gaji.service.global.exception.code.BaseCodeDto;
import gaji.service.global.exception.code.BaseErrorCodeInterface;
import lombok.AllArgsConstructor;
import lombok.Getter;
import org.springframework.http.HttpStatus;


@Getter
@AllArgsConstructor
public enum RoomErrorStatus implements BaseErrorCodeInterface {
    // 스터디룸
    _ROOM_NOT_FOUND(HttpStatus.BAD_REQUEST, "ROOM_4001", "존재하지 않는 스터디룸입니다."),

    // 주차
    _WEEK_NOT_VALID(HttpStatus.BAD_REQUEST, "ROOM_4002", "유효하지 않은 형식의 주차입니다."),

    // 스터디룸 멤버 관련 오류
    _USER_NOT_FOUND(HttpStatus.BAD_REQUEST, "ROOM_4003","사용자를 찾을 수 없습니다."),
    _USER_NOT_IN_ROOM(HttpStatus.BAD_REQUEST, "ROOM_4004","사용자가 해당 스터디룸에 참여하고 있지 않습니다."),

    _USER_NOT_READER_IN_ROOM(HttpStatus.BAD_REQUEST, "ROOM_4005","사용자가 해당 스터디룸의 방장이 아닙니다."),

    _ROOM_EVENT_NOT_FOUND(HttpStatus.BAD_REQUEST, "ROOM_4006","스터디룸의 일정을 찾을 수 없습니다."),

    _NOTICE_NOT_FOUND(HttpStatus.BAD_REQUEST, "ROOM_4007"," 공지사항울 찾을 수 없습니다."),


    _ASSIGNMENT_NOT_FOUND(HttpStatus.BAD_REQUEST, "ASSIGNMENT_4001"," 과제를 찾을 수 없습니다."),
    _INTERNAL_SERVER_ERROR(HttpStatus.BAD_REQUEST, "SERVER_4001"," 에러"),
    _USER_NOT_DELETE_AUTHORITY(HttpStatus.BAD_REQUEST, "USER_4001"," 사용자 삭제 권한이 없습니다.")
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
