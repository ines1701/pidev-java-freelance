package utils;

import java.io.FileOutputStream;
import java.text.SimpleDateFormat;
import java.util.Date;

import com.itextpdf.text.*;
import com.itextpdf.text.pdf.PdfWriter;

public class pdfgenerator {

    public static final Font TITLE_FONT = FontFactory.getFont(FontFactory.HELVETICA_BOLD, 18);
    public static final Font NORMAL_FONT = FontFactory.getFont(FontFactory.HELVETICA, 12);

    public static void addTitle(Document document, String text) throws DocumentException {
        Paragraph title = new Paragraph(text, TITLE_FONT);
        title.setAlignment(Element.ALIGN_CENTER); // Aligner le titre au centre
        document.add(title);
    }

    public static void addNormalText(Document document, String text) throws DocumentException {
        Paragraph normalText = new Paragraph(text, NORMAL_FONT);
        document.add(normalText);
    }

    public static void addAgencyMessage(Document document, String message) throws DocumentException {
        Paragraph agencyMessage = new Paragraph(message, NORMAL_FONT);
        agencyMessage.setAlignment(Element.ALIGN_CENTER); // Aligner le message au centre
        document.add(agencyMessage);
    }

    public static void generatePdf(String nomClient,String description,int montant) {
        Document document = new Document();
        try {
            PdfWriter.getInstance(document, new FileOutputStream("Contrat.pdf"));
            document.open();
// Ajouter l'image au document
           Image image = Image.getInstance("C://Users//user//OneDrive//Bureau//hahaha.png");
            image.scaleToFit(150, 150); // Redimensionner l'image à 100x100 pixels
            document.add(image);


            // Ajouter "HawesBiya" en haut du PDF
            // Ajouter le titre "CoLocStudy" en bleu
            Font titleFont = new Font(Font.FontFamily.HELVETICA, 18, Font.BOLD, BaseColor.BLUE);
           // Paragraph title = new Paragraph("SkillHub", titleFont);
          //  title.setAlignment(Element.ALIGN_CENTER);
          //  document.add(title);

            // Ajouter le titre "Reçu de Location"
            // Ajouter le sous-texte
            Font subTitleFont = new Font(Font.FontFamily.HELVETICA, 11, Font.NORMAL, BaseColor.BLACK);
            Paragraph subTitle = new Paragraph("Reçu de contrat", subTitleFont);
            subTitle.setAlignment(Element.ALIGN_CENTER);
            document.add(subTitle);

            addEmptyLine(document);
            addEmptyLine(document);


            // Ajouter le message de l'agence en bas des détails
            addAgencyMessage(document, "Voici les détails de votre contrat. Veuillez les conserver");
            addEmptyLine(document);
            addEmptyLine(document);


            //  addNormalText(document, "N°"+ id);
            addNormalText(document, "Ce contrat au nom ");
            addBoldRedText(document, nomClient);
            addNormalText(document, "avec cette description");

            addBoldRedText(document, description);
            addNormalText(document, "du tarif  : ");
            addBoldRedText(document, montant + " dinars par mois.\n");

            /*Image iconImage = Image.getInstance("D://IdeaProjects//PidevJ//target//resources//img//paa.png");
            iconImage.scaleToFit(200, 200); // Redimensionner l'icône à la taille souhaitée
            document.add(iconImage);
            addEmptyLine(document);
*/
            addEmptyLine(document);
            addEmptyLine(document);




            // Ajouter le message de l'agence en bas des détails
            addAgencyMessage(document, "Merci d'avoir choisi GIVEST ! ");
            // Ajouter le message de l'agence en bas des détails
            addAgencyMessage(document, "Merci encore pour votre confiance et profitez-en pleinement !");
            // Ajouter l'icône à côté du logo

            addEmptyLine(document);
            addEmptyLine(document);
// Ajouter la date d'impression

            // Ajouter les coordonnées de l'agence
            addNormalText(document, "Adress: Cite el ghazela Ariana 2083");
            addNormalText(document, "Tel: +216 52 724 070");
            addNormalText(document, "E-mail: SkillHub@esprit.tn");
            addEmptyLine(document);
            addEmptyLine(document);
            addDate(document);
            document.close();
        } catch (DocumentException e) {
            e.printStackTrace();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    private static void addBoldRedText(Document document, String text) throws DocumentException {
        Font boldRedFont = new Font(Font.FontFamily.HELVETICA, 12, Font.BOLD, BaseColor.RED);
        Chunk chunk = new Chunk(text, boldRedFont);
        Paragraph paragraph = new Paragraph(chunk);
        document.add(paragraph);
    }
    // Méthode pour ajouter la date d'impression
    private static void addDate(Document document) throws DocumentException {
        SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy HH:mm:ss");
        String dateStr = sdf.format(new Date());
        Paragraph dateParagraph = new Paragraph("Date d'impression : " + dateStr);
        dateParagraph.setAlignment(Element.ALIGN_BASELINE);
        document.add(dateParagraph);
    }
    public static void addEmptyLine(Document document) throws DocumentException {
        document.add(new Paragraph(" "));
    }
}
