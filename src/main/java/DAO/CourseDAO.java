package dao;

import java.util.List;
import java.util.Optional;
import java.util.logging.Level;
import java.util.logging.Logger;

import jakarta.persistence.EntityManager;
import jakarta.persistence.EntityTransaction;
import jakarta.persistence.TypedQuery;
import model.Course;

public class CourseDAO {
    private static final Logger logger = Logger.getLogger(CourseDAO.class.getName());

    public Course saveCourse(Course course) {
        EntityManager em = JPAUtil.getEntityManager();
        EntityTransaction transaction = em.getTransaction();
        try {
            transaction.begin();
            em.persist(course);
            transaction.commit();
            return course;
        } catch (Exception e) {
            if (transaction.isActive()) transaction.rollback();
            throw new RuntimeException("Could not save course", e);
        } finally {
            em.close();
        }
    }

    public Optional<Course> findCourseById(int courseId) {
        EntityManager em = JPAUtil.getEntityManager();
        try {
            return Optional.ofNullable(em.find(Course.class, courseId));
        } finally {
            em.close();
        }
    }

    public List<Course> getCourses() {
        EntityManager em = JPAUtil.getEntityManager();
        try {
            TypedQuery<Course> query = em.createQuery("SELECT c FROM Course c", Course.class);
            return query.getResultList();
        } finally {
            em.close();
        }
    }
    
    public void updateCourse(Course course) {
        EntityManager em = JPAUtil.getEntityManager();
        EntityTransaction transaction = em.getTransaction();
        try {
            transaction.begin();
            em.merge(course);
            transaction.commit();
        } catch (Exception e) {
            if (transaction.isActive()) transaction.rollback();
            throw new RuntimeException("Could not update course", e);
        } finally {
            em.close();
        }
    }

    public void deleteCourse(int courseId) {
        EntityManager em = JPAUtil.getEntityManager();
        EntityTransaction transaction = em.getTransaction();
        try {
            transaction.begin();
            Course course = em.find(Course.class, courseId);
            if (course != null) {
                em.remove(course);
            }
            transaction.commit();
        } catch (Exception e) {
            if (transaction.isActive()) transaction.rollback();
            logger.log(Level.SEVERE, "Error deleting course with ID: " + courseId, e);
            throw new RuntimeException("Failed to delete course", e);
        } finally {
            em.close();
        }
    }
}