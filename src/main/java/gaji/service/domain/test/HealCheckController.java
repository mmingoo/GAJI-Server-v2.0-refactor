package gaji.service.domain.test;

import gaji.service.global.base.BaseResponse;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class HealCheckController {

    @GetMapping("/health-check")
    public BaseResponse healthCheck() {
        return BaseResponse.onSuccess(null);
    }
}
