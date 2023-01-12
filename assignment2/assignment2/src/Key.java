import java.util.Objects;

/**
 * @see <a href="https://www.techiedelight.com/implement-map-with-multiple-keys-multikeymap-java/">...</a>
 * @param <K1> key1
 * @param <K2> key2
 */
public class Key<K1, K2>
{
    public K1 key1;
    public K2 key2;

    public Key(K1 key1, K2 key2) {
        this.key1 = key1;
        this.key2 = key2;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }

        if (o == null || getClass() != o.getClass()) {
            return false;
        }

        Key key = (Key) o;
        if (!Objects.equals(key1, key.key1) || !Objects.equals(key2, key.key2)) {
            return false;
        }

        return true;
    }

    @Override
    public int hashCode()
    {
        int result = key1 != null ? key1.hashCode() : 0;
        result = 31 * result + (key2 != null ? key2.hashCode() : 0);
        return result;
    }

    @Override
    public String toString() {
        return "[" + key1 + ", " + key2 + "]";
    }
}
