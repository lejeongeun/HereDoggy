package org.project.heredoggy.global.exception;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.util.Map;

@RestControllerAdvice
public class GlobalExceptionHandler {

    // 400 - 잘못된 요청
    @ExceptionHandler(BadRequestException.class)
    public ResponseEntity<?> handleBadRequest(BadRequestException e) {
        return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                .body(Map.of("error", "잘못된 요청입니다. 다시 확인하여 주세요.", "details",e.getMessage()));
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<?> handleValidationException(MethodArgumentNotValidException e) {
        String message = e.getBindingResult().getFieldError().getDefaultMessage();
        return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                .body(Map.of("error", "내용을 입력해 주세요.", "details",message));
    }

    // 401 - 인증 필요
    @ExceptionHandler(UnauthorizedException.class)
    public ResponseEntity<?> handleUnauthorized(UnauthorizedException e) {
        return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
                .body(Map.of("error", "인증 에러입니다.", "details",e.getMessage()));
    }

    // 403 - 접근 권한 없음
    @ExceptionHandler(ForbiddenException.class)
    public ResponseEntity<?> handleForbidden(ForbiddenException e) {
        return ResponseEntity.status(HttpStatus.FORBIDDEN)
                .body(Map.of("error", "접근 권한 에러 입니다.", "details",e.getMessage()));
    }

    @ExceptionHandler(InactiveAccountException.class)
    public ResponseEntity<?> handleInactiveAccount(InactiveAccountException e) {
        return ResponseEntity.status(HttpStatus.FORBIDDEN)
                .body(Map.of("error", "비활성화된 계정 입니다.", "details",e.getMessage()));
    }

    // 404 - 존재하지 않는 리소스
    @ExceptionHandler(NotFoundException.class)
    public ResponseEntity<?> handleNotFound(NotFoundException e) {
        return ResponseEntity.status(HttpStatus.NOT_FOUND)
                .body(Map.of("error","존재하지 않는 리소스 입니다.", "details", e.getMessage()));
    }

    // 409 - 중복/충돌
    @ExceptionHandler(ConflictException.class)
    public ResponseEntity<?> handleConflict(ConflictException e) {
        return ResponseEntity.status(HttpStatus.CONFLICT)
                .body(Map.of("error","중복/충돌 에러 입니다.", "details", e.getMessage()));
    }

    // 500 - 서버 내부 에러
    @ExceptionHandler(InternalServerException.class)
    public ResponseEntity<?> handleServerError(InternalServerException e) {
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                .body(Map.of("error","서버 내부 에러 입니다.", "details", e.getMessage()));
    }

    // 500 - 이미지 업로드 실패
    @ExceptionHandler(ImageUploadException.class)
    public ResponseEntity<?> handleImageUploadException(ImageUploadException e){
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                .body(Map.of("error", "이미지 업로드 중 오류가 발생했습니다.", "details", e.getMessage()));
    }
}
