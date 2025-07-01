//package org.project.heredoggy.similarDogRecommend.service;
//
//import ai.djl.modality.cv.Image;
//import ai.djl.ndarray.NDArray;
//import ai.djl.ndarray.NDList;
//import ai.djl.ndarray.types.DataType;
//import ai.djl.translate.Batchifier;
//import ai.djl.translate.Translator;
//import ai.djl.translate.TranslatorContext;
//
//
//
//
//public class ImageEmbeddingTranslator implements Translator<Image, float[]> {
//    @Override
//    public float[] processOutput(TranslatorContext ctx, NDList list){
//        NDArray array = list.singletonOrThrow();
//        return array.toFloatArray();
//    }
//    @Override
//    public NDList processInput(TranslatorContext ctx, Image input){
//        NDArray array = input.toNDArray(ctx.getNDManager())
//                .toType(DataType.FLOAT32, false)
//                .div(255)
//                .expandDims(0);
//        return new NDList(array);
//    }
//    @Override
//    public Batchifier getBatchifier(){
//        return null;
//    }
//
//}
