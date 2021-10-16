import java.io.Serializable;
import java.util.*;

public class TSBHashTableDA<K, V>  implements Map<K,V>, Cloneable, Serializable {


    private Object table[];
    private int initialCapacity;
    private final static int MAX_SIZE = Integer.MAX_VALUE;
    private int count;
    private float loadFactor;

    private transient Set<K> keySet = null;
    private transient Set<Map.Entry<K,V>> entrySet = null;
    private transient Collection<V> values = null;

    private transient int modCount;


    public TSBHashTableDA(){
        this(17,0.8f);
    }

    public TSBHashTableDA(int initialCapacity, float loadFactor) {
        if(loadFactor <= 0) { loadFactor = 0.8f; }
        if(initialCapacity <= 0) { initialCapacity = 17;}
        else
        {
            if(initialCapacity > TSBHashTableDA.MAX_SIZE)
            {
                initialCapacity = TSBHashTableDA.MAX_SIZE;
            }
        }

        this.table = new Object[initialCapacity];
        for(int i = 0; i  < table.length; i++)
        {
            table[i] = new Entry<K,V>();
        }

        this.initialCapacity = initialCapacity;
        this.loadFactor = loadFactor;
        this.count = 0;
        this.modCount = 0;
    }


    private int h(int k)
    {
        return h(k, this.table.length);
    }

    private int h(K key, int t)
    {
        return h(key.hashCode(), t);
    }

    private int h(K key)
    {
        return h(key.hashCode(), this.table.length);
    }

    private int h(int k, int t)
    {
        if(k < 0) k *= -1;
        return k % t;
    }



    @Override
    public Object clone() {
        return new Object();
    }

    @Override
    public int size() {
        return this.count;
    }

    @Override
    public boolean isEmpty() {
        return (this.count == 0);
    }

    @Override
    public boolean containsKey(Object key) {
        return false;
    }

    @Override
    public boolean containsValue(Object value) {
        return false;
    }

    @Override
    public V get(Object key) {
        return null;
    }

    @Override
    public V put(K key, V value) {
        if(key == null || value == null) {throw new NullPointerException("put(): Algun parametro fue null"); }
        int indice = this.h(key);

        


    }

    public int ExploracionCuadratica(int indice) {

        for (int j = 0;; j ++ ) {
            indice = (indice + j*j) % table.length;
            Entry<K, V> entry = (Entry) this.table[indice];
            if (!(entry.isClosed()))
            {
                return indice;
            }
        }
    }

    @Override
    public V remove(Object key) {
        return null;
    }

    @Override
    public void putAll(Map<? extends K, ? extends V> m) {

    }

    @Override
    public void clear() {

    }

    @Override
    public Set<K> keySet()
    {
        if(keySet == null)
        {

            keySet = new KeySet();
        }
        return keySet;
    }


    @Override
    public Collection<V> values()
    {
        if(values==null)
        {
            values = new ValueCollection();
        }
        return values;
    }

    @Override
    public Set<Map.Entry<K, V>> entrySet()
    {
        if(entrySet == null)
        {
            // entrySet = Collections.synchronizedSet(new EntrySet());
            entrySet = new EntrySet();
        }
        return entrySet;
    }



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

        public void tomb(){
            this.condition = 2;
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
                Object t[] = TSBHashTableDA.this.table;

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
                Object t[] = TSBHashTableDA.this.table;


                for (int i = currentKey +1;i<t.length;i++ ) {
                    if(((Entry<K,V>) t[i]).isClosed()) {
                        lastKey = currentKey;
                        currentKey = i;
                        break;
                    }
                }
                nextOk = true;
                return (K) ((Entry)t[currentKey]).getKey();
            }


            @Override
            public void remove(){

                if(!nextOk)
                {
                    throw new IllegalStateException("remove(): debe invocar a next() antes de remove()...");
                }

                Entry<K,V> remover = (Entry<K, V>) TSBHashTableDA.this.table[currentKey];
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
                Object t[] = TSBHashTableDA.this.table;
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
                Object t[] = TSBHashTableDA.this.table;

                for (int i = current_Entry + 1; i < t.length; i++) {
                    if ( ((Entry<K, V>) t[i]).isClosed() ) {
                        last_Entry = current_Entry;
                        current_Entry = i;
                        break;
                    }
                }

                nextOk = true;
                return (Entry) t[current_Entry];

            }

            @Override
            public void remove(){
                if(!nextOk)
                {
                    throw new IllegalStateException("remove(): debe invocar a next() antes de remove()...");
                }

                Entry<K,V> remover = (Entry<K, V>) TSBHashTableDA.this.table[current_Entry];
                TSBHashTableDA.this.remove(remover.getKey());

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

        private class ValueCollection extends AbstractCollection<V> {
        
         @Override
        public Iterator<V> iterator() 
        {
            return new ValueCollectionIterator();
        }
        
        @Override
        public int size() 
        {
            return TSBHashTableDA.this.count;
        }
        
        @Override
        public boolean contains(Object o) 
        {
            return TSBHashTableDA.this.containsValue(o);
        }
        
        @Override
        public void clear() 
        {
            TSBHashTableDA.this.clear();
        }
        
        private class ValueCollectionIterator implements Iterator<V>
        {
            private int currentValue;
            private int lastValue;
            private boolean next;
            private int expectedModCount;

            public ValueCollectionIterator()
            {
                currentValue = 0;
                lastValue = 0;
                next = false;
                expectedModCount = TSBHashTableDA.this.modCount;
            }

            @Override
            public boolean hasNext() 
            {
               Object t[] = TSBHashTableDA.this.table;

                if(TSBHashTableDA.this.isEmpty()) { return false; }
                if(currentValue >= t.length) { return false; }

                for (int i = currentValue + 1 ; i < t.length ; i++) {
                    if(((Entry<K,V>) t[i]).isClosed()) {return true;}
                }
                return false;
            }

            @Override
            public V next() 
            {
                if(TSBHashTableDA.this.modCount != expectedModCount)
                {    
                    throw new ConcurrentModificationException("next(): La tabla fue modificada durante el recorrido");
                }
                
                if(!hasNext()) 
                {
                    throw new NoSuchElementException("next(): no existe elemento siguiente");
                }

                Object t[] = TSBHashTableDA.this.table;

                for (int i = currentValue + 1 ; i < t.length ; i++) {
                    if(((Entry<K,V>) t[i]).isClosed()) {
                        lastValue = currentValue;
                        currentValue = i;
                        break;
                    }
                }

                next = true;

                return (V) ((Entry)t[currentValue]).getValue();
            }

            @Override
            public void remove() 
            {
                if(!next)
                { 
                    throw new IllegalStateException("remove(): debe invocar a next() antes de remove()");
                }

                Entry<K,V> remover = (Entry<K, V>) TSBHashTableDA.this.table[currentValue];
                TSBHashTableDA.this.remove(remover.getKey());
              
                if(lastValue != currentValue)
                {
                    currentValue = lastValue;
                }

                next = false;
                TSBHashTableDA.this.count--;
                TSBHashTableDA.this.modCount++;
                expectedModCount++;
            }     
        }
    }
}