package ps.wecare.cardiacarrestdetector.db;

public class Beloved {
    private long id;
    private long user_id;
    private String phone;
    private String status;

    public void setId(long id) {
        this.id = id;
    }
    public Beloved( long user_id, String phone, String status) {
        this.user_id = user_id;
        this.phone = phone;
        this.status = status;
    }
    public Beloved(long id, int user_id, String phone, String status) {
        this.id = id;
        this.user_id = user_id;
        this.phone = phone;
        this.status = status;
    }

    public long getId() {
        return id;
    }

    public long getUser_id() {
        return user_id;
    }

    public String getPhone() {
        return phone;
    }

    public String getStatus() {
        return status;
    }
}
