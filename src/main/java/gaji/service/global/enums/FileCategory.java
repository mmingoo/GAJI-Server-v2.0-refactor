package gaji.service.global.enums;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum FileCategory {
    PROFILE("profile"), BLOG("blog"), PROJECT("project"), QUESTION("question"), STUDY("study"), ETC("etc");
    private final String category;
}
