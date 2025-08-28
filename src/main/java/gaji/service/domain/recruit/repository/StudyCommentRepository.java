package gaji.service.domain.recruit.repository;

import gaji.service.domain.recruit.entity.StudyComment;
import gaji.service.domain.room.entity.Room;
import org.springframework.data.jpa.repository.JpaRepository;

public interface StudyCommentRepository extends JpaRepository<StudyComment, Long>, StudyCommentCustomRepository {

    int countByRoom(Room room);

    void deleteAllByRoom(Room room);
}
