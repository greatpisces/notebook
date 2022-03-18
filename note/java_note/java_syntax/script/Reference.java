//方法引用
(args) -> ClassName.staticMethod(args)
ClassName::staticMethod

(arg0, rest) -> arg0.instanceMethod(rest)
ClassName::instanceMethod

(args) -> expr.instanceMethod(args)
expr::instanceMethod

//构造函数引用
new Apple();
Supplier<Apple> c1 = Apple::new;
Apple a1 = c1.get();

new Apple(Integer weight);
Function<Integer, Apple> c2 = Apple::new;
Apple a2 = c2.apply(110);
Function<Integer, Apple> cs = (weight) -> new Apple(weight);

List<Integer> weights = Arrays.asList(7, 3, 4, 10);
List<Apple> apples = map(weight, Apple::new);

new Apple(String color, Integer weight);
BiFunction<String, Integer, Apple> c3 = Apple::new;
Apple c3 = c3.apply("green", 110);

static Map<String, Function<Integer, Fruit>> map = new HashMap<>();
static {
    map.put("apple", Apple::new);
}
public static Fruit giveMeFruit(String fruit, Integer weight) {
    return map.get(fruit.toLowerCase()).apply(weight);
}

//复合
//比较器 Comparator<T>
inventory.sort(comparing(Apple::getWeight)).reversed().thenComparing(Apple::getCountry);

//谓词 Predicate<T>
//negate 非， and 与， or 或
Predicate<Apple> notRedApple = redApple.negate();
Predicate<Apple> redAndHeavyAppleOrGreen = redApple.and(a -> a.getWeight() > 150).or(a -> "green".equals(a.getColor()));

//函数 Function<T>
Function<Integer, Integer> f = x -> x + 1;
Function<Integer, Integer> g = x -> x * 2;
Function<Integer, Integer> h = f.andThen(g); //g(f(x))
Function<Integer, Integer> h = f.compose(g); //f(g(x))
