package com.nestingapp;

import org.locationtech.jts.geom.Geometry;
import org.locationtech.jts.geom.GeometryFactory;

/**
 * Utility helpers for geometry operations. Currently only exposes a
 * geometry factory as a placeholder for future logic.
 */
public final class GeometryUtils {
    private static final GeometryFactory FACTORY = new GeometryFactory();

    private GeometryUtils() {
        // utility class
    }

    /**
     * Creates an empty geometry collection. Placeholder until real
     * SVG/DXF parsing is implemented.
     */
    public static Geometry emptyGeometry() {
        return FACTORY.createGeometryCollection(null);
    }
}
