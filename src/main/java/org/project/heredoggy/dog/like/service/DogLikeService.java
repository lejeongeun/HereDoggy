package org.project.heredoggy.dog.like.service;

import lombok.RequiredArgsConstructor;
import org.project.heredoggy.security.CustomUserDetails;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class DogLikeService {

    public boolean toggleDogLike(Long dogId, CustomUserDetails userDetails) {
        return false;
    }
}
