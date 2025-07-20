package dao;

import java.util.List;

import jakarta.persistence.EntityManager;
import jakarta.persistence.EntityTransaction;
import jakarta.persistence.TypedQuery;
import java.util.Collections;
import java.util.logging.Level;
import java.util.logging.Logger;
import model.Enrollment;
import model.User;

public class EnrollmentDAO {
    private static final Logger logger = Logger.getLogger(EnrollmentDAO.class.getName());
    public void save(Enrollment enrollment) {
        EntityManager em = JPAUtil.getEntityManager();
        EntityTransaction transaction = em.getTransaction();
        try {
            transaction.begin();
            em.persist(enrollment);
            transaction.commit();
        } catch (Exception e) {
            if (transaction.isActive()) transaction.rollback();
            throw new RuntimeException("Could not save enrollment", e);
        } finally {
            em.close();
        }
    }
    public void update(Enrollment enrollment) {
        EntityManager em = JPAUtil.getEntityManager();
        EntityTransaction transaction = em.getTransaction();
        try {
            transaction.begin();
            em.merge(enrollment);
            transaction.commit();
        } catch (Exception e) {
            if (transaction.isActive()) transaction.rollback();
            throw new RuntimeException("Could not update enrollment", e);
        } finally {
            em.close();
        }
    }
    
    public Enrollment findById(int id) {
        EntityManager em = JPAUtil.getEntityManager();
        try {
            return em.find(Enrollment.class, id);
        } finally {
            em.close();
        }
    }

    public List<Enrollment> findByCourseId(int courseId) {
        EntityManager em = JPAUtil.getEntityManager();
        try {
            TypedQuery<Enrollment> query = em.createQuery("SELECT e FROM Enrollment e WHERE e.course.id = :courseId", Enrollment.class);
            query.setParameter("courseId", courseId);
            return query.getResultList();
        } finally {
            em.close();
        }
    }



    public void delete(Enrollment enrollment) {
        EntityManager em = JPAUtil.getEntityManager();
        EntityTransaction transaction = em.getTransaction();
        try {
            transaction.begin();
            em.remove(em.contains(enrollment) ? enrollment : em.merge(enrollment));
            transaction.commit();
        } catch (Exception e) {
            if (transaction.isActive()) transaction.rollback();
            throw new RuntimeException("Could not delete enrollment", e);
        } finally {
            em.close();
        }
    }

    public void deleteByCourseId(int courseId) {
        EntityManager em = JPAUtil.getEntityManager();
        EntityTransaction tx = em.getTransaction();
        try {
            tx.begin();
            em.createQuery("DELETE FROM Enrollment e WHERE e.course.id = :courseId")
              .setParameter("courseId", courseId)
              .executeUpdate();
            tx.commit();
        } finally {
            em.close();
        }
        
    }
    public List<User> getStudentsByCourseId(int courseId) {
        EntityManager em = JPAUtil.getEntityManager();
        try {
            TypedQuery<User> query = em.createQuery(
                "SELECT e.student FROM Enrollment e WHERE e.course.idCourse = :courseId AND e.student.role = 'student'",
                User.class
            );
            query.setParameter("courseId", courseId);
            List<User> students = query.getResultList();
            logger.log(Level.INFO, "Total students found for courseId {0}: {1}", 
                new Object[]{courseId, students.size()});
            return students;
        } catch (Exception e) {
            logger.log(Level.SEVERE, "Error retrieving students for courseId: {0}, Error: {1}", 
                new Object[]{courseId, e.getMessage()});
            return Collections.emptyList();
        } finally {
            em.close();
        }
    }

    public List<Enrollment> getEnrollmentsByCourseId(int courseId) {
        EntityManager em = JPAUtil.getEntityManager();
        try {
            TypedQuery<Enrollment> query = em.createQuery(
                "SELECT e FROM Enrollment e WHERE e.course.idCourse = :courseId AND e.student.role = 'student'",
                Enrollment.class
            );
            query.setParameter("courseId", courseId);
            List<Enrollment> enrollments = query.getResultList();
            logger.log(Level.INFO, "Total enrollments found for courseId {0}: {1}", 
                new Object[]{courseId, enrollments.size()});
            return enrollments;
        } catch (Exception e) {
            logger.log(Level.SEVERE, "Error retrieving enrollments for courseId: {0}, Error: {1}", 
                new Object[]{courseId, e.getMessage()});
            return Collections.emptyList();
        } finally {
            em.close();
        }
    }
}