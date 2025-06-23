package org.project.heredoggy.image;

import lombok.RequiredArgsConstructor;
import org.project.heredoggy.domain.postgresql.comment.PostType;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
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

    // 개 이미지 저장
    public String saveImage(MultipartFile file, Long shelterId, Long dogId) throws IOException{
        String fileName = UUID.randomUUID() + "-" + file.getOriginalFilename();

        File folder = Paths.get(
                getAbsoluteUploadDir(),
                "shelters",
                String.valueOf(shelterId),
                "dogs",
                String.valueOf(dogId)
        ).toFile();

        if (!folder.exists()){
            folder.mkdirs(); // 디렉토리 없으면 생성
        }

        File savePath = new File(folder, fileName);
        file.transferTo(savePath);

        return "/uploads/shelters/" + shelterId + "/dogs/" + dogId + "/" + fileName;
    }

    //기본 강아지 이미지 dog api 사용해서 받아오기
    public String saveDogImageFromUrl(String imageUrl, Long shelterId, Long dogId) throws IOException {
        // 파일명 생성
        String fileName = UUID.randomUUID() + "-dog.jpg";

        // 저장할 폴더
        File folder = Paths.get(
                getAbsoluteUploadDir(),
                "shelters",
                String.valueOf(shelterId),
                "dogs",
                String.valueOf(dogId)
        ).toFile();

        if (!folder.exists()) folder.mkdirs();

        // 이미지 다운로드
        Path savePath = new File(folder, fileName).toPath();
        try (var in = new java.net.URL(imageUrl).openStream()) {
            Files.copy(in, savePath);
        }

        return "/uploads/shelters/" + shelterId + "/dogs/" + dogId + "/" + fileName;
    }

    //보호소 사진 저장
    public String saveShelterImage(MultipartFile file, Long shelterId) throws IOException {
        String fileName = UUID.randomUUID() + "-" + file.getOriginalFilename();
        File folder = Paths.get(
                getAbsoluteUploadDir(),
                "shelters",
                String.valueOf(shelterId),
                "shelter-images"
        ).toFile();

        if (!folder.exists()) folder.mkdirs();

        File savePath = new File(folder, fileName);
        file.transferTo(savePath);

        return "/uploads/shelters/" + shelterId + "/shelter-images/" + fileName;
    }

    //문의하기 사진 저장
    public String saveInquiryImage(MultipartFile image, Long inquiryId) throws IOException{
        String fileName = UUID.randomUUID() + "-" + image.getOriginalFilename();
        File folder = Paths.get(
                getAbsoluteUploadDir(),
                "inquiry",
                String.valueOf(inquiryId),
                "inquiry-images"
        ).toFile();

        if(!folder.exists()) folder.mkdirs();

        File savePath = new File(folder, fileName);
        image.transferTo(savePath);

        return "/uploads/inquiries/" + inquiryId + "/inquiry-images/" + fileName;
    }
    // 기본 경로 이미지 저장
    public String saveWalkRouteImage(String imageUrl, Long walkRouteId){
        try(InputStream in = new URL(imageUrl).openStream()) {
            String fileName = UUID.randomUUID() + ".png";

            File folder = Paths.get(
                    getAbsoluteUploadDir(),
                    "walk-route",
                    String.valueOf(walkRouteId),
                    "walk-route-images"
            ).toFile();

            if (!folder.exists()) folder.mkdirs();

            File saveFile = new File(folder, fileName);
            Files.copy(in, saveFile.toPath(), StandardCopyOption.REPLACE_EXISTING);

            return "/uploads/walk-route/" + walkRouteId + "/" + fileName;
        }catch (IOException e){
            throw new RuntimeException("Kakao 이미지 다운로드 실패");
        }
    }

    // 실제 경로 이미지 저장
    public String saveWalkRecordImage(MultipartFile image, Long walkRecordId) throws IOException{
        String fileName = UUID.randomUUID() + "-" + image.getOriginalFilename();

        File folder = Paths.get(
                getAbsoluteUploadDir(),
                "walk-record",
                String.valueOf(walkRecordId),
                "walk-record-images"
        ).toFile();

        if (!folder.exists()) folder.mkdirs();

        File savePath = new File(folder, fileName);
        image.transferTo(savePath);

        return "/uploads/walk-record/" + walkRecordId + "/walk-record-images/" + fileName;
    }

    public String savePostImage(MultipartFile image, PostType postType, Long postId) throws IOException {
        String fileName = UUID.randomUUID() + "-" + image.getOriginalFilename();

        File folder = Paths.get(
                getAbsoluteUploadDir(),
                postType.name().toLowerCase() + "-posts",  // free, review, missing
                String.valueOf(postId)
        ).toFile();

        if (!folder.exists()) folder.mkdirs();

        File savePath = new File(folder, fileName);
        image.transferTo(savePath);

        return "/uploads/" + postType.name().toLowerCase() + "-posts/" + postId + "/" + fileName;
    }

    public void deleteImage(String imageUrl) {
        if (imageUrl == null || imageUrl.isBlank()) {
            return;
        }

        try {
            String relativePath = imageUrl.replaceFirst("^/uploads/", "");

            // OS에 맞는 경로 조합
            Path path = Paths.get(uploadDir, relativePath);
            Files.deleteIfExists(path);

            System.out.println("✅ 이미지 삭제 완료: " + path.toAbsolutePath());
        } catch (IOException e) {
            System.err.println("❌ 이미지 삭제 실패: " + e.getMessage());
        }
    }

}
