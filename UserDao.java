// Contoh UserDao.java (UBAH FILE INI)
// Contoh UserDao.java (UBAH FILE INI)
package org.example.database.dao;

import org.example.database.entity.Users;
import org.hibernate.Session;
import org.hibernate.query.Query;
import org.example.config.HibernateUtil;

public class UserDao extends BaseDao<Users, Long> {

    public UserDao() {
        super(Users.class);
    }

    public Users findByEmail(String email) {
        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
            Query<Users> query = session.createQuery("FROM Users WHERE email = :email", Users.class);
            query.setParameter("email", email);
            return query.uniqueResult();
        } catch (Exception e) {
            e.printStackTrace();
            throw new RuntimeException("Error finding user by email: " + e.getMessage());
        }
    }
}