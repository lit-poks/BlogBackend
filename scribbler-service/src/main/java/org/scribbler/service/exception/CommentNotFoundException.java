package org.scribbler.service.exception;

import java.io.PrintStream;
import java.io.PrintWriter;

/**
 * CommentNotFoundException is thrown when the comment is not found in the database.
 */

public class CommentNotFoundException extends Exception {

    private final String code;
    private final String errorMessage;

    public CommentNotFoundException(final String code, final String errorMessage) {
        this.code = code;
        this.errorMessage = errorMessage;
    }

    @Override
    public void printStackTrace() {
        super.printStackTrace();
    }

    @Override
    public void printStackTrace(PrintStream s) {
        super.printStackTrace(s);
    }

    @Override
    public void printStackTrace(PrintWriter s) {
        super.printStackTrace(s);
    }

    public String getCode() {
        return code;
    }

    public String getErrorMessage() {
        return errorMessage;
    }
}
