package br.dev.dig.text.mask;

import android.text.Editable;
import android.text.TextWatcher;
import android.widget.EditText;

import org.jetbrains.annotations.NotNull;

public final class TextMaskTextWatcher implements TextWatcher {

    @NotNull
    private final EditText editText;
    @NotNull
    private final TextMask mask;

    public TextMaskTextWatcher(@NotNull final EditText editText, @NotNull final TextMask mask) {
        //noinspection ConstantConditions
        if (editText == null) throw new IllegalArgumentException("EditText can not be null");
        //noinspection ConstantConditions
        if (mask == null) throw new IllegalArgumentException("TextMask can not be null");
        this.editText = editText;
        this.mask = mask;
    }

    @Override
    public void beforeTextChanged(CharSequence s, int start, int count, int after) {
    }

    @Override
    public void onTextChanged(CharSequence s, int start, int before, int count) {
    }

    @Override
    public void afterTextChanged(Editable s) {

    }

}
