package top.kkoishi.gif_model.swing;

import top.kkoishi.gif_model.Functions;
import top.kkoishi.gif_model.GifApi;
import top.kkoishi.io.FileChooser;
import top.kkoishi.io.Files;
import top.kkoishi.io.ZipFiles;
import top.kkoishi.log.LogType;
import top.kkoishi.log.Logger;
import top.kkoishi.util.LinkedList;

import javax.imageio.ImageIO;
import javax.swing.JButton;
import javax.swing.JComponent;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JTextField;
import javax.swing.WindowConstants;
import java.awt.Component;
import java.awt.GraphicsEnvironment;
import java.awt.HeadlessException;
import java.awt.Image;
import java.io.File;
import java.io.IOException;
import java.util.Calendar;
import java.util.List;
import java.util.Locale;

import static java.lang.System.gc;

public class Select extends JFrame {
    JLabel label = new JLabel("Input file type(graph/script)");
    JTextField field = new JTextField();
    JButton select = new JButton("Confirm");
    JButton compress = new JButton("compress");
    boolean defCompress = false;

    private static final Calendar CALENDAR = Calendar.getInstance();

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
    public Select () throws HeadlessException {
        super("Gif Generator");
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
        setSize(300, 200);
        setResizable(false);
        setDefaultCloseOperation(WindowConstants.DISPOSE_ON_CLOSE);
        JPanel jp = new JPanel(null);
        label.setBounds(10, 10, 180,30);
        field.setBounds(200, 10, 80, 30);
        select.setBounds(100, 50,100,30);
        compress.setBounds(100, 100, 100, 30);
        jp.add(select);
        jp.add(field);
        jp.add(compress);
        jp.add(label);
        setVisible(true);
        add(jp);

        select.addActionListener(e -> {
            String text = field.getText();
            if ("".equals(text)) {
                JOptionPane.showMessageDialog(null, "Type can not be null");
            } else {
                switch (text.toLowerCase(Locale.ROOT)) {
                    case "graph": {
                        String height = JOptionPane.showInputDialog(null, "Export Height", 512);
                        String width = JOptionPane.showInputDialog(null, "Export Width", 512);
                        if (height.matches("^\\d+$") && width.matches("^\\d+$")) {
                            run(true, Integer.parseInt(width), Integer.parseInt(height));
                        } else {
                            JOptionPane.showMessageDialog(null, "Error Input!",
                                    "top.kkoishi.exception.MathException", JOptionPane.ERROR_MESSAGE);
                        }
                        break;
                    }
                    case "script": {
                        String height = JOptionPane.showInputDialog(null, "Export Height", 512);
                        String width = JOptionPane.showInputDialog(null, "Export Width", 512);
                        if (height == null || width == null) {
                            return;
                        }
                        if (height.matches("^\\d+$") && width.matches("^\\d+$")) {
                            run(false, Integer.parseInt(width), Integer.parseInt(height));
                        } else {
                            JOptionPane.showMessageDialog(null, "Error Input!",
                                    "top.kkoishi.exception.MathException", JOptionPane.ERROR_MESSAGE);
                        }
                        break;
                    }
                    default: {
                        JOptionPane.showMessageDialog(null, "UnsupportedType");
                    }
                }
            }
        });

        compress.addActionListener(e -> {
            if (defCompress) {
                compress.setText("uncompress");
                defCompress = false;
            } else {
                compress.setText("compress");
                defCompress = true;
            }
        });
    }

    private void run (boolean isGraph, int width, int height) {
        try {
            if (isGraph) {
                List<String> strings = FileChooser.
                        DefaultFileChooser.
                        build().
                        files("Choose png files", "pngFiles", "kGraph", "kgraph");
                if (strings.isEmpty()) {
                    System.out.println("Event:No files are selected.");
                } else {
                    File[] files = new File[strings.size()];
                    for (int i = 0; i < strings.size(); i++) {
                        files[i] = new File(strings.get(i));
                    }
                    strings.clear();
                    GifApi api = Functions.buildFromGraph(files, ".", width, height);
                    File export = api.generate(defCompress);
                    JOptionPane.showMessageDialog(null, "ExportSuccess! file:" +
                            export, "Result", JOptionPane.INFORMATION_MESSAGE);
                    gc();
                }
            } else {
                List<String> strings = FileChooser.
                        DefaultFileChooser.
                        build().
                        files("Choose png files", "scriptFiles(.kScript .kscript)", "kScript", "kscript");
                if (strings.isEmpty()) {
                    System.out.println("Event:No files are selected.");
                } else {
                    File[] files = new File[strings.size()];
                    for (int i = 0; i < strings.size(); i++) {
                        files[i] = new File(strings.get(i));
                    }
                    strings.clear();
                    GifApi api = Functions.buildFromScript(files, ".", width, height);
                    File export = api.generate(defCompress);
                    JOptionPane.showMessageDialog(null, "ExportSuccess! file:" +
                            export, "Result", JOptionPane.INFORMATION_MESSAGE);
                    gc();
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
            Logger logger = Logger.Builder.build();
            logger.log(LogType.EXCEPTION, e.getLocalizedMessage());
            JOptionPane.showMessageDialog(null, e.getLocalizedMessage(), "Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    private static String getLogTime () {
        return "[" + CALENDAR.get(Calendar.YEAR) + "/" +
                CALENDAR.get(Calendar.MONTH) + "/" + CALENDAR.get(Calendar.DAY_OF_MONTH) +
                " " + CALENDAR.get(Calendar.HOUR) + ":" + CALENDAR.get(Calendar.MINUTE) +
                ":" + CALENDAR.get(Calendar.SECOND) + "]";
    }
}
