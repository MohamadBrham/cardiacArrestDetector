package ps.wecare.cardiacarrestdetector.db;

public class Medication {

    private long id;
    private long user_id;
    private String name;
    private String start;
    private String end;

    public Medication(long id, long user_id, String name, String start, String end) {
        this.id = id;
        this.name = name;
        this.user_id = user_id;
        this.start = start;
        this.end = end;
    }

    public Medication(long user_id, String name, String start, String end) {
        this.user_id = user_id;
        this.name = name;
        this.start = start;
        this.end = end;
    }

    public String getName() {
        return name;
    }

    public void setId(long id) {
        this.id = id;
    }

    public long getId() {
        return id;
    }

    public long getUser_id() {
        return user_id;
    }

    public String getStart() {
        return start;
    }

    public String getEnd() {
        return end;
    }
}
