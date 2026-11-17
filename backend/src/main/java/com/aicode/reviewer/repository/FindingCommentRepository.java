package com.aicode.reviewer.repository;

import com.aicode.reviewer.entity.FindingComment;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface FindingCommentRepository extends JpaRepository<FindingComment, String> {
    List<FindingComment> findByFindingIdOrderByCreatedAtAsc(String findingId);
}
