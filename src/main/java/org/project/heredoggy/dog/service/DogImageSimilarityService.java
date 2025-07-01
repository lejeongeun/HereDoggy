package org.project.heredoggy.dog.service;
import ai.djl.Application;
import ai.djl.MalformedModelException; // 모델 파일이 손상되었을 때 발생하는 예외
import ai.djl.inference.Predictor; // DJL에서 모델 추론을 수행하는 핵심 인터페이스
import ai.djl.modality.cv.Image; // DJL에서 이미지 데이터를 나타내는 클래스
import ai.djl.modality.cv.ImageFactory; // 파일 등으로부터 Image 객체를 생성하는 유틸리티
import ai.djl.nn.core.Embedding;
import ai.djl.repository.zoo.Criteria; // DJL 모델을 로드하기 위한 기준(모델 이름, 엔진 등)을 정의하는 클래스
import ai.djl.repository.zoo.ModelNotFoundException; // 모델을 찾을 수 없을 때 발생하는 예외
import ai.djl.repository.zoo.ZooModel; // DJL의 모델 저장소(Zoo)에서 로드된 모델 객체
import ai.djl.translate.TranslateException; // 번역기(Translator)에서 데이터 변환 중 발생하는 예외
import jakarta.annotation.PostConstruct; // Spring 컴포넌트 초기화 후 실행되는 메서드를 지정하는 어노테이션
import jakarta.annotation.PreDestroy; // Spring 컴포넌트 소멸 전 실행되는 메서드를 지정하는 어노테이션
import lombok.RequiredArgsConstructor; // Lombok 어노테이션: final 필드를 사용하는 생성자를 자동으로 생성
import org.project.heredoggy.global.exception.InternalServerException; // 사용자 정의 내부 서버 오류 예외
import org.project.heredoggy.global.util.MathUtils; // MathUtils 클래스 임포트: float[]-byte[] 변환 및 코사인 유사도 계산용
import org.project.heredoggy.image.ImageService; // ImageService 클래스 임포트: 이미지 파일 경로 변환용
import org.springframework.stereotype.Service; // 이 클래스가 Spring 서비스 컴포넌트임을 나타냅니다.

import java.io.IOException;

@Service
@RequiredArgsConstructor
public class DogImageSimilarityService {
    private final ImageService imageService;
    // ⭐ 제네릭 타입을 float[] 대신 Embedding으로 변경합니다.
    private Predictor<Image, Embedding> featurePredictor;
    private ZooModel<Image, Embedding> featureModel;

    @PostConstruct
    public void init() {
        try {
            // ⭐ Criteria.builder()의 setApplication 대신 optApplication 사용
            Criteria<Image, Embedding> criteria = (Criteria<Image, Embedding>) Criteria.builder() // ⭐ 제네릭 타입을 float[] 대신 Embedding으로 변경합니다.
                    .optApplication(Application.CV.IMAGE_CLASSIFICATION) // 올바른 사용법
                    .optModelName("efficientnet_b0")
                    .optEngine("TensorFlow") // pom.xml에 tensorflow-engine 의존성 필요
                    .build();

            featureModel = criteria.loadModel();
            featurePredictor = featureModel.newPredictor();

        } catch (ModelNotFoundException | MalformedModelException | IOException e) {
            throw new InternalServerException("Failed to load DJL model for image similarity: " + e.getMessage());
        }
    }
    @PreDestroy
    public void shutdown(){
        if (featurePredictor != null){
            featurePredictor.close();
        }
        if (featureModel != null){
            featureModel.close();
        }
    }


}
