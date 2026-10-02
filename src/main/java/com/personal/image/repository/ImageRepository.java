package com.personal.image.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.personal.image.entity.Image;
import com.personal.image.entity.User;

public interface ImageRepository extends JpaRepository<Image, Long> {

    List<Image> findByUser(User user);

    Optional<Image> findByIdAndUser(Long id, User user);
}