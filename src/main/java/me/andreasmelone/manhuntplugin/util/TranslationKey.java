package me.andreasmelone.manhuntplugin.util;

public interface TranslationKey {
    String key();
    String value();

    static <T> TranslationKey of(String key, T value) {
        return new TranslationKey() {
            @Override
            public String key() {
                return key;
            }

            @Override
            public String value() {
                return String.valueOf(value);
            }
        };
    }
}
