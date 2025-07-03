package org.project.heredoggy.dog.service;

import ai.djl.Application; // DJL의 Application 클래스: 모델 유형(예: 이미지 분류)을 지정
import ai.djl.Device;
import ai.djl.ModelException; // DJL 모델 로드/사용 중 발생하는 예외
import ai.djl.engine.Engine; // DJL 엔진 관리: PyTorch, TensorFlow 등의 백엔드 엔진 관리
import ai.djl.inference.Predictor; // 예측기: 모델로 입력 데이터를 처리해 출력 생성
import ai.djl.modality.cv.Image; // DJL의 이미지 객체: 이미지 데이터를 처리
import ai.djl.modality.cv.ImageFactory; // 이미지 생성 팩토리: 파일/URL에서 이미지 로드
import ai.djl.modality.cv.util.NDImageUtils;
import ai.djl.ndarray.NDArray; // 다차원 배열: 이미지 데이터와 특성 벡터를 저장
import ai.djl.ndarray.NDList; // NDArray의 리스트: 모델 입력/출력을 다룰 때 사용
import ai.djl.ndarray.NDManager; // 메모리 관리: NDArray 생성/삭제 관리
import ai.djl.ndarray.types.DataType; // NDArray의 데이터 타입(예: FLOAT32)
import ai.djl.repository.zoo.Criteria; // 모델 로드 기준: 모델 설정을 정의
import ai.djl.repository.zoo.ZooModel; // 사전 훈련된 모델: ResNet18 등
import ai.djl.translate.TranslateException; // 데이터 변환 중 발생하는 예외
import ai.djl.translate.Translator; // 입력/출력 데이터 변환기: 이미지 -> NDArray 변환
import ai.djl.translate.TranslatorContext; // Translator의 컨텍스트: 변환 과정 관리
import jakarta.annotation.PostConstruct;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.project.heredoggy.domain.postgresql.dog.Dog; // 강아지 엔터티: 강아지 정보 저장
import org.project.heredoggy.domain.postgresql.dog.DogImage; // 강아지 이미지 엔터티: 이미지 URL과 특성 벡터 저장
import org.project.heredoggy.image.ImageService;
import org.springframework.data.redis.core.RedisTemplate; // Redis 작업: 특성 벡터 캐싱
import org.springframework.scheduling.annotation.Async; // 비동기 처리: 대량 연산 속도 개선
import org.springframework.stereotype.Service; // Spring 서비스: 비즈니스 로직 처리

import java.io.IOException; // 입출력 예외
import java.nio.file.Files;
import java.nio.file.Path; // 파일 경로: 이미지 파일 접근
import java.time.Duration; // 캐싱 지속 시간 설정
import java.util.ArrayList; // 리스트: 유사도 계산 결과 저장
import java.util.Collections; // 빈 리스트 반환 시 사용
import java.util.List; // 리스트 인터페이스
import java.util.concurrent.CompletableFuture; // 비동기 처리: 결과를 비동기적으로 반환
import java.util.stream.Collectors; // Stream API: 리스트 처리

import java.io.IOException;
import java.nio.file.Path;
@Slf4j
@Service
public class DogImageSimilarityService {
    private static final String MODEL_URL = "https://mlrepo.djl.ai/model/cv/image_classification/ai/djl/pytorch/resnet/0.0.1/resnet18.zip";
    private final ZooModel<Image, NDArray> model;
    private final ImageService imageService;
    private final RedisTemplate<String, byte[]> redisTemplate;
    private static final String FEATURE_CACHE_KEY = "feature:dog:image:%s";

    public DogImageSimilarityService(ImageService imageService, RedisTemplate<String, byte[]> redisTemplate) throws ModelException, IOException {
        this.imageService = imageService;
        this.redisTemplate = redisTemplate;
        System.setProperty("ai.djl.pytorch.use_mps", "false"); // MPS 비활성화
        Criteria<Image, NDArray> criteria = Criteria.builder()
                .optApplication(Application.CV.IMAGE_CLASSIFICATION)
                .setTypes(Image.class, NDArray.class)
                .optEngine("PyTorch")
                .optDevice(Device.cpu()) // CPU로 기본 설정
                .optModelUrls(MODEL_URL)
                .optOption("batchSize", "16")
                .optTranslator(new FeatureExtractorTranslator())
                .build();
        try {
            this.model = criteria.loadModel();
            log.info("Model loaded successfully: {}", MODEL_URL);
        } catch (Exception e) {
            log.error("Failed to load model: {}", MODEL_URL, e);
            throw new ModelException("Model loading failed", e);
        }
    }

    @PostConstruct
    public void checkDevices() {
        log.info("Available devices: {}", Engine.getInstance().getDevices());
    }

