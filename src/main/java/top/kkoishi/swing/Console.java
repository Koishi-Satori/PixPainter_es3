package top.kkoishi.swing;

import top.kkoishi.concurrent.DefaultThreadFactory;
import top.kkoishi.io.Files;
import top.kkoishi.io.ZipFiles;
import top.kkoishi.log.LogType;
import top.kkoishi.log.Logger;
import top.kkoishi.ss.ConsoleApi;
import top.kkoishi.util.LinkedList;
import top.kkoishi.util.Vector;

import javax.imageio.ImageIO;
import javax.swing.JComponent;
import javax.swing.JFrame;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.WindowConstants;
import java.awt.Color;
import java.awt.Component;
import java.awt.Font;
import java.awt.GraphicsEnvironment;
import java.awt.HeadlessException;
import java.awt.Image;
import java.awt.Toolkit;
import java.awt.datatransfer.Clipboard;
import java.awt.datatransfer.DataFlavor;
import java.awt.datatransfer.Transferable;
import java.awt.datatransfer.UnsupportedFlavorException;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.io.File;
import java.io.IOException;
import java.util.Calendar;
import java.util.List;
import java.util.concurrent.ScheduledThreadPoolExecutor;

import static java.awt.event.KeyEvent.CTRL_DOWN_MASK;
import static java.awt.event.KeyEvent.VK_0;
import static java.awt.event.KeyEvent.VK_1;
import static java.awt.event.KeyEvent.VK_2;
import static java.awt.event.KeyEvent.VK_3;
import static java.awt.event.KeyEvent.VK_4;
import static java.awt.event.KeyEvent.VK_5;
import static java.awt.event.KeyEvent.VK_6;
import static java.awt.event.KeyEvent.VK_7;
import static java.awt.event.KeyEvent.VK_8;
import static java.awt.event.KeyEvent.VK_9;
import static java.awt.event.KeyEvent.VK_A;
import static java.awt.event.KeyEvent.VK_B;
import static java.awt.event.KeyEvent.VK_C;
import static java.awt.event.KeyEvent.VK_CAPS_LOCK;
import static java.awt.event.KeyEvent.VK_D;
import static java.awt.event.KeyEvent.VK_DOWN;
import static java.awt.event.KeyEvent.VK_E;
import static java.awt.event.KeyEvent.VK_F;
import static java.awt.event.KeyEvent.VK_G;
import static java.awt.event.KeyEvent.VK_H;
import static java.awt.event.KeyEvent.VK_I;
import static java.awt.event.KeyEvent.VK_J;
import static java.awt.event.KeyEvent.VK_K;
import static java.awt.event.KeyEvent.VK_L;
import static java.awt.event.KeyEvent.VK_M;
import static java.awt.event.KeyEvent.VK_N;
import static java.awt.event.KeyEvent.VK_O;
import static java.awt.event.KeyEvent.VK_P;
import static java.awt.event.KeyEvent.VK_Q;
import static java.awt.event.KeyEvent.VK_R;
import static java.awt.event.KeyEvent.VK_S;
import static java.awt.event.KeyEvent.VK_SHIFT;
import static java.awt.event.KeyEvent.VK_T;
import static java.awt.event.KeyEvent.VK_U;
import static java.awt.event.KeyEvent.VK_UP;
import static java.awt.event.KeyEvent.VK_V;
import static java.awt.event.KeyEvent.VK_W;
import static java.awt.event.KeyEvent.VK_X;
import static java.awt.event.KeyEvent.VK_Y;
import static java.awt.event.KeyEvent.VK_Z;
import static top.kkoishi.constpool.KeyCharEntry.INTEGER_CHARACTER_HASH_MAP;
import static top.kkoishi.constpool.KeyCharEntry.SPECIALS_HASH_MAP;

/**
 * Console object,implemented by KeyBoard input based on native method.
 * And when invoking it,you might, should make sure the const pool is
 * used by the main class,and change some static member of this class.
 *
 * @author KKoishi_
 */
