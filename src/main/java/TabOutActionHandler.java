import com.intellij.openapi.actionSystem.*;
import com.intellij.openapi.editor.Caret;
import com.intellij.openapi.editor.Editor;
import com.intellij.openapi.editor.actionSystem.EditorWriteActionHandler;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.Arrays;

/**
 * Class for overriding {@code EditorTab} actions to provide Eclipse-style tab-out feature.
 *
 * @author Nick Marino
 */
public class TabOutActionHandler extends EditorWriteActionHandler {
    /**
     * Array of characters to tab-over.
     */
    private static final Character[] TAB_OVER_TARGETS = new Character[] {
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
        Caret editorCaret = editor.getCaretModel().getCurrentCaret();
        int caretOffset = editorCaret.getOffset();
        CharSequence editorText = editor.getDocument().getCharsSequence();
        char nextCharacter = editorText.charAt(caretOffset);

        // If the character at the caret is one of the TAB_OVER_TARGETS, move the caret to the next
        // column; else, delegate the call to the original EditorWriteActionHandler got restore
        // regular tab key functionality.
        if (isTabOverTarget(nextCharacter)) {
            moveCaret(editorCaret, caretOffset + 1);
        } else {
            originalEditorWriteActionHandler.doExecute(editor, caret, dataContext);
        }
    }

    /**
     * Determines if {@code character} is a tab-over target.
     *
     * @param character the character to evaluate.
     * @return {@code true} if {@code character} is a tab-over target; {@code false} if it is not.
     */
    private static boolean isTabOverTarget(char character) {
        return Arrays.asList(TAB_OVER_TARGETS).contains(character);

    }

    /**
     * Moves the {@code currentCaret} to the specified {@code offset}.
     *
     * @param currentCaret the current {@link Caret}.
     * @param offset the location to which to move the {@code currentCaret}.
     */
    private static void moveCaret(Caret currentCaret, int offset) {
        currentCaret.moveToOffset(offset);
    }
}