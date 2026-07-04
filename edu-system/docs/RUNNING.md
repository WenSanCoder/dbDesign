# 高校智慧教务系统运行说明

## 1. 数据库位置确认

你截图中表位于：

```text
database: postgres
schema: sht
```

这表示 SQL 已经建到 `postgres` 数据库的 `sht` schema 下。后端只要把 JDBC URL 指向同一个 database 和 schema 即可。

示例：

```text
jdbc:opengauss://x6.sjcmc.cn:34003/postgres?user=sht&password=@Sht20051229
```

## 2. 后端启动

进入后端目录：

```powershell
cd D:\study\dbClassDesign\edu-system\backend
```

设置连接环境变量。请把尖括号替换成你的真实值：

```powershell
$env:JAVA_HOME="C:\Users\20979\.jdks\ms-17.0.18"
$env:Path="$env:JAVA_HOME\bin;$env:Path"
$env:DB_URL="jdbc:opengauss://x6.sjcmc.cn:34003/postgres?user=sht&password=@Sht20051229"
mvn spring-boot:run
```

如果你的 openGauss 不需要 SSL，不用额外配置。当前后端默认端口是 `8080`。

本项目已在 `backend/.mvn/maven.config` 中把 Maven 本地仓库固定到你 IDEA 已配置的仓库：

```text
D:/develop/apache-maven-3.9.11/mvn_repo
```

这样 Maven 不会写入 `D:\develop\apache-maven-3.9.11\conf`。

## 3. 前端启动

进入前端目录：

```powershell
cd D:\study\dbClassDesign\edu-system\frontend
npm install
npm run dev
```

浏览器打开：

```text
http://localhost:5173/
```

开发代理已配置：前端 `/api` 请求会转发到 `http://localhost:8080`。

## 4. 演示账号

初始化 SQL 内置账号：

```text
管理员：admin / 123456
教师：teacher1 / 123456
学生：student1 / 123456
```

## 5. 常见问题

- 如果后端提示找不到表，优先检查 `DB_URL` 里的 `currentSchema=sht` 是否与实际建表 schema 一致。
- 如果前端登录接口 404 或连接失败，确认后端是否在 `8080` 启动。
- 当前 `opengauss-jdbc-6.0.0.jar` 实际驱动类是 `org.postgresql.Driver`。项目内通过 `OpenGaussAliasDriver` 适配 `jdbc:opengauss://...`，业务配置可以继续使用 openGauss URL。
- 如果 IDEA 或 Maven 使用的是 JDK 25，Lombok 可能报 `TypeTag :: UNKNOWN`。请把项目 SDK、Maven Runner JRE 和运行配置都改为 `C:\Users\20979\.jdks\ms-17.0.18`。
- 如果 Maven 仍尝试写入 `D:\develop\apache-maven-3.9.11\conf`，在 IDEA 的 Maven Runner VM Options 中加入：
  `-Dmaven.repo.local=D:/develop/apache-maven-3.9.11/mvn_repo`
