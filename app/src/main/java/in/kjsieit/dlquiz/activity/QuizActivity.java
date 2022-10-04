package in.kjsieit.dlquiz.activity;

import android.os.Handler;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.TextView;
import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import in.kjsieit.dlquiz.R;

import java.util.Locale;

public class QuizActivity extends AppCompatActivity {
    private int selectedId;
    private int seconds;

    Button submit;
    Button optionA;
    Button optionB;
    Button optionC;
    Button optionD;
    Button[] options;

    TextView timer;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setFullscreen();
        setContentView(R.layout.activity_quiz);
        init();
        setupButtons();
        startTimer();
    }

    private void setFullscreen() {
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        getSupportActionBar().hide();
        this.getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
    }

    private void init() {
        submit = findViewById(R.id.submit);
        optionA = findViewById(R.id.optionA);
        optionB = findViewById(R.id.optionB);
        optionC = findViewById(R.id.optionC);
        optionD = findViewById(R.id.optionD);
        options = new Button[]{optionA, optionB, optionC, optionD};

        timer = findViewById(R.id.timer);
    }

    private void setupButtons() {
        submit.setEnabled(false);

        for (Button selectedButton : options) {
            selectedButton.setOnClickListener(v -> {
                selectedId = selectedButton.getId();
                submit.setEnabled(true);

                for (Button button : options) {
                    button.setBackgroundColor(getResources().getColor(R.color.purple_500, null));
                }

                selectedButton.setBackgroundColor(getResources().getColor(R.color.purple_700, null));
            });
        }
    }

    private void startTimer()
    {
        Handler handler = new Handler();

        handler.post(new Runnable() {
            @Override
            public void run()
            {
                String time;
                int h = seconds / 3600;
                int m = (seconds % 3600) / 60;
                int s = seconds % 60;

                if (h > 0)
                    time = String.format(Locale.getDefault(), "%d:%02d:%02d", h, m, s);
                else
                    time = String.format(Locale.getDefault(), "%02d:%02d", m, s);
                timer.setText(time);

                seconds++;
                handler.postDelayed(this, 1000);
            }
        });
    }
}