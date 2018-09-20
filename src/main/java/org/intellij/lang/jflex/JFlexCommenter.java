package org.intellij.lang.jflex;

import com.intellij.lang.Commenter;
import org.jetbrains.annotations.Nullable;

/**
 * Created by IntelliJ IDEA.
 * User: Max
 * Date: 01.04.2008
 * Time: 23:39:04
 */
public class JFlexCommenter implements Commenter {
    @Nullable
    public String getBlockCommentPrefix() {
        return null;
    }

    @Nullable
    public String getBlockCommentSuffix() {
        return null;
    }

    @Nullable
    public String getLineCommentPrefix() {
        return "//";
    }

    public String getCommentedBlockCommentSuffix() {
        return null;
    }

    public String getCommentedBlockCommentPrefix() {
        return null;
    }
}
