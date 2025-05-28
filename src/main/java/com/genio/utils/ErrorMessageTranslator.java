package com.genio.utils;

public class ErrorMessageTranslator {

    private static final String CHAMP_PREFIX = "Le champ '";
    private static final String CHAMP_SUFFIX = "' ";

    private ErrorMessageTranslator() {
        throw new UnsupportedOperationException("Classe utilitaire - instanciation interdite");
    }

    public static String translate(String field, String rawMessage) {
        if (rawMessage == null)
            return "Une erreur inconnue s'est produite pour le champ " + field + ".";

        return switch (rawMessage) {
            case "must not be null", "ne doit pas être nul" ->
                    CHAMP_PREFIX + field + CHAMP_SUFFIX + "est obligatoire.";
            case "must not be blank" ->
                    CHAMP_PREFIX + field + CHAMP_SUFFIX + "ne peut pas être vide.";
            case "must be a well-formed email address" ->
                    CHAMP_PREFIX + field + CHAMP_SUFFIX + "doit être un email valide.";
            case "must match \"^\\d{4}-\\d{2}-\\d{2}$\"" ->
                    CHAMP_PREFIX + field + CHAMP_SUFFIX + "doit être une date au format YYYY-MM-DD.";
            case "must match \"^\\d{2}\\.\\d{2}\\.\\d{2}\\.\\d{2}\\.\\d{2}$\"" ->
                    CHAMP_PREFIX + field + CHAMP_SUFFIX + "doit être un numéro de téléphone au format XX.XX.XX.XX.XX.";
            case "must match \"^\\d+(\\.\\d{1,2})?€$\"" ->
                    CHAMP_PREFIX + field + CHAMP_SUFFIX + "doit être un montant suivi de '€'.";
            case "must match \"^(H|F)$\"" ->
                    CHAMP_PREFIX + field + CHAMP_SUFFIX + "doit être soit 'H' soit 'F'.";
            case "must match \"^\\d{4}$\"" ->
                    CHAMP_PREFIX + field + CHAMP_SUFFIX + "doit être une année au format YYYY.";
            default ->
                    CHAMP_PREFIX + field + "' est invalide : " + rawMessage;
        };
    }
}