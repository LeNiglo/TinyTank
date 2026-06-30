package javafx.util;

import java.io.Serializable;
import java.util.Objects;

/**
 * Minimal drop-in replacement for {@code javafx.util.Pair}.
 *
 * JavaFX was unbundled from the JDK in Java 11, and the {@code org.openjfx:javafx-base}
 * Maven artifact ships per-platform classifier jars that break cross-platform builds.
 * This module only used {@code javafx.util.Pair} as a plain immutable key/value holder,
 * so it is vendored here with the same public API (constructor, {@link #getKey()},
 * {@link #getValue()}, equals/hashCode/toString) — keeping all existing imports valid.
 *
 * @param <K> key type
 * @param <V> value type
 */
public class Pair<K, V> implements Serializable {

    private final K key;
    private final V value;

    public Pair(K key, V value) {
        this.key = key;
        this.value = value;
    }

    public K getKey() {
        return key;
    }

    public V getValue() {
        return value;
    }

    @Override
    public String toString() {
        return key + "=" + value;
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(key) * 13 + Objects.hashCode(value);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof Pair)) {
            return false;
        }
        Pair<?, ?> other = (Pair<?, ?>) o;
        return Objects.equals(key, other.key) && Objects.equals(value, other.value);
    }
}
