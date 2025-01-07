package com.genio.exception.technical;

/**
 * Exception levée lorsque la conversion d'un fichier échoue.
 * Par exemple, lors de la conversion d'un fichier DOCX en PDF.
 */
public class FileConversionException extends RuntimeException {
    public FileConversionException(String message) {
        super(message);
    }
}
