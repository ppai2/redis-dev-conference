package ch4;

public class EmptyUser extends AbstractHashExample
{
    public User createSampleUser() {
        User sample = new User(147, "ruser", "Redis", "User", "ruser@somedomain.net");
        return sample;
    }

    public static void main(String[] args) {

        try {
            EmptyUser eu = new EmptyUser();
            eu.init();

            User sample = eu.createSampleUser();


            System.out.println("ruser: " + sample);
            System.out.println("user key: " + sample.getKey());

            System.exit(0);
        } catch (Exception e) {
            System.err.println("Failed to execute example");
            e.printStackTrace();
            System.exit(-1);
        }
    }

}
