package uas.transaksi.event; // PERHATIKAN: Paketnya sekarang 'event'

import java.util.ArrayList;
import java.util.List;

public class DataChangePublisher {
    private List<DataChangeListener> listeners = new ArrayList<>();

    public void addListener(DataChangeListener listener) {
        if (!listeners.contains(listener)) {
            listeners.add(listener);
        }
    }

    public void removeListener(DataChangeListener listener) {
        listeners.remove(listener);
    }

    public void fireDataChanged() {
        // Melakukan copy untuk menghindari ConcurrentModificationException jika listener mengubah list saat iterasi
        List<DataChangeListener> listenersCopy = new ArrayList<>(listeners);
        for (DataChangeListener listener : listenersCopy) {
            listener.onDataChanged();
        }
    }
}