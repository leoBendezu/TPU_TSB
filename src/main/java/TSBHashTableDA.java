import java.io.Serializable;
import java.util.*;

public class TSBHashTableDA<K, V> extends AbstractMap implements Map<K, V>, Cloneable, Serializable {

    private Map.Entry<K, V> table[];
    private int initialCapacity;
    private int count;
    private float loadFactor;

    private transient Set<K> keySet = null;
    private transient Set<Map.Entry<K, V>> entrySet = null;
    private transient Collection<V> values = null;

    private transient int modCount;


    private class Entry<K, V> implements Map.Entry<K, V> {
        private K key;
        private V value;
        private int condition;

        public Entry() {
            this.condition = 0;
        }

        public Entry(K key, V value) {

            if (key == null || value == null) {
                throw new IllegalArgumentException("Entry(): uno de los parámetros fue null");
            }
            this.key = key;
            this.value = value;
        }

        @Override
        public K getKey() {
            return key;
        }

        @Override
        public V getValue() {
            return value;
        }

        @Override
        public V setValue(V value) {
            if (value == null) {
                throw new IllegalArgumentException("setValue(): el parámetro es null");
            }

            V old = this.value;
            this.value = value;

            return old;
        }


        @Override
        public int hashCode()
        {
            int hash = 5;
            hash = 61 * hash + Objects.hashCode(this.key);
            hash = 61 * hash + Objects.hashCode(this.value);
            return hash;
        }

        @Override
        public boolean equals(Object obj)
        {
            if (this == obj) { return true; }
            if (obj == null) { return false; }
            if (this.getClass() != obj.getClass()) { return false; }

            final Entry other = (Entry) obj;
            if (!Objects.equals(this.key, other.key)) { return false; }
            if (!Objects.equals(this.value, other.value)) { return false; }
            return true;
        }

        @Override
        public String toString()
        {
            return "( Llave: " + key.toString() + ", Valor: " + value.toString() + ")";
        }

    }

    private class KeySet extends AbstractSet<K> {

    }

    private class EntrySet extends AbstractSet<Map.Entry<K, V>> {

        @Override
        public Itarator<>

    }

    private class ValueColeection extends AbstractCollection<V> {

    }
}
