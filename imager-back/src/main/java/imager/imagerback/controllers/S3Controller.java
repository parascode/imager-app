package imager.imagerback.controllers;

import imager.imagerback.services.ImageUploader;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpRequest;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

@RestController
@RequestMapping("/api/v1/s3")
public class S3Controller {

    @Autowired
    private ImageUploader uploader;
    @PostMapping
    public ResponseEntity<?> uploadImage(@RequestParam MultipartFile file){
        HttpHeaders headers = new HttpHeaders();
        headers.add(HttpHeaders.ACCESS_CONTROL_ALLOW_HEADERS, "*");
        headers.add(HttpHeaders.ACCESS_CONTROL_ALLOW_ORIGIN,"*");
        headers.add(HttpHeaders.ACCESS_CONTROL_REQUEST_METHOD, "*");
        headers.add(HttpHeaders.CONTENT_TYPE, "*");
        headers.add(HttpHeaders.ACCEPT, "*");
        headers.add(HttpHeaders.ORIGIN, "*");
        headers.add(HttpHeaders.ACCESS_CONTROL_MAX_AGE, "*");
//        return ResponseEntity.ok(uploader.uploadImage(file));
        return new ResponseEntity<>(uploader.uploadImage(file), headers, HttpStatus.OK);
    }


    @GetMapping
    public List<String> getAllFiles(){
        return this.uploader.allFiles();
    }
    @GetMapping("/{fileName}")
    public String urlByName(@PathVariable("fileName") String fileName){
        return this.uploader.preSignedUrl(fileName);
    }
}
