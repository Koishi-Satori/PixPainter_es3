package top.kkoishi.g2d;

import top.kkoishi.CompilerSyntaxErrorException;
import top.kkoishi.io.Files;
import top.kkoishi.util.LinkedList;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.Iterator;

import static java.lang.StrictMath.abs;
import static java.lang.System.gc;
import static top.kkoishi.g2d.Color.colors;
import static top.kkoishi.g2d.PaintApi.*;

public class Jcc {
    public static final int HEAD_INITIAL_SIZE = 4;
    private String dir;
    private File file;
    private File script;
    private LinkedList<String> tokens;
    private LinkedList<String> java;

    public Jcc (String file) {
        this.file = new File(file);
        dir = this.file.getParent();
        script = new File(dir + "/" + this.file.getName().
                replaceFirst("\\..+$", "") + ".java");
        tokens = new LinkedList<>();
        java = new LinkedList<>();
    }

    public void read () throws IOException {
        BufferedReader reader = new BufferedReader(new FileReader(file));
        StringBuilder sb = new StringBuilder();
        int len;
        char[] buf = new char[1024];
        while ((len = reader.read(buf)) != -1) {
            sb.append(new String(buf, 0, len));
        }
        buf = null;
        String[] buffer = sb.toString().split("\\s*;\\s*");
        for (int i = 0; i < buffer.length; i++) {
            buffer[i] = buffer[i]
                    .replaceAll("\n", "")
                    .replaceAll("\r", "");
            if (!buffer[i].matches("\\s*")) {
                tokens.add(buffer[i]);
            }
        }
        System.out.println(tokens);
        buffer = null;
        gc();
    }

    final String pre (String command) {
        return command.replaceFirst("^[a-z-A-Z]+\\s*", command.split("\\s+")[0].toLowerCase() + " ");
    }

