package ps.wecare.cardiacarrestdetector.db;

public class Medication {

    private int id;
    private int user_id;
    private String start;
    private String end;

    public Medication(int id, int user_id, String start, String end) {
        this.id = id;
        this.user_id = user_id;
        this.start = start;
        this.end = end;
    }

    public int getId() {
        return id;
    }

    public int getUser_id() {
        return user_id;
    }

    public String getStart() {
        return start;
    }

    public String getEnd() {
        return end;
    }
}
