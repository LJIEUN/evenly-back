package com.codeisevenlycooked.evenly.controller.image;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.InputStreamResource;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.RestTemplate;

import java.io.ByteArrayInputStream;
import java.net.URLDecoder;
import java.nio.charset.StandardCharsets;

@RestController
public class ImageController {

    private final RestTemplate restTemplate;

    @Autowired
    public ImageController(RestTemplate restTemplate) {
        this.restTemplate = restTemplate;
    }

    @GetMapping("/proxy-image")
    public ResponseEntity<InputStreamResource> getImage(@RequestParam String imageUrl) {
        try {
            String decodedUrl = URLDecoder.decode(imageUrl, StandardCharsets.UTF_8.name());

            HttpHeaders headers = new HttpHeaders();
            headers.add("User-Agent", "Mozilla/5.0");

            HttpEntity<String> entity = new HttpEntity<>(headers);

            //외부 이미지 url 요청 후 이미지 가져오기
            ResponseEntity<byte[]> response = restTemplate.exchange(imageUrl, HttpMethod.GET, entity, byte[].class);

            //이미지 클라이언트 반환
            return ResponseEntity.ok()
                    .header("Content-Type", "image/jpeg")
                    .body(new InputStreamResource(new ByteArrayInputStream(response.getBody())));
        } catch (Exception e) {
            return ResponseEntity.status(400).body(null);
        }

    }
}
