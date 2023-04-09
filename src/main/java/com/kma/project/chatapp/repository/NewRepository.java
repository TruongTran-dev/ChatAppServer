package com.kma.project.chatapp.repository;

import com.kma.project.chatapp.entity.NewEntity;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface NewRepository extends JpaRepository<NewEntity, Long> {

    Page<NewEntity> findAllByTitleLikeIgnoreCase(Pageable pageable, String title);
}
