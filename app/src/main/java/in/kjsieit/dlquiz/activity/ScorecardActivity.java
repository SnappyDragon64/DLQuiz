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
        if (dat != null) {
            score = dat.getInt("score");
        }

        ((TextView) findViewById(R.id.scoredisplay)).setText(String.format(Locale.getDefault(), "Score: %d", score));
    }
}
