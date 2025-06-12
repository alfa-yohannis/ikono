package KoreksiStokOutput;

import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.Transaction;
import java.time.LocalDateTime;
import java.util.Collections; // <-- Tambahkan import ini
import java.util.List;

public class StokService {
    private final SessionFactory sessionFactory;

    public StokService(SessionFactory sessionFactory) {
        this.sessionFactory = sessionFactory;
    }

    public List<Barang> getAllBarang() {
        // Siapkan list kosong sebagai nilai default
        List<Barang> barangList = Collections.emptyList();

        try (Session session = sessionFactory.openSession()) {
            // Jika berhasil, isi list dengan data dari database
            barangList = session.createQuery("from Barang", Barang.class).list();
        } catch (Exception e) {
            // Jika terjadi error, cetak error dan biarkan list tetap kosong
            e.printStackTrace();
        }
        // Selalu kembalikan list (baik yang sudah terisi atau yang masih kosong)
        return barangList;
    }

    public void koreksiStok(Barang barang, int jumlahKoreksi) throws Exception {
        Transaction transaction = null;
        try (Session session = sessionFactory.openSession()) {
            transaction = session.beginTransaction();

            Barang barangToUpdate = session.get(Barang.class, barang.getIdBarang());
            if (barangToUpdate == null) {
                throw new Exception("Barang tidak ditemukan dengan ID: " + barang.getIdBarang());
            }

            int stokLama = barangToUpdate.getStok();
            int stokBaru = stokLama - jumlahKoreksi;
            barangToUpdate.setStok(stokBaru);

            LogKoreksiStok log = new LogKoreksiStok();
            log.setBarang(barangToUpdate);
            log.setStokLama(stokLama);
            log.setStokBaru(stokBaru);
            log.setWaktuKoreksi(LocalDateTime.now());
            session.save(log);

            session.update(barangToUpdate);
            transaction.commit();
        } catch (Exception e) {
            if (transaction != null) {
                transaction.rollback();
            }
            throw new Exception("Gagal melakukan koreksi stok.", e);
        }
    }
}