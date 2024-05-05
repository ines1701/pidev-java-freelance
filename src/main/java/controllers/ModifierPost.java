package controllers;

import entities.Post;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.stage.Stage;
import services.PostService;

public class ModifierPost {

    @FXML
    private Label descriptionErrorLabel;

    @FXML
    private TextArea descriptionTextArea;

    @FXML
    private Label nomErrorLabel;

    @FXML
    private TextField nomTextField;

    private PostService postService = new PostService();

    private Long postId;

    private Post post;

    public void initData(Long postId) {
        this.postId = postId;
        post = postService.getById(Math.toIntExact(postId));
        if (post != null) {
            descriptionTextArea.setText(post.getDescription());
            nomTextField.setText(post.getNom());
        }
    }

    @FXML
    void modifierPost(ActionEvent event) {
        post.setDescription(descriptionTextArea.getText());
        post.setNom(nomTextField.getText());

        postService.update(post);

        // Close the window after the update
        closeWindow();
    }

    private void closeWindow() {
        Stage stage = (Stage) descriptionTextArea.getScene().getWindow();
        stage.close();
    }
}
