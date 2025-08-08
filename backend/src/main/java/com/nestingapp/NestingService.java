package com.nestingapp;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import org.locationtech.jts.geom.Envelope;
import org.locationtech.jts.geom.Geometry;
import org.locationtech.jts.geom.Polygon;
import org.locationtech.jts.geom.util.AffineTransformation;
import org.springframework.stereotype.Service;

/**
 * Core nesting logic facade. Provides a naive row-based nesting algorithm
 * which iteratively searches for a layout with the smallest bounding box.
 */
@Service
public class NestingService {

    /**
     * Nests the provided parts onto the given sheet. The algorithm shuffles the
     * parts and places them in rows, keeping the layout with the smallest
     * bounding box area. Iteration stops after {@code maxNoImprovement}
     * consecutive rounds without an improvement.
     */
    public Geometry nest(List<Geometry> parts, Polygon sheet, int maxNoImprovement) {
        if (parts.isEmpty()) {
            return GeometryUtils.emptyGeometry();
        }

        double sheetWidth = sheet.getEnvelopeInternal().getWidth();
        List<Geometry> shuffled = new ArrayList<>(parts);
        Geometry bestLayout = GeometryUtils.emptyGeometry();
        double bestScore = Double.MAX_VALUE;
        int noImprovement = 0;

        while (noImprovement < maxNoImprovement) {
            Collections.shuffle(shuffled);
            List<Geometry> placed = new ArrayList<>();
            double cursorX = 0;
            double cursorY = 0;
            double rowHeight = 0;

            for (Geometry part : shuffled) {
                Envelope env = part.getEnvelopeInternal();
                double width = env.getWidth();
                double height = env.getHeight();

                if (cursorX + width > sheetWidth) {
                    cursorX = 0;
                    cursorY += rowHeight;
                    rowHeight = 0;
                }

                AffineTransformation move = AffineTransformation.translationInstance(
                        cursorX - env.getMinX(),
                        cursorY - env.getMinY());
                Geometry positioned = move.transform(part);
                placed.add(positioned);

                cursorX += width;
                rowHeight = Math.max(rowHeight, height);
            }

            Geometry layout = GeometryUtils.factory().buildGeometry(placed);
            double score = layout.getEnvelope().getArea();
            if (score < bestScore) {
                bestScore = score;
                bestLayout = layout;
                noImprovement = 0;
            } else {
                noImprovement++;
            }
        }

        return bestLayout;
    }

    /**
     * Temporary convenience method returning a simple nested layout in WKT
     * format. This allows the controller to function until real payload
     * handling is implemented.
     */
    public String nest() {
        Geometry square = GeometryUtils.createPolygon(List.of(
            new org.locationtech.jts.geom.Coordinate(0, 0),
            new org.locationtech.jts.geom.Coordinate(1, 0),
            new org.locationtech.jts.geom.Coordinate(1, 1),
            new org.locationtech.jts.geom.Coordinate(0, 1)
        ));
        Geometry layout = nest(List.of(square, square), GeometryUtils.createSheet(10, 10), 5);
        return layout.toText();
    }
}

