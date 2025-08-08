package com.nestingapp;

import static org.junit.jupiter.api.Assertions.assertEquals;

import java.util.List;

import org.junit.jupiter.api.Test;
import org.locationtech.jts.geom.Coordinate;
import org.locationtech.jts.geom.Geometry;
import org.locationtech.jts.geom.Polygon;

/**
 * Basic tests for the NFP based nesting service.
 */
public class NestingServiceTest {

    private final NestingService service = new NestingService();

    @Test
    void nestsTwoSquaresSideBySide() {

        Geometry square = GeometryUtils.createPolygon(List.of(
            new Coordinate(0, 0),
            new Coordinate(1, 0),
            new Coordinate(1, 1),
            new Coordinate(0, 1)
        ));
        Polygon sheet = GeometryUtils.createSheet(10, 10);
        Geometry layout = service.nest(List.of(square, square), sheet, 1);
        assertEquals(2.0, layout.getEnvelope().getArea(), 1e-6);
    }

    @Test
    void nestsRectangleAndSquare() {
        Geometry rect = GeometryUtils.createPolygon(List.of(
            new Coordinate(0, 0),
            new Coordinate(2, 0),
            new Coordinate(2, 1),
            new Coordinate(0, 1)
        ));
        Geometry square = GeometryUtils.createPolygon(List.of(
            new Coordinate(0, 0),
            new Coordinate(1, 0),
            new Coordinate(1, 1),
            new Coordinate(0, 1)
        ));
        Polygon sheet = GeometryUtils.createSheet(10, 10);
        Geometry layout = service.nest(List.of(rect, square), sheet, 1);
        assertEquals(3.0, layout.getEnvelope().getArea(), 1e-6);
    }
}
