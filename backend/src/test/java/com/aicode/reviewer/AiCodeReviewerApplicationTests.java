package com.aicode.reviewer;

import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;

@SpringBootTest(classes = AiCodeReviewerApplication.class)
@ActiveProfiles("test")
class AiCodeReviewerApplicationTests {

    @Test
    void contextLoads() {
    }
}
