package ps.wecare.cardiacarrestdetector.db;

public class Beloved {
    private int id;
    private int user_id;
    private String phone;
    private String status;

    public Beloved(int id, int user_id, String phone, String status) {
        this.id = id;
        this.user_id = user_id;
        this.phone = phone;
        this.status = status;
    }

    public int getId() {
        return id;
    }

    public int getUser_id() {
        return user_id;
    }

    public String getPhone() {
        return phone;
    }

    public String getStatus() {
        return status;
    }
}
