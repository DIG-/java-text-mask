package br.dev.dig.text.mask;

import android.text.Editable;
import android.text.InputFilter;
import android.text.TextWatcher;
import android.widget.EditText;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public final class TextMaskTextWatcher implements TextWatcher {

    @NotNull
    private final EditText editText;
    @NotNull
    private final TextMask mask;
    private boolean isChanging = false;

    public TextMaskTextWatcher(@NotNull final EditText editText, @NotNull final TextMask mask) {
        //noinspection ConstantConditions
        if (editText == null) throw new IllegalArgumentException("EditText can not be null");
        //noinspection ConstantConditions
        if (mask == null) throw new IllegalArgumentException("TextMask can not be null");
        this.editText = editText;
        this.mask = mask;
    }

    @Override
    public void beforeTextChanged(final CharSequence s, final int start, final int count, final int after) {
    }

    @Override
    public void onTextChanged(final CharSequence s, final int start, final int before, final int count) {
    }

    @Override
    public void afterTextChanged(@Nullable final Editable text) {
        if (text == null || text.length() == 0) {
            return;
        }
        if (isChanging) {
            return;
        }
        isChanging = true;
        // Reset InputFilters
        final InputFilter[] filters = text.getFilters();
        text.setFilters(new InputFilter[]{});

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

        isChanging = false;
    }

    @NotNull
    public TextMaskTextWatcher insert() {
        editText.addTextChangedListener(this);
        return this;
    }

    @NotNull
    public TextMaskTextWatcher update() {
        editText.setText(editText.getText());
        return this;
    }

    public boolean isComplete() {
        return mask.isComplete(editText.getText());
    }

    @NotNull
    public CharSequence unformat() {
        return mask.unformat(editText.getText());
    }

    @Override
    @NotNull
    public String toString() {
        return "TextMaskTextWatcher(" +
                "editText=" + editText +
                ", mask=" + mask +
                ')';
    }

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
