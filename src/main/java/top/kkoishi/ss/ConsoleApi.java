package top.kkoishi.ss;

import top.kkoishi.g2d.PaintApi;

/**
 * Api for console command
 * @author KKoishi_
 */
public class ConsoleApi {
    static Api api = new PaintApi("./g2d");

    private ConsoleApi () {
    }



    public static String run (String command) {
        api.execute(command);
        return api.getCall().replaceFirst("PixPainter>$","");
    }
}
