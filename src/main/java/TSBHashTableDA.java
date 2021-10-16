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
        public Iterator<Map.Entry<K,V>> iterator() {
            return new EntrySetIterator();
        }

        @Override
        public boolean contains(Object o) {
            if(o == null) { return false; }
            if(!(o instanceof Entry)) { return false;}

            Map.Entry<K,V > entry = (Map.Entry<K,V>) o;
            return TSBHashTableDA.this.containsKey(entry.getKey()) ;
        }

        @Override
        public boolean remove(Object o) {
            if(o == null) { throw new NullPointerException("remove(): parámetro null");}
            if(!(o instanceof Entry)) { return false; }

            Map.Entry<K,V > entry = (Map.Entry<K,V>) o;


            if(TSBHashTableDA.this.remove(entry.getKey()) != null)
            {
                TSBHashTableDA.this.count--;
                TSBHashTableDA.this.modCount++;
                return true;
            }

            return false;
        }

        @Override
        public int size()
        {
            return TSBHashTableDA.this.count;
        }

        @Override
        public void clear()
        {
            TSBHashTableDA.this.clear();
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
            public boolean hasNext() {
                Map.Entry<K, V> t[] = TSBHashTableDA.this.table;
                if (TSBHashTableDA.this.isEmpty()) {
                    return false;
                }
                if (current_Entry >= t.length) {
                    return false;
                }

                for (int i = current_Entry + 1; i < t.length; i++) {
                    if ( ((Entry<K, V>) t[i]).isClosed() ) {
                        return true;
                    }
                }

                return  false;
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

                for (int i = current_Entry + 1; i < t.length; i++) {
                    if ( ((Entry<K, V>) t[i]).isClosed() ) {
                        last_Entry = current_Entry;
                        current_Entry = i;
                        break;
                    }
                }

                nextOk = true;
                return t[current_Entry];

            }

            @Override
            public void remove(){
                if(!nextOk)
                {
                    throw new IllegalStateException("remove(): debe invocar a next() antes de remove()...");
                }

                Map.Entry<K,V> removido = ((Entry<K,V>)TSBHashTableDA.this.table[current_Entry]).remove();

                if(last_Entry != current_Entry) {
                    current_Entry = last_Entry;
                }

                nextOk = false;
                TSBHashTableDA.this.count --;
                TSBHashTableDA.this.modCount ++;
                expected_modCount++;

            }
        }
    }

    private class ValueColeection extends AbstractCollection<V> {

    }
}
