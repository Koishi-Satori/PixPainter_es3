package top.kkoishi.editor;

import top.kkoishi.PixPainter;
import top.kkoishi.concurrent.DefaultThreadFactory;
import top.kkoishi.io.Files;
import top.kkoishi.util.LinkedList;
import top.kkoishi.util.Vector;

import javax.swing.*;
import java.awt.*;
import java.awt.event.InputEvent;
import java.awt.event.KeyEvent;
import java.io.File;
import java.io.IOException;
import java.util.Arrays;
import java.util.HashMap;
import java.util.concurrent.ScheduledThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

public class FileEditor extends JFrame {
    String filePath;
    String fileName;
    File script;

    private JTextArea state = new JTextArea("Code analysis running:no error");
    private JTextArea file;
    private JTextArea editor = new JTextArea();

    private static final HashMap<String, String> SYNTAX_CHECK_MAP = PixPainter.SYNTAX_CHECK_MAP;
    private static final int INITIAL_WIDTH = 900;
    private static final int INITIAL_HEIGHT = 600;

    /**
     * Constructs a new frame that is initially invisible.
     * <p>
     * This constructor sets the component's locale property to the value
     * returned by <code>JComponent.getDefaultLocale</code>.
     *
     * @throws HeadlessException if GraphicsEnvironment.isHeadless()
     *                           returns true.
     * @see GraphicsEnvironment#isHeadless
     * @see Component#setSize
     * @see Component#setVisible
     * @see JComponent#getDefaultLocale
     */
    public FileEditor (String filePath, String fileName) throws HeadlessException {
        this.filePath = filePath;
        this.fileName = fileName;
        setFocusable(true);
        this.script = new File(filePath + "/" + fileName);
        file = new JTextArea("ScriptFile:" + filePath + "/" + fileName + "\tYou can use CTRL + S to save.And auto save time is 15sec");
        file.setBounds(0, 0, INITIAL_WIDTH, INITIAL_HEIGHT / 20);
        state.setBounds(0, INITIAL_HEIGHT / 20, INITIAL_WIDTH, INITIAL_HEIGHT / 20);
        editor.setBounds(0, INITIAL_HEIGHT / 10, INITIAL_WIDTH, 9 * INITIAL_HEIGHT / 10);
        file.setEditable(false);
        state.setEditable(false);
        file.setLineWrap(false);
        state.setLineWrap(false);
        editor.setLineWrap(false);
        setSize(INITIAL_WIDTH + 15, INITIAL_HEIGHT + 35);
        setResizable(false);
        setDefaultCloseOperation(WindowConstants.DISPOSE_ON_CLOSE);
        JScrollPane jsp2 = new JScrollPane(editor);
        jsp2.setBounds(0, INITIAL_HEIGHT / 10, INITIAL_WIDTH, INITIAL_HEIGHT * 9 / 10);
        editor.setFont(new Font("", Font.ITALIC, 15));
        this.setLayout(null);
        file.setBorder(BorderFactory.createLineBorder(Color.CYAN, 2, true));
        state.setBorder(BorderFactory.createLineBorder(Color.CYAN, 2, true));
        add(jsp2);
        add(state);
        add(file);
        setVisible(true);

        ScheduledThreadPoolExecutor pool = new ScheduledThreadPoolExecutor(2,
                new DefaultThreadFactory());
        pool.scheduleAtFixedRate(new SyntaxAnalysis(), 0, 50, TimeUnit.MILLISECONDS);
        pool.scheduleAtFixedRate(new SaveFileThread(), 0, 15000, TimeUnit.MILLISECONDS);
        JMenuItem item = new JMenuItem();
        item.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_S, InputEvent.CTRL_DOWN_MASK));
        item.addActionListener(e -> new SaveFileThread().run());
    }

    private class SaveFileThread implements Runnable {
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
            try {
                System.out.println(script.createNewFile());
                Files.DefaultFiles.build().write(script, editor.getText());
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    private class SyntaxAnalysis implements Runnable {
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
            StringBuilder str = new StringBuilder("Code analysis running->");
            LinkedList<Integer> integers = analysis();
            if (!integers.isEmpty()) {
                str.append("Error at Sentence:");
                for (Integer integer : integers) {
                    str.append(integer).append(" ");
                }
            } else {
                str.append("No error");
            }
            state.setText(str.toString());
        }

        final LinkedList<Integer> analysis () {
            Vector<String> tokens = new Vector<>();
            tokens.addAll(Arrays.asList(editor.getText().split(";")));
            if (tokens.isEmpty()) {
                return new LinkedList<>();
            }
            int i = 1;
            LinkedList<Integer> ans = new LinkedList<>();
            for (String token : tokens) {
                token = token.replaceAll("\n", "").replaceAll("\r", "");
                if (token.matches("\\s*")) {
                    continue;
                }
                if (!SYNTAX_CHECK_MAP.containsKey(token.split("\\s+")[0].toLowerCase())) {
                    ans.add(i);
                }
                i++;
            }
            return ans;
        }
    }
}
