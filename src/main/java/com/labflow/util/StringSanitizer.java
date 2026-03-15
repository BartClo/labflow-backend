package com.labflow.util;

import java.text.Normalizer;

public final class StringSanitizer {

    private StringSanitizer() {
    }

    public static String normalizarParaComparar(String input) {
        if (input == null || input.trim().isEmpty()) {
            return "";
        }

        String limpio = input.replaceAll("[\\r\\n]+", " ");
        limpio = limpio.replaceAll("\\t+", " ");
        limpio = limpio.trim().replaceAll(" +", " ");

        String sinTildes = Normalizer.normalize(limpio, Normalizer.Form.NFD)
                .replaceAll("\\p{InCombiningDiacriticalMarks}+", "");

        return sinTildes.toUpperCase();
    }

    public static String limpiarParaFrontend(String input) {
        if (input == null) {
            return "";
        }
        return input.replaceAll("[\\r\\n\\t]+", " ")
                .trim()
                .replaceAll(" +", " ");
    }
}