package uas.transaksi.dao;

import uas.transaksi.data.ShipmentData;
import uas.transaksi.data.SenderData;
import uas.transaksi.data.ReceiverData;
import uas.transaksi.util.HibernateUtil;
import org.hibernate.Session;
import org.hibernate.Transaction;
import jakarta.persistence.TypedQuery;
import jakarta.persistence.NoResultException;

import java.sql.Date;
import java.util.List;

public class ShipmentDao implements IShipmentDao { // <--- Implement interface

    // DAO lain diinisialisasi di sini karena sering dipakai untuk relasi
    // Ganti ke interface
    private ISenderDao senderDao;
    private IReceiverDao receiverDao;

    public ShipmentDao() {
        this.senderDao = new SenderDao(); // Inisialisasi implementasi konkret
        this.receiverDao = new ReceiverDao(); // Inisialisasi implementasi konkret
    }

    @Override // <--- Tambahkan @Override
    public void saveShipment(ShipmentData shipment) {
        Transaction transaction = null;
        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
            transaction = session.beginTransaction();
            session.persist(shipment);
            transaction.commit();
        } catch (Exception e) {
            if (transaction != null) {
                transaction.rollback();
            }
            System.err.println("Error saving shipment: " + e.getMessage());
            e.printStackTrace();
            throw e;
        }
    }

    @Override // <--- Tambahkan @Override
    public ShipmentData getShipmentById(int id) {
        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
            TypedQuery<ShipmentData> query = session.createQuery(
                "SELECT s FROM ShipmentData s JOIN FETCH s.sender JOIN FETCH s.receiver WHERE s.id = :id",
                ShipmentData.class
            );
            query.setParameter("id", id);
            return query.getSingleResult();
        } catch (NoResultException e) {
            System.out.println("No shipment found with ID: " + id);
            return null;
        } catch (Exception e) {
            System.err.println("Error getting shipment by ID: " + e.getMessage());
            e.printStackTrace();
            return null;
        }
    }

    @Override // <--- Tambahkan @Override
    public List<ShipmentData> getAllShipments() {
        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
            return session.createQuery("SELECT s FROM ShipmentData s JOIN FETCH s.sender JOIN FETCH s.receiver", ShipmentData.class).list();
        } catch (Exception e) {
            System.err.println("Error getting all shipments: " + e.getMessage());
            e.printStackTrace();
            return null;
        }
    }

    @Override // <--- Tambahkan @Override
    public void updateShipment(ShipmentData shipment) {
        Transaction transaction = null;
        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
            transaction = session.beginTransaction();
            session.merge(shipment);
            transaction.commit();
        } catch (Exception e) {
            if (transaction != null) {
                transaction.rollback();
            }
            System.err.println("Error updating shipment: " + e.getMessage());
            e.printStackTrace();
            throw e;
        }
    }

    @Override // <--- Tambahkan @Override
    public void deleteShipment(int id) {
        Transaction transaction = null;
        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
            transaction = session.beginTransaction();
            ShipmentData shipment = session.get(ShipmentData.class, id);
            if (shipment != null) {
                session.remove(shipment);
            }
            transaction.commit();
        } catch (Exception e) {
            if (transaction != null) {
                transaction.rollback();
            }
            System.err.println("Error deleting shipment: " + e.getMessage());
            e.printStackTrace();
            throw e;
        }
    }

    // Metode tambahan untuk Service Layer yang menggunakan DAO ini (sudah pakai interface)
    public SenderData getSenderDataForShipment(int senderId) {
        return senderDao.getSenderById(senderId);
    }

    public ReceiverData getReceiverDataForShipment(int receiverId) {
        return receiverDao.getReceiverById(receiverId);
    }
}