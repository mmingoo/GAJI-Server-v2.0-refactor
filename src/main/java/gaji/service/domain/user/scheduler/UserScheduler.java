package gaji.service.domain.user.scheduler;

import gaji.service.domain.user.service.UserCommandService;
import lombok.RequiredArgsConstructor;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class UserScheduler {

    private final UserCommandService userCommandService;

    @Scheduled(cron = "0 0 0 * * ?")
    public void removeDeletedUsers() {
        userCommandService.hardDeleteInactiveUsers();
    }
}
