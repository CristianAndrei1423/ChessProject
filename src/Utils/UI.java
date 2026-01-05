package Utils;

public class UI {
    /// Clasa doar pentru a afisa bucati de stringuri pentru main

    public static void clearScreen() {
        for (int i = 0; i < 50; ++i) System.out.println();
    }

    public static boolean checkOkEmail(String email){
        // lazy check if an email address has an @ and a . after it
        // it ain't much, but it's honest work
        int i = email.indexOf('@');

        if(i == 0 || i == email.length() - 1 || i == -1)
            return false;

        int j = email.lastIndexOf('.');

        return i < j;
    }

}
