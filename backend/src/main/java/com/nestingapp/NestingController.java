package com.nestingapp;

import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.List;

import org.locationtech.jts.geom.Envelope;
import org.locationtech.jts.geom.Geometry;
import org.locationtech.jts.geom.Polygon;
import org.springframework.core.io.ByteArrayResource;
import org.springframework.core.io.Resource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import com.fasterxml.jackson.databind.ObjectMapper;

/**
 * REST endpoints for nesting operations. These APIs allow the frontend to
 * upload parts, run the nesting algorithm and export the resulting layout.
 */
@RestController
@RequestMapping("/api")
public class NestingController {

    private final NestingService nestingService;

    public NestingController(NestingService nestingService) {
        this.nestingService = nestingService;
    }

    @PostMapping("/nest")
    public NestingResult nestFiles(@RequestParam("files") MultipartFile[] files,
                                   @RequestParam("config") String configJson) throws Exception {
        ObjectMapper mapper = new ObjectMapper();
        NestingConfig config = mapper.readValue(configJson, NestingConfig.class);

        List<Geometry> parts = new ArrayList<>();
        List<String> ids = new ArrayList<>();
        for (MultipartFile file : files) {
            String name = file.getOriginalFilename();
            String content = new String(file.getBytes(), StandardCharsets.UTF_8);
            Geometry geom = null;
            if (name != null && name.toLowerCase().endsWith(".svg")) {
                geom = new SvgParser().parseToGeometry(content);
            } else if (name != null && name.toLowerCase().endsWith(".dxf")) {
                geom = new DxfParser().parse(content);
            }
            if (geom != null) {
                for (int i = 0; i < geom.getNumGeometries(); i++) {
                    parts.add(geom.getGeometryN(i));
                    ids.add(name);
                }
            }
        }

        Polygon sheet = GeometryUtils.createSheet(config.sheetWidth(), config.sheetHeight());
        List<Geometry> placed = nestingService.nestParts(parts, sheet, config.maxNoImprovement());
        List<Placement> placements = new ArrayList<>();
        for (int i = 0; i < placed.size(); i++) {
            Geometry g = placed.get(i);
            Envelope env = g.getEnvelopeInternal();
            placements.add(new Placement(ids.get(i), env.getMinX(), env.getMinY(), 0));
        }
        return new NestingResult(placements, new SheetSpec(config.sheetWidth(), config.sheetHeight()));
    }

    @GetMapping("/status")
    public StatusResponse status() {
        return new StatusResponse("completed", 100, "0s");
    }

    @PostMapping("/export")
    public ResponseEntity<Resource> exportLayout(@RequestBody ExportRequest request) {
        String format = request.format().toLowerCase();
        String content;
        if ("svg".equals(format)) {
            StringBuilder sb = new StringBuilder();
            sb.append("<svg xmlns=\"http://www.w3.org/2000/svg\">");
            for (Placement p : request.layout().nestedParts()) {
                sb.append("<rect x=\"").append(p.x()).append("\" y=\"")
                  .append(p.y()).append("\" width=\"1\" height=\"1\"/>");
            }
            sb.append("</svg>");
            content = sb.toString();
        } else if ("dxf".equals(format)) {
            StringBuilder sb = new StringBuilder();
            sb.append("0\nSECTION\n2\nENTITIES\n");
            for (Placement p : request.layout().nestedParts()) {
                sb.append("0\nCIRCLE\n8\n0\n10\n").append(p.x())
                  .append("\n20\n").append(p.y()).append("\n40\n1\n");
            }
            sb.append("0\nENDSEC\n0\nEOF\n");
            content = sb.toString();
        } else {
            return ResponseEntity.badRequest().build();
        }
        byte[] bytes = content.getBytes(StandardCharsets.UTF_8);
        MediaType type = "svg".equals(format) ? MediaType.APPLICATION_XML : MediaType.TEXT_PLAIN;
        return ResponseEntity.ok()
                .header(HttpHeaders.CONTENT_DISPOSITION,
                        "attachment; filename=\"nested_layout." + format + "\"")
                .contentType(type)
                .contentLength(bytes.length)
                .body(new ByteArrayResource(bytes));
    }
}
