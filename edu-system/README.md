# 高校智慧教务与校园管理系统 Demo

第一轮 Demo 包含：

- `sql/01_init_opengauss.sql`：openGauss 初始化 SQL。
- `backend`：Spring Boot 3 后端。
- `frontend`：Vue 3 + Element Plus 前端。
- `docs/RUNNING.md`：运行说明。

## 数据库

你当前截图中的表在：

```text
database: postgres
schema: sht
```

后端连接必须使用同一个 schema：

```text
jdbc:opengauss://x6.sjcmc.cn:34003/postgres?user=sht&password=@Sht20051229
```

## 启动顺序

1. 先确认 SQL 已在 openGauss 执行完成。
2. 启动后端：

```powershell
cd D:\study\dbClassDesign\edu-system\backend
$env:JAVA_HOME="C:\Users\20979\.jdks\ms-17.0.18"
$env:Path="$env:JAVA_HOME\bin;$env:Path"
$env:DB_URL="jdbc:opengauss://x6.sjcmc.cn:34003/postgres?user=sht&password=@Sht20051229"
mvn spring-boot:run
```

也可以复制 `backend/start-java17.ps1`，填入数据库连接后直接执行。

项目已配置 Maven 本地仓库：

```text
D:/develop/apache-maven-3.9.11/mvn_repo
```

如果 IDEA 仍写入 Maven 安装目录，在 Maven Runner VM Options 中加入：

```text
-Dmaven.repo.local=D:/develop/apache-maven-3.9.11/mvn_repo
```

3. 启动前端：

```powershell
cd D:\study\dbClassDesign\edu-system\frontend
npm install
npm run dev
```

4. 打开：

```text
http://localhost:5173/
```

## 演示账号

```text
管理员：admin / 123456
教师：teacher1 / 123456
学生：student1 / 123456
```
