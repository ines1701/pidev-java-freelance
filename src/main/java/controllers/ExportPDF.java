package controllers;

import entities.Post;
import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.pdmodel.PDPage;
import org.apache.pdfbox.pdmodel.PDPageContentStream;
import org.apache.pdfbox.pdmodel.font.PDFont;
import org.apache.pdfbox.pdmodel.font.PDType1Font;

import javafx.scene.control.ListView; // Correct import statement for ListView
import java.io.IOException;
import java.util.List;

public class ExportPDF {

    public void generatePDF(String filePath, ListView<Post> postListView) throws IOException {
        try (PDDocument document = new PDDocument()) {
            PDPage page = new PDPage();
            document.addPage(page);

            try (PDPageContentStream contentStream = new PDPageContentStream(document, page)) {
                // Choose a different font that supports a wider range of characters
                PDFont font = PDType1Font.HELVETICA;
                contentStream.setFont(font, 12);
                contentStream.beginText();
                contentStream.newLineAtOffset(100, 700);
                contentStream.showText("nom   Description    DatePublication  ");
                contentStream.endText();

                int y = 680;
                List<Post> posts = postListView.getItems(); // Retrieve all posts from the ListView
                for (Post post : posts) {
                    contentStream.beginText();
                    contentStream.newLineAtOffset(100, y);
                    String description = post.getDescription().replace("\t", "    "); // Replace tabs with spaces
                    String rowData = String.format("%-20s%-40s%s", post.getNom(), description, post.getDatePublication());
                    contentStream.showText(rowData);
                    contentStream.endText();
                    y -= 20;
                }
            }

            document.save(filePath);
            System.out.println("PDF generated successfully: " + filePath);
        } catch (IOException e) {
            System.err.println("Error occurred while generating PDF: " + e.getMessage());
            throw e; // Re-throw the exception to propagate it to the caller
        }
    }


}


