import javafx.application.Application;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.stage.Stage;
import java.util.*;

public class CareerPathAdvisor extends Application {

    private TextArea chatArea;
    private TextField inputField;
    private Button sendButton;
    private Label advisorStatus;
    private int step = 0;
    private String userName = "", userStream = "";
    private Map<String, String> subjectInfo = new HashMap<>();
    private Map<String, List<String>> recommendations = new HashMap<>();

    @Override
    public void start(Stage stage) {
        stage.setTitle("CareerPath Advisor");

        // Welcome Screen
        VBox welcomeBox = new VBox(15);
        welcomeBox.setAlignment(Pos.CENTER);
        welcomeBox.setStyle("-fx-background-color: linear-gradient(to bottom right, #4f8cff, #38ef7d);");
        Label welcomeText = new Label("🎉 Welcome to CareerPath Advisor!");
        welcomeText.setTextFill(Color.WHITE);
        welcomeText.setFont(Font.font("Baloo 2", 26));
        Button startButton = new Button("Start");
        startButton.setStyle("-fx-background-color: white; -fx-text-fill: #38ef7d; -fx-font-weight: bold; "
                + "-fx-background-radius: 20;");
        welcomeBox.getChildren().addAll(welcomeText, startButton);

        Scene welcomeScene = new Scene(welcomeBox, 500, 400);
        stage.setScene(welcomeScene);
        stage.show();

        // Chat UI Components
        chatArea = new TextArea();
        chatArea.setEditable(false);
        chatArea.setWrapText(true);
        chatArea.setStyle("-fx-control-inner-background: #f7fafd;");

        advisorStatus = new Label("Advisor 🤖");
        advisorStatus.setTextFill(Color.GREEN);

        inputField = new TextField();
        inputField.setPromptText("Type your answer...");
        sendButton = new Button("Send");

        HBox inputBar = new HBox(10, inputField, sendButton);
        inputBar.setAlignment(Pos.CENTER);

        VBox chatBox = new VBox(10, advisorStatus, chatArea, inputBar);
        chatBox.setPadding(new Insets(10));
        Scene chatScene = new Scene(chatBox, 500, 500);

        // Initialize data
        loadSubjectInfo();
        loadRecommendations();

        startButton.setOnAction(e -> {
            stage.setScene(chatScene);
            appendBot("Hi! 👋 I'm here to help you decide your career after 12th. First, what's your name?");
        });

        sendButton.setOnAction(e -> handleUserInput());
        inputField.setOnAction(e -> handleUserInput());
    }

    private void appendUser(String msg) {
        chatArea.appendText("You: " + msg + "\n");
    }

    private void appendBot(String msg) {
        chatArea.appendText("Advisor: " + msg + "\n");
    }

    private void handleUserInput() {
        String input = inputField.getText().trim();
        if (input.isEmpty()) return;
        appendUser(input);
        inputField.clear();

        new Thread(() -> {
            try { Thread.sleep(600); } catch (InterruptedException ignored) {}
            javafx.application.Platform.runLater(() -> handleBotResponse(input));
        }).start();
    }

    private void handleBotResponse(String input) {
        String lower = input.toLowerCase();

        if (lower.startsWith("tell me about ")) {
            showSubjectInfo(input.substring(14).trim());
            return;
        } else if (lower.startsWith("info about ")) {
            showSubjectInfo(input.substring(10).trim());
            return;
        }

        switch (step) {
            case 0:
                userName = input.split(" ")[0];
                appendBot("Nice to meet you, " + userName + "! Which stream did you choose after 10th? (Science/Commerce/Arts/Vocational/Other)");
                step++;
                break;

            case 1:
                userStream = capitalize(input);
                String q;
                switch (userStream) {
                    case "Science" -> q = "Awesome! Are you more interested in Engineering, Medicine, or Research?";
                    case "Commerce" -> q = "Great! Are you more interested in Accounting, Business, or Finance?";
                    case "Arts" -> q = "Wonderful! Are you into Design, Law, or Journalism?";
                    case "Vocational" -> q = "Nice! Do you like IT, Beauty, or Hospitality?";
                    default -> q = "Tell me your favorite subject or field.";
                }
                appendBot(q);
                step++;
                break;

            case 2:
                appendBot("How would you describe your academic performance, " + userName + "? (excellent/good/average)");
                step++;
                break;

            case 3:
                appendBot("Would you like suggestions for traditional degrees or skill-based courses?");
                step++;
                break;

            case 4:
                appendBot("✨ " + userName + ", based on your answers, here are some suggestions:");
                if (recommendations.containsKey(userStream))
                    recommendations.get(userStream).forEach(this::appendBot);
                step++;
                break;

            default:
                appendBot("Thank you, " + userName + "! Refresh to start again.");
        }
    }

    private void showSubjectInfo(String subject) {
        if (subjectInfo.containsKey(subject)) appendBot("About " + subject + ": " + subjectInfo.get(subject));
        else appendBot("Sorry, I don't have information about \"" + subject + "\".");
    }

    private String capitalize(String text) {
        if (text == null || text.isEmpty()) return text;
        return text.substring(0, 1).toUpperCase() + text.substring(1).toLowerCase();
    }

    private void loadSubjectInfo() {
        subjectInfo.put("Physics", "B.Sc./M.Sc./PhD in Physics. Careers in R&D, academics, and data science.");
        subjectInfo.put("Engineering", "B.Tech/B.E. in multiple branches. Wide opportunities in tech and design.");
        subjectInfo.put("Medical", "MBBS/BDS and allied fields. Excellent scope in healthcare and research.");
        subjectInfo.put("Business", "BBA/MBA with jobs in management, marketing, and entrepreneurship.");
        subjectInfo.put("Law", "LL.B/LL.M opens doors to legal, corporate, and policy careers.");
    }

    private void loadRecommendations() {
        recommendations.put("Science", List.of("🛠️ Engineering", "🩺 Medicine", "🔬 Research"));
        recommendations.put("Commerce", List.of("📊 Accounting", "💹 Economics", "💻 Business"));
        recommendations.put("Arts", List.of("🎨 Design", "📺 Journalism", "⚖️ Law"));
        recommendations.put("Vocational", List.of("💻 IT", "🎬 Animation", "🏨 Hospitality"));
    }

    public static void main(String[] args) {
        launch();
    }
}
