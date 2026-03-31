package com.example.grademanagementsystem.dto.response;

import jakarta.persistence.criteria.CriteriaBuilder;
import org.springframework.beans.factory.support.ManagedProperties;

import java.util.Map;

public record GradeResponse(
        int student_id,
        /**int course_id,
        int score,**/
        Map<Integer, Integer> coursesAndScores
) {
}
