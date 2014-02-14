package com.epicsell.utils;

import org.apache.log4j.Logger;

import java.io.*;
import java.net.MalformedURLException;
import java.net.URL;

/**
 * Created with IntelliJ IDEA.
 * User: Jane T.
 * Date: 09.02.13
 */
public class TestUtils {
    private static final Logger logger = Logger.getLogger(TestUtils.class);

    public static String getProjectLocation() {
        return new File("").getAbsolutePath();
    }

    public static File downloadFileFromUrl(String fileUrl, String fileLocation) {
        try {
            File file = new File(fileLocation);
            if (file.exists()) {
                if (file.delete()) {
                    logger.error("can't delete file " + fileLocation);
                }
            }
            URL url = new URL(fileUrl);
            url.openConnection();
            InputStream reader = url.openStream();
            FileOutputStream writer = new FileOutputStream(fileLocation);
            byte[] buffer = new byte[153600];
            int bytesRead;
            while ((bytesRead = reader.read(buffer)) > 0) {
                writer.write(buffer, 0, bytesRead);
                buffer = new byte[153600];
            }
            writer.close();
            reader.close();
        } catch (MalformedURLException mue) {
            logger.error(mue);
        } catch (IOException ioe) {
            logger.error(ioe);
        }
        return new File(fileLocation);
    }

    public static String readResourceFileContent(String filename) throws Exception {
        BufferedReader br = new BufferedReader(new FileReader(new File("").getAbsolutePath() + File.separator + "files" +
                File.separator + "resources" + File.separator + filename));
        StringBuilder sb;
        try {
            sb = new StringBuilder();
            String line = br.readLine();
            while (line != null) {
                sb.append(line);
                line = br.readLine();
            }
        } finally {
            br.close();
        }
        return sb.toString();
    }
}
