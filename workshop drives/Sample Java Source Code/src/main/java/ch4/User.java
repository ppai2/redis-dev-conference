package ch4;

public class User
{
    private int id;
    private String username = null;
    private String fName = null;
    private String lName = null;
    private String email = null;

    public User() {
        // required for Bean Utils
    }

    public User(int id, String username, String fName, String lName, String email) {
        this.id = id;
        this.username = username;
        this.fName = fName;
        this.lName = lName;
        this.email = email;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getfName() {
        return fName;
    }

    public void setfName(String fName) {
        this.fName = fName;
    }

    public String getlName() {
        return lName;
    }

    public void setlName(String lName) {
        this.lName = lName;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }


    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }


    public String getKey() {
        return new StringBuilder()
                .append("user:")
                .append(id)
                .toString();
    }

    public String toString() {
       return new StringBuilder()
               .append("{ ")
               .append("id: ")
               .append(id)
               .append(", username: '")
               .append(username)
               .append("', fName: '")
               .append(fName)
               .append("', lName: '")
               .append(lName)
               .append("', email: '")
               .append(email)
               .append("' }").toString();
    }

}
