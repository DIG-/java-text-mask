package br.dev.dig.text.mask;

import android.text.Editable;
import android.text.InputFilter;
import android.text.TextWatcher;
import android.widget.EditText;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public abstract class BaseMaskTextWatcher implements TextWatcher {

    @NotNull
    protected final EditText editText;
    @SuppressWarnings("NotNullFieldNotInitialized")
    @NotNull
    protected TextMask mask;
    private boolean isChanging = false;

    BaseMaskTextWatcher(@NotNull final EditText editText) {
        //noinspection ConstantConditions
        if (editText == null) throw new IllegalArgumentException("EditText must not be null");

        this.editText = editText;
    }

    @Override
    public void beforeTextChanged(final CharSequence s, final int start, final int count, final int after) {
    }

    @Override
    public void onTextChanged(final CharSequence s, final int start, final int before, final int count) {
    }

    @Override
    public void afterTextChanged(@Nullable final Editable text) {
        if (isChanging || text == null) return;
        isChanging = true;
        format(text);
        isChanging = false;
    }

    @SuppressWarnings("unused")
    public boolean isComplete() {
        return mask.isComplete(editText.getText());
    }

    @SuppressWarnings({"unused", "SpellCheckingInspection"})
    @NotNull
    public CharSequence unformat() {
        return mask.unformat(editText.getText());
    }

    private void format(@NotNull final Editable text) {
        // Reset InputFilters
        final InputFilter[] filters = text.getFilters();
        text.setFilters(new InputFilter[]{});

        // Remove current mask
        final CharSequence raw = mask.unformat(text);
        mask = getMaskFor(raw);

        // Apply mask
        final CharSequence formatted = mask.format(text);
        final int previousLength = text.length();
        final int currentLength = formatted.length();
        final CharSequence previous = text.subSequence(0, previousLength);
        final int previousSelection = editText.getSelectionStart();
        text.replace(0, previousLength, formatted, 0, currentLength);
        final int currentSelection = editText.getSelectionStart();

        // Update cursor
        if (currentSelection < previousSelection) {
            if (mask.keepMask && previousSelection > 0 && previousSelection < currentLength) {
                editText.setSelection(findCursorPosition(text, previousSelection) - 1);
            } else if (previousSelection <= currentLength) {
                editText.setSelection(previousSelection);
            }
        } else if (currentSelection == previousSelection) {
            if (currentSelection != 0) {
                int i = currentSelection - 1;
                if (mask.mask.charAt(i) != mask.maskCharacter && mask.mask.charAt(i) != previous.charAt(i)) {
                    editText.setSelection(findCursorPosition(text, i));
                }
            }
        } else {
            if (currentSelection >= mask.mask.length() && mask.keepMask) {
                int i = currentLength - 1;
                for (; i > 0; i--) {
                    if (mask.mask.charAt(i) != text.charAt(i)) {
                        break;
                    }
                }
                editText.setSelection(i + 1);
            } else {
                editText.setSelection(findCursorPosition(text, previousSelection));
            }
        }

        // Restore InputFilters
        text.setFilters(filters);
    }

    @NotNull
    protected abstract TextMask getMaskFor(@NotNull final CharSequence text);

    private int findCursorPosition(@NotNull final Editable text, final int start) {
        int i = start;
        for (; i < mask.mask.length(); i++) {
            if (i >= text.length()) {
                break;
            }
            if (mask.mask.charAt(i) == mask.maskCharacter) {
                i++;
                break;
            }
        }
        return i;
    }

}