package in.kjsieit.dlquiz.quiz.util;

import java.util.Locale;

public class Stringify {
    public static String time(int seconds) {
        int h = seconds / 3600;
        int m = (seconds % 3600) / 60;
        int s = seconds % 60;

        if (h > 0)
            return String.format(Locale.getDefault(), "%d:%02d:%02d", h, m, s);
        else
            return String.format(Locale.getDefault(), "%02d:%02d", m, s);
    }
}
