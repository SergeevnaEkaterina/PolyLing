package generation.grammar;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Node;

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.*;
import java.util.stream.Stream;
import org.apache.log4j.BasicConfigurator;



public class LexicalRulesGeneration {

    public static String inflectionUrl = "http://inflectonline.ru/{word}";
    public static String dislecsionUrl = "https://glagol.reverso.net/спряжение-русский-глагол-{word}.html";

    public static String getNoun(String gender, String caseNoun, boolean isPlural) {
        String path = "src/main/resources/datasets/";
        // String line;
        if (gender.toLowerCase(Locale.ROOT).equals("male")) {
            path += "nounsMale.txt";
        } else if (gender.toLowerCase(Locale.ROOT).equals("female")) {
            path += "nounsFemale.txt";
        } else if (gender.toLowerCase(Locale.ROOT).equals("neutral")) {
            path += "nounsNeutral.txt";
        }

        Document doc = getResponseDocument(path, inflectionUrl);

        Node nounCases = null;
        if (doc.body().childNode(3).childNode(3).toString().contains("2 значения")) {
            nounCases = doc.body().childNode(3).childNode(15).childNode(3);
        } else {
            nounCases = doc.body().childNode(3).childNode(8).childNode(3);
        }
        Node getCase = getNounCases(caseNoun, nounCases);
        if (isPlural) {
            return getCase.childNode(7).childNode(0).toString();
        } else {
            return getCase.childNode(5).childNode(0).toString();
        }


    }

    public static String getNoun(String gender) {
        String path = "src/main/resources/datasets/";
        String line;
        if (gender.toLowerCase(Locale.ROOT).equals("male")) {
            path += "nounsMale.txt";
        } else if (gender.toLowerCase(Locale.ROOT).equals("female")) {
            path += "nounsFemale.txt";
        } else if (gender.toLowerCase(Locale.ROOT).equals("neutral")) {
            path += "nounsNeutral.txt";
        }

        return getLine(path);
    }

    public static String getAbbreviation() {
        String path = "src/main/resources/datasets/abbreviations.txt";
        String line;
        return getLine(path);
    }

    public static String getPreposition(String padej) {
        String path = "src/main/resources/datasets/";
        String line;
        if (padej.toLowerCase(Locale.ROOT).equals("roditelnyi")) {
            path += "RodPreps.txt";
        } else if (padej.toLowerCase(Locale.ROOT).equals("datelnyi")) {
            path += "DatPreps.txt";
        } else if (padej.toLowerCase(Locale.ROOT).equals("vinitelnyi")) {
            path += "VinPreps.txt";
        } else if (padej.toLowerCase(Locale.ROOT).equals("tvoritelnyi")) {
            path += "TvorPreps.txt";
        } else if (padej.toLowerCase(Locale.ROOT).equals("predlozhnyi")) {
            path += "PredPreps.txt";
        }


        return getLine(path);

    }

    public static String getUnity(String type) {
        String path = "src/main/resources/datasets/";
        String line;
        if (type.toLowerCase(Locale.ROOT).equals("compound")) {
            path += "compoundUnities.txt";
        } else if (type.toLowerCase(Locale.ROOT).equals("complex")) {
            path += "complexUnities.txt";
        }



        return getLine(path);

    }

    public static String getConnector() {
        String path = "src/main/resources/datasets/connectorsWithoutUnities.txt";
        String line;

        return getLine(path);

    }

