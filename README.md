# Very simple example with JSP and servlets

This example shows how to work with Java servlets and JSP pages

## How to build and run

* Clone this repository 

```
git clone https://github.com/yotammadem/jsp-servlet-example.git
```

* From the folder **jsp-servlet-example** run

```
mvn clean install jetty:run
```

* Browse to [http://localhost:8080/users](http://localhost:8080/users)


## intelliJ 用法
* import maven項目 選擇pom.xml
* 點擊右側Ｍ 的 Lifecycle 再按下 clean --> install
* 下方的 Plugins 再按下 jetty --> jetty:run
* 如果要停止 jetty:stop
* 更新 Dao 內 MSSQL的Driver、使用者、密碼
* 瀏覽器輸入 http://localhost:8080/users
* 即可訪問範例程式碼
