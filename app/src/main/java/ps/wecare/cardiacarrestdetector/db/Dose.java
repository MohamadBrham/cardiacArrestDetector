package ps.wecare.cardiacarrestdetector.db;

public class Dose {
    private int id;
    private String time;

    public Dose(int id, String time) {
        this.id = id;
        this.time = time;
    }

    public int getId() {
        return id;
    }

    public String getTime() {
        return time;
    }
}
