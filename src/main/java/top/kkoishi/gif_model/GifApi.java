package top.kkoishi.gif_model;

import java.io.File;

/**
 * @author KKoishi_
 */
public interface GifApi {
    int CREATE_FROM_PNG = 1 << 1;
    int CREATE_FROM_KGRAPH = 1 << 2;
    int CREATE_FROM_KSCRIPT = 1 << 3;

    File generate (boolean compressed);

    File generate ();

    void setDelay (int ms);
}
