package in.kjsieit.dlquiz.activity;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.TypedValue;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.Space;
import android.widget.TextView;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.app.AppCompatDelegate;
import androidx.cardview.widget.CardView;
import androidx.preference.PreferenceManager;
import in.kjsieit.dlquiz.R;
import in.kjsieit.dlquiz.quiz.Difficulty;
import in.kjsieit.dlquiz.quiz.question.AnsweredQuestion;
import in.kjsieit.dlquiz.quiz.util.ResourcesHelper;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Locale;

public class HistoryActivity extends AppCompatActivity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_history);

        LinearLayout layout = findViewById(R.id.hCardList);
        layout.removeAllViews();

        SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(this);
        JSONArray historyList = new JSONArray();
        String value = preferences.getString("history", historyList.toString());
        try {
            historyList = new JSONArray(value);
        } catch (JSONException e) {
            e.printStackTrace();
        }

        int historyLen = historyList.length();

        for (int i = 0; i < historyLen; i++) {
            try {
                JSONObject historyObj = historyList.getJSONObject(i);
                int score = historyObj.getInt("score");
                int seconds = historyObj.getInt("time");
                int easyCtr = historyObj.getInt("easy");
                int mediumCtr = historyObj.getInt("medium");
                int hardCtr = historyObj.getInt("hard");
                JSONArray answeredObj = historyObj.getJSONArray("answeredQuestions");
                ArrayList<AnsweredQuestion> answeredQuestions = new ArrayList<>();

                int answeredLen = answeredObj.length();

                for (int j = 0; j < answeredLen; j++) {
                    try {
                        JSONObject answeredQ = answeredObj.getJSONObject(j);
                        answeredQuestions.add(new AnsweredQuestion(
                                Difficulty.valueOf(answeredQ.getString("difficulty")),
                                answeredQ.getString("question"),
                                answeredQ.getString("answer"),
                                answeredQ.getString("selectedAnswer"),
                                answeredQ.getBoolean("isCorrect") ? 1 : 0
                        ));
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }

                LayoutInflater infalInflater = (LayoutInflater) getBaseContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
                View childView = infalInflater.inflate(R.layout.history_card_layout, null);
                CardView cardView = childView.findViewById(R.id.hCard);

                String timestr;
                int h = seconds / 3600;
                int m = (seconds % 3600) / 60;
                int s = seconds % 60;

                if (h > 0)
                    timestr = String.format(Locale.getDefault(), "%d:%02d:%02d", h, m, s);
                else
                    timestr = String.format(Locale.getDefault(), "%02d:%02d", m, s);
                TextView scoreDisp = childView.findViewById(R.id.scoreDisp);
                scoreDisp.setText(String.format(Locale.getDefault(), "%d/20", score));
                TextView timeDisp = childView.findViewById(R.id.timeDisp);
                timeDisp.setText(timestr);

                Intent intent = new Intent(HistoryActivity.this, ScorecardActivity.class);
                Bundle dat = new Bundle();
                dat.putInt("score", score);
                dat.putInt("time", seconds);
                dat.putInt("easy", easyCtr);
                dat.putInt("medium", mediumCtr);
                dat.putInt("hard", hardCtr);
                dat.putParcelableArrayList("answered", answeredQuestions);
                intent.putExtras(dat);

                cardView.setOnClickListener(v -> startActivity(intent));

                layout.addView(childView);

                if (i < historyLen - 1) {
                    Space space = new Space(getBaseContext());
                    space.setMinimumHeight((int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 8, getResources().getDisplayMetrics()));
                    layout.addView(space);
                }
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
    }
}
