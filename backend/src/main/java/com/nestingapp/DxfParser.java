package com.nestingapp;

import java.util.ArrayList;
import java.util.List;

import org.locationtech.jts.geom.Coordinate;
import org.locationtech.jts.geom.Geometry;
import org.locationtech.jts.geom.GeometryFactory;
import org.locationtech.jts.geom.Polygon;

/**
 * Very small DXF parser supporting extraction of simple LWPOLYLINE entities
 * into JTS polygons. The implementation is intentionally minimal and only
 * handles XY coordinates.
 */
public class DxfParser {

    /**
     * Parses DXF content and returns a geometry collection of any discovered
     * polygons.
     */
    public Geometry parse(String dxfContent) {
        String[] lines = dxfContent.split("\r?\n");
        List<Polygon> polygons = new ArrayList<>();
        List<Coordinate> current = null;
        for (int i = 0; i < lines.length; i++) {
            String line = lines[i].trim();
            if ("LWPOLYLINE".equalsIgnoreCase(line)) {
                current = new ArrayList<>();
            } else if (current != null) {
                if ("0".equals(line)) {
                    if (!current.isEmpty()) {
                        polygons.add(GeometryUtils.createPolygon(current));
                    }
                    current = null;
                } else if ("10".equals(line) && i + 3 < lines.length) {
                    double x = Double.parseDouble(lines[++i].trim());
                    String code = lines[++i].trim();
                    if ("20".equals(code)) {
                        double y = Double.parseDouble(lines[++i].trim());
                        current.add(new Coordinate(x, y));
                    }
                }
            }
        }
        GeometryFactory gf = GeometryUtils.factory();
        return gf.createGeometryCollection(polygons.toArray(new Geometry[0]));
    }
}
