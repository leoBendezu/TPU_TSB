import jdk.swing.interop.SwingInterOpUtils;

public class Main {
    public static void main(String[] args) {
       TSBHashTableDA<String,Integer> table = new TSBHashTableDA<>();

        String key = "key";
        Integer result;
        result = table.put(key, 1);
        System.out.println(result);
        result = table.put(key, 2);
        System.out.println(result);

    }
}
