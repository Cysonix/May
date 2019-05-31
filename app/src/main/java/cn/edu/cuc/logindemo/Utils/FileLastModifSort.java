package cn.edu.cuc.logindemo.Utils;

import java.io.File;
import java.util.Comparator;

/**
 * Created by DELL on 2019/4/30.
 */

public class FileLastModifSort implements Comparator<File> {
    public int compare(File arg0, File arg1) {
        if (arg0.lastModified() > arg1.lastModified()) {
            return 1;
        } else if (arg0.lastModified() == arg1.lastModified()) {
            return 0;
        } else {
            return -1;
        }
    }
}
