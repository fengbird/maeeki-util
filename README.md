### 目的

* 本工具包主要用于解决在进行对象转对象,对象转json, json转对象时存在的转换前后属性字段不一致的问题

### 包依赖关系

* 本项目包依赖于fastjson和hutool工具包, 工具包中json与对象之间的转换基于fastjson, 对象之间的转换基于hutool工具包.

```xml
<dependencies>
        <dependency>
            <groupId>cn.hutool</groupId>
            <artifactId>hutool-all</artifactId>
            <version>4.1.19</version>
        </dependency>
        <dependency>
            <groupId>com.alibaba</groupId>
            <artifactId>fastjson</artifactId>
            <version>1.2.53</version>
        </dependency>
 </dependencies>
```



### 场景假设

#### 场景假设1

* 假设本项目中有一bean对象User,定义如下(省略了getter,setter方法):

  ```java
  public class User {
      private String username;
      private String password;
      private Integer age;
      private String sex;
  }
  ```

* 本项目是服务提供方, 需要给其他客户方提供报文, 若存在客户方所需报文格式为:

  ```json
  {
      "name":"用户名",
      "pwd":"密码",
      "age":34,
      "sex":"男"
  }
  ```

* 由于服务方项目为老项目,若是直接修改User类中的字段, 那么本项目受影响将会较大, 而客户方严格要求必须按照客户方提供的报文来走, 这时就会遇到问题, User中的username和报文中要求的name不一致,那么在进行字段转换的时候就会遇到问题,需要开发人员在代码中手动进行映射, 那么每次遇到这种问题都需要开发者在代码中手动进行一次映射....相应的, 若从报文转为User对象,也存在相同的问题.

#### 场景假设2

* 假设项目还为一个老旧项目,其bean对象User还是如上定义,若对该项目添加新功能的时候在新添加的数据表中字段名称和原先的不一样导致新写的bean对象MyUser也与原对象属性字段不一致.如下(省略了getter/setter方法)

```java
public class MyUser {
    private String name;
    private String pwd;
    private Integer age;
}
```

* 当从先前的数据表中获取了数据并且存在了User对象中, 现需将数据转存到MyUser所对应的数据表中,由于字段不一致,又需要在代码中手动对属性进行映射,若类中不一致的字段很多就会导致一个方法中很长一段都是在进行属性映射....



### 用法

#### bean与json之间的转换

* 本工具包就是为了解决类似上述的情况而做的,对应场景一, 本工具包的处理方式如下:

  ```java
  1.新建枚举类,在枚举类中写出需要字段映射关系:
  public enum  UserEnum {
      username("name"),
      password("pwd"),
      private String key;
      UserEnum(String key) {
          this.key = key;
      }
      public String key() {
          return key;
      }
  }
  
  2.在需要进行字段转换的地方调用ConvertUtil中的方法进行转换即得到所需的json字符串
  String json = ConvertUtil.beanToJson(user, UserEnum.class);
  ```

* 利用工具如上操作, 将字段的对应关系在枚举类中明确的写了出来,便于维护对应关系,而且在进行bean与bean,bean与json之间进行转换的时候均可使用此枚举类

* **注意: 上例为最简做法,若要如上例做法,必须保证枚举中的获取值方法为key() ,key()方法在本工具包中为默认处理方法**

* 若枚举方法用户想要自定义也是可以的, 不过在进行使用的时候需要用配置器来配置

  ```java
  public enum  UserEnum {
      username("name"),
      password("pwd"),
      private String value;
      UserEnum(String value) {
          this.value = value;
      }
      public String getValue() {
          return value;
      }
  }
  
  方法调用方式:
  String json = ConvertUtil.beanToJson(user, UserEnum.class,
                                       new ConvertConfig("getValue"));
  如上,需指明所要调用获取值的方法.
  ```

* 若字段存在多种对应方式,只需如下配置

  ```java
  public enum  UserEnum {
      username("name","userName"),
      password("pwd","PASSWORD"),
      sex("xingbie","xb");
      private String key;
      private String other;
      UserEnum(String key,String other) {
          this.key = key;
          this.other = other;
      }
  
      public String key() {
          return key;
      }
  
      public String other() {
          return other;
      }
  }
  方法调用方式:
  String json = ConvertUtil.beanToJson(user, UserEnum.class,
                                       new ConvertConfig("other"));
  只需指明映射的字段的获取方法即可.
  ```

* 以上的所有示例均为枚举的name根据映射方法映射到对应的枚举值上面, 例如从User的username字段映射到name字段, 那么,如何配置可以利用上面的枚举类从name逆向映射到username呢?

  ```java
  设置:new ConvertConfig(true)
  String json = ConvertUtil.beanToJson(user, UserEnum.class,
                                       new ConvertConfig(true));
  若即时自定义方法又是反转映射,只需如下调用:
  String json = ConvertUtil.beanToJson(user, UserEnum.class,
                                       new ConvertConfig(true,"other"));
  ```

* 对ConvertConfig的解释

  * 该配置器中有两个可配置属性: `convert` 默认值为false,意思是枚举中由name映射到值, 当为true时,即逆向映射, `methodName` 默认值为key, 对应枚举获取值的方法名, 若自定义方法名,需要配置ConvertConfig来进行指明.

#### bean与bean之间的转换

* 对象的一对一转换及一对多转换方式如下:

  ```java
  public class User {
      private String username;
      private String password;
      private Integer age;
      private String sex;
  }
  
  public class MyUser {
      private String name;
      private String pwd;
      private Integer age;
  }
  
  public class HerUser {
      private String xingbie;
  }
  
  public enum  UserEnum {
      username("name","userName"),
      password("pwd","PASSWORD"),
      sex("xingbie","xb");
      private String key;
      private String other;
      UserEnum(String key,String other) {
          this.key = key;
          this.other = other;
      }
  
      public String key() {
          return key;
      }
  
      public String other() {
          return other;
      }
  }
  
  一对一转换:
  MyUser myUser = new MyUser();
  myUser.setName("zs");
  myUser.setPwd("123456");
  myUser.setAge(23);
  User user = new User();
  ConvertUtil.beanToBean(myUser,user, UserEnum.class,new ConvertConfig(true));
  
  多个对象转到一个对象中:
  HerUser herUser = new HerUser();
  herUser.setXingbie("男");
  MyUser myUser = new MyUser();
  myUser.setName("zs");
  myUser.setPwd("123456");
  myUser.setAge(23);
  User user = new User();
  ConvertUtil.beanToBean(Arrays.asList(myUser,herUser),user, UserEnum.class,new ConvertConfig(true));
  ```

### 总结

* 本工具包是针对公司的现有情况做出来的初版, 若以后有新的优化的地方会进行同步修改, 本人才疏学浅,若您在使用本工具包的时候遇到了BUG或建议, 请不吝赐教.

