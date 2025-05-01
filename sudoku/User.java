package sudoku;

import java.io.Serializable;

public class User implements Serializable {
    private String username;
    private String password;
    private int xp;

    public User(String username, String password) {
        this.username = username;
        this.password = password;
        this.xp = 0;
    }

    public String getUsername() {
        return username;
    }

    public boolean checkPassword(String password) {
        return this.password.equals(password);
    }

    public int getXP() {
        return xp;
    }

    public void addXP(int amount) {
        xp += amount;
    }

    public boolean useXP(int cost) {
        if (xp >= cost) {
            xp -= cost;
            return true;
        }
        return false;
    }
}
