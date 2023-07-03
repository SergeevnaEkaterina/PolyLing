package generation.visual;

import org.apache.poi.xwpf.usermodel.ParagraphAlignment;

import static generation.visual.VisualTextGeneration.generateDocxFile;

public class CreateWordDocument {
    public static void main(String[] args) throws Exception {
        generateDocxFile(ParagraphAlignment.RIGHT, true, "Serif", false, "000000", false, true, false, true, false, true);

    }
}