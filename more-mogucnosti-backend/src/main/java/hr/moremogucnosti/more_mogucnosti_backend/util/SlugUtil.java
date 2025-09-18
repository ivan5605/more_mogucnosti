package hr.moremogucnosti.more_mogucnosti_backend.util;

import org.springframework.stereotype.Component;

import java.text.Normalizer;
import java.util.Locale;
import java.util.regex.Pattern;

@Component
public final class SlugUtil {

    private static final Pattern RAZMAK = Pattern.compile("\\s+");
    private static final Pattern DIJAKRTICI = Pattern.compile("\\p{M}+");

    public static String slugify(String string) {
        if (string == null) return "";

        String s = string.trim();
        s = RAZMAK.matcher(s).replaceAll(" ");
        s = s.toLowerCase(Locale.ROOT);
        s = Normalizer.normalize(s, Normalizer.Form.NFD);
        s = DIJAKRTICI.matcher(s).replaceAll("");
        return s;
    }
}
