package com.nestingapp;

import java.io.StringReader;

import org.apache.batik.anim.dom.SAXSVGDocumentFactory;
import org.apache.batik.util.XMLResourceDescriptor;
import org.w3c.dom.svg.SVGDocument;

/**
 * Simple SVG parser built on Apache Batik. For now it only parses a raw
 * string into an {@link SVGDocument} instance.
 */
public class SvgParser {

    public SVGDocument parse(String svgContent) throws Exception {
        String parser = XMLResourceDescriptor.getXMLParserClassName();
        SAXSVGDocumentFactory factory = new SAXSVGDocumentFactory(parser);
        return factory.createSVGDocument("", new StringReader(svgContent));
    }
}
