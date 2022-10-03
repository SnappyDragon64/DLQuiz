package in.kjsieit.dlquiz.activity;

import android.content.Intent;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import in.kjsieit.dlquiz.R;

import java.util.ArrayList;
import java.util.List;

public class QuizActivity extends AppCompatActivity {
    private int selectedId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        getSupportActionBar().hide();
        this.getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_quiz);

        Button submit = findViewById(R.id.submit);
        Button optionA = findViewById(R.id.optionA);
        Button optionB = findViewById(R.id.optionB);
        Button optionC = findViewById(R.id.optionC);
        Button optionD = findViewById(R.id.optionD);
        Button[] options = {optionA, optionB, optionC, optionD};

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
}