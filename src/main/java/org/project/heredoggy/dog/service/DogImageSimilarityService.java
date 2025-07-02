package org.project.heredoggy.dog.service;

import ai.djl.Application;
import ai.djl.MalformedModelException; // 모델 파일이 손상되었을 때 발생하는 예외
import ai.djl.inference.Predictor; // DJL에서 모델 추론을 수행하는 핵심 인터페이스
import ai.djl.modality.cv.Image; // DJL에서 이미지 데이터를 나타내는 클래스
import ai.djl.modality.cv.ImageFactory; // 파일 등으로부터 Image 객체를 생성하는 유틸리티
import ai.djl.ndarray.NDArray; // NDArray 클래스 임포트: 모델의 특징 벡터 출력을 나타냅니다.
import ai.djl.repository.zoo.Criteria; // DJL 모델을 로드하기 위한 기준(모델 이름, 엔진 등)을 정의하는 클래스
import ai.djl.repository.zoo.ModelNotFoundException; // 모델을 찾을 수 없을 때 발생하는 예외
import ai.djl.repository.zoo.ZooModel;
import ai.djl.training.util.ProgressBar;
import ai.djl.translate.TranslateException; // 번역기(Translator)에서 데이터 변환 중 발생하는 예외
import jakarta.annotation.PostConstruct; // Spring 컴포넌트 초기화 후 실행되는 메서드를 지정하는 어노테이션
import jakarta.annotation.PreDestroy; // Spring 컴포넌트 소멸 전 실행되는 메서드를 지정하는 어노테이션
import lombok.RequiredArgsConstructor; // Lombok 어노테이션: final 필드를 사용하는 생성자를 자동으로 생성
import lombok.extern.slf4j.Slf4j;
import org.project.heredoggy.global.exception.InternalServerException; // 사용자 정의 내부 서버 오류 예외
import org.project.heredoggy.global.util.MathUtils; // MathUtils 클래스 임포트: float[]-byte[] 변환 및 코사인 유사도 계산용
import org.project.heredoggy.image.ImageService;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.nio.file.Path;
@Slf4j
@Service
@RequiredArgsConstructor
public class DogImageSimilarityService {
    private final ImageService imageService;
    private Predictor<Image, NDArray> featurePredictor;
    private ZooModel<Image, NDArray> featureModel; // DJL의 모델 저장소(Zoo)에서 로드된 모델 객체
    @PostConstruct
    public void init(){
        try {
            // Criteria.builder()의 제네릭 타입을 Image 입력과 NDArray 출력으로 변경합니다.
            Criteria<Image, NDArray> criteria = Criteria.builder()
                    // 특징을 추춣하기 위해 이미지 분류 모델 사용
                    .optApplication(Application.CV.IMAGE_CLASSIFICATION)
                    // 입출력 타입
                    .setTypes(Image.class, NDArray.class)
                    // 사용할 모델 이름
                    .optModelName("efficientnet_b0")
                    // 사용할 딥러닝 엔진
                    .optEngine("PyTorch")
                    // 모델 로딩중 표시
                    .optProgress(new ProgressBar())
                    .build();

            // 모델 로드
            featureModel = criteria.loadModel();
            // 로드된 후 실제 추론을 담당하는 Predictor 생성
            featurePredictor = featureModel.newPredictor();
        } catch (ModelNotFoundException | MalformedModelException | IOException e) {
            // 모델을 찾을 수 없거나, 모델 파일이 손상되었거나, I/O 오류가 발생했을 때 예외를 처리합니다.
            throw new InternalServerException("Failed to load DJL model for image similarity: " + e.getMessage());
        }
    }

    @PreDestroy
    public void shutdown(){
        // 어플리케이션 종료 시 예측기와 모델 리소스를 해제
        // 메모리 누수를 방지하고 시스템 자원을 효율적으로 관리
        if (featurePredictor != null){
            featurePredictor.close();
        }
        if (featureModel != null){
            featureModel.close();
        }
    }
    /**
     * 이미지 URL로부터 특징 벡터를 추출 => 이미지 파일을 읽고, 미리 로드된 DJL 모델을 사용하여 이미지의 고유 특징인 수치 벡터(특징 벡터)를 생성
     * @param imageUrl 이미지 웹 URL ( imageService를 통해 로컬 경로로 변환 )
     * @return 추출된 특징 벡터(byte[] 형태로 직렬화되어 반환됨)
     */
    public byte[] extractFeatureVector(String imageUrl){
        if (imageUrl == null || imageUrl.isEmpty()){
            return null;
        }
        try {
            Path imagePath = imageService.getImagePath(imageUrl);
            Image img = ImageFactory.getInstance().fromFile(imagePath);
            NDArray featureNDArray = featurePredictor.predict(img);
            float[] featureVector = featureNDArray.toFloatArray();
            return MathUtils.floatArrayToByteArray(featureVector);
        } catch (TranslateException e) {
            throw new InternalServerException("Failed to predict feature vector for image: " + imageUrl + ", " + e.getMessage());
        } catch (IOException e) {
            throw new InternalServerException("An unexpected error occurred during feature extraction for image: " + imageUrl + ", " + e.getMessage());
        }
    }

    /**
     * 두 이미지의 특징 벡터간의 코사인 유사도를 계산 (얼마나 유사한지를 나타내는 지표)
     *
     */
    public double calculateSimilarity(byte[] vector1, byte[] vector2){
        // 입력 벡터 중 하나라도 null 이면 유사도를 계산할 수 없으므로 0.0을 반환
        if (vector1 == null || vector2 == null){
            return 0.0;
        }
        float[] fv1 = MathUtils.byteArrayToFloatArray(vector1);
        float[] fv2 = MathUtils.byteArrayToFloatArray(vector2);
        return MathUtils.cosineSimilarity(fv1, fv2);
    }


}
