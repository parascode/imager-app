package imager.imagerback.services;

import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

public interface ImageUploader {
    String uploadImage(MultipartFile image);

    List<String> allFiles();

    String preSignedUrl(String fileName);
}
