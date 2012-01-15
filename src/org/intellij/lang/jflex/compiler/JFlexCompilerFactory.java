package org.intellij.lang.jflex.compiler;

import com.intellij.openapi.compiler.*;
import com.intellij.openapi.compiler.Compiler;
import org.intellij.lang.jflex.fileTypes.JFlexFileType;
import org.jetbrains.annotations.NotNull;

/**
 * Creates a compiler for JFlex and attaches it to IDEA
 *
 * @author Jan Dolecek
 */
public class JFlexCompilerFactory implements CompilerFactory {
    @Override
    public Compiler[] createCompilers(@NotNull CompilerManager mgr) {
        mgr.addCompilableFileType(JFlexFileType.FILE_TYPE);
        mgr.addCompiler(new JFlexSourceGeneratingCompiler());
        return new Compiler[0];
    }
}
