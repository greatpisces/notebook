public interface Function<T, R> {
    public R apply(T t);
}

public static <T, R> List<R> map(List<T> list, Function<T, R> f) {
    List<R> result = new ArrayList<> ();
    for (T t : list) {
        result.add(f.apply(s));
    }
    return result;
}