package com.volvo.phoenix.orion.util;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

import javax.activation.FileDataSource;

/**
 * <p>
 * FileDataSource for caching all InputStreams that are produced. It allows
 * tracking, how many InputStreams are created and gives possibility to close
 * them in case they were not closed.
 * </p>
 * 
 * <strong>WARNING: </strong>OrionAPI checks the name of DataSource it is using
 * and is doing file separator character replacements depending on OS it is
 * running on. There is an <code>if</code> statement that checks the name of the
 * datasource class and if this class name ends with FileDataSource, it performs
 * the replacement. Hence, don't change name of this class so the OrionAPI code
 * won't go into this <code>if</code>.
 * 
 * @author a042076
 *
 */
public class StreamCachingFileCustomDataSource extends FileDataSource {

    private final List<InputStream> inputStreams = new ArrayList<InputStream>();

    public StreamCachingFileCustomDataSource(File file) {
        super(file);
    }

    public StreamCachingFileCustomDataSource(String name) {
        super(name);
    }

    /**
     * @see javax.activation.FileDataSource#getInputStream()
     */
    @Override
    public InputStream getInputStream() throws IOException {
        InputStream inputStream = super.getInputStream();
        this.inputStreams.add(inputStream);
        return inputStream;
    }

    /**
     * Closes all cached streams.
     * 
     * @return true if all streams have been closed succesfully, false otherwise
     */
    public boolean closeStreams() {
        boolean resultOk = true;
        for (InputStream inputStream : this.inputStreams) {
            try {
                inputStream.close();
            } catch (IOException e) {
                resultOk = false;
            }
        }
        return resultOk;
    }

}
