package com.nestingapp;

import java.io.StringReader;
import java.util.ArrayList;
import java.util.List;

import org.apache.batik.anim.dom.SAXSVGDocumentFactory;
import org.apache.batik.util.XMLResourceDescriptor;
import org.locationtech.jts.geom.Coordinate;
import org.locationtech.jts.geom.Geometry;
import org.locationtech.jts.geom.GeometryFactory;
import org.locationtech.jts.geom.Polygon;
import org.w3c.dom.Element;
import org.w3c.dom.NodeList;
import org.w3c.dom.svg.SVGDocument;

/**
 * Simple SVG parser built on Apache Batik. Supports extracting polygons from
 * the document into JTS geometries.
 */
public class SvgParser {

    /**
     * Parses raw SVG content into an {@link SVGDocument}.
     */
    public SVGDocument parse(String svgContent) throws Exception {
        String parser = XMLResourceDescriptor.getXMLParserClassName();
        SAXSVGDocumentFactory factory = new SAXSVGDocumentFactory(parser);
        return factory.createSVGDocument("", new StringReader(svgContent));
    }

    /**
     * Parses the provided SVG string into a JTS {@link Geometry} collection. At
     * present only <polygon> elements are supported.
     */
    public Geometry parseToGeometry(String svgContent) throws Exception {
        SVGDocument doc = parse(svgContent);
        NodeList polyNodes = doc.getElementsByTagName("polygon");
        List<Polygon> polys = new ArrayList<>();
        for (int i = 0; i < polyNodes.getLength(); i++) {
            Element el = (Element) polyNodes.item(i);
            String points = el.getAttribute("points");
            String[] pairs = points.trim().split("\\s+");
            List<Coordinate> coords = new ArrayList<>();
            for (String pair : pairs) {
                String[] xy = pair.split(",");
                if (xy.length == 2) {
                    try {
                        double x = Double.parseDouble(xy[0]);
                        double y = Double.parseDouble(xy[1]);
                        coords.add(new Coordinate(x, y));
                    } catch (NumberFormatException ex) {
                        // ignore invalid coordinate
                    }
                }
            }
            if (coords.size() >= 3) {
                polys.add(GeometryUtils.createPolygon(coords));
            }
        }
        GeometryFactory gf = GeometryUtils.factory();
        return gf.createGeometryCollection(polys.toArray(new Geometry[0]));
    }
}
