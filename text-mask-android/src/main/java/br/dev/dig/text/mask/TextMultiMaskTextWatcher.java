package br.dev.dig.text.mask;

import android.util.Log;
import android.widget.EditText;

import org.jetbrains.annotations.NotNull;

import java.util.Arrays;

public class TextMultiMaskTextWatcher extends BaseMaskTextWatcher {

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
    private final Iterable<Option> options;

    public TextMultiMaskTextWatcher(@NotNull final EditText editText, @NotNull final Iterable<Option> options) {
        super(editText);
        //noinspection ConstantConditions
        if (options == null) throw new IllegalArgumentException("Options must not be null");
        int count = 0;
        for (final Option ignored : options) {
            count++;
        }
        if (count == 0) throw new IllegalArgumentException("Options must not be empty");
        else if (count == 1)
            Log.i("TextMultiMaskTextWatch", "Using MultiMask with only one TextMask");

        this.options = options;
        this.mask = getMaskFor("");
    }

    @SuppressWarnings("unused")
    public TextMultiMaskTextWatcher(@NotNull final EditText editText, @NotNull final Option... options) {
        this(editText, Arrays.asList(options));
    }

    public TextMultiMaskTextWatcher insert() {
        editText.addTextChangedListener(this);
        return this;
    }

    public TextMultiMaskTextWatcher update() {
        editText.setText(editText.getText());
        return this;
    }

    @NotNull
    protected TextMask getMaskFor(@NotNull final CharSequence text) {
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

}