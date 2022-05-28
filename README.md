# ROKU  

Roku is a library to help with bukkit, web requests, JSON and more.  
> ## **IMPORTANT NOTE**  
> This library is still in early development. Most features I plan to add are either unfinished, slightly broken, or not even started yet.  
> **Use this library with caution.**

## Importing and Shading.

Whilst this project is in development, you can still import and shade it into your project by utilising Jitpack.  
To import this using maven, you can use the following.
```xml
<repositories>
    <repository>
        <id>jitpack.io</id>
        <url>https://jitpack.io</url>
    </repository>
</repositories>

<dependencies>
    <dependency>
        <groupId>com.github.Ankoki</groupId>
        <artifactId>Roku</artifactId>
        <version>master-SNAPSHOT</version>
        <scope>compile</scope>
    </dependency>
</dependencies>
```  
However; if you are using Gradle, you can use the following.  
```groovy
repositories {
    maven { url 'https://jitpack.io' }
}

dependencies {
    implementation 'com.github.Ankoki:Roku:master-SNAPSHOT'
}
```
## Examples  

Here you can find examples for most features within Roku.

### GUIs  

---
With Roku, creating Bukkit GUIs is easy! You have two options.  

#### GUI.java
You can use the provided [GUI](https://github.com/Ankoki/Roku/blob/master/src/main/java/com/ankoki/roku/bukkit/guis/GUI.java) class, which allows to set the click events for each slot without any hassle, and set the items in a defined shape.
Here is an example of how I have done this.
```java
GUI gui = new GUI("§eVoltage §7~ §cITZY", 27)
        .setShape(List.of("xxxxxxxxx", "xxxxAxxxx", "xxxxxxxxx"))
        .setShapeItem('x', ItemUtils.getBlank(new ItemStack(Material.BLACK_STAINED_GLASS_PANE)))
        .setShapeItem('A', ItemUtils.getSkull("3ec6c6e00a6ad055f250546a8c0da070df4613a5f65517a9933bd5de969d8406"))
        .addClickEvent(event -> {
            event.setCancelled(true);
            HumanEntity entity = event.getWhoClicked();
            entity.sendMessage("§7§oRoku; §9Liquid Smooth §7~ §8Mitski");
        }).setDragEvent(event -> event.setCancelled(true));
GUI.registerGUI(gui);
```
The GUI then looked like this: <img width="966" alt="image" src="https://user-images.githubusercontent.com/72931234/161297243-bb5d1835-e6d3-4aa2-b918-ec04ed75be9a.png">
  

#### IGUI.java  
Making your own implementation of an IGUI is easy; simply implement the provided [IGUI](https://github.com/Ankoki/Roku/blob/master/src/main/java/com/ankoki/roku/bukkit/guis/IGUI.java) interface and override the required methods!  
If you want an example, you can check out [GUI.java](https://github.com/Ankoki/Roku/blob/master/src/main/java/com/ankoki/roku/bukkit/guis/GUI.java), as it does exactly that!

### Advancements

---
Advancements can be a hassle, however Roku stops that! You can easily create an advancement with custom messages, criteria and icons.
```java
NamespacedKey ADVANCEMENT_KEY = new NamespacedKey(Roku.getInstance(), "roku_adv")
try {
    if (!Advancement.advancementExists(ADVANCEMENT_KEY))
        new Advancement(ADVANCEMENT_KEY)
            .setTitle("§7§oRoku Development Build")
            .setDescription("§fYou used a development build of §7§oRoku§f!")
            .setFrame(Frame.CHALLENGE)
            .setAnnounced(true)
            .setIcon(Material.DIAMOND)
            .setBackground(Background.END)
            .addCriteria("default", AdvancementTrigger.IMPOSSIBLE)
            .load();
    BukkitImpl.info("Advancement loaded");
} catch (InvalidAdvancementException ex) {
    ex.printStackTrace();
}
```  
Advancements are persistent, so make sure to only register them if they don't exist!  
To give a player an advancement, you can use the static methods provided in [Advancement.java](https://www.github.com/Ankoki/Roku/blob/master/src/main/java/com/ankoki/roku/bukkit/advancements/Advancement.java). You can also use these to revoke them.  
```java
Advancement.awardAdvancement(player, Advancement.getAdvancement(ADVANCEMENT_KEY));
Advancement.revokeAdvancement(player, Advancement.getAdvancement(ADVANCEMENT_KEY));
```

### JSON  

---
Parsing and creating JSON is a breeze with Roku! You are able to convert a JSON string to a Map, and vice versa!  
Doing as such is very simple, use the constructor that takes in what you want to be the map. It's that simple!  
From a map, you can do a little something like this.
```java
Map<Object, Object> map = new HashMap<>();
List<String> list = new ArrayList<>();
list.add("list value one");
list.add("list value two");
Map<String, Object> map1 = new HashMap<>();
map1.put("one", true);
map1.put("wow a number", 12L);
map.put("hello", "test");
map.put("boom", list);
map.put("map thingy", map1);
JSON json = new JSON(map);
System.out.println(json);
```
The text that is outputted is `"{"map thingy":{"one":true,"wow a number":12},"boom":["list value one","list value two"],"hello":"test"}"`! You can also use `JSON#toPrettyString()` or `JSON.toString(JSON, boolean)` to add new lines and indentation.  
An example of a `JSON#toPrettyString()` output would be: 
```json
{
  "map thingy": {
    "one": true,
    "wow a number": 12
  },
  "boom": [
    "list value one",
    "list value two"
  ],
  "hello": "test"
}
```
You can also convert a JSON string to a JSON!
```java
String unparsed = "{\"test key\":[\"value 1\",\"value 2\",\"woo im a list\"],\"test again\":\"lololol\",\"test-map\":{\"one\":\"two\"}}";
try {
    JSON json = new JSON(unparsed);
    System.out.println(json.get("test again"));;
    System.out.println(String.join(", ", (List) json.get("test key")));
    System.out.println(((Map) json.get("test-map")).get("one"));
} catch (MalformedJsonException ex) {
    ex.printStackTrace();
}
```
The result from this query is:  
`lololol`  
`value 1, value 2, woo im a list`  
`two`.
