package in.kjsieit.dlquiz.quiz.json;

import android.util.JsonReader;
import com.google.firebase.crashlytics.buildtools.reloc.com.google.common.collect.ImmutableList;
import in.kjsieit.dlquiz.R;
import in.kjsieit.dlquiz.quiz.Difficulty;
import in.kjsieit.dlquiz.quiz.util.ResourcesHelper;

import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;
import java.util.*;

public class QuestionSetParser {
    public static ImmutableList<Question> parseSet(int id) {
        return ImmutableList.<Question>builder().addAll(parse(ResourcesHelper.resources.openRawResource(id))).build();
    }

    private static List<Question> parse(InputStream raw) {
        try (JsonReader reader = new JsonReader(new InputStreamReader(raw, StandardCharsets.UTF_8))) {
            List<Question> questions = new ArrayList<>();

            reader.beginArray();
            while (reader.hasNext()) {
                questions.add(readQuestion(reader));
            }
            reader.endArray();

            return questions;
        } catch (IOException e) {
            e.printStackTrace();
            return Collections.emptyList();
        }
    }

    private static Question readQuestion(JsonReader reader) throws IOException {
        String question = null;
        List<String> options = null;
        int answer = -1;

        reader.beginObject();
        while (reader.hasNext()) {
            String key = reader.nextName();
            switch (key) {
                case "question":
                    question = reader.nextString();
                    break;
                case "options":
                    options = new ArrayList<>();
                    reader.beginArray();
                    while (reader.hasNext()) {
                        options.add(reader.nextString());
                    }
                    reader.endArray();
                    break;
                case "answer":
                    answer = reader.nextInt();
                    break;
                default:
                    reader.skipValue();
            }
        }
        reader.endObject();

        return new Question(question, options, answer);
    }
}
