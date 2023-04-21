package com.example.sns.member.application;

import org.springframework.web.multipart.MultipartFile;

public interface ProfileImageStore {
    String saveProfileImage(MultipartFile image);
}
