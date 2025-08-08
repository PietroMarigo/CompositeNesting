package com.nestingapp;

import static org.junit.jupiter.api.Assertions.*;

import java.util.Arrays;

import org.junit.jupiter.api.Test;
import org.locationtech.jts.geom.Coordinate;
import org.locationtech.jts.geom.Geometry;
import org.locationtech.jts.geom.Polygon;

class GeometryCoreTests {

    @Test
    void polygonHelpersWork() {
        Polygon square = GeometryUtils.createPolygon(Arrays.asList(
            new Coordinate(0, 0),
            new Coordinate(1, 0),
            new Coordinate(1, 1),
            new Coordinate(0, 1)
        ));
        Geometry rotated = GeometryUtils.rotate(square, 90);
        Geometry spaced = GeometryUtils.applySpacing(square, 0.1);
        assertFalse(rotated.isEmpty());
        assertTrue(spaced.getArea() > square.getArea());
    }

    @Test
    void svgParserParsesPolygon() throws Exception {
        String svg = "<svg><polygon points='0,0 1,0 1,1 0,1'/></svg>";
        Geometry geom = new SvgParser().parseToGeometry(svg);
        assertEquals(1, geom.getNumGeometries());
    }

    @Test
    void dxfParserParsesPolyline() {
        String dxf = String.join("\n",
            "0", "SECTION", "2", "ENTITIES",
            "0", "LWPOLYLINE",
            "10", "0", "20", "0",
            "10", "1", "20", "0",
            "10", "1", "20", "1",
            "10", "0", "20", "1",
            "0",
            "0", "ENDSEC", "0", "EOF"
        );
        Geometry geom = new DxfParser().parse(dxf);
        assertEquals(1, geom.getNumGeometries());
    }
}
