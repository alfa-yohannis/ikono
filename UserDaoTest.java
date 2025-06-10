// UserDaoTest.java (UBAH FILE INI)
package org.example.database;

import org.example.config.HibernateUtil;
import org.example.database.dao.UserDao;
import org.example.database.entity.Users;
import org.hibernate.Session;
import org.hibernate.Transaction;
import org.junit.jupiter.api.*;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class UserDaoTest {

    private UserDao userDao;

    @BeforeEach
    void setUp() {
        userDao = new UserDao();
        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
            Transaction transaction = session.beginTransaction();
            session.createQuery("DELETE FROM Users").executeUpdate();
            transaction.commit();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    
    @Test
    @DisplayName("Should save a new user")
    void shouldSaveUser() {
        Users user = new Users("John Doe", "john.doe@example.com");
        Users savedUser = userDao.save(user);

        assertNotNull(savedUser.getId());
        assertEquals("John Doe", savedUser.getNama());
        assertEquals("john.doe@example.com", savedUser.getEmail());
    }

    @Test
    @DisplayName("Should find user by ID")
    void shouldFindUserById() {
        Users user = new Users("Jane Doe", "jane.doe@example.com");
        userDao.save(user);

        Users foundUser = userDao.findById(user.getId());
        assertNotNull(foundUser);
        assertEquals("Jane Doe", foundUser.getNama());
    }

    @Test
    @DisplayName("Should find user by email")
    void shouldFindUserByEmail() {
        Users user = new Users("Alice Smith", "alice.smith@example.com");
        userDao.save(user);

        Users foundUser = userDao.findByEmail("alice.smith@example.com");
        assertNotNull(foundUser);
        assertEquals("Alice Smith", foundUser.getNama());
    }

    @Test
    @DisplayName("Should get all users")
    void shouldGetAllUsers() {
        userDao.save(new Users("User 1", "user1@example.com"));
        userDao.save(new Users("User 2", "user2@example.com"));

        List<Users> users = userDao.findAll();
        assertNotNull(users);
        assertEquals(2, users.size());
    }

    @Test
    @DisplayName("Should update an existing user")
    void shouldUpdateUser() {
        Users user = new Users("Old Name", "old.email@example.com");
        userDao.save(user);

        user.setNama("New Name");
        user.setEmail("new.email@example.com");
        Users updatedUser = userDao.update(user);

        assertNotNull(updatedUser);
        assertEquals("New Name", updatedUser.getNama());
        assertEquals("new.email@example.com", updatedUser.getEmail());

        Users foundUser = userDao.findById(user.getId());
        assertEquals("New Name", foundUser.getNama());
    }

    @Test
    @DisplayName("Should delete a user")
    void shouldDeleteUser() {
        Users user = new Users("Users to Delete", "delete.me@example.com");
        userDao.save(user);

        userDao.deleteById(user.getId());

        Users deletedUser = userDao.findById(user.getId());
        assertNull(deletedUser);
    }
}