package gaji.service.domain.room.web.dto.response;

import lombok.AllArgsConstructor;
import lombok.Builder;

import java.util.List;

@Builder
@AllArgsConstructor
public class ToggleAssignmentResponseDto {
    private List<ToggleAssignment> assignments;

    record ToggleAssignment(
            String content, boolean completedStatus
    ) {};
}
