package com.epam.alextuleninov.taxiservice.config.mail;

/**
 * Class for getting subject, body of letter depending on the locale.
 */
public class EmailByLocaleConfig {

    /**
     * Get text of letter depending on the locale.
     *
     * @param locale current locale
     * @return subject of letter depending on the locale
     */
    public String getTextByLocale(String locale, String textUk, String text) {
        if (locale.equals("uk_UA")) {
            return textUk;
        } else {
            return text;
        }
    }
}
