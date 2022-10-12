package in.kjsieit.dlquiz.activity;

import android.content.Intent;
import android.os.Handler;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import com.google.firebase.crashlytics.buildtools.reloc.com.google.common.collect.Maps;
import in.kjsieit.dlquiz.R;
import in.kjsieit.dlquiz.quiz.Difficulty;
import in.kjsieit.dlquiz.quiz.Phase;
import in.kjsieit.dlquiz.quiz.question.Question;
import in.kjsieit.dlquiz.quiz.util.ResourcesHelper;

import java.util.*;

public class QuizActivity extends AppCompatActivity {
    private Random random = new Random();

    private final Map<Difficulty, List<Question>> difficultyQuestionSetMap = Maps.newEnumMap(Difficulty.class);

    private Question current;
    private Difficulty difficulty = Difficulty.EASY;
    private Phase phase = Phase.ANSWER;
    private int selectedId = -1;
    private int seconds = 0;
    private int streakCtr = 0;
    private int qCtr = 0;
    private int scoreCtr = 0;
    private int easyCtr = 0;
    private int mediumCtr = 0;
    private int hardCtr = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_quiz);
        setupButtons();
        startTimer();
        loadQuestion();
    }

    @Override
    public void onBackPressed() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setCancelable(false);
        builder.setMessage("Do you want to exit? Current progress will be lost!");
        builder.setPositiveButton("Exit", (dialog, which) -> finish());
        builder.setNegativeButton("Cancel", (dialog, which) -> dialog.cancel());
        AlertDialog alert=builder.create();
        alert.show();
    }

    private void setupButtons() {
        int i = 0;
        Button submit = findViewById(R.id.submit);
        submit.setEnabled(false);
        submit.setText("Submit");

        Button optionA = findViewById(R.id.optionA);
        Button optionB = findViewById(R.id.optionB);
        Button optionC = findViewById(R.id.optionC);
        Button optionD = findViewById(R.id.optionD);
        Button[] options = new Button[]{optionA, optionB, optionC, optionD};
        for (Button selectedButton : options) {
            int id = i;
            selectedButton.setOnClickListener(v -> {
                selectedId = id;
                submit.setEnabled(true);

                for (Button button : options) {
                    button.setBackgroundColor(getResources().getColor(R.color.purple_500, null));
                }

                selectedButton.setBackgroundColor(getResources().getColor(R.color.purple_700, null));
            });
            i++;
        }

        submit.setOnClickListener(v -> {
            switch (phase) {
                case ANSWER:
                    if (current.getAnswer() == selectedId) {
                        streakCtr++;
                        TextView scoreView = findViewById(R.id.score);
                        scoreView.setText(String.format(Locale.getDefault(), "Score: %d", ++scoreCtr));

                        if (difficulty == Difficulty.EASY)
                            easyCtr++;
                        else if (difficulty == Difficulty.MEDIUM)
                            mediumCtr++;
                        else
                            hardCtr++;
                    } else {
                        streakCtr = -1;
                        options[selectedId].setBackgroundColor(getResources().getColor(R.color.red));
                    }

                    options[current.getAnswer()].setBackgroundColor(getResources().getColor(R.color.green));

                    for (Button buttons : options) {
                        buttons.setEnabled(false);
                    }
                    phase = Phase.CHECK;
                    submit.setText("Next");
                    break;
                case CHECK:
                    if (qCtr == 20) {
                        Intent intent = new Intent(QuizActivity.this, ScorecardActivity.class);
                        Bundle dat = new Bundle();
                        dat.putInt("score", scoreCtr);
                        dat.putInt("time", seconds);
                        dat.putInt("easy", easyCtr);
                        dat.putInt("medium", mediumCtr);
                        dat.putInt("hard", hardCtr);
                        intent.putExtras(dat);
                        startActivity(intent);
                    }
                    else {
                        selectedId = -1;
                        submit.setEnabled(false);
                        for (Button button : options) {
                            button.setBackgroundColor(getResources().getColor(R.color.purple_500, null));
                            button.setEnabled(true);
                        }
                        phase = Phase.ANSWER;
                        submit.setText("Submit");
                        loadQuestion();
                    }
                    break;
            }
        });
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
                TextView timerView = findViewById(R.id.timer);
                timerView.setText(time);

                seconds++;
                handler.postDelayed(this, 1000);
            }
        });
    }

    public List<Question> getCurrentSet() {
        this.difficultyQuestionSetMap.putIfAbsent(this.difficulty, new ArrayList<>());
        return this.difficultyQuestionSetMap.get(this.difficulty);
    }

    private void loadQuestion() {
        qCtr++;
        balance();
        loadSet();

        current = getCurrentSet().get(random.nextInt(getCurrentSet().size()));
        getCurrentSet().remove(current);

        TextView questionView = findViewById(R.id.question);
        questionView.setText(current.getQuestion());
        TextView questionNumberView = findViewById(R.id.questionNumber);
        questionNumberView.setText(String.format(Locale.getDefault(), "%d/20", qCtr));

        Button optionA = findViewById(R.id.optionA);
        Button optionB = findViewById(R.id.optionB);
        Button optionC = findViewById(R.id.optionC);
        Button optionD = findViewById(R.id.optionD);
        Button[] options = new Button[]{optionA, optionB, optionC, optionD};
        for (int i = 0; i < 4; i++) {
            options[i].setText(current.getOptions().get(i));
        }
    }

    private void balance() {
        if (streakCtr == 4) {
            streakCtr = 0;
            difficulty = difficulty.increase();
        }
        else if (streakCtr == -1) {
            streakCtr = 0;
            difficulty = difficulty.decrease();
        }
    }

    private void loadSet() {
        if (getCurrentSet().isEmpty()) {
            this.getCurrentSet().addAll(this.difficulty.questionSet);
        }
    }
}
