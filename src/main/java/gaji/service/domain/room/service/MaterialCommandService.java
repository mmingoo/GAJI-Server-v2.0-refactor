package gaji.service.domain.room.service;

import gaji.service.domain.room.entity.Material;
import gaji.service.domain.room.entity.Room;

public interface MaterialCommandService {
    void saveMaterial(Material material);

    void deleteAllByRoom(Room room);
}
