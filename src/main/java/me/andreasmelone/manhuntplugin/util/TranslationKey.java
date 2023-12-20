package me.andreasmelone.manhuntplugin.util;

public interface TranslationKey {
    String key();
    String value();

    static TranslationKey of(String key, String value) {
        return new TranslationKey() {
            @Override
            public String key() {
                return key;
            }

            @Override
            public String value() {
                return value;
            }
        };
    }
}
