package com.nestingapp;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import org.locationtech.jts.geom.Coordinate;
import org.locationtech.jts.geom.Envelope;
import org.locationtech.jts.geom.Geometry;
import org.locationtech.jts.geom.Polygon;
import org.locationtech.jts.geom.util.AffineTransformation;
import org.springframework.stereotype.Service;

/**
 * Core nesting logic facade. Provides a simple NFP based search that positions
 * parts along no-fit polygon boundaries and applies a small hill climbing
 * refinement of rotation angles.
 */
@Service
public class NestingService {

    /**
     * Nests the provided parts onto the given sheet. Parts are shuffled and
     * placed sequentially by searching along NFP boundaries for the position
     * that yields the smallest bounding box. After an initial placement a hill
     * climbing refinement is applied which perturbs rotation angles by small
     * steps and repositions the parts if an improvement is found. Iteration
     * stops after {@code maxNoImprovement} consecutive rounds without a better
     * layout.
     */
    public Geometry nest(List<Geometry> parts, Polygon sheet, int maxNoImprovement) {
        return nest(parts, sheet, 0, 15, maxNoImprovement);
    }

    /**
     * Nests the provided parts with configurable spacing and rotation step.
     */
    public Geometry nest(List<Geometry> parts, Polygon sheet, double spacing, double rotationStep, int maxNoImprovement) {
        List<Geometry> placed = nestParts(parts, sheet, spacing, rotationStep, maxNoImprovement);
        return GeometryUtils.factory().buildGeometry(placed);
    }

    /**
     * Nests the provided parts and returns the individual placed geometries.
     * This is useful for callers that need part-level placement data rather
     * than a combined layout geometry.
     */
    public List<Geometry> nestParts(List<Geometry> parts, Polygon sheet, int maxNoImprovement) {
        return nestParts(parts, sheet, 0, 15, maxNoImprovement);
    }

    /**
     * Nests the provided parts and returns the individual placed geometries.
     * Parts are first buffered by the given spacing before placement.
     */
    public List<Geometry> nestParts(List<Geometry> parts, Polygon sheet, double spacing, double rotationStep, int maxNoImprovement) {
        if (parts.isEmpty()) {
            return List.of();
        }

        List<Geometry> buffered = new ArrayList<>();
        for (Geometry part : parts) {
            buffered.add(GeometryUtils.applySpacing(part, spacing));
        }

        List<Geometry> shuffled = new ArrayList<>(buffered);
        List<Geometry> bestPlaced = new ArrayList<>();
        double bestScore = Double.MAX_VALUE;
        int noImprovement = 0;

        while (noImprovement < maxNoImprovement) {
            Collections.shuffle(shuffled);
            List<Geometry> placed = new ArrayList<>();
            // place first part at origin
            Geometry first = translateToOrigin(shuffled.get(0));
            placed.add(first);

            for (int i = 1; i < shuffled.size(); i++) {
                Geometry part = shuffled.get(i);
                Geometry positioned = placePart(part, placed);
                placed.add(positioned);
            }

            List<Geometry> improved = hillClimb(placed, rotationStep);
            double score = layoutArea(improved);
            if (score < bestScore) {
                bestScore = score;
                bestPlaced = improved;
                noImprovement = 0;
            } else {
                noImprovement++;
            }
        }

        return bestPlaced;
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

    /**
     * Places {@code part} relative to already {@code placed} parts by exploring
     * all vertices of the pairwise NFPs. The candidate yielding the smallest
     * bounding box is returned. If no valid position is found the part is
     * returned translated to the origin.
     */
    private Geometry placePart(Geometry part, List<Geometry> placed) {
        Geometry best = null;
        double bestArea = Double.MAX_VALUE;

        for (Geometry p : placed) {
            Geometry nfp = GeometryUtils.noFitPolygon(p, part);
            Coordinate[] coords = nfp.getCoordinates();
            for (int i = 0; i < coords.length - 1; i++) {
                Coordinate vertex = coords[i];
                Coordinate next = coords[i + 1];
                Coordinate midpoint = new Coordinate(
                    (vertex.x + next.x) / 2.0,
                    (vertex.y + next.y) / 2.0
                );
                for (Coordinate c : new Coordinate[] { vertex, midpoint }) {
                    AffineTransformation move = AffineTransformation.translationInstance(c.x, c.y);
                    Geometry candidate = move.transform(part);
                    if (intersectsAny(candidate, placed)) {
                        continue;
                    }
                    List<Geometry> temp = new ArrayList<>(placed);
                    temp.add(candidate);
                    double area = layoutArea(temp);
                    if (area < bestArea) {
                        bestArea = area;
                        best = candidate;
                    }
                }
            }
        }

        return best != null ? best : translateToOrigin(part);
    }

    private boolean intersectsAny(Geometry g, List<Geometry> geoms) {
        for (Geometry other : geoms) {
            if (g.intersects(other) && !g.touches(other)) {
                return true;
            }
        }
        return false;
    }

    private Geometry translateToOrigin(Geometry g) {
        Envelope env = g.getEnvelopeInternal();
        AffineTransformation move = AffineTransformation.translationInstance(-env.getMinX(), -env.getMinY());
        return move.transform(g);
    }

    /**
     * Simple hill climbing over rotation angles. Each part is rotated by the
     * given {@code rotationStep} in both directions and repositioned; improvements
     * are kept until no further reduction of the layout bounding box area is found.
     */
    private List<Geometry> hillClimb(List<Geometry> parts, double rotationStep) {
        List<Geometry> placed = new ArrayList<>(parts);
        double bestArea = layoutArea(placed);
        boolean improved = true;
        while (improved) {
            improved = false;
            for (int i = 0; i < placed.size(); i++) {
                Geometry original = placed.get(i);
                List<Geometry> others = new ArrayList<>(placed);
                others.remove(i);
                for (double angle : new double[] { -rotationStep, rotationStep }) {
                    Geometry rotated = GeometryUtils.rotate(original, angle);
                    Geometry repositioned = placePart(rotated, others);
                    List<Geometry> candidate = new ArrayList<>(others);
                    candidate.add(repositioned);
                    double area = layoutArea(candidate);
                    if (area < bestArea) {
                        placed = candidate;
                        bestArea = area;
                        improved = true;
                        break;
                    }
                }
            }
        }
        return placed;
    }

    private double layoutArea(List<Geometry> geoms) {
        Geometry layout = GeometryUtils.factory().buildGeometry(geoms);
        return layout.getEnvelope().getArea();
    }
}

