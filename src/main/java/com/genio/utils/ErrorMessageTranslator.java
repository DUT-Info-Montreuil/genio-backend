package com.genio.utils;

public class ErrorMessageTranslator {
    public static String translate(String field, String rawMessage) {
        if (rawMessage == null) return "Une erreur inconnue s'est produite pour le champ " + field + ".";

        return switch (rawMessage) {
            case "must not be null", "ne doit pas être nul" -> "Le champ '" + field + "' est obligatoire.";
            case "must not be blank" -> "Le champ '" + field + "' ne peut pas être vide.";
            case "must be a well-formed email address" -> "Le champ '" + field + "' doit être un email valide.";
            case "must match \"^\\d{4}-\\d{2}-\\d{2}$\"" -> "Le champ '" + field + "' doit être une date au format YYYY-MM-DD.";
            case "must match \"^\\d{2}\\.\\d{2}\\.\\d{2}\\.\\d{2}\\.\\d{2}$\"" -> "Le champ '" + field + "' doit être un numéro de téléphone au format XX.XX.XX.XX.XX.";
            case "must match \"^\\d+(\\.\\d{1,2})?€$\"" -> "Le champ '" + field + "' doit être un montant suivi de '€'.";
            case "must match \"^(H|F)$\"" -> "Le champ '" + field + "' doit être soit 'H' soit 'F'.";
            case "must match \"^\\d{4}$\"" -> "Le champ '" + field + "' doit être une année au format YYYY.";
            default -> "Le champ '" + field + "' est invalide : " + rawMessage;
        };
    }
}