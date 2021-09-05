package fr.bananasmoothii.bulkymltranslator;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.util.List;
import java.util.Objects;

public class TranslationNode {
    public @NotNull File file;
    public @NotNull List<Object> nodeIndexes;
    public @NotNull String untranslated;

    public @Nullable String translated;

    public TranslationNode() {
    }

    public TranslationNode(@NotNull File file, @NotNull List<Object> nodeIndexes, @NotNull String untranslated) {
        if (!file.isFile()) throw new IllegalStateException("Not an existing file");
        this.file = file;
        this.nodeIndexes = nodeIndexes;
        this.untranslated = Objects.requireNonNull(untranslated);
    }


    public static String translate(Language from, Language to, String text) {
        try {
            String urlStr = "https://script.google.com/macros/s/AKfycbyB0lFWhNPcNjvlTXTW6Tl730TMGBZH-qXLb7sSafAyfCj8VHENbz7x_iM3II17YJkcrw/exec" +
                    "?q=" + URLEncoder.encode(text, StandardCharsets.UTF_8) +
                    "&target=" + to.code +
                    "&source=" + from.code;
            URL url = new URL(urlStr);
            StringBuilder response = new StringBuilder();
            HttpURLConnection con = (HttpURLConnection) url.openConnection();
            con.setRequestProperty("User-Agent", "Mozilla/5.0");
            BufferedReader in = new BufferedReader(new InputStreamReader(con.getInputStream()));
            String inputLine;
            while ((inputLine = in.readLine()) != null) {
                response.append(inputLine);
            }
            in.close();
            return response.toString();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return "<error>";
    }

    @SuppressWarnings("unused")
    public enum Language {
        AFRIKAANS("Afrikaans", "af"),
        ALBANIAN("Albanian", "sq"),
        AMHARIC("Amharic", "am"),
        ARABIC("Arabic", "ar"),
        ARMENIAN("Armenian", "hy"),
        AZERBAIJANI("Azerbaijani", "az"),
        BASQUE("Basque", "eu"),
        BELARUSIAN("Belarusian", "be"),
        BENGALI("Bengali", "bn"),
        BOSNIAN("Bosnian", "bs"),
        BULGARIAN("Bulgarian", "bg"),
        CATALAN("Catalan", "ca"),
        CEBUANO("Cebuano", "ceb"),
        CHINESE_SIMPLIFIED("Chinese (Simplified)", "zh-CN"),
        CHINESE_TRADITIONAL("Chinese (Traditional)", "zh-TW"),
        CORSICAN("Corsican", "co"),
        CROATIAN("Croatian", "hr"),
        CZECH("Czech", "cs"),
        DANISH("Danish", "da"),
        DUTCH("Dutch", "nl"),
        ENGLISH("English", "en"),
        ESPERANTO("Esperanto", "eo"),
        ESTONIAN("Estonian", "et"),
        FINNISH("Finnish", "fi"),
        FRENCH("French", "fr"),
        FRISIAN("Frisian", "fy"),
        GALICIAN("Galician", "gl"),
        GEORGIAN("Georgian", "ka"),
        GERMAN("German", "de"),
        GREEK("Greek", "el"),
        GUJARATI("Gujarati", "gu"),
        HAITIAN("Haitian Creole", "ht"),
        HAUSA("Hausa", "ha"),
        HAWAIIAN("Hawaiian", "haw"),
        HEBREW("Hebrew", "he"),
        HINDI("Hindi", "hi"),
        HMONG("Hmong", "hmn"),
        HUNGARIAN("Hungarian", "hu"),
        ICELANDIC("Icelandic", "is"),
        IGBO("Igbo", "ig"),
        INDONESIAN("Indonesian", "id"),
        IRISH("Irish", "ga"),
        ITALIAN("Italian", "it"),
        JAPANESE("Japanese", "ja"),
        JAVANESE("Javanese", "jv"),
        KANNADA("Kannada", "kn"),
        KAZAKH("Kazakh", "kk"),
        KHMER("Khmer", "km"),
        KINYARWANDA("Kinyarwanda", "rw"),
        KOREAN("Korean", "ko"),
        KURDISH("Kurdish", "ku"),
        KYRGYZ("Kyrgyz", "ky"),
        LAO("Lao", "lo"),
        LATVIAN("Latvian", "lv"),
        LITHUANIAN("Lithuanian", "lt"),
        LUXEMBOURGISH("Luxembourgish", "lb"),
        MACEDONIAN("Macedonian", "mk"),
        MALAGASY("Malagasy", "mg"),
        MALAY("Malay", "ms"),
        MALAYALAM("Malayalam", "ml"),
        MALTESE("Maltese", "mt"),
        MAORI("Maori", "mi"),
        MARATHI("Marathi", "mr"),
        MONGOLIAN("Mongolian", "mn"),
        MYANMAR("Myanmar (Burmese)", "my"),
        NEPALI("Nepali", "ne"),
        NORWEGIAN("Norwegian", "no"),
        NYANJA("Nyanja (Chichewa)", "ny"),
        ODIA("Odia (Oriya)", "or"),
        PASHTO("Pashto", "ps"),
        PERSIAN("Persian", "fa"),
        POLISH("Polish", "pl"),
        PORTUGUESE("Portuguese (Portugal, Brazil)", "pt"),
        PUNJABI("Punjabi", "pa"),
        ROMANIAN("Romanian", "ro"),
        RUSSIAN("Russian", "ru"),
        SAMOAN("Samoan", "sm"),
        SCOTS("Scots Gaelic", "gd"),
        SERBIAN("Serbian", "sr"),
        SESOTHO("Sesotho", "st"),
        SHONA("Shona", "sn"),
        SINDHI("Sindhi", "sd"),
        SINHALA("Sinhala (Sinhalese)", "si"),
        SLOVAK("Slovak", "sk"),
        SLOVENIAN("Slovenian", "sl"),
        SOMALI("Somali", "so"),
        SPANISH("Spanish", "es"),
        SUNDANESE("Sundanese", "su"),
        SWAHILI("Swahili", "sw"),
        SWEDISH("Swedish", "sv"),
        TAGALOG("Tagalog (Filipino)", "tl"),
        TAJIK("Tajik", "tg"),
        TAMIL("Tamil", "ta"),
        TATAR("Tatar", "tt"),
        TELUGU("Telugu", "te"),
        THAI("Thai", "th"),
        TURKISH("Turkish", "tr"),
        TURKMEN("Turkmen", "tk"),
        UKRAINIAN("Ukrainian", "uk"),
        URDU("Urdu", "ur"),
        UYGHUR("Uyghur", "ug"),
        UZBEK("Uzbek", "uz"),
        VIETNAMESE("Vietnamese", "vi"),
        WELSH("Welsh", "cy"),
        XHOSA("Xhosa", "xh"),
        YIDDISH("Yiddish", "yi"),
        YORUBA("Yoruba", "yo"),
        ZULU("Zulu", "zu");

        public final String name;
        public final String code;

        Language(String name, String code) {
            this.name = name;
            this.code = code;
        }
    }
}
