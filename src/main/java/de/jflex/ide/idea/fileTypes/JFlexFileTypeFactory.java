package de.jflex.ide.idea.fileTypes;

import com.intellij.openapi.fileTypes.FileTypeConsumer;
import com.intellij.openapi.fileTypes.FileTypeFactory;
import org.jetbrains.annotations.NotNull;

/**
 * JFlex file type factory, tells IDEA about new file type
 *
 * @author Jan Dolecek
 */
public final class JFlexFileTypeFactory extends FileTypeFactory {
    @Override
    public void createFileTypes(@NotNull FileTypeConsumer consumer) {
        consumer.consume(JFlexFileType.FILE_TYPE, "flex;jflex");
    }
}
