m# 1. 根据属性查询
```java

public Users getUserByMobile(String mobile) {
    QueryWrapper<Users> usersQueryWrapper = new QueryWrapper<>();
    usersQueryWrapper.eq("mobile",mobile).orderByDesc("id").last("limit 1");
    return usersMapper.selectOne(usersQueryWrapper);
}



```


问题：
> Java 没有内建 User | null 这样的联合类型（Union Type）,所以定义返回值`User` 会返回null这样的值;

# 2, 使用stream

如果没有stream 就必须使用`for (User u : users) {}` 等方法了

| 操作              | 说明                   |
| --------------- | -------------------- |
| `.filter(...)`  | 筛选出满足条件的元素           |
| `.map(...)`     | 转换每个元素               |
| `.collect(...)` | 收集成 List/Set/Map 等集合 |
| `.findFirst()`  | 找到第一个元素（如果有）         |
| `.count()`      | 统计数量                 |


| 用法                | 说明              |
| ----------------- | --------------- |
| `.orElse(null)`   | 没找到时返回 null     |
| `.orElse(xxx)`    | 没找到时返回默认值 `xxx` |
| `.orElseThrow()`  | 没找到时抛异常         |
| `.isPresent()`    | 是否存在            |
| `.ifPresent(...)` | 存在就执行逻辑         |


```java

// 例1：
List<String> names = Arrays.asList("Tom", "Jerry", "Spike");

// 找到第一个以 "J" 开头的名字
String name = names.stream()
                   .filter(n -> n.startsWith("J"))
                   .findFirst()
                   .orElse(null); // .orElse(null) 的作用是在 Stream 操作中，如果没有找到符合条件的结果，就返回 null，避免抛出异常。

System.out.println(name); // 输出: Jerry


// 复杂的例子：
/**
 * 有一个 List<User>，你希望：
    找到年龄大于 18 岁且城市为 "Beijing" 的用户，
    按注册时间排序（最早的在前），
    返回第一个匹配用户的名字，
    如果找不到，返回 "Anonymous"。
 *
 */
List<User> users = List.of(
        new User("Alice", 22, "Beijing", LocalDate.of(2021, 3, 10)),
        new User("Bob", 19, "Shanghai", LocalDate.of(2022, 5, 12)),
        new User("Charlie", 30, "Beijing", LocalDate.of(2020, 1, 8)),
        new User("David", 17, "Beijing", LocalDate.of(2023, 7, 1))
);

public class User {
    private String name;
    private int age;
    private String city;
    private LocalDate registerDate;

    // 构造器 + Getter/Setter 省略
}

String userName = users.stream()
        .filter(u -> u.getAge() > 18)                        // 条件1：年龄大于18
        .filter(u -> "Beijing".equals(u.getCity()))         // 条件2：城市为北京
        .sorted(Comparator.comparing(User::getRegisterDate))// 排序：注册时间最早优先
        .map(User::getName)                                 // 映射：只取名字
        .findFirst()                                        // 找第一个匹配的
        .orElse("Anonymous");                               // 没有就用默认值

System.out.println(userName); // 输出：Charlie




```




