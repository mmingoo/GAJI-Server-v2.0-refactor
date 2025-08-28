package gaji.service.domain.studyMate.code;

import gaji.service.global.exception.code.BaseCodeDto;
import gaji.service.global.exception.code.BaseErrorCodeInterface;
import lombok.AllArgsConstructor;
import lombok.Getter;
import org.springframework.http.HttpStatus;


@Getter
@AllArgsConstructor
public enum StudyMateErrorStatus implements BaseErrorCodeInterface {
    // 스터디룸 게시판
    _USER_NOT_IN_STUDYROOM(HttpStatus.BAD_REQUEST, "StudyMateError_4001", "회원이 해당 스터디룸에 참여하고 있지 않습니다."),
    _USER_ALREADY_JOIN(HttpStatus.BAD_REQUEST, "StudyMateError_4002", "이미 참여중인 회원입니다."),
    _ONLY_LEADER_POSSIBLE(HttpStatus.FORBIDDEN, "StudyMateError_4003", "스터디 리더만 가능한 작업입니다."),
    _LEADER_IMPOSSIBLE_LEAVE(HttpStatus.BAD_REQUEST, "StudyMateError_4004", "스터디 리더는 나갈 수 없습니다."),
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