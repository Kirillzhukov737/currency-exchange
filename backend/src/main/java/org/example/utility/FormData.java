package org.example.utility;

import jakarta.servlet.http.HttpServletRequest;

import java.io.BufferedReader;
import java.io.IOException;
import java.net.URLDecoder;
import java.nio.charset.StandardCharsets;
import java.util.HashMap;
import java.util.Map;

public final class FormData {

    private FormData() {
    }

    public static Map<String, String> readBodyForm(HttpServletRequest req) throws IOException {
        StringBuilder sb = new StringBuilder();

        try (BufferedReader r = req.getReader()) {
            String line;
            while ((line = r.readLine()) != null) {
                sb.append(line);
            }
        }

        String body = sb.toString().trim();
        Map<String, String> map = new HashMap<>();
        if (body.isEmpty()) return map;

        for (String pair : body.split("&")) {
            int idx = pair.indexOf('=');
            if (idx < 0) continue;

            String key = URLDecoder.decode(pair.substring(0, idx), StandardCharsets.UTF_8);
            String val = URLDecoder.decode(pair.substring(idx + 1), StandardCharsets.UTF_8);

            if (key != null && !key.isBlank()) {
                map.put(key, val);
            }
        }

        return map;
    }
}