    public byte[] extractFeatureVector(String imageUrl) throws IOException, TranslateException {
        String cacheKey = String.format(FEATURE_CACHE_KEY, imageUrl);
        byte[] cachedVector = redisTemplate.opsForValue().get(cacheKey);
        if (cachedVector != null) {
            log.debug("Cache hit for image: {}", imageUrl);
            return cachedVector;
        }

        try {
            Path imagePath = imageService.getImagePath(imageUrl);
            log.debug("Loading image from path: {}", imagePath.toAbsolutePath());
            Image img = ImageFactory.getInstance().fromFile(imagePath);

            // 이미지 유효성 검사
            if (img.getHeight() <= 0 || img.getWidth() <= 0) {
                log.error("Invalid image dimensions for: {}", imageUrl);
                throw new IOException("Invalid image dimensions: " + imageUrl);
            }

            try (NDManager manager = NDManager.newBaseManager(Device.cpu())) {
                try (Predictor<Image, NDArray> predictor = model.newPredictor()) {
                    NDArray feature = predictor.predict(img);
                    if (feature == null) {
                        log.error("Feature extraction failed for image: {}", imageUrl);
                        throw new TranslateException("Feature extraction returned null");
                    }
                    byte[] featureVector = feature.toByteArray();
                    redisTemplate.opsForValue().set(cacheKey, featureVector, Duration.ofDays(7));
                    log.debug("Feature vector extracted and cached for image: {}", imageUrl);
                    return featureVector;
                } catch (TranslateException e) {
                    log.error("Model prediction failed for image: {}", imageUrl, e);
                    throw new TranslateException("Model prediction failed for image: " + imageUrl, e);
                }
            }
        } catch (IOException e) {
            log.error("Failed to process image: {}", imageUrl, e);
            throw new IOException("Image processing failed: " + imageUrl, e);
        } catch (IllegalArgumentException e) {
            log.error("Invalid image URL: {}", imageUrl, e);
            throw new IOException("Invalid image URL: " + imageUrl, e);
        }
    }

    public double calculateCosineSimilarity(byte[] vector1, byte[] vector2) {
        try (NDManager manager = NDManager.newBaseManager(Device.cpu())) {
            NDArray v1 = manager.create(vector1).toType(DataType.FLOAT32, false);
            NDArray v2 = manager.create(vector2).toType(DataType.FLOAT32, false);
            NDArray dotProduct = v1.dot(v2);
            NDArray norm1 = v1.norm();
            NDArray norm2 = v2.norm();
            double result = dotProduct.div(norm1.mul(norm2)).getFloat();
            if (Double.isNaN(result) || Double.isInfinite(result)) {
                log.warn("Invalid cosine similarity result, returning 0.0");
                return 0.0;
            }
            return result;
        } catch (Exception e) {
            log.error("Cosine similarity calculation failed", e);
            return 0.0;
        }
    }

    @Async
    public CompletableFuture<List<Dog>> findTop3SimilarDogs(Dog targetDog, List<Dog> allDogs) {
        DogImage targetImage = targetDog.getImages().stream().findFirst().orElse(null);
        if (targetImage == null || targetImage.getFeatureVector() == null) {
            log.warn("No image or feature vector for dog ID: {}", targetDog.getId());
            return CompletableFuture.completedFuture(Collections.emptyList());
        }

        List<DogSimilarity> similarities = new ArrayList<>();
        for (Dog otherDog : allDogs) {
            if (otherDog.getId().equals(targetDog.getId())) continue;
            DogImage otherImage = otherDog.getImages().stream().findFirst().orElse(null);
            if (otherImage == null || otherImage.getFeatureVector() == null) continue;

            double similarity = calculateCosineSimilarity(targetImage.getFeatureVector(), otherImage.getFeatureVector());
            similarities.add(new DogSimilarity(otherDog, similarity));
        }

        List<Dog> top3 = similarities.stream()
                .sorted((a, b) -> Double.compare(b.similarity, a.similarity))
                .limit(3)
                .map(ds -> ds.dog)
                .collect(Collectors.toList());

        return CompletableFuture.completedFuture(top3);
    }

    private static class FeatureExtractorTranslator implements Translator<Image, NDArray> {
        @Override
        public NDArray processOutput(TranslatorContext ctx, NDList list) {
            return list.get(0);
        }

        @Override
        public NDList processInput(TranslatorContext ctx, Image input) {
            NDArray array = input.toNDArray(ctx.getNDManager(), Image.Flag.COLOR);
            array = NDImageUtils.resize(array, 224, 224);
            array = NDImageUtils.toTensor(array);
            array = NDImageUtils.normalize(array, new float[]{0.485f, 0.456f, 0.406f}, new float[]{0.229f, 0.224f, 0.225f});
            return new NDList(array);
        }
    }

    private static class DogSimilarity {
        Dog dog;
        double similarity;

        DogSimilarity(Dog dog, double similarity) {
            this.dog = dog;
            this.similarity = similarity;
        }
    }
}