import java.util.HashMap;
import java.util.Map;

public class CaesarCipher {
    private static final char[] ALPHABET = {
            'а', 'б', 'в', 'г', 'д', 'е', 'ж', 'з', 'и', 'к', 'л', 'м', 'н', 'о', 'п', 'р',
            'с', 'т', 'у', 'ф', 'х', 'ц', 'ч', 'ш', 'щ', 'ъ', 'ы', 'ь', 'э', 'я',
            '.', ',', '«', '»', '"', '\'', ':', '!', '?', ' '
    };

    private static final Map<Character, Integer> CHAR_INDEX = new HashMap<>();

    static {
        for (int i = 0; i < ALPHABET.length; i++) {
            CHAR_INDEX.put(ALPHABET[i], i);
        }
    }

    public String encrypt(String text, int key) {
        key = normalizeKey(key);
        StringBuilder result = new StringBuilder(text.length());

        for (int i = 0; i < text.length(); i++) {
            char current = normalizeChar(text.charAt(i));
            Integer index = CHAR_INDEX.get(current);
            if (index != null) {
                int newIndex = (index + key) % ALPHABET.length;
                result.append(ALPHABET[newIndex]);
            } else {
                result.append(text.charAt(i));
            }
        }
        return result.toString();
    }

    public String decrypt(String text, int key) {
        return encrypt(text, ALPHABET.length - normalizeKey(key));
    }

    public int getAlphabetSize() {
        return ALPHABET.length;
    }

    public char[] getAlphabet() {
        return ALPHABET.clone();
    }

    public int findCharIndex(char c) {
        Integer index = CHAR_INDEX.get(normalizeChar(c));
        return index == null ? -1 : index;
    }

    private int normalizeKey(int key) {
        key = key % ALPHABET.length;
        if (key < 0) {
            key += ALPHABET.length;
        }
        return key;
    }

    private char normalizeChar(char c) {
        if (c >= 'А' && c <= 'Я') {
            return (char) (c + ('а' - 'А'));
        }
        if (c == 'Ё' || c == 'ё') {
            return 'е';
        }
        return c;
    }
}
