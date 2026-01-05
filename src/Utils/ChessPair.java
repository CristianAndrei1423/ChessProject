package Utils;

public class ChessPair<K, V> implements Comparable<ChessPair<K, V>> {
    private K key;
    private V value;

    public ChessPair(K pos, V piece) {
        key = pos;
        value = piece;
    }

    public K getKey(){
        return key;
    }

    public V getValue(){
        return value;
    }

    public void setKey(K key){
        this.key = key;
    }

    public void setValue(V value){
        this.value = value;
    }

    public String getPair(){
        return "" + key + value;
    }

    @Override
    public int compareTo(ChessPair<K, V> o) {
        K keyO = o.getKey();
        return ((Position) keyO).compare(keyO, key);
    }

}
