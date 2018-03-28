public interface IMap {
    boolean containsKey(String key);
    String get(String key);
    String put(String key, String value);
    String remove(String key);

}