    public static String getVerb(String tense, int face, boolean isPlural) {
        String result = null;
        String path = "src/main/resources/datasets/verbs.txt";
        String line = null;

        List<Object> documentWithWord = getResponseDocumentWithWord(path, inflectionUrl);
        Document doc = (Document) documentWithWord.get(0);

        Node tenses = doc.childNode(1).childNode(2).childNode(3);
        Node getTense = getTense(tense, tenses);
        if (getTense.toString().contains("не употребляется")) {
            result = getVerb("past", "male");
        } else if (Objects.equals(tense, "present")) {
            if (doc.childNode(1).childNode(2).childNode(3).childNode(10).toString().contains("не употребляется")) {
                result = getVerb("past", "male");
            } else {
                if (doc.childNode(1).childNode(2).childNode(3).childNode(3).toString().contains("2 значения")) {
                    Node faces = null;
                    try {
                         faces = doc.childNode(1).childNode(2).childNode(3).childNode(11).childNode(1).childNode(3);
                    }catch (IndexOutOfBoundsException e){
                        faces = doc.childNode(1).childNode(2).childNode(3).childNode(17).childNode(1).childNode(3);
                    }
                    Node getFace = getFace(face, faces);
                    result = getPlural(isPlural, getFace);
                } else {
                    Node faces =null;
                    try {
                        faces = doc.childNode(1).childNode(2).childNode(3).childNode(10).childNode(1).childNode(3);
                    }catch (IndexOutOfBoundsException e){
                        faces = doc.childNode(1).childNode(2).childNode(3).childNode(21).childNode(1).childNode(3);
                    }
                    Node getFace = getFace(face, faces);
                    result = getPlural(isPlural, getFace);
                }
            }
        } else if (Objects.equals(tense, "future")) {
            if (doc.childNode(1).childNode(2).childNode(3).childNode(10).toString().contains("не употребляется")) {
                getVerb("past", "male");
            } else {
                if (doc.childNode(1).childNode(2).childNode(3).childNode(3).toString().contains("2 значения")) {
                    if (doc.childNode(1).childNode(2).childNode(3).childNode(15).toString().contains("не употребляется")) {
                        getVerb("past", "male");
                    } else {
                        Node faces = doc.childNode(1).childNode(2).childNode(3).childNode(15).childNode(1).childNode(3);
                        Node getFace = getFace(face, faces);
                        result = getPlural(isPlural, getFace);
                    }
                } else {
                    if (doc.childNode(1).childNode(2).childNode(3).childNode(14).toString().contains("не употребляется")) {
                        getVerb("past", "male");
                    } else {
                        Node faces = doc.childNode(1).childNode(2).childNode(3).childNode(14).childNode(1).childNode(3);
                        Node getFace = getFace(face, faces);
                        result = getPlural(isPlural, getFace);
                    }
                }
            }
        }



        return result;
    }

    public static String getVerb(String tense, String gender) {
        String result = null;
        String path = "src/main/resources/datasets/verbs.txt";
        String line;

        Document doc = getResponseDocument(path, inflectionUrl);
        Node tenses = doc.childNode(1).childNode(2).childNode(3);
        Node getTense = getTense(tense, tenses);
        Node genders = getTense.childNode(1).childNode(3).childNode(3);
        result = getGenderVerb(gender, genders);

        return result;
    }

    public static String getDeePrichastie(String tense) {
        String path = "src/main/resources/datasets/verbs.txt";
        String line;


        String result = null;

        Document doc = getResponseDocument(path, dislecsionUrl);
        Node deeprichastie = doc.childNode(1).childNode(2).childNode(1).childNode(3).childNode(1).childNode(1).childNode(1).childNode(1).childNode(19).childNode(1).childNode(1).childNode(7).childNode(3).childNode(0).childNode(1);
        Node getTense = null;
        Node initialRoot = null;

        if ("present".equals(tense)) {
            getTense = deeprichastie.childNode(1);
        } else {
            getTense = deeprichastie.childNode(2);
        }
        try {
            initialRoot = getTense.childNode(0).childNode(1).childNode(0).childNode(0).childNode(0);
        } catch (IndexOutOfBoundsException e) {
            getTense = deeprichastie.childNode(2);
            initialRoot = getTense.childNode(0).childNode(1).childNode(0).childNode(0).childNode(0);
        }
        try {
            result = initialRoot.childNode(0).childNode(0).toString() + initialRoot.childNode(1).childNode(0).toString();

            result = result.contains("/") ? result.substring(0, result.indexOf("/")) : result;

        }catch (IndexOutOfBoundsException e){
            result = initialRoot.childNode(0).childNode(0).toString();
        }

        return result;
    }


