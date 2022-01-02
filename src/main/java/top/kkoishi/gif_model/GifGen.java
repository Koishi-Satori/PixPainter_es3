package top.kkoishi.gif_model;

import top.kkoishi.CompilerSyntaxErrorException;
import top.kkoishi.PixPainter;
import top.kkoishi.gif_model.swing.Select;

import javax.swing.*;

/**
 * @author KKoishi_
 */
public class GifGen extends Thread {
    /**
     * If this thread was constructed using a separate
     * {@code Runnable} run object, then that
     * {@code Runnable} object's {@code run} method is called;
     * otherwise, this method does nothing and returns.
     * <p>
     * Subclasses of {@code Thread} should override this method.
     *
     * @see #start()
     * @see #stop()
     * @see #Thread(ThreadGroup, Runnable, String)
     */
    @Override
    public void run () {
        super.run();
        try {
            main(PixPainter.args);
        } catch (UnsupportedLookAndFeelException | ClassNotFoundException |
                InstantiationException | IllegalAccessException |
                CompilerSyntaxErrorException e) {
            e.printStackTrace();
        }
    }

    public static void main (String[] args)
            throws UnsupportedLookAndFeelException,
            ClassNotFoundException,
            InstantiationException,
            IllegalAccessException,
            CompilerSyntaxErrorException {
        new Select();
    }
}
