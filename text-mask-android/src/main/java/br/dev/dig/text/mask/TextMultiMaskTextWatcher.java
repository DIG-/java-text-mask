package br.dev.dig.text.mask;

import android.text.Editable;
import android.text.InputFilter;
import android.text.TextWatcher;
import android.util.Log;
import android.widget.EditText;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.Arrays;

public class TextMultiMaskTextWatcher implements TextWatcher {

    @FunctionalInterface
    public interface Condition {
        boolean is(@NotNull final CharSequence text);
    }

    public static class Option {
        @NotNull
        public final TextMask mask;
        @NotNull
        public final Condition condition;

        public Option(@NotNull final TextMask mask, @NotNull final Condition condition) {
            //noinspection ConstantConditions
            if (mask == null) throw new IllegalArgumentException("TextMask must not be null");
            //noinspection ConstantConditions
            if (condition == null) throw new IllegalArgumentException("Condition must not be null");

            this.mask = mask;
            this.condition = condition;
        }

        @Override
        @NotNull
        public String toString() {
            return "Option(" +
                    "mask=" + mask +
                    ", condition=" + condition +
                    ')';
        }
    }

    @NotNull
    private final EditText editText;
    @NotNull
    private final Iterable<Option> options;
    @NotNull
    private TextMask mask;
    private boolean isChanging = false;

    public TextMultiMaskTextWatcher(@NotNull final EditText editText, @NotNull final Iterable<Option> options) {
        //noinspection ConstantConditions
        if (editText == null) throw new IllegalArgumentException("EditText must not be null");
        //noinspection ConstantConditions
        if (options == null) throw new IllegalArgumentException("Options must not be null");
        int count = 0;
        for (final Option ignored : options) {
            count++;
        }
        if (count == 0) throw new IllegalArgumentException("Options must not be empty");
        else if (count == 1) Log.i("TextMultiMaskTextWatch", "Using MultiMask to only one Mask");

        this.editText = editText;
        this.options = options;
        this.mask = getMaskFor("");
    }

    public TextMultiMaskTextWatcher(@NotNull final EditText editText, @NotNull final Option... options) {
        this(editText, Arrays.asList(options));
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

    public TextMultiMaskTextWatcher insert() {
        editText.addTextChangedListener(this);
        return this;
    }

    public TextMultiMaskTextWatcher update() {
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
    private TextMask getMaskFor(@NotNull final CharSequence text) {
        for (final Option option : options) {
            if (option.condition.is(text)) {
                return option.mask;
            }
        }
        throw new IndexOutOfBoundsException("Can not found TextMask for \"" + text + "\"");
    }

    @Override
    @NotNull
    public String toString() {
        return "TextMultiMaskTextWatcher(" +
                "editText=" + editText +
                ", options=" + options +
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