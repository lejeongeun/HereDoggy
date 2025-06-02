//package org.project.heredoggy.config;
//
//import com.google.auth.oauth2.GoogleCredentials;
//import com.google.firebase.FirebaseApp;
//import com.google.firebase.FirebaseOptions;
//import jakarta.annotation.PostConstruct;
//import org.springframework.context.annotation.Configuration;
//import org.springframework.core.io.ClassPathResource;
//
//import java.io.InputStream;
//
//@Configuration
//public class FirebaseConfig {
//    @PostConstruct
//    public void initialize() {
//        try (InputStream serviceAccount = new ClassPathResource("firebase/heredoggy-firebase-adminsdk.json").getInputStream()) {
//            FirebaseOptions options = FirebaseOptions.builder()
//                    .setCredentials(GoogleCredentials.fromStream(serviceAccount))
//                    .build();
//            if (FirebaseApp.getApps().isEmpty()) {
//                FirebaseApp.initializeApp(options);
//            }
//        } catch (Exception e) {
//            throw new RuntimeException("Firebase 초기화 실패", e);
//        }
//    }
//}