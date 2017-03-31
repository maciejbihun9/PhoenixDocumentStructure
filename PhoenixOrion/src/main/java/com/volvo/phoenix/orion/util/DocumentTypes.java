package com.volvo.phoenix.orion.util;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.StreamTokenizer;
import java.util.Enumeration;
import java.util.Hashtable;
import java.util.StringTokenizer;
import java.util.Vector;

/**
 * A container for Orion document types for a specific vault. This is a singleton that holds instances of DocumentType for the Orion vault. Reads and parses a
 * document type file specified in the constructor.
 */
public class DocumentTypes {

    private static final int TDMTYPE = 0;
    private static final int MIMETYPE = 1;
    private static final int EXTENSION = 2;
    private static final int IMAGE = 3;
    private static final int DESCRIPTION = 4;

    private static DocumentTypes instance = null;
    private Hashtable<Object, DocumentType> extensionsHash = new Hashtable<Object, DocumentType>();

    /**
     * Returns the one and only instance. This method should be used only after getInstance(String fileName) has been called.
     */
    public static DocumentTypes getInstance() {
        if (instance == null) {
            try {
                instance = new DocumentTypes("orionservlet.types");
            } catch (FileNotFoundException e) {
                throw new RuntimeException("Error while loading orion types", e);
            } catch (IOException e) {
                throw new RuntimeException("Error while loading orion types", e);
            }
        }
        return instance;
    }

    private DocumentTypes(String fileName) throws IOException {
        parseFile(fileName, extensionsHash);
    }

    public DocumentType getDocumentTypeByExtension(String extension) {
        return (DocumentType) extensionsHash.get(extension.toLowerCase());
    }

    private static void parseFile(String fileName, Hashtable<Object, DocumentType> extHash) throws IOException {
        InputStream in = Thread.currentThread().getContextClassLoader().getResourceAsStream(fileName);
        if (in == null) {
            throw new FileNotFoundException("File types file could not be found - " + fileName);
        }

        StreamTokenizer t = new StreamTokenizer(new InputStreamReader(in));
        t.commentChar('#');
        t.eolIsSignificant(true);
        t.quoteChar('"');
        t.wordChars('/', '/');
        t.wordChars(',', ',');
        int ttype;
        int col = 0;
        DocumentType dt = null;

        while ((ttype = t.nextToken()) != StreamTokenizer.TT_EOF) {
            if (ttype == StreamTokenizer.TT_WORD || ttype == '"') {
                if (dt == null)
                    dt = new DocumentType();
                switch (col) {
                case TDMTYPE:
                    dt.setTdmType(t.sval);
                    break;
                case MIMETYPE:
                    dt.setMimeType(t.sval);
                    break;
                case EXTENSION:
                    dt.setExtensions(parseExtensions(t.sval.toLowerCase()));
                    break;
                case IMAGE:
                    dt.setImage(t.sval);
                    break;
                case DESCRIPTION:
                    dt.setDescription(t.sval);
                }
                col++;
            } else if (ttype == StreamTokenizer.TT_EOL) {
                if (dt != null) {
                    if (col > MIMETYPE) {
                        Enumeration<?> e = dt.getExtensions().elements();
                        while (e.hasMoreElements()) {
                            extHash.put(e.nextElement(), dt);
                        }
                    }
                }
                col = 0;
                dt = null;
            }
        }
    }

    private static Vector<String> parseExtensions(String extensionsStr) {
        Vector<String> extensions = new Vector<String>();
        StringTokenizer st = new StringTokenizer(extensionsStr, ",", false);
        while (st.hasMoreTokens()) {
            extensions.addElement(st.nextToken());
        }
        return extensions;
    }
}
