package org.project.heredoggy.image;

import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.nio.file.Paths;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class ImageService {

    @Value("${file.upload-dir}")
    private String uploadDir;  // "uploads"

    // 이미지 생성
    public String saveImage(MultipartFile file) throws IOException {
        File dir = new File(uploadDir);
        if (!dir.exists()) dir.mkdir();

        String fileName = UUID.randomUUID() + "_" + file.getOriginalFilename();
        String filePath = uploadDir + "/" + fileName;
        file.transferTo(new File(filePath));
        return "/uploads/" + fileName;
    }
    // 이미지 삭제
    public void deleteImage(String imageUrl){
        String fileName = Paths.get(imageUrl).getFileName().toString();
        File file = new File(uploadDir + "/" + fileName);
        if (file.exists()){
            file.delete();
        }

    }

}
