## 1. GSON对于LocalDate的处理

GSON使用`new Gson().toJson(class)`时候，如果其中有
参考 [GsonTest.java](../auth-service-8111/src/test/java/com/imooc/test/GsonTest.java)

```java
public class TestUsers {

    private static final long serialVersionUID = 1L;

    private String id;
    private String mobile;
    private String nickname;
    private String realName;
    private Integer showWhichName;
    private LocalDate birthday;

}


```


使用`String json = gson.toJson(testUsers);`会出现错误

> com.google.gson.JsonIOException: Failed making field 'java.time.LocalDate#year' accessible; either increase its visibility or write a custom TypeAdapter for its declaring type.


这时候需要手动register adaptor


```java
public class LocalDateAdapter implements JsonSerializer<LocalDate>, JsonDeserializer<LocalDate> {

    private static final DateTimeFormatter formatter = DateTimeFormatter.ISO_LOCAL_DATE;

    @Override
    public JsonElement serialize(LocalDate date, Type typeOfSrc, JsonSerializationContext context) {
        return new JsonPrimitive(date.format(formatter));  // yyyy-MM-dd
    }

    @Override
    public LocalDate deserialize(JsonElement json, Type typeOfT, JsonDeserializationContext context)
            throws JsonParseException {
        return LocalDate.parse(json.getAsString(), formatter);
    }
}

```