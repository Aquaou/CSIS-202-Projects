import java.io.*;
import java.util.Map;

public class Lexer {

    // ----------
    private static final Map<String, String> KEYWORDS = Map.of(
            "print",  "print",
            "fi",     "fi",
            "if",     "if",
            "return", "return"
    );
    // ----------


    // ----------
    private static String readNumber(PushbackReader reader, char firstChar) throws IOException {
        StringBuilder digits = new StringBuilder();
        digits.append(firstChar);

        while (true) {
            int next = reader.read();
            if (next == -1) break;
            char nc = (char) next;

            if (Character.isDigit(nc)) {
                digits.append(nc);
            } else {
                reader.unread(next);
                break;
            }
        }
        return digits.toString();
    }
    // ----------


    // ----------
    private static String readWord(PushbackReader reader, char firstChar) throws IOException {
        StringBuilder word = new StringBuilder();
        word.append(firstChar);

        while (true) {
            int next = reader.read();
            if (next == -1) break;
            char nc = Character.toLowerCase((char) next);

            if (Character.isLetter(nc)) {
                word.append(nc);
            } else {
                reader.unread(next);
                break;
            }
        }
        return word.toString();
    }
    // ----------


    // ----------
    private static void skipLineComment(PushbackReader reader) throws IOException {
        while (true) {
            int next = reader.read();
            if (next == -1 || next == '\n') break;
        }
    }
    // ----------


    // ----------
    private static String readOperator(PushbackReader reader, char firstChar) throws IOException {
        int next = reader.read();
        if (next == -1) {
            return String.valueOf(firstChar);
        }

        char nc = (char) next;

        if (nc == '=' && firstChar != '=') {
            return "" + firstChar + nc;
        } else {
            reader.unread(next);
            return String.valueOf(firstChar);
        }
    }
    // ----------


    // ----------
    public static void main(String[] args) throws IOException {
        PushbackReader reader = new PushbackReader(new InputStreamReader(System.in));

        while (true) {
            int raw = reader.read();
            if (raw == -1) break;

            char c = Character.toLowerCase((char) raw);

            if (Character.isWhitespace(c)) {

                continue;

            } else if (Character.isLetter(c)) {

                String word = readWord(reader, c);

                if (KEYWORDS.containsKey(word)) {
                    System.out.println(KEYWORDS.get(word));
                } else {
                    for (int i = 0; i < word.length(); i++) {
                        System.out.println(word.charAt(i));
                    }
                }

            } else if (Character.isDigit(c)) {

                String number = readNumber(reader, c);
                System.out.println(number);

            } else if (c == '#') {

                skipLineComment(reader);

            } else if (c == '+' || c == '-' || c == '*' || c == '<' || c == '>' || c == '/' || c == '!' || c == '=') {

                String op = readOperator(reader, c);
                System.out.println(op);

            } else {

                System.err.println("Lexer crashed, due to the following: " + c);
                System.exit(0);

            }
        }
    }
    // ----------

}