package br.dev.dig.text.mask;

import android.widget.EditText;

import org.jetbrains.annotations.NotNull;

public final class TextMaskTextWatcher extends BaseMaskTextWatcher {

    public TextMaskTextWatcher(@NotNull final EditText editText, @NotNull final TextMask mask) {
        super(editText);
        //noinspection ConstantConditions
        if (mask == null) throw new IllegalArgumentException("TextMask can not be null");
        this.mask = mask;
    }

    @Override
    @NotNull
    protected TextMask getMaskFor(@NotNull final CharSequence text) {
        return mask;
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

    @Override
    @NotNull
    public String toString() {
        return "TextMaskTextWatcher(" +
                "editText=" + editText +
                ", mask=" + mask +
                ')';
    }

}
