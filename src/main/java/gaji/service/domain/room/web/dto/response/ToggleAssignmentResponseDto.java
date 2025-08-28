package gaji.service.domain.room.web.dto.response;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

import java.util.List;

@Builder
@Getter
@AllArgsConstructor
@JsonInclude(JsonInclude.Include.NON_NULL)
public class ToggleAssignmentResponseDto {
    private List<ToggleAssignment> assignments;

    public record ToggleAssignment(
            Long assignmentId,
            String content,
            boolean completedStatus
    ) {};
}
