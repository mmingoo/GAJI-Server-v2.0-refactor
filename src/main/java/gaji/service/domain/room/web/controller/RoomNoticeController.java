package gaji.service.domain.room.web.controller;

import gaji.service.domain.room.converter.RoomConverter;
import gaji.service.domain.room.entity.RoomNotice;
import gaji.service.domain.room.service.RoomCommandService;
import gaji.service.domain.room.service.RoomQueryService;
import gaji.service.domain.room.web.dto.RoomRequestDto;
import gaji.service.domain.room.web.dto.RoomResponseDto;
import gaji.service.global.base.BaseResponse;
import gaji.service.jwt.service.TokenProviderService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RequestMapping("/api/study-rooms")
@RequiredArgsConstructor
@RestController
@Tag(name = "스터디룸 공지 APi", description = "스터디룸 공지 API")
public class RoomNoticeController {


    private final RoomCommandService roomCommandService;
    private final RoomQueryService roomQueryService;
    private final TokenProviderService tokenProviderService;


    @PostMapping("/notices/{roomId}")
    @Operation(summary = "스터디룸 공지 등록 API",description = "스터디룸의 공지를 등록하는 API입니다. room의 id가 존재하는지 검증합니다.")
    public BaseResponse<RoomResponseDto.RoomNoticeDto> NoticeController(
            @RequestBody @Valid RoomRequestDto.RoomNoticeDto requestDto,
            @RequestHeader("Authorization") String authorizationHeader,
            @PathVariable Long roomId) {

        Long userId = tokenProviderService.getUserIdFromToken(authorizationHeader);
        RoomNotice roomNotice = roomCommandService.createNotice(roomId, userId, requestDto);

        return BaseResponse.onSuccess(RoomConverter.toRoomNoticeDto(roomNotice));
    }

    @GetMapping("/{roomId}/notices")
    @Operation(summary = "스터디룸 공지 무한 스크롤 조회", description = "공지를 무한 스크롤 방식으로 조회합니다.")
    @ApiResponse(responseCode = "200", description = "조회 성공")
    public BaseResponse<List<RoomResponseDto.NoticeDto>> getNextNotices(
            @PathVariable @Parameter(description = "스터디룸 ID") Long roomId,
            @RequestParam @Parameter(description = "마지막으로 로드된 공지 ID") Long lastNoticeId,
            @RequestParam(defaultValue = "5") @Parameter(description = "조회할 공지 수") int size) {
        List<RoomResponseDto.NoticeDto> notices = roomQueryService.getNextNotices(roomId, lastNoticeId, size);
        return BaseResponse.onSuccess(notices);
    }


//    @GetMapping("/notice/{noticeId}")
//    @Operation(summary = "특정 공지사항을 조회하는 API")
//    public ResponseEntity<RoomResponseDto.NoticeDto> getNoticeDetail(
//            @PathVariable Long roomId,
//            @PathVariable Long noticeId) {
//        RoomResponseDto.NoticeDto notice = roomQueryService.getNoticeDetail(roomId, noticeId);
//        return ResponseEntity.ok(notice);
//    }


    @PostMapping("/{roomId}notices/{noticeId}/confirm/{userId}")
    @Operation(summary = "스터디룸 공지 확인 버튼 누르기 API", description = "공지사항 확인 상태를 토글합니다.")
    public BaseResponse<RoomResponseDto.IsConfirmedResponse> toggleNoticeConfirmation(
            @PathVariable Long roomId,
            @PathVariable Long noticeId,
            @RequestHeader("Authorization") String authorizationHeader) {

        Long userId = tokenProviderService.getUserIdFromToken(authorizationHeader);
        boolean isConfirmed = roomCommandService.toggleNoticeConfirmation(roomId,noticeId,userId);

        return BaseResponse.onSuccess(
                new RoomResponseDto.IsConfirmedResponse(isConfirmed)
        );
    }

    @GetMapping("/notices/{noticeId}/confirmed-users")
    @Operation(summary = "스터디룸 공지 확인 버튼 누른 회원 조회 API", description = "공지사항 확인버튼을 누른 사람을 조회합니다..")

    public BaseResponse<List<String>> getConfirmedUserNicknames(@PathVariable Long noticeId) {
        List<String> confirmedNicknames = roomQueryService.getConfirmedUserNicknames(noticeId);
        return BaseResponse.onSuccess(confirmedNicknames);
    }

    @PutMapping("/notice/{noticeId}/update")
    @Operation(summary = "공지사항 수정 API")
    public BaseResponse<RoomResponseDto.NoticeIdDto> updateNotice(
            @PathVariable Long noticeId,
            @RequestHeader("Authorization") String authorization,
            @RequestBody RoomRequestDto.AssignmentUpdateDTO request
    ) {
        Long userId = tokenProviderService.getUserIdFromToken(authorization);
        RoomNotice notice = roomCommandService.updateRoomNotice(noticeId,userId,request.getDescription());
        return BaseResponse.onSuccess(RoomConverter.toNoticeIdDto(notice));
    }

    @DeleteMapping("/notice/{noticeId}/delete")
    @Operation(summary = "공지사항 삭제 API")
    public BaseResponse<String> deleteNotice(
            @PathVariable Long noticeId,
            @RequestHeader("Authorization") String authorization
    ) {
        Long userId = tokenProviderService.getUserIdFromToken(authorization);
        roomCommandService.deleteRoomNotice(noticeId,userId);
        return BaseResponse.onSuccess("delete complete");
    }

}
