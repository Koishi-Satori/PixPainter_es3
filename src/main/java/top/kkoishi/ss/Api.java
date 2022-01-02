package top.kkoishi.ss;

/**
 * Api interface for console api to execute functions.
 * @author KKoishi_
 * @apiNote You might need to change the implement of the Class "ConsoleApi"
 * @see ConsoleApi
 * @see ConsoleApi#api
 * @since java8
 */
public interface Api {
    /**
     * functions execute part.
     * @param command execute command, a string
     * @see ConsoleApi#run(String)
     */
    void execute (String command);

    /**
     * If functions have returned value,then impl it,ot return "";
     * @return functions'returned value
     * @see ConsoleApi#run(String)
     */
    String getCall ();
}
