package org.intellij.lang.jflex.compiler;

import java.text.MessageFormat;

/**
 * JFlex message.
 *
 * @author Alexey Efimov
 */
public final class JFlexMessage {
    private final int line;
    private final int column;
    private final String message;

    public JFlexMessage(String message, int line, int column) {
        this.line = line;
        this.column = column;
        this.message = message;
    }

    public JFlexMessage(String message) {
        this(message, -1, -1);
    }

    public int getLine() {
        return line;
    }

    public int getColumn() {
        return column;
    }

    public String getMessage() {
        return message;
    }

    public String toString() {
        return line >= 0 ? column >= 0 ? MessageFormat.format("{0} ({1}:{2})", message, line, column) : MessageFormat.format("{0} ({1})", message, line) : message;
    }
}
