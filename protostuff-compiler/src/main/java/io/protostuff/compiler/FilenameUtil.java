package io.protostuff.compiler;

/**
 * Utility methods for manipulations with file names
 *
 * @author Konstantin Shchepanovskyi
 */
public class FilenameUtil
{
    // path delimiters
    public static final char WINDOWS_DELIMITER = '\\';
    public static final char LINUX_DELIMITER = '/';

    /**
     * Returns file name by given absolute or relative file location.
     *
     * @param fullPath
     *            file location
     * @return file name
     * @since 1.3.1
     */
    public static String getFileName(String fullPath)
    {
        if (fullPath == null)
        {
            return null;
        }
        int winDelimiterPos = fullPath.lastIndexOf(WINDOWS_DELIMITER);
        int linDelimiterPos = fullPath.lastIndexOf(LINUX_DELIMITER);
        int pos = Math.max(winDelimiterPos, linDelimiterPos);
        return fullPath.substring(pos + 1, fullPath.length());
    }
}
