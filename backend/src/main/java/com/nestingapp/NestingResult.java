package com.nestingapp;

import java.util.List;

/**
 * DTOs used by the REST API for exchanging nesting payloads.
 */
public record NestingResult(List<Placement> nestedParts, SheetSpec sheet) {}

record NestingConfig(double spacing, double rotationStep, double sheetWidth, double sheetHeight, int maxNoImprovement) {}

record Placement(String id, double x, double y, double rotation) {}

record SheetSpec(double width, double height) {}

record ExportRequest(String format, NestingResult layout) {}

record StatusResponse(String status, int progress, String estimatedTime) {}
