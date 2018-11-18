package ps.wecare.cardiacarrestdetector.db;

public class User {

    private int id;
    private String name;
    private String phone;
    private String password;
    private String age;

    public User(int id, String name, String phone, String password, String age) {
        this.id = id;
        this.name = name;
        this.phone = phone;
        this.password = password;
        this.age = age;
    }

    public int getId() {
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