    public static String getPrichastie(boolean isPassive) {
        String path = "src/main/resources/datasets/verbs.txt";
        String line;


        String result = null;

        Document doc = getResponseDocument(path, dislecsionUrl);
        Node prichastie = doc.childNode(1).childNode(2).childNode(1).childNode(3).childNode(1).childNode(1).childNode(1).childNode(1).childNode(19).childNode(1).childNode(1).childNode(7).childNode(3).childNode(0).childNode(2);
        Node getVoice = null;
        Node initialRoot = null;
        if (isPassive) {
            getVoice = prichastie.childNode(2);
        } else {
            getVoice = prichastie.childNode(1);
        }

        try {
            initialRoot = getVoice.childNode(0).childNode(1).childNode(0).childNode(0).childNode(0);
        } catch (IndexOutOfBoundsException e) {
            getVoice = prichastie.childNode(1);
            initialRoot = getVoice.childNode(0).childNode(1).childNode(0).childNode(0).childNode(0);
        }
        result = initialRoot.childNode(0).childNode(0).toString() + initialRoot.childNode(1).childNode(0).toString();

        result = result.contains("/") ? result.substring(0, result.indexOf("/")) : result;


        return result;
    }

    public static Node getTense(String tense, Node node) {

        Node res = switch (tense) {
            case ("present") -> node.childNode(10);
            case ("future") -> node.childNode(14);
            case ("past") -> node.childNode(18);
            default -> node.childNode(27);
        };
        if (res.toString().equals(" ")) {
            res = switch (tense) {
                case ("present") -> node.childNode(11);
                case ("future") -> node.childNode(15);
                case ("past") -> node.childNode(19);
                default -> node.childNode(23);
            };
        }
        if (node.childNode(15).toString().contains("Настоящее")) {
            res = switch (tense) {
                case ("present") -> node.childNode(17);
                case ("future") -> node.childNode(21);
                case ("past") -> node.childNode(25);
                default -> node.childNode(29);
            };
        }
        return res;
    }

    public static Node getNounCases(String nounCase, Node node) {
        return switch (nounCase) {
            case ("imenitselnyi") -> node.childNode(1);
            case ("roditelnyi") -> node.childNode(3);
            case ("datelnyi") -> node.childNode(5);
            case ("vinitelnyi") -> node.childNode(7);
            case ("tvoritelnyi") -> node.childNode(9);
            default -> node.childNode(11);
        };
    }

    public static String getVerbFutureForm(String verb, int face, boolean isPlural) {
        if (isPlural) {
            return switch (face) {
                case (1) -> "будем " + verb;
                case (2) -> "будете " + verb;
                default -> "будут " + verb;
            };
        } else {
            return switch (face) {
                case (1) -> "буду " + verb;
                case (2) -> "будешь " + verb;
                default -> "будет " + verb;
            };
        }
    }

    public static Node getFace(int face, Node node) {
        return switch (face) {
            case (1) -> node.childNode(1);
            case (2) -> node.childNode(3);
            default -> node.childNode(5);
        };
    }

    public static String getPlural(boolean isPlural, Node node) {
        if (isPlural) {
            return node.childNode(9).childNode(0).toString();
        } else return node.childNode(5).childNode(0).toString();
    }

    public static String getGenderVerb(String gender, Node node) {
        return switch (gender) {
            case ("male") -> node.childNode(1).childNode(0).toString();
            case ("female") -> node.childNode(3).childNode(0).toString();
            case ("neutral") -> node.childNode(5).childNode(0).toString();
            default -> node.childNode(7).childNode(0).toString();
        };
    }


