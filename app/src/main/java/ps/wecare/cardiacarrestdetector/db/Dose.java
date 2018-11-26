package ps.wecare.cardiacarrestdetector.db;

public class Dose {
    private long id;
    private long medication_id;
    private String time;

    public Dose(long id, long medication_id,String time) {
        this.id = id;
        this.medication_id = medication_id;
        this.time = time;
    }
    public Dose(long medication_id,String time) {
        this.medication_id = medication_id;
        this.time = time;
    }
    public long getMedication_id() {
        return medication_id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public long getId() {
        return id;
    }

    public String getTime() {
        return time;
    }
}
