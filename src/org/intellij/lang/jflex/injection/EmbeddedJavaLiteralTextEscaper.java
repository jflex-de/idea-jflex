package org.intellij.lang.jflex.injection;

import com.intellij.openapi.util.TextRange;
import com.intellij.psi.LiteralTextEscaper;
import org.intellij.lang.jflex.psi.JFlexJavaCode;
import org.jetbrains.annotations.NotNull;

/**
 * Created by IntelliJ IDEA.
 * User: Max
 * Date: 15.03.2008
 * Time: 21:01:02
 */
public class EmbeddedJavaLiteralTextEscaper extends LiteralTextEscaper<JFlexJavaCode> {

    public EmbeddedJavaLiteralTextEscaper(@NotNull JFlexJavaCode host) {
        super(host);
    }

    public boolean decode(@NotNull TextRange textrange, @NotNull StringBuilder stringbuilder) {
        stringbuilder.append(myHost.getText(), textrange.getStartOffset(), textrange.getEndOffset());
        return true;
    }

    public int getOffsetInHost(int i, @NotNull TextRange textrange) {
        int j = i + textrange.getStartOffset();
        if (j < textrange.getStartOffset())
            j = textrange.getStartOffset();
        if (j > textrange.getEndOffset())
            j = textrange.getEndOffset();
        return j;
    }

    public boolean isOneLine() {
        return false;
    }

}
