package dao;

import jakarta.persistence.*;
import java.util.List;

public class UserProgressDAO {
    public Integer getLastStudiedLectureId(int userId, int courseId) {
        EntityManager em = JPAUtil.getEntityManager();
        try {
            TypedQuery<Integer> query = em.createQuery(
                "SELECT p.lecture.idLecture FROM UserProgress p WHERE p.user.idUser = :userId AND p.course.idCourse = :courseId ORDER BY p.lastAccessed DESC",
                Integer.class
            );
            query.setParameter("userId", userId);
            query.setParameter("courseId", courseId);
            query.setMaxResults(1);
            List<Integer> result = query.getResultList();
            return result.isEmpty() ? null : result.get(0);
        } finally {
            em.close();
        }
    }
}
