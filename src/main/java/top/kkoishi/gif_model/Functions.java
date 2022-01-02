package top.kkoishi.gif_model;

import com.madgag.gif.fmsware.AnimatedGifEncoder;
import top.kkoishi.CompilerSyntaxErrorException;
import top.kkoishi.g2d.GraphCompiler;
import top.kkoishi.g2d.ScriptExplainer;
import top.kkoishi.log.LogType;
import top.kkoishi.log.Logger;
import top.kkoishi.util.Vector;

import javax.imageio.ImageIO;
import java.awt.Image;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.Calendar;

import static java.lang.StrictMath.abs;
import static java.lang.StrictMath.max;
import static java.lang.System.gc;

/**
 * @author KKoishi_
 */
public class Functions implements GifApi {
    private final int createFrom;
    private final File[] srcFiles;
    private final String workspace;
    private BufferedImage[] images;
    private final int amount;
    private final int outputWidth;
    private final int outputHeight;
    private int delay = 100;

    private Functions (int createFrom, File[] srcFiles, String workspace, int outputWidth, int outputHeight) {
        this.createFrom = createFrom;
        this.srcFiles = srcFiles;
        this.workspace = workspace;
        amount = srcFiles.length;
        images = new BufferedImage[amount];
        this.outputHeight = outputHeight;
        this.outputWidth = outputWidth;
    }

    public static GifApi buildFromPic (File[] pngFiles, String workspace, int outputWidth, int outputHeight)
            throws CompilerSyntaxErrorException {
        checkType(pngFiles, CREATE_FROM_PNG);
        return new Functions(CREATE_FROM_PNG, pngFiles, workspace, outputWidth, outputHeight);
    }

    public static GifApi buildFromGraph (File[] graphFiles, String workspace, int outputWidth, int outputHeight)
            throws CompilerSyntaxErrorException {
        checkType(graphFiles, CREATE_FROM_KGRAPH);
        return new Functions(CREATE_FROM_KGRAPH, graphFiles, workspace, outputWidth, outputHeight);
    }

    public static GifApi buildFromScript (File[] scriptFiles, String workspace, int outputWidth, int outputHeight)
            throws CompilerSyntaxErrorException {
        checkType(scriptFiles, CREATE_FROM_KSCRIPT);
        return new Functions(CREATE_FROM_KSCRIPT, scriptFiles, workspace, outputWidth, outputHeight);
    }

    @Override
    public void setDelay (int ms) {
        delay = ms;
    }

    private static void checkType (File[] srcFiles, int createFrom)
            throws CompilerSyntaxErrorException {
        Vector<String> extensions = new Vector<>();
        switch (createFrom) {
            case CREATE_FROM_PNG: {
                extensions.add("png");
                break;
            }
            case CREATE_FROM_KGRAPH: {
                extensions.add("kgraph");
                extensions.add("kGraph");
                break;
            }
            case CREATE_FROM_KSCRIPT: {
                extensions.add("kScript");
                extensions.add("kscript");
                break;
            }
            default: {
                throw new CompilerSyntaxErrorException("Unsupported File Type");
            }
        }
        for (File srcFile : srcFiles) {
            String extension = srcFile.getName();
            if (extension.matches(".+\\.[^.]+$")) {
                String[] as = extension.split("\\.");
                extension = as[as.length - 1];
                System.out.println("Event:Separate extension->" + extension);
                System.out.println("Export type:" + createFrom);
                if (!extensions.contains(extension)) {
                    throw new CompilerSyntaxErrorException("Unsupported File Type");
                }
            } else {
                throw new CompilerSyntaxErrorException("Unsupported File Type");
            }
        }
        gc();
    }

    @Override
    public File generate () {
        return generate(true);
    }

    @Override
    public File generate (boolean compressed) {
        if (createFrom == CREATE_FROM_PNG) {
            return generate(images, compressed);
        } else {
            try {
                prepare();
                return generate(images, compressed);
            } catch (CompilerSyntaxErrorException | IOException e) {
                Logger.Builder.set("./log");
                Logger logger = Logger.Builder.build();
                logger.log(LogType.EXCEPTION, e.getLocalizedMessage());
                return null;
            }
        }
    }

