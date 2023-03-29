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
        text.replace(0, previousLength, formatted, 0, currentLength);

        // Update cursor
        if (currentLength < previousLength) {
            editText.setSelection(findCursorPosition(text, editText.getSelectionStart()));
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

    private int findCursorPosition(@NotNull final Editable text, final int start) {
        if (text.length() == 0) return start;
        int position = start;
        for (int i = start; i < mask.mask.length(); i++) {
            if (mask.mask.charAt(i) == mask.maskCharacter) {
                break;
            }
            position++;
        }
        position++;
        return Math.min(position, text.length());
    }

}
