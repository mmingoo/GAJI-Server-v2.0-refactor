package gaji.service.domain.room.converter;

import gaji.service.domain.room.entity.RoomEvent;
import gaji.service.domain.room.entity.RoomNotice;
import gaji.service.domain.room.web.dto.RoomResponseDto;
import gaji.service.domain.studyMate.entity.Assignment;

public class RoomConverter {

    public static RoomResponseDto.AssignmentDto toAssignmentDto(Assignment assignment) {
        return RoomResponseDto.AssignmentDto.builder()
                .id(assignment.getId())
                .body(assignment.getBody())
                .build();
    }

    public static RoomResponseDto.RoomNoticeDto toRoomNoticeDto(RoomNotice roomNotice) {
        return RoomResponseDto.RoomNoticeDto.builder()
                .noticeId(roomNotice.getId())
                .build();
    }

    public static RoomResponseDto.roomEventIdDto toRoomEventIdDto(RoomEvent roomEvent) {
        return RoomResponseDto.roomEventIdDto.builder()
                .roomEventId(roomEvent.getId())
                .build();
    }


    public static RoomResponseDto.AssignmentIdDto toAssignmentIdDto(Assignment assignment) {
        return RoomResponseDto.AssignmentIdDto.builder()
                .assignmentId(assignment.getId())
                .build();
    }


    public static RoomResponseDto.NoticeIdDto toNoticeIdDto(RoomNotice roomNotice) {
        return RoomResponseDto.NoticeIdDto.builder()
                .noticeId(roomNotice.getId())
                .build();
    }
}
