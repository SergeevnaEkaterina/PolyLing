package generation.visual;

import org.apache.commons.lang3.RandomStringUtils;
import org.apache.poi.openxml4j.exceptions.InvalidFormatException;
import org.apache.poi.util.Units;
import org.apache.poi.xwpf.usermodel.*;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.math.BigInteger;
import java.util.Random;

public class VisualTextGeneration {
    public static String generateDocxFile(ParagraphAlignment alignment, boolean isItalic, String fontName, boolean isFontSize, String textColor, boolean isParagraphSize, boolean isStringSize, boolean isBold, boolean isImage, boolean isImageDividedByText, boolean isNumbering) {
        String fileName;
        String path = "";
        GeneratedTextContext context = calculateSizes(isParagraphSize, isStringSize, isFontSize);
        try {
            XWPFDocument docxModel = new XWPFDocument();
            XWPFParagraph bodyParagraph = docxModel.createParagraph();
            bodyParagraph.setAlignment(alignment);

            createParagraph(bodyParagraph, isItalic, fontName, context.getFontSize(), textColor, context.getParagraphSize());
            if (isImage) {
                createImageParagraph(bodyParagraph, 1);

            if (isImageDividedByText) {
                createParagraph(bodyParagraph, isItalic, fontName, context.getFontSize(), textColor, context.getParagraphSize());
            } else {
                createImageParagraph(bodyParagraph, 2);
            }
        }
            if (isBold) {
                createBoldParagraph(bodyParagraph, isItalic, fontName, context.getFontSize(), textColor, true, context.getBoldSize());
            }
            createParagraph(bodyParagraph, isItalic, fontName, context.getFontSize(), textColor, context.getParagraphSize());
            if (isImage) {
                createImageParagraph(bodyParagraph, 3);
            }

            if (isNumbering) {
                //Create paragragh and set its list level
                XWPFParagraph para1 = docxModel.createParagraph();
                XWPFRun run1 = para1.createRun();
                addParagraphFeatures(run1, isItalic, fontName, textColor);
                run1.setText("The first paragraph");
                para1.setNumID(BigInteger.valueOf(0));
                //Create paragragh and set the list level
                XWPFParagraph para2 = docxModel.createParagraph();
                XWPFRun run2 = para2.createRun();
                addParagraphFeatures(run2, isItalic, fontName, textColor);
                run2.setText("The second paragraph");
                para2.setNumID(BigInteger.valueOf(0));
                //Create paragragh and apply multi level list
                XWPFParagraph para3 = docxModel.createParagraph();
                XWPFRun run3 = para3.createRun();
                addParagraphFeatures(run3, isItalic, fontName, textColor);
                run3.setText("The third paragraph");
                para3.setNumID(BigInteger.valueOf(0));
                para3 = docxModel.createParagraph();
                run3 = para3.createRun();
                run3.setText("The first sub-item");
                para3.setNumID(BigInteger.valueOf(1));
                para3 = docxModel.createParagraph();
                run3 = para3.createRun();
                run3.setText("The second sub-item");
                para3.setNumID(BigInteger.valueOf(1));
                para3 = docxModel.createParagraph();
                run3 = para3.createRun();
                run3.setText("The third sub-item");
                para3.setNumID(BigInteger.valueOf(2));

            }
            fileName = RandomStringUtils.random(10, true, false);
            path = "src/main/resources/" + fileName + ".docx";
            FileOutputStream outputStream = new FileOutputStream(path);
            docxModel.write(outputStream);
            outputStream.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
        System.out.println("Успешно записан в файл");
        return path;
    }

    private static void createParagraph(XWPFParagraph bodyParagraph, boolean isItalic, String fontName, int fontSize, String textColor, int textSize) {
        XWPFRun paragraphConfig = bodyParagraph.createRun();
        paragraphConfig.setItalic(isItalic);
        paragraphConfig.setFontFamily(fontName);
        paragraphConfig.setFontSize(fontSize);
        paragraphConfig.setColor(textColor);
        paragraphConfig.setText(RandomStringUtils.random(textSize, true, false));
        paragraphConfig.addBreak();

    }

    private static void addParagraphFeatures(XWPFRun paragraphConfig, boolean isItalic, String fontName, String textColor){
        paragraphConfig.setItalic(isItalic);
        paragraphConfig.setFontFamily(fontName);
        paragraphConfig.setFontSize(14);
        paragraphConfig.setColor(textColor);
    }

    private static void createImageParagraph(XWPFParagraph bodyParagraph, int imageCount) {
        XWPFRun paragraphConfig = bodyParagraph.createRun();
        FileInputStream inputStream;
        try {
            inputStream = new FileInputStream("src/main/resources/images/img" + imageCount + ".png");
        } catch (FileNotFoundException e) {
            throw new RuntimeException(e);
        }
        try {
            paragraphConfig.addPicture(inputStream, Document.PICTURE_TYPE_PNG, "my pic", Units.toEMU(200), Units.toEMU(200));
        } catch (InvalidFormatException e) {
            throw new RuntimeException(e);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        paragraphConfig.addBreak();

    }

    private static void createBoldParagraph(XWPFParagraph bodyParagraph, boolean isItalic, String fontName, int fontSize, String textColor, boolean isBold, int textSize) {
        XWPFRun paragraphConfig = bodyParagraph.createRun();
        paragraphConfig.setItalic(isItalic);
        paragraphConfig.setFontFamily(fontName);
        paragraphConfig.setFontSize(fontSize);
        paragraphConfig.setColor(textColor);
        paragraphConfig.setBold(isBold);
        paragraphConfig.setText(RandomStringUtils.random(textSize, true, false));
        paragraphConfig.addBreak();

    }

    private static GeneratedTextContext calculateSizes(boolean isParagraphSize, boolean isStringSize, boolean isFontSize) {
        Random random = new Random();
        int paragraphSize;
        int boldSize;
        int cursiveSize;
        int stringSize;
        int fontSize;
        if (isParagraphSize) {
            paragraphSize = random.nextInt(400, 750);
        } else {
            paragraphSize = random.nextInt(751, 800);
        }
        if (isStringSize) {
            stringSize = random.nextInt(20, 75);

        } else {
            stringSize = random.nextInt(76, 100);
        }
        if (isFontSize) {
            fontSize = random.nextInt(12, 14);
        } else {
            fontSize = random.nextInt(1, 12);
        }
        cursiveSize = random.nextInt(1, (paragraphSize / 100) * 15);
        boldSize = random.nextInt(1, (paragraphSize / 100) * 15);
        return new GeneratedTextContext(paragraphSize, fontSize, stringSize, boldSize, cursiveSize);
    }
}
