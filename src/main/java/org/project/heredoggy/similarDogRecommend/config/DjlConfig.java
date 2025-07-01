//package org.project.heredoggy.similarDogRecommend.config;
//
//import ai.djl.MalformedModelException;
//import ai.djl.modality.cv.Image;
//import ai.djl.repository.zoo.Criteria;
//import ai.djl.repository.zoo.ModelNotFoundException;
//import ai.djl.repository.zoo.ZooModel;
//import ai.djl.tensorflow.zoo.TfModelZoo;
//import org.project.heredoggy.similarDogRecommend.service.ImageEmbeddingTranslator;
//import org.springframework.context.annotation.Bean;
//import org.springframework.context.annotation.Configuration;
//
//
//import java.io.IOException;
//
//@Configuration
//public class DjlConfig {
//    @Bean
//    public ZooModel<Image, float[]> efficientNetModel() throws IOException, ModelNotFoundException, MalformedModelException{
//        Criteria<Image, float[]> criteria = Criteria.builder()
//                .setTypes(Image.class, float[].class)
////                .optModelZoo(TfModelZoo.IMAGE_CLASSIFICATION)
//                .optArtifactId("efficientnet_b0")
//                .optTranslator(new ImageEmbeddingTranslator())
//                .build();
//
//        return criteria.loadModel();
//    }
//
//}
