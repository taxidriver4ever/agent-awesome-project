package org.example.agentprojectjava;

import jakarta.annotation.Resource;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.web.client.RestTemplate;

import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.time.ZoneId;

@SpringBootTest
class AgentProjectJavaApplicationTests {

    @Resource
    private RedisTemplate<String, String> redisTemplate;

    @Test
    void contextLoads() {
        System.out.println(System.currentTimeMillis());
    }

}
