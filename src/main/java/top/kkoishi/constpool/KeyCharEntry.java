package top.kkoishi.constpool;

import java.util.HashMap;

import static java.awt.event.KeyEvent.*;

/**
 * Key-char hash map const-pool.
 *
 * @author KKoishi_
 */
public class KeyCharEntry {
    public static final HashMap<Integer, Character> INTEGER_CHARACTER_HASH_MAP = new HashMap<>();
    public static final HashMap<Integer, Character> SPECIALS_HASH_MAP = new HashMap<>();
    public static final HashMap<Integer, Character> OPERATE_HASH_MAP = new HashMap<>();

    /*
    Register const pool
     */
    static {
        INTEGER_CHARACTER_HASH_MAP.put(VK_0, '0');
        INTEGER_CHARACTER_HASH_MAP.put(VK_1, '1');
        INTEGER_CHARACTER_HASH_MAP.put(VK_2, '2');
        INTEGER_CHARACTER_HASH_MAP.put(VK_3, '3');
        INTEGER_CHARACTER_HASH_MAP.put(VK_4, '4');
        INTEGER_CHARACTER_HASH_MAP.put(VK_5, '5');
        INTEGER_CHARACTER_HASH_MAP.put(VK_6, '6');
        INTEGER_CHARACTER_HASH_MAP.put(VK_7, '7');
        INTEGER_CHARACTER_HASH_MAP.put(VK_8, '8');
        INTEGER_CHARACTER_HASH_MAP.put(VK_9, '9');
        INTEGER_CHARACTER_HASH_MAP.put(VK_A, 'a');
        INTEGER_CHARACTER_HASH_MAP.put(VK_B, 'b');
        INTEGER_CHARACTER_HASH_MAP.put(VK_C, 'c');
        INTEGER_CHARACTER_HASH_MAP.put(VK_D, 'd');
        INTEGER_CHARACTER_HASH_MAP.put(VK_E, 'e');
        INTEGER_CHARACTER_HASH_MAP.put(VK_F, 'f');
        INTEGER_CHARACTER_HASH_MAP.put(VK_G, 'g');
        INTEGER_CHARACTER_HASH_MAP.put(VK_H, 'h');
        INTEGER_CHARACTER_HASH_MAP.put(VK_I, 'i');
        INTEGER_CHARACTER_HASH_MAP.put(VK_J, 'j');
        INTEGER_CHARACTER_HASH_MAP.put(VK_K, 'k');
        INTEGER_CHARACTER_HASH_MAP.put(VK_L, 'l');
        INTEGER_CHARACTER_HASH_MAP.put(VK_M, 'm');
        INTEGER_CHARACTER_HASH_MAP.put(VK_N, 'n');
        INTEGER_CHARACTER_HASH_MAP.put(VK_O, 'o');
        INTEGER_CHARACTER_HASH_MAP.put(VK_P, 'p');
        INTEGER_CHARACTER_HASH_MAP.put(VK_Q, 'q');
        INTEGER_CHARACTER_HASH_MAP.put(VK_R, 'r');
        INTEGER_CHARACTER_HASH_MAP.put(VK_S, 's');
        INTEGER_CHARACTER_HASH_MAP.put(VK_T, 't');
        INTEGER_CHARACTER_HASH_MAP.put(VK_U, 'u');
        INTEGER_CHARACTER_HASH_MAP.put(VK_V, 'v');
        INTEGER_CHARACTER_HASH_MAP.put(VK_W, 'w');
        INTEGER_CHARACTER_HASH_MAP.put(VK_X, 'x');
        INTEGER_CHARACTER_HASH_MAP.put(VK_Y, 'y');
        INTEGER_CHARACTER_HASH_MAP.put(VK_Z, 'z');
        INTEGER_CHARACTER_HASH_MAP.put(VK_SPACE, ' ');
        INTEGER_CHARACTER_HASH_MAP.put(VK_PLUS, '+');
        INTEGER_CHARACTER_HASH_MAP.put(VK_MINUS, '-');
        INTEGER_CHARACTER_HASH_MAP.put(VK_LEFT_PARENTHESIS, '{');
        INTEGER_CHARACTER_HASH_MAP.put(VK_RIGHT_PARENTHESIS, '}');

        SPECIALS_HASH_MAP.put(VK_PLUS, '=');
        SPECIALS_HASH_MAP.put(VK_MINUS, '_');
        SPECIALS_HASH_MAP.put(VK_0, '!');
        SPECIALS_HASH_MAP.put(VK_1, '@');
        SPECIALS_HASH_MAP.put(VK_2, '#');
        SPECIALS_HASH_MAP.put(VK_3, '$');
        SPECIALS_HASH_MAP.put(VK_4, '%');
        SPECIALS_HASH_MAP.put(VK_5, '^');
        SPECIALS_HASH_MAP.put(VK_6, '&');
        SPECIALS_HASH_MAP.put(VK_7, '*');
        SPECIALS_HASH_MAP.put(VK_8, '(');
        SPECIALS_HASH_MAP.put(VK_9, ')');
        SPECIALS_HASH_MAP.put(VK_A, 'A');
        SPECIALS_HASH_MAP.put(VK_B, 'B');
        SPECIALS_HASH_MAP.put(VK_C, 'C');
        SPECIALS_HASH_MAP.put(VK_D, 'D');
        SPECIALS_HASH_MAP.put(VK_E, 'E');
        SPECIALS_HASH_MAP.put(VK_F, 'F');
        SPECIALS_HASH_MAP.put(VK_G, 'G');
        SPECIALS_HASH_MAP.put(VK_H, 'H');
        SPECIALS_HASH_MAP.put(VK_I, 'I');
        SPECIALS_HASH_MAP.put(VK_J, 'J');
        SPECIALS_HASH_MAP.put(VK_K, 'K');
        SPECIALS_HASH_MAP.put(VK_L, 'L');
        SPECIALS_HASH_MAP.put(VK_M, 'M');
        SPECIALS_HASH_MAP.put(VK_N, 'N');
        SPECIALS_HASH_MAP.put(VK_O, 'O');
        SPECIALS_HASH_MAP.put(VK_P, 'P');
        SPECIALS_HASH_MAP.put(VK_Q, 'Q');
        SPECIALS_HASH_MAP.put(VK_R, 'R');
        SPECIALS_HASH_MAP.put(VK_S, 'S');
        SPECIALS_HASH_MAP.put(VK_T, 'T');
        SPECIALS_HASH_MAP.put(VK_U, 'U');
        SPECIALS_HASH_MAP.put(VK_V, 'V');
        SPECIALS_HASH_MAP.put(VK_W, 'W');
        SPECIALS_HASH_MAP.put(VK_X, 'X');
        SPECIALS_HASH_MAP.put(VK_Y, 'Y');
        SPECIALS_HASH_MAP.put(VK_Z, 'Z');


        OPERATE_HASH_MAP.put(VK_BACK_SPACE, '\b');
    }
}
