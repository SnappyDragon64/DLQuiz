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
    private Random random;

    private Map<Difficulty, List<Question>> difficultyQuestionSetMap;

    private Question current;
    private Difficulty difficulty;
    private Phase phase;
    private int selectedId;
    private int seconds;
    private int streakCtr;
    private int qCtr;
    private int scoreCtr;
    private int easyCtr;
    private int mediumCtr;
    private int hardCtr;

    Button submit;
    Button optionA;
    Button optionB;
    Button optionC;
    Button optionD;
    Button[] options;

    TextView timer;
    TextView questionNumber;
    TextView score;
    TextView question;

    ImageView image;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_quiz);
        init();
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

    private void init() {
        random = new Random();

        difficultyQuestionSetMap = Maps.newEnumMap(Difficulty.class);

        difficulty = Difficulty.EASY;
        phase = Phase.ANSWER;
        selectedId = -1;
        seconds = 0;
        streakCtr = 0;
        qCtr = 0;
        scoreCtr = 0;
        easyCtr = 0;
        mediumCtr = 0;
        hardCtr = 0;

        submit = findViewById(R.id.submit);
        optionA = findViewById(R.id.optionA);
        optionB = findViewById(R.id.optionB);
        optionC = findViewById(R.id.optionC);
        optionD = findViewById(R.id.optionD);
        options = new Button[]{optionA, optionB, optionC, optionD};

        timer = findViewById(R.id.timer);
        questionNumber = findViewById(R.id.questionNumber);
        score = findViewById(R.id.score);
        question = findViewById(R.id.question);

        image = findViewById(R.id.image);
    }

    private void setupButtons() {
        int i = 0;
        submit.setEnabled(false);
        submit.setText("Submit");

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
                        score.setText(String.format(Locale.getDefault(), "Score: %d", ++scoreCtr));

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
                timer.setText(time);

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

        question.setText(current.getQuestion());
        questionNumber.setText(String.format(Locale.getDefault(), "%d/20", qCtr));

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
            this.getCurrentSet().addAll(this.difficulty.getQuestionSet());
        }
    }
}
