import java.io.Serializable;
import java.util.AbstractMap;
import java.util.Map;

public class TSBHashTableDA<K,V> extends AbstractMap implements Map<K,V>, Cloneable, Serializable {

    private Map.Entry<K,V> table[];
    private int count;

    private class Entry<K,V> implements Map.Entry< K, V> {
    }
}
