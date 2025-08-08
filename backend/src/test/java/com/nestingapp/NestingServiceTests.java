package com.nestingapp;

import static org.junit.jupiter.api.Assertions.*;

import java.util.List;

import org.junit.jupiter.api.Test;
import org.locationtech.jts.geom.Coordinate;
import org.locationtech.jts.geom.Geometry;
import org.locationtech.jts.geom.Polygon;

class NestingServiceTests {

    @Test
    void nestsSquaresWithoutOverlap() {
        Geometry square1 = GeometryUtils.createPolygon(List.of(
            new Coordinate(0, 0),
            new Coordinate(1, 0),
            new Coordinate(1, 1),
            new Coordinate(0, 1)
        ));
        Geometry square2 = GeometryUtils.createPolygon(List.of(
            new Coordinate(0, 0),
            new Coordinate(1, 0),
            new Coordinate(1, 1),
            new Coordinate(0, 1)
        ));
        Polygon sheet = GeometryUtils.createSheet(10, 10);
        Geometry layout = new NestingService().nest(List.of(square1, square2), sheet, 5);
        assertEquals(2.0, layout.getArea(), 1e-9);
        assertEquals(2.0, layout.getEnvelope().getArea(), 1e-9);
    }
}
