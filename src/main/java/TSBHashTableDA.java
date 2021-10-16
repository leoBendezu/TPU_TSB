import java.io.Serializable;
import java.security.Key;
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

        public Iterator<K> iterator() {
            return new KeySetIterator();
        }

        @Override
        public int size() {
            return TSBHashTableDA.this.count;
        }

        @Override
        public boolean contains(Object o) {
            return TSBHashTableDA.this.containsKey(o);
        }

        @Override
        public boolean remove(Object o) {
            return (TSBHashTableDA.this.remove(o) != null);
        }

        @Override
        public void clear() {
            TSBHashTableDA.this.clear();
        }

        private class KeySetIterator implements Iterator<K>{
            // índice de la lista actualmente recorrida...
            private int currentKey;

            // índice de la lista anterior (si se requiere en remove())...
            private int lastKey;



            // flag para controlar si remove() está bien invocado...
            private boolean nextOk;

            // el valor que debería tener el modCount de la tabla completa...
            private int expectedModCount;

            public KeySetIterator()
            {
                currentKey = 0;
                lastKey = 0;
                nextOk = false;
                expectedModCount = TSBHashTableDA.this.modCount;
            }

            @Override
            public boolean hasNext(){

                // variable auxiliar t para simplificar accesos...
                Map.Entry<K, V> t[] = TSBHashTableDA.this.table;

                if(TSBHashTableDA.this.isEmpty()) { return false; }
                if( currentKey >= t.length){ return false; }

                for (int i = currentKey +1;i<t.length;i++ ) {
                    if(((Entry<K,V>) t[i]).isClosed()) { return true;}
                }
                return false;
            }

            @Override
            public K next(){
                if(TSBHashTableDA.this.modCount != expectedModCount)
                {
                    throw new ConcurrentModificationException("next(): La tabla fue modificada durante el recorrido");

                }

                if(!hasNext())
                {
                    throw new NoSuchElementException("next(): no existe elemento siguiente");
                }
                Map.Entry<K, V> t[] = TSBHashTableDA.this.table;


                for (int i = currentKey +1;i<t.length;i++ ) {
                    if(((Entry<K,V>) t[i]).isClosed()) {
                        lastKey = currentKey;
                        currentKey = i;
                        break;
                    }
                }
                nextOk = true;
                return t[currentKey].getKey();
            }


            @Override
            public void remove(){

                if(!nextOk)
                {
                    throw new IllegalStateException("remove(): debe invocar a next() antes de remove()...");
                }

                Map.Entry < K,V > remover = TSBHashTableDA.this.table[currentKey];
                TSBHashTableDA.this.remove(remover.getKey());

                if(lastKey != currentKey)
                {
                    currentKey = lastKey;
                }

                nextOk=false;
                TSBHashTableDA.this.count--;

                TSBHashTableDA.this.modCount++;
                expectedModCount++;

            }





        }




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
