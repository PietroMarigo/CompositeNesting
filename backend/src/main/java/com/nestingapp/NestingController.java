package com.nestingapp;

import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * REST endpoints for nesting operations.
 */
@RestController
@RequestMapping("/api")
public class NestingController {

    private final NestingService nestingService;

    public NestingController(NestingService nestingService) {
        this.nestingService = nestingService;
    }

    @PostMapping("/nest")
    public String nest() {
        return nestingService.nest();
    }
}
