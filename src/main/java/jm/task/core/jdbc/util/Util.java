package jm.task.core.jdbc.util;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

import jm.task.core.jdbc.model.User;
import org.hibernate.SessionFactory;

import org.hibernate.cfg.Configuration;
import org.hibernate.cfg.Environment;




public class Util {
    private static final String URL = "jdbc:mysql://127.0.0.2:3306/1_1_3";
    private static final String USERNAME = "root";
    private static final String PASSWORD = "kata";

    public static Connection getConnection() {
        try {

            return DriverManager.getConnection(URL, USERNAME, PASSWORD);
        } catch (SQLException e) {

            throw new RuntimeException("Ошибка подключения к БД. Проверь url, username, password", e);
        }
    }



        public static class HibernateUtil {
            private static volatile SessionFactory sessionFactory;

            public static SessionFactory getSessionFactory() {
                if (sessionFactory == null) {
                    synchronized (HibernateUtil.class) {
                        if (sessionFactory == null) {
                            Configuration configuration = new Configuration()
                                    .setProperty("hibernate.connection.driver_class", "com.mysql.cj.jdbc.Driver")
                                    .setProperty("hibernate.connection.url", URL)
                                    .setProperty("hibernate.connection.username", USERNAME)
                                    .setProperty("hibernate.connection.password", PASSWORD)
                                    .setProperty("hibernate.dialect", "org.hibernate.dialect.MySQLDialect")
                                    .setProperty("hibernate.show_sql", "true")
                                    .setProperty("hibernate.format_sql", "true")
                                    .setProperty("hibernate.hbm2ddl.auto", "update")
                                    .setProperty("hibernate.current_session_context_class", "thread")
                                    .addAnnotatedClass(User.class);


                            sessionFactory = configuration.buildSessionFactory();
                        }
                    }
                }
                return sessionFactory;
            }

            public static void shutdown() {
                if (sessionFactory != null) {
                    sessionFactory.close();
                    sessionFactory = null;
                }
            }
        }
    }// реализуйте настройку соеденения с БД