public class Console extends JFrame {
    public static final char RUN_CHAR = '\n';
    public static final String SET_FONT = "\\s*font\\s*\\d+\\s*";
    public static final String S_CLEAR_S = "\\s*clear\\s*";
    public static final String S_EXIT_S = "\\s*exit\\s*";
    static JTextArea terminalDisplay = new JTextArea();
    static JScrollPane jsp = new JScrollPane(terminalDisplay);
    static boolean isUsedByAction = false;
    static String command = "";
    static String time;
    static LinkedList<String> history = new LinkedList<>();
    static Vector<String> historyCommands = new Vector<>();
    static int historyPointer = 0;
    static boolean isAlive = true;
    static boolean isCapsLockOn = false;
    static boolean isShiftPressed = false;
    static boolean isCtrlPressed = false;
    static boolean emptyEchoFirst = false;
    static boolean emptyEchoSecond = false;
    static boolean isDownPressed = false;
    static boolean isUpPressed = false;
    static String terminalName = "PixPainter>";
    private static final Calendar CALENDAR = Calendar.getInstance();

    static {
        time = getTime();
        terminalDisplay.setLineWrap(false);
        terminalDisplay.setEditable(false);
        terminalDisplay.setText(time + terminalName);
        history.add(time + terminalName);
        terminalDisplay.setSelectedTextColor(Color.GREEN);
        Font font = terminalDisplay.getFont();
        terminalDisplay.setFont(new Font(font.getName(), font.getStyle(), 15));
    }

    /**
     * Creates a new, initially invisible <code>Frame</code> with the
     * specified title.
     * <p>
     * This constructor sets the component's locale property to the value
     * returned by <code>JComponent.getDefaultLocale</code>.
     *
     * @param title the title for the frame
     * @throws HeadlessException if GraphicsEnvironment.isHeadless()
     *                           returns true.
     * @see GraphicsEnvironment#isHeadless
     * @see Component#setSize
     * @see Component#setVisible
     * @see JComponent#getDefaultLocale
     */
    public Console (String title) throws HeadlessException {
        super(title);
        String icoDir = "./IconRuntimeTemp";
        String datDecompressState = "Load icon files, decompress dat file state:" + ZipFiles.decompress(new File("./icons.dat"), icoDir) + "";
        Logger.Builder.set("./log");
        Logger logger = Logger.Builder.build();
        logger.log(LogType.EVENT, datDecompressState);
        System.out.println("Load icon files, decompress dat file state:" + datDecompressState);
        List<Image> images = new LinkedList<>();
        try {
            images.add(ImageIO.read(new File("./IconRuntimeTemp/379f219f5393398.ico")));
            images.add(ImageIO.read(new File("./IconRuntimeTemp/logo.ico")));
            if (!images.isEmpty()) {
                setIconImages(images);
                Files.DefaultFiles.access.append("./all.log", getLogTime() + "Successfully set icon.\n");
                System.out.println("Delete icon file temp state:" +
                        (new File("./IconRuntimeTemp/379f219f5393398.ico").delete() &&
                                new File("./IconRuntimeTemp/logo.ico").delete()));
                System.out.println("Delete icon dir temp state:" + new File(icoDir).delete());
            } else {
                new IOException("Failed to read icons").printStackTrace();
            }
        } catch (IOException e) {
            e.printStackTrace();
            System.out.println(e.getLocalizedMessage());
            logger.log(LogType.EXCEPTION, e.getLocalizedMessage());
        }
        setSize(500, 500);
        setFocusable(true);
        add(jsp);
        setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);

        terminalDisplay.addMouseListener(new MouseAdapter() {
            /**
             * {@inheritDoc}
             *
             * @param e
             */
            @Override
            public void mouseClicked (MouseEvent e) {
                super.mouseClicked(e);
                if (e.getButton() == MouseEvent.BUTTON3) {
                    paste();
                }
            }
        });

        ScheduledThreadPoolExecutor poolExecutor = new ScheduledThreadPoolExecutor(1, new DefaultThreadFactory());
        poolExecutor.execute(new FlushCursor());

