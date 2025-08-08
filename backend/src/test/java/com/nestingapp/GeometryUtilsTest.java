package com.nestingapp;

import static org.junit.jupiter.api.Assertions.assertEquals;

import java.util.List;

import org.junit.jupiter.api.Test;
import org.locationtech.jts.geom.Coordinate;
import org.locationtech.jts.geom.Geometry;

/**
 * Tests for geometry helper utilities such as Minkowski sums and NFPs.
 */
public class GeometryUtilsTest {

    @Test
    void computesNfpViaMinkowskiSum() {
        Geometry square = GeometryUtils.createPolygon(List.of(
            new Coordinate(0, 0),
            new Coordinate(1, 0),
            new Coordinate(1, 1),
            new Coordinate(0, 1)
        ));
        Geometry nfp = GeometryUtils.noFitPolygon(square, square);
        assertEquals(2.0, nfp.getEnvelopeInternal().getWidth(), 1e-6);
        assertEquals(2.0, nfp.getEnvelopeInternal().getHeight(), 1e-6);
    }
}
