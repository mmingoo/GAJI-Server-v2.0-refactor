package gaji.service.domain.room.service;

import gaji.service.domain.room.entity.Room;
import gaji.service.domain.room.entity.RoomEvent;
import gaji.service.domain.room.entity.RoomNotice;
import gaji.service.domain.room.web.dto.RoomResponseDto;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

public interface RoomQueryService {

    Room findRoomById(Long roomId);

    RoomEvent findRoomEventByRoomIdAndWeeks(Long roomId, Integer weeks);

//    List<RoomResponseDto.NoticeDto> getNotices(Long roomId, int page, int size);
//
//    RoomResponseDto.NoticeDto getNoticeDetail(Long roomId, Long noticeId);
//
//    List<RoomResponseDto.NoticeDto> getNextNotices(Long roomId, Long lastNoticeId, int size);

    List<RoomResponseDto.NoticeDto> getNextNotices(Long roomId, Long lastNoticeId, int size);

    @Transactional(readOnly = true)
    RoomResponseDto.WeeklyStudyInfoDTO getWeeklyStudyInfo(Long roomId, Integer weeks);

    List<RoomResponseDto.UserProgressDTO> getUserProgressByRoomEventId(Long roomId, Integer weeks);

    RoomResponseDto.RoomMainDto getMainStudyRoom(Long roomId);

    RoomResponseDto.MainRoomNoticeDto getMainRoomNotice(Long roomId);

    List<String> getConfirmedUserNicknames(Long noticeId);

    RoomNotice findRoomNoticeById(Long noticeId);
}
