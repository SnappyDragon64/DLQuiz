package in.kjsieit.dlquiz.activity;

import android.content.Intent;
import android.os.Bundle;
import android.view.Window;
import android.view.WindowManager;
import android.widget.TextView;
import androidx.appcompat.app.AppCompatActivity;
import in.kjsieit.dlquiz.R;

import java.util.Locale;

public class ScorecardActivity extends AppCompatActivity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_scorecard);

        Bundle dat = getIntent().getExtras();
        int score  = 0;
        int time = 0;
        int easy = 0;
        int medium = 0;
        int hard = 0;
        if (dat != null) {
            score = dat.getInt("score");
            time = dat.getInt("time");
            easy = dat.getInt("easy");
            medium = dat.getInt("medium");
            hard = dat.getInt("hard");
        }
        String scorestr = String.format(Locale.getDefault(), "Score: %d", score);;

        String timestr;
        int h = time / 3600;
        int m = (time % 3600) / 60;
        int s = time % 60;

        if (h > 0)
            timestr = String.format(Locale.getDefault(), "%d:%02d:%02d", h, m, s);
        else
            timestr = String.format(Locale.getDefault(), "%02d:%02d", m, s);

        String qstr = String.format(Locale.getDefault(), "Easy: %d\nMedium: %d\nHard: %d", easy, medium, hard);

        ((TextView) findViewById(R.id.scoredisplay)).setText(String.format("%s\n%s\n%s", scorestr, timestr, qstr));
    }
}
