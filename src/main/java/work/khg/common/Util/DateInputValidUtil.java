package work.khg.common.Util;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Scanner;

public class DateInputValidUtil {
    private SimpleDateFormat DATE_FORMAT;

    public void setDateFormatPattern (String DatePattern) {
        try {
            this.DATE_FORMAT = new SimpleDateFormat(DatePattern);
        }catch (Exception e) {
            e.printStackTrace();
        }
    }

    public String getDateFromUser(String prompt, Scanner scanner, String DatePattern) {
        String date = "";
        setDateFormatPattern(DatePattern);

        while (true) {
            System.out.println(prompt);
            date = scanner.nextLine();
            if (isValidDate(date)) {
                break;
            } else {
                System.out.println("Invalid date format or date does not exist. Please enter the date in " + prompt +" format.");
            }
        }
        return date;
    }

    public boolean isValidDate(String date) {
        try {
            this.DATE_FORMAT.setLenient(false); // Strict parsing
            Date parsedDate = this.DATE_FORMAT.parse(date);
            return this.DATE_FORMAT.format(parsedDate).equals(date);
        } catch (ParseException e) {
            return false;
        }
    }
}
