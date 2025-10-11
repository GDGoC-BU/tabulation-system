package com.michaelcanonizado.backend.utilities;

import org.springframework.stereotype.Component;

import java.util.LinkedHashSet;
import java.util.Set;
import java.util.UUID;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/* Utility component to encode and decode formulas to be SpEL
   safe. SpEL variable evaluation mechanism uses these rules:
   1) Must start with "#"
   2) Must only begin with an alphabet
   3) Can only contain alphanumeric and underscores

   Therefore, we must transform the formula to be SpEL safe,
   inorder to properly parse it, and substitute the candidate's
   score for that criterion.

   Example:
       Raw Formula: "0.4 * 0f0f0f0f-1e1e-2d2d-3c3c-4b4b4b4b4b4b + 0.6 * 5a5a5a5a-6b6b-7c7c-8d8d-9e9e9e9e9e9e"
   Encoded Formula: "0.4 * #C0f0f0f0f_1e1e_2d2d_3c3c_4b4b4b4b4b4b + 0.6 * #C5a5a5a5a_6b6b_7c7c_8d8d_9e9e9e9e9e9e"
   Decoded Formula: "0.4 * 0f0f0f0f-1e1e-2d2d-3c3c-4b4b4b4b4b4b + 0.6 * 5a5a5a5a-6b6b-7c7c-8d8d-9e9e9e9e9e9e"

   NOTE: SpEL variables are case-sensitive and UUIDs aren't.
   So ensure that formula is uses the same casing for UUIDs.
   Frontend also now has to properly handle this.

   Example:
      Frontend sends formula: "0F0f0F0f-1e1E-2D2D-3c3c-4B4b4B4b4b4b / 0.5"
   Frontend receives formula: "0f0f0f0f-1e1e-2d2d-3c3c-4b4b4b4b4b4b / 0.5"
    Frontend simply can't do: "0F0f0F0f-1e1E-2D2D-3c3c-4B4b4B4b4b4b" === "0f0f0f0f-1e1e-2d2d-3c3c-4b4b4b4b4b4b" */
@Component
public class FormulaEncoder {

    private final String PREFIX = "C";
    private final String DASH_REPLACEMENT = "_";

    /* Regex for raw UUIDs (canonical with dashes) */
    private final Pattern RAW_UUID_PATTERN =
            Pattern.compile(
                    "([0-9a-fA-F]{8}-[0-9a-fA-F]{4}-[0-9a-fA-F]{4}-[0-9a-fA-F]{4}-[0-9a-fA-F]{12})"
            );

    /* Regex for encoded UUIDs: Cxxxxxxxx_xxxx_xxxx_xxxx_xxxxxxxxxxxx */
    private final Pattern ENCODED_PATTERN =
            Pattern.compile(
                      "#(" +
                            PREFIX +
                            "[0-9a-fA-F]{8}" +
                            DASH_REPLACEMENT +
                            "[0-9a-fA-F]{4}" +
                            DASH_REPLACEMENT +
                            "[0-9a-fA-F]{4}" +
                            DASH_REPLACEMENT +
                            "[0-9a-fA-F]{4}" +
                            DASH_REPLACEMENT +
                            "[0-9a-fA-F]{12}" +
                            ")"
            );

    /* Main encode method */
    public String encodeFormula(String rawFormula) {
        /* Identify the UUIDs */
        Matcher matcher = RAW_UUID_PATTERN.matcher(rawFormula);

        /* Create new string for encoded formula */
        StringBuilder sb = new StringBuilder();

        /* Loop through all matches */
        while (matcher.find()) {
            String raw = matcher.group(1);
            /* Encoding matched UUID */
            String encoded = encodeUUID(UUID.fromString(raw));
            /* Replace the matched portion with the encoded UUID */
            matcher.appendReplacement(sb, Matcher.quoteReplacement("#" + encoded));
        }
        /* Append remaining substring after the last match */
        matcher.appendTail(sb);

        /* Return encoded formula */
        return sb.toString();
    }

    /* Main decode method */
    public String decodeFormula(String encodedFormula) {
        /* Identify the encoded UUIDs */
        Matcher matcher = ENCODED_PATTERN.matcher(encodedFormula);

        /* Create new string for decoded formula */
        StringBuilder sb = new StringBuilder();

        /* Loop through all matches */
        while (matcher.find()) {
            String encoded = matcher.group(1);
            /* Decode matched encoded UUID */
            UUID uuid = decodeEncodedUUID(encoded);
            /* Replace the matched portion with the decoded UUID */
            matcher.appendReplacement(sb, Matcher.quoteReplacement(uuid.toString()));
        }
        /* Append remaining substring after the last match */
        matcher.appendTail(sb);

        /* Return decoded formula */
        return sb.toString();
    }

    /* Extract UUIDs from an encoded formula to a set */
    public Set<UUID> extractEncodedUUIDs(String encodedFormula) {
        Set<UUID> set = new LinkedHashSet<>();

        /* Extract encoded UUIDs */
        Matcher matcher = ENCODED_PATTERN.matcher(encodedFormula);

        /* Loop through all matches */
        while (matcher.find()) {
            /* Decode encoded UUID and load to set */
            set.add(decodeEncodedUUID(matcher.group(1)));
        }

        return set;
    }

    /* Encode raw UUID */
    public String encodeUUID(UUID rawUUID) {
        if (rawUUID == null) {
            /* THROW CUSTOM EXCEPTION */
            throw new IllegalArgumentException("Trying to encode UUID that is null");
        }

        return PREFIX + rawUUID.toString().replace("-", DASH_REPLACEMENT).toLowerCase();
    }

    /* Decode encoded UUID string */
    private UUID decodeEncodedUUID(String encodedUUID) {
        if (!encodedUUID.startsWith(PREFIX)) {
            /* THROW CUSTOM EXCEPTION */
            throw new IllegalArgumentException("Invalid encoded id: " + encodedUUID);
        }
        String decodedUUID = encodedUUID
                                /* Skip PREFIX */
                                .substring(1)
                                /* Put back the dashes */
                                .replace(DASH_REPLACEMENT, "-");
        return UUID.fromString(decodedUUID.toLowerCase());
    }
}
