import jdk.swing.interop.SwingInterOpUtils;

public class Main {
    public static void main(String[] args) {
       TSBHashTableDA<String,Integer> table = new TSBHashTableDA<>();

        String key = "key";
        String key2= "key2";
        Integer result;
        result = table.put(key, 1);

        result = table.put(key2, 2);
        System.out.println(result);

        System.out.println(table);

    }
}
