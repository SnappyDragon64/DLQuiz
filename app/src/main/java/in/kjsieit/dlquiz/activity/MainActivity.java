package in.kjsieit.dlquiz.activity;

import android.content.Intent;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import in.kjsieit.dlquiz.R;
import in.kjsieit.dlquiz.quiz.util.ResourcesHelper;

public class MainActivity extends AppCompatActivity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        if (ResourcesHelper.resources == null) {
            ResourcesHelper.resources = getResources();
        }

        setupButtons();
    }

    @Override
    public void onBackPressed() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setCancelable(false);
        builder.setMessage("Do you want to exit?");
        builder.setPositiveButton("Exit", (dialog, which) -> finish());
        builder.setNegativeButton("Cancel", (dialog, which) -> dialog.cancel());
        AlertDialog alert=builder.create();
        alert.show();
    }

    private void setupButtons() {
        Button startButton = findViewById(R.id.startButton);
        startButton.setOnClickListener(v -> startActivity(new Intent(MainActivity.this, QuizActivity.class)));
    }
}
