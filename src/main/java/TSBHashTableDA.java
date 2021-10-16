import java.io.Serializable;
import java.util.*;

public class TSBHashTableDA<K,V> extends AbstractMap implements Map<K,V>, Cloneable, Serializable {

    private Map.Entry<K,V> table[];
    private int initialCapacity;
    private int count;
    private float loadFactor;

    private transient Set<K> keySet = null;
    private transient Set<Map.Entry<K,V>> entrySet = null;
    private transient Collection<V> values = null;

    private transient  int modCount;


    private class Entry<K,V> implements Map.Entry< K, V> {
        private K key;
        private V value;
        private int condition;

        public Entry(K key, V value) {

        }
    }

    private class KeySet extends AbstractSet<K> {

    }

    private class EntrySet extends AbstractSet<Map.Entry<K,V>> {

    }

    private class ValueColeection extends  AbstractCollection<V> {
        
    }
}
