package org.intellij.lang.jflex;

import com.intellij.psi.tree.IElementType;
import org.intellij.lang.jflex.fileTypes.JFlexFileTypeManager;
import org.jetbrains.annotations.NonNls;
import org.jetbrains.annotations.NotNull;

import java.text.MessageFormat;

public class JFlexElementType extends IElementType {
    private final IElementType parsedType;

    public JFlexElementType(@NotNull @NonNls String debugName, IElementType parsedType) {
        super(debugName, JFlexFileTypeManager.getInstance().getFileType().getLanguage());
        this.parsedType = parsedType;
    }

    public JFlexElementType(@NotNull @NonNls String debugName) {
        this(debugName, null);
    }

    public IElementType getParsedType() {
        return parsedType != null ? parsedType : this;
    }

    @SuppressWarnings({"HardCodedStringLiteral"})
    public String toString() {
      return MessageFormat.format("JFlex:{0}", super.toString());
    }
}