    private File generate (BufferedImage[] images, boolean compressed) {
        int len = images.length;
        if (compressed) {
            for (int i = 0; i < len ;i++) {
                images[i] = compressPic(images[i]);
            }
        }
        File exportFile = new File(workspace + "/" + getTime() + "GifFile_byKKoishi_.gif");
        System.out.println("Event:create gif file[" + exportFile + "]");
        AnimatedGifEncoder encoder = new AnimatedGifEncoder();
        encoder.setRepeat(0);
        try {
            encoder.start(new FileOutputStream(exportFile));
            int count = 1;
            int f = delay / 50;
            for (BufferedImage image : images) {
                if ((++count) % f == 0) {
                    encoder.setDelay(50 * f);
                    encoder.addFrame(image);
                }
            }
            encoder.finish();
            System.out.println("Event:Finish export");
            return exportFile;
        } catch (FileNotFoundException e) {
            e.printStackTrace();
            System.out.println("Error:IOError");
            return null;
        }
    }

    /**
     * Get time for log.
     *
     * @return time, like [2021/12/23 8:24:59]
     */
    protected String getTime () {
        Calendar calendar = Calendar.getInstance();
        return "[" + calendar.get(Calendar.YEAR)
                + calendar.get(Calendar.MONTH)
                + calendar.get(Calendar.DAY_OF_MONTH)
                + calendar.get(Calendar.HOUR_OF_DAY)
                + calendar.get(Calendar.MINUTE)
                + calendar.get(Calendar.SECOND) + "]";
    }

    private void prepare () throws CompilerSyntaxErrorException, IOException {
        int i = 0;
        if (createFrom == CREATE_FROM_KGRAPH) {
            for (File srcFile : srcFiles) {
                GraphCompiler compiler = new GraphCompiler(workspace, srcFile);
                compiler.read();
                compiler.compileImpl();
                String path = compiler.getPath();
                ScriptExplainer explainer = new ScriptExplainer(new File(path));
                explainer.read();
                explainer.execute();
                images[i++] = explainer.get();
            }
        } else {
            for (File srcFile : srcFiles) {
                ScriptExplainer explainer = new ScriptExplainer(srcFile);
                explainer.read();
                explainer.executeImpl();
                images[i++] = explainer.get();
            }
        }
    }

    private BufferedImage[] parseCompressed(File[] files) {
        BufferedImage[] bi = new BufferedImage[files.length];
        try {
            for (int index = 0; index < files.length; index++) {
                bi[index] = compressPic(ImageIO.read(files[index]));
            }
        } catch (IOException e) {
            System.out.println("fail to parse template");
            throw new RuntimeException("fail to parse template", e);
        }
        return bi;
    }

    private boolean isProportion (BufferedImage image) {
        return abs(((double) image.getHeight() / (double) outputHeight)
                - ((double) image.getWidth() / (double) outputWidth)) < 0.01;
    }

    public BufferedImage compressPic(BufferedImage img) {
        BufferedImage imgCompressed = null;
        try {
            if (img.getWidth(null) == -1) {
                System.out.println("ErrorType");
                return null;
            } else {
                int newWidth;
                int newHeight;
                if (isProportion(img)) {
                    double rate1 = ((double) img.getWidth(null))
                            / (double) outputWidth;
                    double rate2 = ((double) img.getHeight(null))
                            / (double) outputHeight;
                    double rate = max(rate1, rate2);
                    newWidth = (int) (((double) img.getWidth(null)) / rate);
                    newHeight = (int) (((double) img.getHeight(null)) / rate);
                } else {
                    newWidth = outputWidth;
                    newHeight = outputHeight;
                }
                imgCompressed = new BufferedImage(newWidth, newHeight, BufferedImage.TYPE_INT_RGB);
                imgCompressed.getGraphics().drawImage(img.getScaledInstance(newWidth, newHeight, Image.SCALE_SMOOTH)
                        , 0, 0, null);
            }
        } catch (Exception ex) {
            ex.printStackTrace();
        }
        return imgCompressed;
    }
}