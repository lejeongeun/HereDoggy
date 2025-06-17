package org.project.heredoggy.config.oauth;

import java.util.Map;

public interface OAuth2UserInfo {
    String getEmail();
    String getNickname();
    String getName();
    String getPhone();
    String getProfileImageUrl();
    Map<String, Object> getAttributes();
}