package dao;

import java.time.LocalDateTime;
import java.util.List;

import jakarta.persistence.EntityManager;
import jakarta.persistence.EntityTransaction;
import jakarta.persistence.TypedQuery;
import model.User;
import model.UserActivity;

public class ActivityDAO {

    public void logLogin(User user) {
        UserActivity activity = new UserActivity();
        activity.setUser(user);
        activity.setLoginTime(LocalDateTime.now());
        activity.setLogoutTime(null); // Logout time is null at login
        activity.setDurationMinutes(0L); // Duration is 0 at login

        EntityManager em = JPAUtil.getEntityManager();
        EntityTransaction transaction = em.getTransaction();
        try {
            transaction.begin();
            em.persist(activity);
            transaction.commit();
        } catch (Exception e) {
            if (transaction.isActive()) transaction.rollback();
            throw new RuntimeException("Could not log login activity", e);
        } finally {
            em.close();
        }
    }
    
    // Additional methods for logout and activity reporting would be added here
    public void logLogout(User user) {
        UserActivity activity = new UserActivity();
        activity.setUser(user);
        activity.setLoginTime(LocalDateTime.now());
        activity.setLogoutTime(LocalDateTime.now());

        EntityManager em = JPAUtil.getEntityManager();
        EntityTransaction transaction = em.getTransaction();
        try {
            transaction.begin();
            em.persist(activity);
            transaction.commit();
        } catch (Exception e) {
            if (transaction.isActive()) transaction.rollback();
            throw new RuntimeException("Could not log logout activity", e);
        } finally {
            em.close();
        }
    }

    public List<UserActivity> getUserActivities(User user) {
        EntityManager em = JPAUtil.getEntityManager();
        try {
            TypedQuery<UserActivity> query = em.createQuery("SELECT ua FROM UserActivity ua WHERE ua.user = :user", UserActivity.class);
            query.setParameter("user", user);
            return query.getResultList();
        } finally {
            em.close();
        }
    }

    public int getTotalActivityForDate(String username, String today) {
        EntityManager em = JPAUtil.getEntityManager();
        try {
            TypedQuery<Long> query = em.createQuery(
                "SELECT COUNT(ua) FROM UserActivity ua WHERE ua.user = :username AND DATE(ua.loginTime) = :today",
                Long.class
            );
            query.setParameter("username", username);
            query.setParameter("today", today);
            Long count = query.getSingleResult();
            return count != null ? count.intValue() : 0;
        } finally {
            em.close();
        }
    }    
}