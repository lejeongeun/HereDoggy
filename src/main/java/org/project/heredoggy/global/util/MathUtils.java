package org.project.heredoggy.global.util;

public class MathUtils {
    /**
     * 두 벡터의 코사인 유사도 계산 (0 = 유사성 없음, -1 = 두 벡터가 완전 다름 => 정 반대, 1 = 유사도 가장 높음, 같은 종끼리는 거의 1에 유사)
     * 코사인 유사도 = (A dot B) / (||A|| * ||B||)
     * 차원 : 각 백터가 갖는 특징의 값, 내적 = `(A1 * B1) + (A2 * B2) + (A3 * B3)`
     * 각 벡터는 특징마다 각각의 값을 보유 VectorA = [A1, A2, A3] VectorB = [B1, B2, B3]
     * @param vectorA 첫 번째 벡터
     * @param vectorB 두 번째 벡터
     * @return 코사인 유사도 값 (범위: -1.0 ~ 1.0, 유사할수록 1.0에 가까움)
     */

    // 각 두 이미지를 벡터화하여 두 벡터의 이미지를 비교 => 두 벡터의 길이가 다를 경우 비교 불가함으로 같은 길이를 갖고있어야함
    public static double cosineSimilarity(float[] vectorA, float[] vectorB){
        if (vectorA.length != vectorB.length){
            throw new IllegalArgumentException("벡터값의 길이가 동일해야 합니다.");
        }

        // 내적 값을 저장
        double dotProduct = 0.0;
        // 벡터의 크기를 제급 하여 비교 할 두 값
        double normA = 0.0;
        double normB = 0.0;

        // 이미지의 특정 시각적 특징을 순차적으로 돌면서 하나씩 비교
        for (int i = 0; i < vectorA.length; i++) {
            dotProduct += vectorA[i] * vectorB[i];
            normA += Math.pow(vectorA[i], 2);
            normB += Math.pow(vectorB[i], 2);
        }
        double magnitude = Math.sqrt(normA) * Math.sqrt(normB); // 코사인 유사도 공식의 분모(`||A|| * ||B||`)
        if (magnitude == 0 || Double.isNaN(magnitude)){
            // 벡터의 값이 0이거나 Nan일 경우 비교할 수 없으므로 예외처리
            return 0.0;
        }
        // 코사인 유사도 공식에 따라 `dotProduct` (내적)을 `magnitude` (두 벡터 크기의 곱)로 나누어 최종 코사인 유사도 값을 반환
        return dotProduct / magnitude;
    }

    // floatArrayToByteArray 메서드 - Float 배열을 Byte 배열로 변환 (데이터베이스 저장용)
    // float[] 배열 (강아지 이미지의 특징 벡터)을 입력
    public static byte[] floatArrayToByteArray(float[] floatArray){
        if (floatArray == null){
            return null;
        }
        int len = floatArray.length; // float 배열의 길이 (차원 수)

        // float 배열 하나는 4바이트로 구성
        // `[0.1f, 0.2f]`라는 `float[]` 배열=> 0.1f가 4바이트, 0.2f가 4바이트를 차지해서 총 8바이트 필ㅇ요
        byte[] byteArray = new byte[len * 4];

        for (int i = 0; i < len; i++) {
            int floatBits = Float.floatToIntBits(floatArray[i]);
            byteArray[i * 4] = (byte) (floatBits >> 24);
            byteArray[i * 4 + 1] = (byte) (floatBits >> 16);
            byteArray[i * 4 + 2] = (byte) (floatBits >> 8);
            byteArray[i * 4 + 3] = (byte) floatBits;

        }
        return byteArray;
    }
    /** // Javadoc 주석 시작
     * byte[] 배열을 float[] 배열로 역직렬화
     * 저장된 바이트들을 다시 32비트 정수로 조합한 후 float 값으로 변환
     * @param byteArray 역직렬화할 byte[] 배열
     * @return 역직렬화된 float[] 배열
     */
    public static float[] byteArrayToFloatArray(byte[] byteArray){
        if (byteArray == null){
            return null;
        }
        if (byteArray.length % 4 != 0){
            // byte 배열을 float 배열로 역변환하는 정적 메서드
            throw new IllegalArgumentException("바이트 길이가 올바르지 않습니다. 길이는 4의 배수여야 합니다.");
        }
        // byte 배열 길이를 4로 나누어 float 배열의 길이 계산
        int len = byteArray.length / 4;
        float[] floatArray = new float[len];
        for (int i = 0; i < len; i++) {
            int floatBits = ((byteArray[i*4] & 0xFF) << 24) | // 4개의 비트를 조합하여 32비트 정수(int)를 재구성
                    ((byteArray[i * 4 + 1] & 0xFF) << 16) | // 각 byte는 0xFF와 비트 AND 연산을 통해 음수 확장을 방지
                    ((byteArray[i * 4 + 2] & 0xFF) << 8) | // 직렬화된 비트만큼 왼쪽으로 시프트하여 올바른 위치로 이동
                    (byteArray[i * 4 + 3] & 0xFF);
            floatArray[i] = Float.intBitsToFloat(floatBits); // 재구성한 32비트 정수를 다시 float 값으로 변환
        }
        return floatArray;
    }




}
