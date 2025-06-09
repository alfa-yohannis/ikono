package dokter.dao.impl;

import java.util.List;

import org.hibernate.Session;
import org.hibernate.Transaction;
import org.hibernate.query.Query;

import dokter.dao.ScheduleDAO;
import dokter.model.Schedule;
import dokter.util.HibernateUtil;

public class ScheduleDAOImpl implements ScheduleDAO {

    @Override
    public void save(Schedule schedule) {
        Transaction transaction = null;
        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
            transaction = session.beginTransaction();
            session.save(schedule);
            transaction.commit();
        } catch (Exception e) {
            if (transaction != null) transaction.rollback();
            e.printStackTrace();
        }
    }

    @Override
    public List<Schedule> getByDoctorId(int doctorId) {
        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
            Query<Schedule> query = session.createQuery("FROM Schedule s WHERE s.doctor.id = :doctorId", Schedule.class);
            query.setParameter("doctorId", doctorId);
            List<Schedule> schedules = query.list();
            schedules.forEach(Schedule::updateProperties); // Important for JavaFX
            return schedules;
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }
} 