package address.teruten.com.terutenaddress.vo;

import java.util.ArrayList;

/**
 * Created by teruten on 2017-08-10.
 */

public class CafeteriaMenu {
    private String date;
    private ArrayList<String> lunch;
    private ArrayList<String> dinner;

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public ArrayList<String> getLunch() {
        return lunch;
    }

    public void setLunch(ArrayList<String> lunch) {
        this.lunch = lunch;
    }

    public ArrayList<String> getDinner() {
        return dinner;
    }

    public void setDinner(ArrayList<String> dinner) {
        this.dinner = dinner;
    }
}
