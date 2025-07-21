package dao;

import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.logging.Level;
import java.util.logging.Logger;

import jakarta.persistence.EntityManager;
import jakarta.persistence.EntityTransaction;
import jakarta.persistence.TypedQuery;
import model.Assignment;
import model.Submission;

public class SubmissionDAO {

    private static final Logger logger = Logger.getLogger(SubmissionDAO.class.getName());

    /**
     * Saves a new submission record to the database.
     *
     * @param submission The Submission object to persist.
     * @return The saved Submission object.
     */
    public Submission saveSubmission(Submission submission) {
        EntityManager em = JPAUtil.getEntityManager();
        EntityTransaction transaction = em.getTransaction();
        try {
            transaction.begin();
            em.persist(submission);
            transaction.commit();
            logger.info("Successfully saved submission for assignment ID: " + submission.getAssignment().getIdAssignment());
            return submission;
        } catch (Exception e) {
            if (transaction.isActive()) {
                transaction.rollback();
            }
            logger.log(Level.SEVERE, "Error saving submission", e);
            throw new RuntimeException("Failed to save submission", e);
        } finally {
            em.close();
        }
    }

    /**
     * Updates a submission, typically for grading.
     *
     * @param submission The Submission object with updated data (e.g., a grade).
     */
    public void updateSubmission(Submission submission) {
        EntityManager em = JPAUtil.getEntityManager();
        EntityTransaction transaction = em.getTransaction();
        try {
            transaction.begin();
            em.merge(submission);
            transaction.commit();
            logger.info("Successfully updated submission ID: " + submission.getIdSubmission());
        } catch (Exception e) {
            if (transaction.isActive()) {
                transaction.rollback();
            }
            logger.log(Level.SEVERE, "Error updating submission ID: " + submission.getIdSubmission(), e);
            throw new RuntimeException("Failed to update submission", e);
        } finally {
            em.close();
        }
    }

    /**
     * Retrieves a submission by its unique ID.
     *
     * @param submissionId The ID of the submission.
     * @return An Optional containing the submission if found.
     */
    public Optional<Submission> findSubmissionById(int submissionId) {
        EntityManager em = JPAUtil.getEntityManager();
        try {
            return Optional.ofNullable(em.find(Submission.class, submissionId));
        } finally {
            em.close();
        }
    }

    /**
     * Retrieves all submissions for a specific assignment.
     *
     * @param assignmentId The ID of the assignment.
     * @return A list of submissions.
     */
    public List<Submission> getSubmissionsByAssignment(int assignmentId) {
        EntityManager em = JPAUtil.getEntityManager();
        try {
            TypedQuery<Submission> query = em.createQuery(
                "SELECT s FROM Submission s WHERE s.assignment.idAss = :assignmentId",
                Submission.class
            );
            query.setParameter("assignmentId", assignmentId);
            return query.getResultList();
        } catch (Exception e) {
            logger.log(Level.SEVERE, "Error retrieving submissions for assignment ID: " + assignmentId, e);
            return Collections.emptyList();
        } finally {
            em.close();
        }
    }

    public List<Submission> getSubmissionsByStudent(int studentId) {
        EntityManager em = JPAUtil.getEntityManager();
        try {
            TypedQuery<Submission> query = em.createQuery(
                "SELECT s FROM Submission s WHERE s.student.idUser = :studentId",
                Submission.class    
            );
            query.setParameter("studentId", studentId);
            return query.getResultList();
        } catch (Exception e) {
            logger.log(Level.SEVERE, "Error retrieving submissions for student ID: " + studentId,
                e);
            return Collections.emptyList(); 
        } finally {
            em.close();
        }
    }

    public List<Submission> getSubmissionsByStudentAndCourse(int studentId, int courseId) {
        EntityManager em = JPAUtil.getEntityManager();
        try {
            TypedQuery<Submission> query = em.createQuery(
                "SELECT s FROM Submission s WHERE s.student.idUser = :studentId AND s.assignment.course.idCourse = :courseId",
                Submission.class
            );
            query.setParameter("studentId", studentId);
            query.setParameter("courseId", courseId);
            return query.getResultList();
        } catch (Exception e) {
            logger.log(Level.SEVERE, "Error retrieving submissions for student ID: " + studentId + " and course ID: " + courseId, e);
            return Collections.emptyList();
        } finally {
            em.close();
        }
    }

    // For teacher: Map assignmentId -> List<Submission>
    public Map<Integer, List<Submission>> getSubmissionsByAssignments(List<Assignment> assignments) {
        Map<Integer, List<Submission>> result = new HashMap<>();
        for (Assignment assignment : assignments) {
            result.put(assignment.getIdAssignment(), getSubmissionsByAssignment(assignment.getIdAssignment()));
        }
        return result;
    }

    // For student: Map assignmentId -> Submission
    public Map<Integer, Submission> getSubmissionsByStudentAndAssignments(int studentId, List<Assignment> assignments) {
        Map<Integer, Submission> result = new HashMap<>();
        EntityManager em = JPAUtil.getEntityManager();
        try {
            for (Assignment assignment : assignments) {
                TypedQuery<Submission> query = em.createQuery(
                    "SELECT s FROM Submission s WHERE s.assignment.idAssignment = :assignmentId AND s.student.idUser = :studentId",
                    Submission.class
                );
                query.setParameter("assignmentId", assignment.getIdAssignment());
                query.setParameter("studentId", studentId);
                List<Submission> submissions = query.getResultList();
                if (!submissions.isEmpty()) {
                    result.put(assignment.getIdAssignment(), submissions.get(0));
                }
            }
        } finally {
            em.close();
        }
        return result;
    }
}