        terminalDisplay.addKeyListener(new KeyAdapter() {
            /**
             * Invoked when a key has been typed.
             * This event occurs when a key press is followed by a key release.
             *
             * @param e key input
             */
            @Override
            public void keyTyped (KeyEvent e) {
                super.keyTyped(e);
                if (e.getKeyCode() == VK_CAPS_LOCK) {
                    isCapsLockOn = true;
                }
                int keyCode = e.getKeyCode();
                switch (keyCode) {
                    case VK_0:
                    case VK_1:
                    case VK_2:
                    case VK_3:
                    case VK_4:
                    case VK_5:
                    case VK_6:
                    case VK_7:
                    case VK_8:
                    case VK_9:
                    case VK_A:
                    case VK_B:
                    case VK_C:
                    case VK_D:
                    case VK_E:
                    case VK_F:
                    case VK_G:
                    case VK_H:
                    case VK_I:
                    case VK_J:
                    case VK_K:
                    case VK_L:
                    case VK_M:
                    case VK_N:
                    case VK_O:
                    case VK_P:
                    case VK_Q:
                    case VK_R:
                    case VK_S:
                    case VK_T:
                    case VK_U:
                    case VK_V:
                    case VK_W:
                    case VK_X:
                    case VK_Y:
                    case VK_Z: {
                        edit(keyCode);
                        break;
                    }
                    default: {
                        if (e.getKeyChar() != (char) 3) {
                            print(e.getKeyChar());
                        }
                        break;
                    }
                }
            }

            /**
             * Invoked when a key has been pressed.
             *
             * @param e e
             */
            @Override
            public void keyPressed (KeyEvent e) {
                super.keyPressed(e);
                int keyCode = e.getKeyCode();
                if (keyCode == VK_SHIFT) {
                    isShiftPressed = true;
                }
                if (keyCode == CTRL_DOWN_MASK) {
                    isCtrlPressed = true;
                }
                if (keyCode == VK_UP) {
                    System.out.println("Event:Press up");
                    isUpPressed = true;
                }
                if (keyCode == VK_DOWN) {
                    System.out.println("Event:Press down");
                    isDownPressed = true;
                }
            }

            /**
             * Invoked when a key has been released.
             *
             * @param e e
             */
            @Override
            public void keyReleased (KeyEvent e) {
                super.keyReleased(e);
                int keyCode = e.getKeyCode();
                if (keyCode == VK_SHIFT) {
                    isShiftPressed = false;
                }
                if (keyCode == CTRL_DOWN_MASK) {
                    isCtrlPressed = false;
                }
                if (isUpPressed) {
                    System.out.println("Event:Try to get front history event");
                    getHistoryInput();
                    isUpPressed = false;
                }
                if (isDownPressed) {
                    System.out.println("Event:Try to get next history event");
                    getNextHistoryInput();
                    isDownPressed = false;
                }
            }
        });
        setVisible(true);
    }

    final void print (char keyChar) {
        if (keyChar == RUN_CHAR) {
            isUsedByAction = true;
            historyCommands.add(command);
            history.add(command + "\n" + terminalName);
            System.out.println("History Buffer Zone size: " + history.size());
        }
        if (!isUsedByAction) {
            if (keyChar == '\b') {
                command = command.replaceFirst(".$", "");
            } else {
                command += keyChar;
            }
        } else {
            System.out.println("Event:Run");
            loadAction();
            isUsedByAction = false;
            command = "";
        }
        terminalDisplay.setText(flush() + command);
    }

    final String flush () {
        if (history.isEmpty()) {
            return "";
        }
        while (history.size() >= (1 << 11)) {
            history.removeFirst();
        }
        StringBuilder sb = new StringBuilder();
        for (String s : history) {
            sb.append(s);
        }
        return sb.toString();
    }

    final void edit (int keyCode) {
        //if the terminal is not be placed by other thread's event
        //then normally print into the terminal
        //and add the character to the command string,
        //or invoke the method 'extendExecute' to make sure that
        //output is correct.
        if (!isUsedByAction) {
            //if caps lock is on.
            if (isCapsLockOn || isShiftPressed) {
                if (isShiftPressed) {
                    System.out.println(SPECIALS_HASH_MAP.get(keyCode));
                } else {
                    System.out.println(INTEGER_CHARACTER_HASH_MAP.get(keyCode));
                }
            }
        } else {
            loadAction();
            isUsedByAction = false;
        }
    }

    final void loadAction () {
        //temp part
        System.out.println(command);
        if (!"".equals(command)) {
            history.add("run command!\n");
            //not finished.
            read();
            history.add(terminalName);
        } else {
            history.add("");
        }

        //end
    }

    final void read () {
        historyPointer = historyCommands.size();
        if (command.matches(S_EXIT_S)) {
            isAlive = false;
            try {
                Thread.sleep(50);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            System.exit(514);
        } else if (command.matches(S_CLEAR_S)) {
            history.clear();
            terminalDisplay.setText("");
            time = getTime();
            history.add(time);
        }
//        } else if (command.matches(SET_FONT)) {
//            setFont(command);
//        }
        else {
            history.add(ConsoleApi.run(command));
        }
    }

    final void setFont (String command) {
        history.add(command);
        Font font = terminalDisplay.getFont();
        int size = 10;
        for (String s : command.split("\\s*")) {
            if (s.matches("\\d+")) {
                size = Integer.parseInt(s);
            }
        }
        terminalDisplay.setFont(new Font(font.getName(), font.getStyle(), size));
        System.out.println(terminalDisplay.getText());
    }

    /**
     * right click to get paste content
     */
    final void paste () {
        //get system clipboard
        Clipboard clipboard = Toolkit.getDefaultToolkit().getSystemClipboard();
        //get content
        Transferable content = clipboard.getContents(null);
        //if not null,then get string data(if not string,continue.).
        if (content != null) {
            if (content.isDataFlavorSupported(DataFlavor.stringFlavor)) {
                try {
                    String text = (String) content.getTransferData(DataFlavor.stringFlavor);
                    System.out.println("Event:paste content [" + text + ']');
                    command += text;
                    terminalDisplay.setText(flush() + command);
                } catch (UnsupportedFlavorException | IOException e) {
                    System.out.println("Error:paste failed.");
                }
            }
        }
    }

    final void getHistoryInput () {
        historyPointer--;
        if (historyPointer >= 0) {
            System.out.println("Event:Success get front history");
            command = historyCommands.get(historyPointer);
            terminalDisplay.setText(flush() + command);
        } else {
            if (command.equals(historyCommands.get(0))) {
                historyPointer = 0;
            } else {
                historyPointer = historyCommands.size();
            }
        }
    }

    final void getNextHistoryInput () {
        historyPointer++;
        if (historyPointer < historyCommands.size()) {
            System.out.println("Event:Success get next history");
            command = historyCommands.get(historyPointer);
            terminalDisplay.setText(flush() + command);
        } else {
            historyPointer = historyCommands.size();
        }
    }

    private static String getTime () {
        return "System Time:" + CALENDAR.get(Calendar.YEAR) + "/" +
                CALENDAR.get(Calendar.MONTH) + "/" + CALENDAR.get(Calendar.DAY_OF_MONTH) +
                "\t" + CALENDAR.get(Calendar.HOUR) + ":" + CALENDAR.get(Calendar.MINUTE) +
                ":" + CALENDAR.get(Calendar.SECOND) + "\n";
    }

    private static String getLogTime () {
        return "[" + CALENDAR.get(Calendar.YEAR) + "/" +
                CALENDAR.get(Calendar.MONTH) + "/" + CALENDAR.get(Calendar.DAY_OF_MONTH) +
                " " + CALENDAR.get(Calendar.HOUR) + ":" + CALENDAR.get(Calendar.MINUTE) +
                ":" + CALENDAR.get(Calendar.SECOND) + "]";
    }

    static class FlushCursor implements Runnable {
        /**
         * When an object implementing interface {@code Runnable} is used
         * to create a thread, starting the thread causes the object's
         * {@code run} method to be called in that separately executing
         * thread.
         * <p>
         * The general contract of the method {@code run} is that it may
         * take any action whatsoever.
         *
         * @see Thread#run()
         */
        @Override
        public void run () {
            while (isAlive) {
                terminalDisplay.append("_");
                try {
                    Thread.sleep(500);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                    System.exit(514);
                }
                int size = terminalDisplay.getText().length();
                if (terminalDisplay.getText().charAt(size - 1) == '_') {
                    terminalDisplay.replaceRange("", size - 1, size);
                }
                try {
                    Thread.sleep(500);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        }
    }
}