    public void compile () throws CompilerSyntaxErrorException {
        java.add("import javax.imageio.ImageIO;\n" +
                "import java.awt.Font;\n" +
                "import java.awt.Graphics2D;\n" +
                "import java.awt.Image;\n" +
                "import java.awt.Shape;\n" +
                "import java.awt.geom.Ellipse2D;\n" +
                "import java.awt.geom.GeneralPath;\n" +
                "import java.awt.geom.Point2D;\n" +
                "import java.awt.image.BufferedImage;\n" +
                "import java.io.File;\n" +
                "import java.io.IOException;\n" +
                "import java.util.LinkedList;\n" +
                "import java.util.List;\n" +
                "import java.awt.Color;\n" +
                "import java.util.Vector;\n\n" +
                "import static java.lang.StrictMath.*;\n" +
                "import static java.lang.System.*;\n\n");
        String className = script.getName().replaceFirst("\\..+$", "");
        java.add("public class " + className + " {\n");
        boolean flag = false;
        boolean flagPline = false;
        if (!tokens.getFirst().matches(CREATE_REGEX)) {
            flag = true;
            java.add("\tpublic static BufferedImage image = new BufferedImage(600, 600, BufferedImage.TYPE_INT_RGB);\n");
            java.add("\tpublic static File file = new File(" + className + ");");
            java.add("\tpublic static Graphics2D g2D = image.createGraphics();\n");
        }
        if (tokens.isEmpty()) {
            throw new CompilerSyntaxErrorException("The file is empty!");
        }
        //get First.
        Iterator<String> iterator = tokens.iterator();
        String head = iterator.next();
        if (!flag) {
            String[] strings = head.split("\\s+");
            if (strings.length != HEAD_INITIAL_SIZE) {
                throw new CompilerSyntaxErrorException("SyntaxError at head part.");
            }
            java.add("\tpublic static BufferedImage image = new BufferedImage("
                    + Integer.parseInt(strings[1]) + ", " +
                    Integer.parseInt(strings[2]) +
                    ", BufferedImage.TYPE_INT_RGB);\n");
            java.add("\tpublic static File file = new File(\"" + dir.replaceAll("\\\\", "/") + "/" + className + ".png\");\n");
            java.add("\tpublic static Graphics2D g2D = image.createGraphics();\n");
        }
        java.add("\n\tpublic static void main(String[] args) {\n");
        while (iterator.hasNext()) {
            String token = iterator.next();
            token = pre(token);
            //if change dir
            if (token.matches(CHANGEDIR_REGEX)) {
                className = token.split("\\s+")[1] + "/" +
                        file.getName().replaceFirst("\\..+$", "");
                java.add("\t\tfile = new File(\"" + className + ".png\");\n");
            }
            //if setfont
            else if (token.matches(SETFONT_REGEX)) {
                String[] strings = token.split("\\s+");
                String fontFamily = ("null".equals(strings[1]) ? "\"\"" : strings[1]);
                String fontType = ("bold".equals(strings[2]) ?
                        "Font.BOLD" : "plain".equals(strings[2]) ?
                        "Font.PLAIN" : "Font.ITALIC");
                try {
                    int fontSize = Integer.parseInt(strings[3]);
                    String font = "new Font(" + fontFamily + ", " + fontType + ", " + fontSize + ")";
                    java.add("\t\tg2D.setFont(" + font + ");\n");
                } catch (NumberFormatException e) {
                    throw new CompilerSyntaxErrorException(e.getCause().getMessage());
                }
            }
            //if setcolor
            else if (token.matches(SETCOLOR_REGEX)) {
                String[] strings = token.split("\\s+");
                if (colors.containsKey(strings[1].toLowerCase())) {
                    java.add("\t\tg2D.setColor(" + "Color." + strings[1].toLowerCase() + ");\n");
                } else {
                    throw new CompilerSyntaxErrorException("Error color");
                }
            }
            //if draw circle
            else if (token.matches(DRAWCIRCLE_REGEX)) {
                try {
                    String[] strings = token.split("\\s+");
                    int x = Integer.parseInt(strings[1]);
                    int y = Integer.parseInt(strings[2]);
                    int r = Integer.parseInt(strings[3]);
                    //new Ellipse2D.Double(x - r, y - r, 2 * r, 2 * r)
                    java.add("\t\tg2D.draw(new Ellipse2D.Double(" + (x - r) +
                            ", " + (y - r) + ", " + (2 * r) + ", " + (2 * r) + "));\n");
                } catch (NumberFormatException e) {
                    throw new CompilerSyntaxErrorException("Illegal Number casting");
                }
            }
            //if enhanced draw
            else if (token.matches(DRAW_SIMPLE_SHAPE)) {
                String[] params = token.split("\\s+");
                try {
                    int x1 = Integer.parseInt(params[2]);
                    int y1 = Integer.parseInt(params[3]);
                    int x2 = Integer.parseInt(params[4]);
                    int y2 = Integer.parseInt(params[5]);
                    switch (params[1]) {
                        case ELLIPSE: {
                            java.add("\t\tg2D.draw(new Ellipse2D.double(" + x1 + ", " + y1 + ", " +
                                    abs(x1 - x2) + ", " + abs(y1 - y2) + "));\n");
                            break;
                        }
                        case RECT: {
                            java.add("\t\tg2D.drawRect(" + x1 + ", " + y1 + ", " +
                                    abs(x1 - x2) + ", " + abs(y1 - y2) + ");\n");
                            break;
                        }
                        case LINE: {
                            java.add("\t\tg2D.drawLine(" + x1 + ", " + y1 + ", " + x2 + ", " + y2 + ");\n");
                            break;
                        }
                        default: {
                            throw new CompilerSyntaxErrorException("Illegal param");
                        }
                    }
                } catch (NumberFormatException e) {
                    throw new CompilerSyntaxErrorException("Illegal Number casting");
                }
            }
            //if draw string
            if (token.matches(DRAWSTRING_REGEX)) {
                try {
                    int x = Integer.parseInt(token.split("\\s+")[1]);
                    int y = Integer.parseInt(token.split("\\s+")[2]);
                    int p = token.split("\\s+")[3].length();
                    java.add("\t\tg2D.drawString(\"" + token.split("\\s+")[3] + "\""
                            + ", " + x + " - g2D.getFont().getSize() * " +
                            p + " / 3, " + y + ");\n");
                } catch (NumberFormatException e) {
                    throw new CompilerSyntaxErrorException("Illegal Number casting");
                }
            }
            //if draw pline
            if (token.matches(DRAW_POLY_LINE_REGEX)) {
                String[] params = token.split("\\s+");
                try {
                    int amount = Integer.parseInt(params[1]);
                    int[] xs = new int[amount];
                    int[] ys = new int[amount];
                    if (params.length % 2 == 0) {
                        for (int i = 0; i < amount; i++) {
                            xs[i] = Integer.parseInt(params[i * 2 + 2]);
                            ys[i] = Integer.parseInt(params[i * 2 + 3]);
                        }
                        StringBuilder sb = new StringBuilder("{");
                        for (int i = 0, len = xs.length - 1; i < len; i++) {
                            sb.append(xs[i]).append(", ");
                        }
                        sb.append(xs[xs.length - 1]);
                        sb.append("};\n");
                        String j = "\t\t" + (!flagPline ? "int[] xs = " : "xs = new int[]") + sb;
                        sb = new StringBuilder("{");
                        for (int i = 0, len = ys.length - 1; i < len; i++) {
                            sb.append(ys[i]).append(", ");
                        }
                        sb.append(ys[ys.length - 1]);
                        sb.append("};\n");
                        String k = "\t\t" + (!flagPline ? "int[] ys = " : "ys = new int[]") + sb;
                        java.add(j + k + "\t\tg2D.drawPolyline(xs, ys, " + amount + ");\n");
                        flagPline = true;
                    } else {
                        throw new CompilerSyntaxErrorException("Illegal param amount");
                    }
                } catch (NumberFormatException e) {
                    throw new CompilerSyntaxErrorException("Illegal Number casting");
                }
            }
        }
        java.add("\t\tg2D.dispose();\n");
        java.add("\t\ttry {\n" +
                "\t\t\tImageIO.write(image, \"png\", file);\n" +
                "\t\t\timage = null;\n" +
                "\t\t} catch (IOException e) {\n" +
                "\t\t\te.printStackTrace();\n" +
                "\t\t}\n");
        java.add("\t}\n}");
    }

    public LinkedList<String> export () {
        return java;
    }

    public void write () throws IOException {
        Files.DefaultFiles.build().write(script, code());
    }

    public File get () {
        return script;
    }

    public String code () {
        StringBuilder sb = new StringBuilder();
        for (String j : java) {
            sb.append(j);
        }
        return sb.toString();
    }

    public void clear () {
        dir = null;
        file = null;
        script = null;
        tokens = null;
        java = null;
    }
}
