package org.project.heredoggy.global.util;

import lombok.*;

import java.time.LocalDateTime;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ApiResponse<T> {
    private String status; // "success" or "error"
    private String message; // 응답 메세지
    private T data; // 실제 응답 데이터 (제네릭 타입)
    private LocalDateTime timestamp; // 응답 시각

    // 성공 응답 (반환 데이터 포함)
    public static <T> ApiResponse<T> success(String message, T data){
        return ApiResponse.<T>builder()
                .status("success")
                .message(message)
                .data(data)
                .timestamp(LocalDateTime.now())
                .build();
    }

    // 성공응답 (데이터 없음)
    public static <T> ApiResponse<T> success(String message){
        return success(message, null);
    }

    // 실패 응답
    public static <T> ApiResponse<T> error(String message){
        return ApiResponse.<T>builder()
                .status("error")
                .message(message)
                .data(null)
                .timestamp(LocalDateTime.now())
                .build();
    }

}