    public static String getAdjective(String gender, String adjCase) {
        String path = "src/main/resources/datasets/adjectives.txt";
        String line;

        Node node;

        List<Node> padej = new ArrayList<>();
        Document doc = getResponseDocument(path, inflectionUrl);
        if (doc.body().childNode(3).childNode(3).toString().contains("2 значения")) {
            try {
                padej = doc.body().childNode(3).childNode(17).childNode(3).childNodes();
            } catch (IndexOutOfBoundsException ex) {
                padej = doc.body().childNode(3).childNode(11).childNode(3).childNodes();
            }

        } else {
            try {
                padej = doc.body().childNodes().get(3).childNodes().get(10).childNodes().get(3).childNodes();
            } catch (IndexOutOfBoundsException ex) {
                padej = doc.body().childNode(3).childNode(11).childNode(3).childNodes();
            }
        }
        node = switch (adjCase) {
            case ("imenitelnyi") -> padej.get(3);
            case ("roditelnyi") -> padej.get(7);
            case ("datelnyi") -> padej.get(11);
            case ("vinitelnyi") -> padej.get(15);
            case ("tvoritelnyi") -> padej.get(23);
            default -> padej.get(27);
        };


        return getGenderAfterCase(gender, node);
    }

    public static String getLine(String path) {
        String line;
        try {
            Stream<String> lines = Files.lines(Paths.get(path));
            long length = Files.lines(Paths.get(path)).count();
            Random random = new Random();
            long index = random.nextInt(Math.toIntExact(length));
            line = lines.skip(index).findFirst().get();
            System.out.println(line);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        return line;
    }

    public static Document getDocument(String url) throws IOException {
        return Jsoup.connect(url)
                .get();
    }

    public static Document getResponseDocument(String path, String url) {
        String word = getLine(path);
        String link = url.replace("{word}", word);
        Document doc;

        while (true) {
            try {
                doc = getDocument(link);
                break;
            } catch (IOException e) {
                word = getLine(path);
                link = url.replace("{word}", word);
            }
        }
        return doc;
    }

    public static List<Object> getResponseDocumentWithWord(String path, String url) {
        String word = getLine(path);
        String link = url.replace("{word}",word);
        Document doc;

        while (true) {
            try {
                doc = getDocument(link);
                break;
            } catch (IOException e) {
                word = getLine(path);
                link = url.replace("{word}", word);
            }
        }
        List<Object> result = Arrays.asList(doc, word);
        return result;
    }


    public static String getGenderAfterCase(String gender, Node node) {
        return switch (gender) {
            case ("male") -> node.childNode(1).childNode(0).toString();
            case ("female") -> node.childNode(3).childNode(0).toString();
            case ("neutral") -> node.childNode(5).childNode(0).toString();
            default -> node.childNode(7).childNode(0).toString();
        };
    }

    public static void addGeneratedSentenceToFile(String str) throws IOException {
        BufferedWriter writer = new BufferedWriter(new FileWriter("C:/Users/geras/IdeaProjects/CucumberJavaExample/src/main/resources/grammarRes/res.txt", true));
        writer.append(str);

        writer.close();
    }

    public static void main(String[] args) throws Exception {
        BasicConfigurator.configure();
        createSimpleSentence();
    }
    public static String capitalize(String str)
    {
        if (str == null || str.length() == 0) return str;
        return str.substring(0, 1).toUpperCase() + str.substring(1);
    }


    public static void createSimpleSentence() {
        for (int i = 0; i < 10; i++) {

      //       String simpleSentence = " " + capitalize(getAbbreviation()) + " " + getAbbreviation() +" " + getAbbreviation()+ ".";

          //   String simpleSentence = " " + capitalize(getNoun("male")) + " " + getVerb("present", 3, true) + " " + getPreposition("datelnyi") + " " + getNoun("female", "datelnyi", false) + ".";
//         String rule2 = " " + getAdjective("male", "imenitelnyi") + " " + getNoun("male") + " " + getVerb("future", 2, false) + " " + getPreposition("datelnyi") + " " + getAdjective("female", "datelnyi") + " " + getNoun("female", "datelnyi", false) + ".";
            //   String compoundRule = " " + getAdjective("female", "imenitelnyi") + " " + getNoun("female") + " " + getVerb("past", "female") + ", " + getUnity("compound") + " " + getAdjective("neutral", "imenitelnyi") + " " + getNoun("neutral") + " " + getVerb("past", "neutral") + " " + getPreposition("tvoritelnyi") + " " + getNoun("female", "tvoritelnyi", true) + ".";
//          String complexRule = " " + getAdjective("male", "imenitelnyi") + " " + getNoun("male") + " " + getVerb("present", 3, false) + ", " + getUnity("complex") + " " + getAdjective("male", "imenitelnyi") + " " + getNoun("male") + " " + getVerb("past", "male") + " " + getPreposition("tvoritelnyi") + " " + getNoun("female", "tvoritelnyi", true) + ".";
      //      String onlyNoun = " " + capitalize(getNoun("female")) + ".";
         //   String onlyVerb = " " + capitalize(getVerb("past", "neutral")) + ".";
//////
            //    String withoutPreps = " " + getNoun("male") + " " + getVerb("future", 2, false) + " " + getPreposition("datelnyi") + " " + getNoun("female", "datelnyi", false) + getConnector() + " " + getNoun("female") + " " + getVerb("future", 3, false) + ".";
//               String complexRuleThreeParts = " " + getAdjective("male", "imenitelnyi") + " " + getNoun("male") + " " + getVerb("present", 3, false) + ", "  + getAdjective("neutral", "imenitelnyi") + " " + getNoun("neutral") + " " + getVerb("past", "neutral") +  " " + getNoun("male", "datelnyi", false) + ", "  + getAdjective("male", "imenitelnyi") + " " + getNoun("male") + " " + getVerb("past", "male") + " " + getNoun("female", "tvoritelnyi", true)+ ", " +getNoun("male") + " " + getVerb("present", 3, true) + " " + getNoun("female", "datelnyi", false) + ".";
            //   String complexRuleFourParts = " " + capitalize(getAdjective("male", "imenitelnyi")) + " " + getNoun("male") + " " + getVerb("present", 3, false) + ", " + getUnity("complex") + " " + getAdjective("neutral", "imenitelnyi") + " " + getNoun("neutral") + " " + getVerb("past", "neutral") + " " + getPreposition("datelnyi") + " " + getNoun("male", "datelnyi", false) + " " + getUnity("complex") + " " + getAdjective("male", "imenitelnyi") + " " + getNoun("male") + " " + getVerb("past", "male") + " " + getPreposition("tvoritelnyi") + " " + getNoun("female", "tvoritelnyi", true)+ ", "+ getUnity("complex") +" " + getNoun("male") + " " + getVerb("present", 3, true) + " " + getPreposition("datelnyi") + " " + getNoun("female", "datelnyi", false) + ", " + getUnity("complex")+" " + getAdjective("male", "imenitelnyi") + " " + getNoun("male") + " " + getVerb("present", 3, false)+".";

//         String complexAndCompound = " " + getAdjective("male", "imenitelnyi") + " " + getNoun("male") + " " + getVerb("present", 3, false) + ", " + getUnity("complex") + " " + getAdjective("neutral", "imenitelnyi") + " " + getNoun("neutral") + " " + getVerb("past", "neutral") + " " + getPreposition("datelnyi") + " " + getNoun("male", "datelnyi", false) + ", " + getUnity("compound") + " " + getAdjective("male", "imenitelnyi") + " " + getNoun("male") + " " + getVerb("past", "male") + " " + getPreposition("tvoritelnyi") + " " + getNoun("female", "tvoritelnyi", true) + " " + getUnity("complex") + " " + getNoun("male") + " " + getVerb("past", "male") + ".";
//   if(i==5) {
       String deeprichastie = " " + capitalize(getDeePrichastie("past")) + ".";
            String prichastie = " " + capitalize(getPrichastie(true)) + ".";

//       rulesList.add(passive);
//   }
          List<String> rulesList = Arrays.asList(deeprichastie, prichastie);
        rulesList.forEach(e -> {
            try {
                addGeneratedSentenceToFile(e);
            } catch (IOException ex) {
                throw new RuntimeException(ex);
            }
        });
//            try {
//                addGeneratedSentenceToFile(getPrichastie(true) + getDeePrichastie("present"));
//            } catch (IOException e) {
//                throw new RuntimeException(e);
//            }
        }

    }
}
