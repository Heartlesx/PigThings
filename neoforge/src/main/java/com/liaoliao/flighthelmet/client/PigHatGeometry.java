package com.liaoliao.flighthelmet.client;

import com.google.gson.Gson;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;

/** Exact per-face UVs and head placement from the bundled Blockbench model. */
final class PigHatGeometry {
    static final float[][] QUADS = load();

    private static float[][] load() {
        String resource = "/assets/pigthings/models/armor/pig_straw_hat.json";
        try (InputStream stream = PigHatGeometry.class.getResourceAsStream(resource)) {
            if (stream == null) throw new IllegalStateException("Missing " + resource);
            return new Gson().fromJson(new InputStreamReader(stream, StandardCharsets.UTF_8), float[][].class);
        } catch (java.io.IOException exception) {
            throw new IllegalStateException("Unable to read pig hat geometry", exception);
        }
    }

    private PigHatGeometry() {}
}
