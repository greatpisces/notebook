public interface Consumer<T> {
    public void accept(T t);
}

public static <T> void forEach(List<T> list, Consumer<T> c) {
    for (T t : list) {
        c.accept(i);
    }
}