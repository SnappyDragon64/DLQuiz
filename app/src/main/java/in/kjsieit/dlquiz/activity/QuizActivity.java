package in.kjsieit.dlquiz.activity;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Handler;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import in.kjsieit.dlquiz.R;
import in.kjsieit.dlquiz.quiz.Difficulty;
import in.kjsieit.dlquiz.quiz.Phase;
import in.kjsieit.dlquiz.quiz.json.Question;
import in.kjsieit.dlquiz.quiz.json.QuestionSetParser;

import java.util.*;

public class QuizActivity extends AppCompatActivity {
    private Random random;

    private List<Question> EASY_QUESTION_SET;
    private List<Question> MEDIUM_QUESTION_SET;
    private List<Question> HARD_QUESTION_SET;

    private List<Question> easyQuestionSet;
    private List<Question> mediumQuestionSet;
    private List<Question> hardQuestionSet;

    private List<Question> currentSet;
    private Question current;
    private Difficulty difficulty;
    private Phase phase;
    private int selectedId;
    private int seconds;
    private int streakCtr;
    private int qCtr;
    private int scoreCtr;

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
        setFullscreen();
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

    private void setFullscreen() {
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        getSupportActionBar().hide();
        this.getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
    }

    private void init() {
        random = new Random();

        EASY_QUESTION_SET = QuestionSetParser.parse(getResources().openRawResource(R.raw.easy));
        MEDIUM_QUESTION_SET = QuestionSetParser.parse(getResources().openRawResource(R.raw.medium));
        HARD_QUESTION_SET = QuestionSetParser.parse(getResources().openRawResource(R.raw.hard));

        easyQuestionSet = new ArrayList<>(EASY_QUESTION_SET);
        mediumQuestionSet = new ArrayList<>(MEDIUM_QUESTION_SET);
        hardQuestionSet = new ArrayList<>(HARD_QUESTION_SET);

        currentSet = Collections.emptyList();
        difficulty = Difficulty.EASY;
        phase = Phase.ANSWER;
        int selectedId = -1;
        int seconds = 0;
        int streakCtr = 0;
        int qCtr = 0;
        int scoreCtr = 0;

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
        currentSet = easyQuestionSet;

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

    private void loadQuestion() {
        qCtr++;
        balance();

        if (currentSet.isEmpty()) {
            refillSet(difficulty);
            currentSet = getSet(difficulty);
        }

        current = currentSet.get(random.nextInt(currentSet.size()));
        currentSet.remove(current);

        question.setText(current.getQuestion());
        questionNumber.setText(String.format(Locale.getDefault(), "%d/20", qCtr));

        for (int i = 0; i < 4; i++) {
            options[i].setText(current.getOptions().get(i));
        }
    }

    private void balance() {
        if (streakCtr == 4 && difficulty != Difficulty.HARD) {
            streakCtr = 0;

            switch (difficulty) {
                case EASY:
                    difficulty = Difficulty.MEDIUM;
                    break;
                case MEDIUM:
                    difficulty = Difficulty.HARD;
                    break;
            }
        }
        else if (streakCtr == -1) {
            streakCtr = 0;

            switch (difficulty) {
                case HARD:
                    difficulty = Difficulty.MEDIUM;
                    break;
                case MEDIUM:
                    difficulty = Difficulty.EASY;
                    break;
            }
        }

        currentSet = getSet(difficulty);
    }

    private List<Question> getSet(Difficulty difficulty) {
        switch (difficulty) {
            case EASY:
                return easyQuestionSet;
            case MEDIUM:
                return mediumQuestionSet;
            case HARD:
                return hardQuestionSet;
            default:
                return null;
        }
    }

    private void refillSet(Difficulty difficulty) {
        switch (difficulty) {
            case EASY:
                easyQuestionSet = new ArrayList<>(EASY_QUESTION_SET);
                break;
            case MEDIUM:
                mediumQuestionSet = new ArrayList<>(MEDIUM_QUESTION_SET);
                break;
            case HARD:
                hardQuestionSet = new ArrayList<>(HARD_QUESTION_SET);
                break;
        }
    }
}