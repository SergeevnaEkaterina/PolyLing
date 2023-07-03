package data;

import model.response.Comment;

import java.util.Arrays;
import java.util.List;

public class LinkAnalyseDataProvider {

    public static final String LINK =  "https://www.spbstu.ru/media/edition/";
    static Comment serif = new Comment("serif_font", false, "Шрифты с засечками", "Шрифты с засечками (serif fonts) читаются легче, поэтому лучше использовать их");
    static Comment useble = new Comment("useble_list", true, "Использование списков", "Вертикальные списки в тексте повышают воспринимаемость");
    static Comment paragraph = new Comment("paragraph_len", true, "Размер абзаца", "Размер абзаца не должен превышать 750 символов");
    static Comment spacing = new Comment("line_spacing", true, "Межстрочный интервал", "Межстрочный интервал должен быть в диапазоне от 120 % до 180% от размера кегля использованного шрифта");
    static Comment string = new Comment("string_len", true, "Длина строки", "Длина строки не должна превышать 65-75 символов и должна занимать не менее 30% и не более 80% страницы");
    static Comment caps = new Comment("not_caps", true, "Использование капслока", "Капслок должен использоваться только для аббревиатур");
    static Comment bold = new Comment("bold_count", true, "Полужирное начертание", "Объем полужирных начертаний не должен превышать 15%");
    static Comment img = new Comment("img_tree", true, "Расположение изображений", "Изображения должны находиться между блоками текста. При этом желательно, чтобы между блоками было не более одного изображения");
    static Comment font = new Comment("font_set", true, "Набор шрифтов", "Не стоит использовать более двух типов шрифтов");
    static Comment italic = new Comment("italic_count", true, "Курсивные начертания", "Объем курсивных начертаний не должен превышать 15%");
    static Comment size = new Comment("font_size", true, "Размер шрифта", "Размер шрифта должен быть не менее 12 и не более 14");


    public static final List<Comment> SCORE_ATTRIBUTES = Arrays.asList(serif, useble, paragraph, spacing, string, caps, bold, img, font, italic, size);



}
