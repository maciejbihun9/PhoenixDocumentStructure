package com.volvo.phoenix.orion.util;


import java.util.Vector;
/**
* Represents a document type in Orion.
* Holds five attributes for a specific documenttype.
* <UL>
*  <LI>tdmType - Orion ALIAS_TYPE. For example PDF.
*  <LI>mimeType - Mime type. For example application/pdf.
*  <LI>extension - A collection of extensions for this documenttype. For exampls "pdf" or "htm" and "html".
*  <LI>image - A file name that could be used to construct an URL that points to a thumbnail image to represent the document type. For example "pdf.gif"
*  <LI>Description - A String that describes this document type.
* </UL>
* This class is also used by @see DocumentTypes
*
* @author      Stefan Skagert, Xdin AB
* @version     Rel. 0.81
*/

public class DocumentType {
  private String tdmType = null;
  private String mimeType = null;
  private Vector<String> extensions = new Vector<String>();
  private String image = null;
  private String description = null;

  /**
   * Default constructor.
   */
  public DocumentType() {
  }

  /**
   * @param       tdmType         The name of the Orion document type (JPEG etc.)
   * @param       extension       File name extenstion (jpg etc.)
   * @param       mimeType        The mime type for this document type.  (image/jpeg etc.)
   * @param       image           The file name of an image that represents this file type in a browser.
   * @param       description     A description of this file type. Could for example be used as description of a document in a web page.
   *
   */
  public DocumentType(
      String tdmType,
      String mimeType,
      Vector<String> extensions,
      String image,
      String description) {
      this.tdmType = tdmType;
      this.mimeType = mimeType;
      this.extensions = extensions;
      this.image = image;
      this.description = description;
  }

  /**
   * @param       tdmType         The name of the Orion document type (JPEG etc.)
   * @param       extension       File name extenstion (jpg etc.)
   * @param       mimeType        The mime type for this document type.  (image/jpeg etc.)
   * @param       image           The file name of an image that represents this file type in a browser.
   */
  public DocumentType(
      String tdmType,
      String mimeType,
      Vector<String> extensions,
      String image) {
      this(tdmType, mimeType, extensions, image, null);
  }

  /**
  * Adds an extension.
   * @param       extension       Document type file extension.
   */
  public void addExtension(String extension) {
      this.extensions.addElement(extension);
  }
  /**
   * Sets all extensions for this document type.
       * @param       extensions Vector filled with extensions.
       */
  public void setExtensions(Vector<String> extensions) {
      this.extensions = extensions;
  }
  /**
  * Sets the Orion ALIAS_TYPE.
   * @param       tdmType     Document type tdm type.
   */
  public void setTdmType(String tdmType) {
      this.tdmType = tdmType;
  }
  /**
  * Sets the mimetype.
   * @param       mimeType        Document type mime type.
   */
  public void setMimeType(String mimeType) {
      this.mimeType = mimeType;
  }

  /**
  * Sets the image filename.
   * @param       image       Document type image name.
   */
  public void setImage(String image) {
      this.image = image;
  }

  /**
  * Sets the description.
   * @param       description     Document type description.
   */
  public void setDescription(String description) {
      this.description = description;
  }

  /**
  * Returns the extensions in a Vector.
   * @return      Document type file extensions.
   */
  public Vector<String> getExtensions() {
      return extensions;
  }

  /**
  * Returns the Orion ALIAS_TYPE as a String.
   * @return      Document type file Orion ALIAS_TYPE type.
   */
  public String getTdmType() {
      return tdmType;
  }

  /**
  * Returns the mimetype.
   * @return  Docuement type mime type.
   */
  public String getMimeType() {
      return mimeType;
  }

  /**
  * Returns the image filename
   * @return  Document type image filename.
   */
  public String getImage() {
      return image;
  }

  /**
   * @return      Document type description.
   */
  public String getDescription() {
      return description;
  }

  public String toString() {
      StringBuffer extensionsStrb = new StringBuffer();

      extensionsStrb.append((String) extensions.firstElement());
      for (int i = 1; i < extensions.size(); i++) {
          extensionsStrb.append("," + extensions.elementAt(i));
      }

      return "Extensions:"
          + extensionsStrb.toString()
          + "\nTdmType:"
          + tdmType
          + "\nMimeType:"
          + mimeType
          + "\nImage:"
          + image
          + "\nDescription:"
          + description
          + "\n";
  }
}
