package com.geekbrains.spoonaccular;

import net.javacrumbs.jsonunit.JsonAssert;
import net.javacrumbs.jsonunit.core.Option;

import java.io.File;
import java.io.InputStream;
import java.nio.charset.StandardCharsets;
import java.util.Objects;

public class AbstractTest {

    public void assertJson(Object expected, Object actually) {

        JsonAssert.assertJsonEquals(
                expected,
                actually,
                JsonAssert.when(Option.IGNORING_ARRAY_ORDER)
        );
    }

    public String getResource(String name) throws Exception {
        String resource = getClass().getSimpleName() + "/" + name;
        InputStream resourceAsStream = getClass().getResourceAsStream(resource);
        assert resourceAsStream != null;
        byte[] bytes = resourceAsStream.readAllBytes();
        return new String(bytes, StandardCharsets.UTF_8);
    }

    public File getFile(String name) {
        String resource = getClass().getSimpleName() + "/" + name;
        String file = Objects.requireNonNull(getClass().getResource(resource)).getFile();
        return new File(file);
    }
}
