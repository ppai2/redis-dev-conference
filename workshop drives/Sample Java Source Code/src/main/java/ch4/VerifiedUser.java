package ch4;


public class VerifiedUser extends User {

    private Boolean verified = null;

    public VerifiedUser() {

    }

    public VerifiedUser(int id, String username, String fName, String lName, String email, boolean verified) {
        super(id, username, fName, lName, email);

        this.verified = verified;
    }

    public Boolean getVerified() {
        return verified;
    }

    public boolean isVerified() {
        return verified;
    }

    public void setVerified(boolean verified) {
        this.verified = verified;
    }

    public String toString() {
        return new StringBuilder()
                .append("{ ")
                .append("id: ")
                .append(getId())
                .append(", username: '")
                .append(getUsername())
                .append("', fName: '")
                .append(getfName())
                .append("', lName: '")
                .append(getlName())
                .append("', email: '")
                .append(getEmail())
                .append("', verified: ")
                .append(verified)
                .append(" }").toString();
    }
}
