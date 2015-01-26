package me.artspb.idea.pitest.plugin;

import java.io.File;

/**
 * @author Artem Khvastunov &lt;contact@artspb.me&gt;
 */
public final class Utils {

    private Utils() {
    }

    public static String concatenatePath(String parent, String child) {
        return new File(parent, child).getPath();
    }
}
