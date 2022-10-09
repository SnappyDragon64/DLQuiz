package in.kjsieit.dlquiz.activity;

import android.content.Intent;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import in.kjsieit.dlquiz.R;
import in.kjsieit.dlquiz.quiz.util.ResourcesHelper;

public class MainActivity extends AppCompatActivity {
    Button startButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        init();
        setupButtons();
    }

    private void init() {
        if (ResourcesHelper.resources == null) {
            ResourcesHelper.resources = getResources();
        }

        startButton = findViewById(R.id.startButton);
    }

    private void setupButtons() {
        startButton.setOnClickListener(v -> startActivity(new Intent(MainActivity.this, QuizActivity.class)));
    }
}
