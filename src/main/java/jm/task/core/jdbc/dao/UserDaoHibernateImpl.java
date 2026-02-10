package jm.task.core.jdbc.dao;

import jm.task.core.jdbc.model.User;
import jm.task.core.jdbc.util.Util;
import org.hibernate.Session;

import org.hibernate.SessionFactory;
import org.hibernate.Transaction;

import java.util.List;


public class UserDaoHibernateImpl implements UserDao {
    private final SessionFactory sessionFactory = Util.getSessionFactory();

    @Override
    public void createUsersTable() {


        try (Session session = sessionFactory.openSession()) {
            Transaction tx = session.beginTransaction();
            try {
                session.createNativeQuery("""
                        CREATE TABLE IF NOT EXISTS users (
                        id BIGINT PRIMARY KEY AUTO_INCREMENT,
                        name VARCHAR(50),
                        lastName VARCHAR(50),
                        age TINYINT
                        )""", Void.class).executeUpdate();
                tx.commit();
            } catch (Exception e) {
                tx.rollback();
                throw new RuntimeException(e);
            }
        }
    }


    @Override
    public void dropUsersTable() {
        try (Session session = sessionFactory.openSession()) {
            Transaction tx = session.beginTransaction();
            try {
                session.createNativeQuery("DROP TABLE IF EXISTS users", Void.class).executeUpdate();
                tx.commit();
            } catch (Exception e) {
                tx.rollback();
                throw new RuntimeException(e);
            }
        }
    }

    @Override
    public void saveUser(String name, String lastName, byte age) {
        try (Session session = sessionFactory.openSession()) {
            Transaction tx = session.beginTransaction();
            try {
                session.persist(new User(name, lastName, age));
                tx.commit();
            } catch (Exception e) {
                tx.rollback();
                throw new RuntimeException(e);
            }
        }
    }

    @Override
    public void removeUserById(long id) {
        try (Session session = sessionFactory.openSession()) {
            Transaction tx = session.beginTransaction();
            try {
                User user = session.get(User.class, id);
                if (user != null) {
                    session.remove(user);
                }
                tx.commit();
            } catch (Exception e) {
                tx.rollback();
                throw new RuntimeException(e);
            }
        }
    }

    @Override
    public List<User> getAllUsers() {
        try (Session session = sessionFactory.openSession()) {
            return session.createQuery("FROM User", User.class).list();
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public void cleanUsersTable() {
        try (Session session = sessionFactory.openSession()) {
            Transaction tx = session.beginTransaction();
            try {
                session.createMutationQuery("DELETE FROM User").executeUpdate();
                tx.commit();
            } catch (Exception e) {
                tx.rollback();
                throw new RuntimeException(e);
            }
        }
    }
}
