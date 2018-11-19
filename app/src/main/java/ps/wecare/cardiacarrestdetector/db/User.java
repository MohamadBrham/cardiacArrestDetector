package ps.wecare.cardiacarrestdetector.db;

public class User {

    private long id;
    private String name;
    private String phone;
    private String password;
    private String age;

    public User( String name, String phone, String password, String age) {
        this.name = name;
        this.phone = phone;
        this.password = password;
        this.age = age;
    }

    public User(long id, String name, String phone, String password, String age) {
        this.id = id;
        this.name = name;
        this.phone = phone;
        this.password = password;
        this.age = age;
    }

    public void setId(long id) {
        this.id = id;
    }

    public long getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public String getPhone() {
        return phone;
    }


    public String getPassword() {
        return password;
    }

    public String getAge() {
        return age;
    }

}
