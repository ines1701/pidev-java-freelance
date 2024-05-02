package com.example.jessem;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.stage.Stage;
public class ChatbotPopupController {
    @FXML
    private TextField userInputField;
    @FXML
    private TextArea chatArea;
    @FXML
    private Button sendButton;
    // Define predefined questions and responses
    private final String[] predefinedQuestions = {
            "salut je veux savoir est ce que ma transaction est validé ou non?",
            "alors?",
            "merci beaucoup"
    };
    private final String[] predefinedResponses = {
            "juste quelques secondes je vais vérifier mes données ",
            "votre transaction a été bien reçu",
            "pas de quoi je suis ici pour vous aider "
    };
    @FXML
    public void initialize() {
        // Display the introductory message from the chatbot
        chatArea.appendText("Hello! I'm Chatbot. How can I assist you today?\n");
    }
    @FXML
    void sendMessage() {
        // Get user input
        String userMessage = userInputField.getText().trim();
        // Display user input in the chat area
        chatArea.appendText("You: " + userMessage + "\n");
        // Check if the user's message matches any predefined question
        for (int i = 0; i < predefinedQuestions.length; i++) {
            if (userMessage.equalsIgnoreCase(predefinedQuestions[i])) {
                // Display predefined response
                chatArea.appendText("Chatbot: " + predefinedResponses[i] + "\n");
                // Clear user input field and return
                userInputField.clear();
                return;
            }
        }
        // If the user's message doesn't match any predefined question, ask for more details
        chatArea.appendText("Chatbot: Je n'ai pas compris. Pouvez-vous donner plus de détails?\n");
        // Clear user input field
        userInputField.clear();
    }
    @FXML
    void onClose() {
        // Create and show an alert message
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle("Chatbot");
        alert.setHeaderText(null);
        alert.setContentText("Thank you for using Chatbot!");
        alert.showAndWait();
        // Close the popup window
        Stage stage = (Stage) sendButton.getScene().getWindow();
        stage.close();
    }
}

