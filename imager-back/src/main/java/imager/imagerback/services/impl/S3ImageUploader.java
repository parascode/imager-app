package imager.imagerback.services.impl;

import com.amazonaws.HttpMethod;
import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.model.*;
import imager.imagerback.exceptions.ImageUploadException;
import imager.imagerback.services.ImageUploader;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.net.URL;
import java.util.Date;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
public class S3ImageUploader implements ImageUploader {

    @Autowired
    private AmazonS3 client;
    @Value("${app.s3.bucket}")
    private String bucket;
    @Override
    public String uploadImage(MultipartFile image) {

        if(image.isEmpty()){
            throw new ImageUploadException("file is empty !!");
        }

        String actualFileName = image.getOriginalFilename();

        String fileName = UUID.randomUUID().toString() + actualFileName.substring(actualFileName.lastIndexOf("."));

        ObjectMetadata metadata = new ObjectMetadata();
        metadata.setContentLength(image.getSize());

        try {
            PutObjectResult putObjectResult = client.putObject(new PutObjectRequest(bucket, fileName, image.getInputStream(), metadata));
            return preSignedUrl(fileName);
        } catch (IOException e) {
            throw new ImageUploadException("problem in uploading image");
        }
    }

    @Override
    public List<String> allFiles() {
        ListObjectsV2Request listObjectsV2Request =new ListObjectsV2Request().withBucketName(bucket);
        ListObjectsV2Result listObjectsV2Result = client.listObjectsV2(listObjectsV2Request);
        List<S3ObjectSummary> objectSummaries = listObjectsV2Result.getObjectSummaries();
        List<String> listFileUrls = objectSummaries.stream().map(item -> this.preSignedUrl(item.getKey())).collect(Collectors.toList());
        return listFileUrls;
    }

    @Override
    public String preSignedUrl(String fileName) {
        Date expirationDate = new Date();
        int hour = 2;
        expirationDate.setTime(expirationDate.getTime() + hour * 60 * 60 * 1000);

        GeneratePresignedUrlRequest generatePresignedUrlRequest
                = new GeneratePresignedUrlRequest(bucket, fileName)
                .withMethod(HttpMethod.GET)
                .withExpiration(expirationDate);
        URL url = client.generatePresignedUrl(generatePresignedUrlRequest);
        return url.toString();
    }
}
