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

        public boolean isClosed() {
            return condition == 1;
        }

        public boolean isOpen() {
            return condition == 0;
        }

        public boolean isTomb() {
            return condition == 2;
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
        public Iterator<Map.Entry<K,V>> itarator() {
            return new EntrySetIterator();
        }



        private class EntrySetIterator implements  Iterator<Map.Entry<K,V>> {

            private int current_Entry;
            private int last_Entry;
            private boolean nextOk;
            private int expected_modCount;

            public EntrySetIterator(){
                this.current_Entry = 0;
                this.last_Entry = 0;
                this.nextOk = false;
                this.expected_modCount =TSBHashTableDA.this.modCount;
            }


            @Override
            public boolean hasNext(){
                Map.Entry<K,V> t[] = TSBHashTableDA.this.table;
                if(TSBHashTableDA.this.isEmpty()) {return  false;}
                return current_Entry < t.length;
            }

            @Override
            public Map.Entry<K,V> next() {

                if(TSBHashTableDA.this.modCount != expected_modCount)
                {
                    throw new ConcurrentModificationException("next(): la tabla fue modificada durante el recorrido");
                }

                if(!hasNext()) {
                    throw new NoSuchElementException("next(): no existe el elemento siguiente");
                }
                Map.Entry<K,V> t[] = TSBHashTableDA.this.table;

                for(Map.Entry<K,V> entry : t) {
                    Entry<K,V>  entr
                }

            }
        }
    }

    private class ValueColeection extends AbstractCollection<V> {

    }
}
