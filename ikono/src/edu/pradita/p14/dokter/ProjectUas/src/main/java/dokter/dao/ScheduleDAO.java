package dokter.dao;

import dokter.model.Schedule;
import java.util.List;

public interface ScheduleDAO {
    void save(Schedule schedule);
    List<Schedule> getByDoctorId(int doctorId);
    // Add other methods like update, delete if needed
} 