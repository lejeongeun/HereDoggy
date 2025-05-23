package global.exception;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.util.Map;

@RestControllerAdvice
public class GlobalExceptionHandler {

    // 400 - 잘못된 요청
    @ExceptionHandler(BadRequestException.class)
    public ResponseEntity<?> handleBadRequest(BadRequestException e) {
        return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                .body(Map.of("error", e.getMessage()));
    }

    // 401 - 인증 필요
    @ExceptionHandler(UnauthorizedException.class)
    public ResponseEntity<?> handleUnauthorized(UnauthorizedException e) {
        return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
                .body(Map.of("error", e.getMessage()));
    }

    // 403 - 접근 권한 없음
    @ExceptionHandler(ForbiddenException.class)
    public ResponseEntity<?> handleForbidden(ForbiddenException e) {
        return ResponseEntity.status(HttpStatus.FORBIDDEN)
                .body(Map.of("error", e.getMessage()));
    }

    @ExceptionHandler(InactiveAccountException.class)
    public ResponseEntity<?> handleInactiveAccount(InactiveAccountException e) {
        return ResponseEntity.status(HttpStatus.FORBIDDEN)
                .body(Map.of("error", e.getMessage()));
    }

    // 404 - 존재하지 않는 리소스
    @ExceptionHandler(NotFoundException.class)
    public ResponseEntity<?> handleNotFound(NotFoundException e) {
        return ResponseEntity.status(HttpStatus.NOT_FOUND)
                .body(Map.of("error", e.getMessage()));
    }

    // 409 - 중복/충돌
    @ExceptionHandler(ConflictException.class)
    public ResponseEntity<?> handleConflict(ConflictException e) {
        return ResponseEntity.status(HttpStatus.CONFLICT)
                .body(Map.of("error", e.getMessage()));
    }

    // 500 - 서버 내부 에러
    @ExceptionHandler(InternalServerException.class)
    public ResponseEntity<?> handleServerError(InternalServerException e) {
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                .body(Map.of("error", e.getMessage()));
    }
}
