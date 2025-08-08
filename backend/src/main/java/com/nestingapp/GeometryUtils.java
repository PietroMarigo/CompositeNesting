package com.nestingapp;

import java.util.List;

import org.locationtech.jts.geom.Coordinate;
import org.locationtech.jts.geom.Geometry;
import org.locationtech.jts.geom.GeometryFactory;
import org.locationtech.jts.geom.Polygon;
import org.locationtech.jts.geom.util.AffineTransformation;

/**
 * Utility helpers for geometry operations. Provides basic polygon creation,
 * no-fit polygon (NFP) calculation, spacing, rotation and simple sheet helpers
 * using the JTS topology suite.
 */
public final class GeometryUtils {
    private static final GeometryFactory FACTORY = new GeometryFactory();

    private GeometryUtils() {
        // utility class
    }

    /**
     * Creates an empty geometry collection.
     */
    public static Geometry emptyGeometry() {
        return FACTORY.createGeometryCollection(null);
    }

    /**
     * Returns a shared {@link GeometryFactory} instance.
     */
    public static GeometryFactory factory() {
        return FACTORY;
    }

    /**
     * Creates a {@link Polygon} from the provided coordinates. The first and
     * last coordinate will be closed automatically if necessary.
     */
    public static Polygon createPolygon(List<Coordinate> coordinates) {
        if (coordinates.isEmpty()) {
            throw new IllegalArgumentException("Polygon requires at least one coordinate");
        }
        List<Coordinate> coords = new java.util.ArrayList<>(coordinates);
        if (!coords.get(0).equals2D(coords.get(coords.size() - 1))) {
            coords.add(coords.get(0));
        }
        Coordinate[] array = coords.toArray(new Coordinate[0]);
        return FACTORY.createPolygon(array);
    }

    /**
     * Computes a very simple no-fit polygon by subtracting the moving geometry
     * from the base geometry. This is a placeholder for a proper NFP
     * implementation.
     */
    public static Geometry computeNoFitPolygon(Geometry base, Geometry moving) {
        return base.difference(moving);
    }

    /**
     * Applies a spacing/offset to the given geometry. Positive values expand,
     * negative values contract.
     */
    public static Geometry applySpacing(Geometry geometry, double spacing) {
        return geometry.buffer(spacing);
    }

    /**
     * Rotates the geometry by the given angle in degrees around the origin.
     */
    public static Geometry rotate(Geometry geometry, double angleDegrees) {
        double radians = Math.toRadians(angleDegrees);
        AffineTransformation rotation = AffineTransformation.rotationInstance(radians);
        return rotation.transform(geometry);
    }

    /**
     * Creates a rectangular material sheet polygon starting at the origin.
     */
    public static Polygon createSheet(double width, double height) {
        return createPolygon(List.of(
            new Coordinate(0, 0),
            new Coordinate(width, 0),
            new Coordinate(width, height),
            new Coordinate(0, height)
        ));
    }
}
