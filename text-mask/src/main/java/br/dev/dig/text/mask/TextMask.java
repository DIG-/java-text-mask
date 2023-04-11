package br.dev.dig.text.mask;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public final class TextMask {

    @FunctionalInterface
    interface IsValidCharacter {
        boolean is(char ch);
    }

    @NotNull
    final CharSequence mask;
    final char maskCharacter;
    final boolean keepMask;
    @NotNull
    final IsValidCharacter validCharacter;

    public TextMask(@NotNull final CharSequence mask, final char maskCharacter, final boolean keepMask, @NotNull final IsValidCharacter validCharacter) {
        //noinspection ConstantConditions
        if (mask == null)
            throw new IllegalArgumentException("Mask can not be null");
        //noinspection ConstantConditions
        if (validCharacter == null)
            throw new IllegalArgumentException("IsValidCharacter can not be null");

        this.mask = mask;
        this.maskCharacter = maskCharacter;
        this.keepMask = keepMask;
        this.validCharacter = validCharacter;
    }

    public TextMask(@NotNull final CharSequence mask, final char maskCharacter, final boolean keepMask) {
        this(mask, maskCharacter, keepMask, Character::isLetterOrDigit);
    }

    public TextMask(@NotNull final CharSequence mask, final char maskCharacter) {
        this(mask, maskCharacter, false);
    }

    public TextMask(@NotNull final CharSequence mask) {
        this(mask, '#');
    }

    @NotNull
    public CharSequence format(@Nullable final CharSequence value) {
        if (value == null || value.length() == 0) {
            if (keepMask) {
                return mask;
            } else {
                return "";
            }
        }
        final StringBuilder builder = new StringBuilder(mask.length());

        int j = 0;
        for (int i = 0; i < mask.length(); i++) {
            final char m = mask.charAt(i);
            if (j >= value.length()) {
                if (keepMask) {
                    builder.append(m);
                    continue;
                }
                break;
            }
            char c = value.charAt(j);
            if (m == maskCharacter) {
                while (!validCharacter.is(c)) {
                    j++;
                    if (j >= value.length()) {
                        break;
                    }
                    c = value.charAt(j);
                }
                if (validCharacter.is(c)) {
                    builder.append(c);
                    j++;
                } else if (keepMask) {
                    builder.append(m);
                }
            } else {
                builder.append(m);
                if (m == c) {
                    j++;
                }
            }
        }

        return builder;
    }

    public boolean isComplete(@Nullable final CharSequence value) {
        if (value == null || value.length() == 0) {
            return false;
        }
        if (value.length() != mask.length()) {
            return false;
        }
        if (!keepMask) {
            return true;
        }
        for (int i = 0; i < mask.length(); i++) {
            if (mask.charAt(i) == maskCharacter) {
                if (!validCharacter.is(value.charAt(i))) {
                    return false;
                }
            } else {
                if (mask.charAt(i) != value.charAt(i)) {
                    return false;
                }
            }
        }
        return true;
    }

    @SuppressWarnings("SpellCheckingInspection")
    @NotNull
    public CharSequence unformat(@Nullable final CharSequence value) {
        if (value == null || value.length() == 0) {
            return "";
        }
        final StringBuilder builder = new StringBuilder(value.length());
        for (int i = 0; i < value.length(); i++) {
            final char c = value.charAt(i);
            if (i >= mask.length()) {
                if (validCharacter.is(c)) {
                    builder.append(c);
                }
                continue;
            }
            if (mask.charAt(i) != maskCharacter) {
                continue;
            }
            if (validCharacter.is(c)) {
                builder.append(c);
            }
        }
        return builder;
    }

    @Override
    @NotNull
    public String toString() {
        return "TextMask(" +
                "mask=" + mask +
                ", maskCharacter=" + maskCharacter +
                ", keepMask=" + keepMask +
                ", validCharacter=" + validCharacter +
                ')';
    }

}