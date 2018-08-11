import com.intellij.openapi.actionSystem.*;
import com.intellij.openapi.editor.Caret;
import com.intellij.openapi.editor.Editor;
import com.intellij.openapi.editor.actionSystem.EditorWriteActionHandler;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.Arrays;

/**
 * Class for overriding {@code EditorTab} actions to behave like Eclipse tab-out feature.
 *
 * @author Nick Marino
 */
public class TabOutActionHandler extends EditorWriteActionHandler {
    /**
     * Array of characters to tab-over.
     */
    private final Character[] TAB_OVER_TARGETS = new Character[] {
            '}', ')', '>', '"', ';'
    };

    /**
     * The original {@link EditorWriteActionHandler}.
     */
    private final EditorWriteActionHandler originalEditorWriteActionHandler;

    /**
     * Creates a {@link TabOutActionHandler}.
     *
     * @param originalEditorWriteActionHandler the original {@link EditorWriteActionHandler}.
     */
    public TabOutActionHandler(EditorWriteActionHandler originalEditorWriteActionHandler) {
        this.originalEditorWriteActionHandler = originalEditorWriteActionHandler;
    }

    @Override
    public void doExecute(@NotNull Editor editor, @Nullable Caret caret, DataContext dataContext) {
        CharSequence editorText = editor.getDocument().getCharsSequence();
        int caretOffset = editor.getCaretModel().getOffset();
        char nextCharacter = editorText.charAt(caretOffset);

        // If the character at the caret is one of our TAB_OVER_TARGETS, move the caret to the next
        // column; else, delegate the call to the original EditorWriteActionHandler.
        if (Arrays.asList(TAB_OVER_TARGETS).contains(nextCharacter)) {
            editor.getCaretModel().moveToOffset(caretOffset + 1);
        } else {
            originalEditorWriteActionHandler.doExecute(editor, caret, dataContext);
        }
    }
}