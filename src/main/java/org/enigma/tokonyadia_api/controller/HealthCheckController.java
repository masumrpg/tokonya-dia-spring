package org.enigma.tokonyadia_api.controller;

import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

@RestController
@RequestMapping("/")
public class HealthCheckController {

    private final JdbcTemplate jdbcTemplate;
    private final RedisTemplate<String, String> redisTemplate;

    public HealthCheckController(JdbcTemplate jdbcTemplate, RedisTemplate<String, String> redisTemplate) {
        this.jdbcTemplate = jdbcTemplate;
        this.redisTemplate = redisTemplate;
    }

    @GetMapping
    public ResponseEntity<Map<String, Object>> healthCheck() {
        Map<String, Object> health = new HashMap<>();
        health.put("status", "UP");
        health.put("timestamp", new Date());

        // Check PostgreSQL
        try {
            jdbcTemplate.queryForObject("SELECT 1", Integer.class);
            health.put("postgresql", Map.of(
                    "status", "UP",
                    "message", "Database is responding normally"
            ));
        } catch (Exception e) {
            health.put("postgresql", Map.of(
                    "status", "DOWN",
                    "message", "Database connection failed: " + e.getMessage()
            ));
            health.put("status", "DOWN");
        }

        // Check Redis
        try {
            Objects.requireNonNull(redisTemplate.getConnectionFactory()).getConnection().ping();
            health.put("redis", Map.of(
                    "status", "UP",
                    "message", "Redis is responding normally"
            ));
        } catch (Exception e) {
            health.put("redis", Map.of(
                    "status", "DOWN",
                    "message", "Redis connection failed: " + e.getMessage()
            ));
            health.put("status", "DOWN");
        }

        health.put("app", "Tokonyadia");


        HttpStatus httpStatus = "UP".equals(health.get("status")) ?
                HttpStatus.OK : HttpStatus.SERVICE_UNAVAILABLE;

        return new ResponseEntity<>(health, httpStatus);
    }
}
