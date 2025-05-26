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
    private String uploadDir; // upload

    // 절대 경로로 반환
    private String getAbsoluteUploadDir(){
        File folder = new File(uploadDir);
        if (!folder.isAbsolute()){
            folder = new File(System.getProperty("user.dir"), uploadDir);
        }
        if (!folder.exists()){
            folder.mkdir();
        }
        return folder.getAbsolutePath();
    }
    public String saveImage(MultipartFile file) throws IOException{
        String fileName = UUID.randomUUID() + "-" + file.getOriginalFilename();
        File savePath = new File(getAbsoluteUploadDir(), fileName);
        file.transferTo(savePath);
        return "/uploads/" + fileName;
    }
    public void deleteImage(String imageUrl){
        String relativeFile = imageUrl.replaceFirst("^/uploads/", "");
        File file = new File(getAbsoluteUploadDir(), relativeFile);
        if (file.exists()) file.delete();
    }
}
