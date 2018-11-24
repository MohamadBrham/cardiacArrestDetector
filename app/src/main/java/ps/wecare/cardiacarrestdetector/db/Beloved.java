package ps.wecare.cardiacarrestdetector.db;

public class Beloved {
    private long id;
    private long user_id;
    private String phone;
    private String name;
    private String status;

    public void setId(long id) {
        this.id = id;
    }

    public Beloved( long user_id, String phone,String name, String status) {
        this.user_id = user_id;
        this.phone = phone;
        this.status = status;
        this.name = name;
    }
    public Beloved(long id, long user_id, String phone,String name, String status) {
        this.id = id;
        this.user_id = user_id;
        this.phone = phone;
        this.status = status;
        this.name = name;
    }
    public String getName() {
        return name;
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
