package org.orm.framework.ModelsMapper.Mappers;

import java.io.File;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

public class ModelsLoader {

    public static List<Class<?>> getModels(String pckgname) throws ClassNotFoundException {
        List<Class<?>> classes = new ArrayList<>();

        File directory = null;

        try {
            ClassLoader cld = Thread.currentThread().getContextClassLoader();
            if (cld == null) {
                throw new ClassNotFoundException("Can't get class loader.");
            }
            String path = pckgname.replace('.', '/');
            URL resource = cld.getResource(path);
            if (resource == null) {
                throw new ClassNotFoundException("No resource for " + path);
            }
            directory = new File(resource.getFile());
        }
        catch (NullPointerException x) {
            throw new ClassNotFoundException(pckgname + " (" + directory + ") does not appear to be a valid package");
        }
        if (directory.exists()) {
            // Get the list of the files contained in the package
            String[] files = directory.list();
            for (int i = 0; i < files.length; i++) {
                // we are only interested in .class files
                if (files[i].endsWith(".class")) {
                    // removes the .class extension
                    classes.add(Class.forName(pckgname + '.' + files[i].substring(0, files[i].length() - 6)));
                }
            }
        }
        else {
            throw new ClassNotFoundException(pckgname + " does not appear to be a valid package");
        }
        return classes;
    }
}
