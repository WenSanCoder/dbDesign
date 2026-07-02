# Redis 与 Spring Boot 课设实战指导

## 1. 学习目标

这份指导面向我们的“高校智慧教务与校园管理系统”课设，不追求把 Redis 所有命令背完，而是围绕选课高并发场景掌握能落地的能力：

- 能在云服务器上部署 Redis，并用本机或 Spring Boot 连接。
- 能理解 Redis 的 Key-Value 模型、常用数据结构和典型业务场景。
- 能用 Spring Data Redis 操作字符串、Hash、Set 等基础数据。
- 能实现课程容量缓存、缓存预热、Lua 原子预扣减、失败回补。
- 能解释缓存穿透、击穿、雪崩，以及它们和课设场景的关系。
- 能在答辩中说明：Redis 只是缓存和并发缓冲层，openGauss 才是最终可信数据源。

## 2. Redis.txt 内容提炼

视频里对 Redis 的核心解释可以压缩成一句话：

Redis 是一个基于内存的远程 Key-Value 存储系统，读写非常快，常用于缓存、分布式 Session、分布式锁、计数器、排行榜、限流和热点数据预热。

和我们课设最相关的点如下：

- 首页热门文章缓存，对应我们的“热门教学班信息缓存”。
- 缓存预热，对应“选课开始前把教学班剩余容量加载到 Redis”。
- Lua 脚本原子操作，对应“判断容量是否大于 0 并扣减 1”。
- 缓存穿透，对应“恶意查询不存在课程或教学班 ID”。
- 缓存击穿，对应“热门教学班容量 Key 过期后大量请求打到 openGauss”。
- 缓存雪崩，对应“大量教学班 Key 同时过期导致数据库压力突增”。
- RDB/AOF、主从、哨兵、集群属于生产级高可用能力，课设中重点理解，演示可不强制实现。

## 3. 云服务器部署 Redis

建议把 Redis 部署在“大厂高配置云服务器”上，作为应用与中间件节点。小厂云服务器专门跑 openGauss，个人电脑负责开发和演示。

### 3.1 Docker 部署

推荐用 Docker，方便复现和截图。

```bash
docker pull redis:7.2

docker run -d \
  --name course-redis \
  -p 6379:6379 \
  -v /opt/course-redis/data:/data \
  redis:7.2 \
  redis-server --appendonly yes --requirepass your_redis_password
```

参数说明：

- `-p 6379:6379`：暴露 Redis 服务端口。
- `-v /opt/course-redis/data:/data`：把 Redis 数据挂载到宿主机。
- `--appendonly yes`：开启 AOF 持久化。
- `--requirepass`：设置密码，避免 Redis 裸奔。

### 3.2 安全组建议

Redis 不建议对公网所有 IP 开放。

建议安全组只允许：

- 个人电脑公网 IP 访问 `6379`，用于开发调试。
- 大厂云服务器本机访问 `6379`，用于 Spring Boot 部署后连接。
- 如 Spring Boot 在个人电脑运行，则允许个人电脑公网 IP 访问。

### 3.3 基础验证命令

```bash
docker exec -it course-redis redis-cli -a your_redis_password
```

进入后执行：

```redis
PING
SET course:capacity:1001 50
GET course:capacity:1001
DECR course:capacity:1001
GET course:capacity:1001
```

验收截图建议保留：

- Docker 容器运行截图。
- `redis-cli PING` 成功截图。
- 课程容量 Key 设置和扣减截图。

## 4. Spring Boot SDK 接入

Spring Boot 推荐使用 `spring-boot-starter-data-redis`，底层默认使用 Lettuce 客户端。

### 4.1 Maven 依赖

```xml
<dependency>
    <groupId>org.springframework.boot</groupId>
    <artifactId>spring-boot-starter-data-redis</artifactId>
</dependency>
```

### 4.2 application.yml

```yaml
spring:
  data:
    redis:
      host: your.redis.server.ip
      port: 6379
      password: your_redis_password
      database: 0
      timeout: 3s
```

### 4.3 RedisTemplate 配置

```java
@Configuration
public class RedisConfig {

    @Bean
    public RedisTemplate<String, Object> redisTemplate(RedisConnectionFactory connectionFactory) {
        RedisTemplate<String, Object> template = new RedisTemplate<>();
        template.setConnectionFactory(connectionFactory);

        StringRedisSerializer stringSerializer = new StringRedisSerializer();
        GenericJackson2JsonRedisSerializer jsonSerializer = new GenericJackson2JsonRedisSerializer();

        template.setKeySerializer(stringSerializer);
        template.setHashKeySerializer(stringSerializer);
        template.setValueSerializer(jsonSerializer);
        template.setHashValueSerializer(jsonSerializer);
        template.afterPropertiesSet();
        return template;
    }
}
```

## 5. 练习 Demo 设计

### Demo 1：基础 Key-Value 操作

目标：熟悉 Spring Boot 操作 Redis。

接口：

```text
POST /demo/redis/string
GET  /demo/redis/string/{key}
DELETE /demo/redis/string/{key}
```

练习点：

- `opsForValue().set()`
- `opsForValue().get()`
- `delete()`
- 设置 TTL

业务类比：

```text
course:info:1001 -> 数据库系统课程简介
```

### Demo 2：课程详情缓存

目标：掌握经典缓存模式。

流程：

1. 前端查询课程详情。
2. 后端先查 Redis：`course:detail:{courseId}`。
3. Redis 命中则直接返回。
4. Redis 未命中则查 openGauss。
5. 查到后写入 Redis，并设置随机 TTL。
6. 查不到也缓存空值，避免缓存穿透。

推荐 Key：

```text
course:detail:{courseId}
```

TTL 建议：

```text
30 分钟 + 0 到 5 分钟随机扰动
```

课设价值：

- 能说明缓存穿透和缓存雪崩处理。
- 能展示课程详情查询速度优化。

### Demo 3：选课容量预热

目标：把 openGauss 中的教学班剩余容量写入 Redis。

接口：

```text
POST /api/admin/selection-rounds/{roundId}/preheat
```

流程：

1. 查询某轮次下所有开放教学班。
2. 计算剩余容量：`capacity - selected_count`。
3. 写入 Redis。

Key 设计：

```text
course:capacity:{teachingClassId}
```

示例：

```text
course:capacity:20001 -> 50
course:capacity:20002 -> 30
```

### Demo 4：Lua 原子预扣减

目标：实现高并发下“有容量才扣减”，避免多个请求同时把容量扣成负数。

Lua 脚本：

```lua
local key = KEYS[1]
local stock = tonumber(redis.call('get', key))

if stock == nil then
    return -1
end

if stock <= 0 then
    return 0
end

redis.call('decr', key)
return 1
```

返回值：

```text
1  预扣减成功
0  容量不足
-1 Redis 中没有该教学班容量
```

Spring Boot 调用示例：

```java
@Service
public class CourseCapacityService {

    private final StringRedisTemplate stringRedisTemplate;

    private static final DefaultRedisScript<Long> DEDUCT_SCRIPT = new DefaultRedisScript<>(
        """
        local key = KEYS[1]
        local stock = tonumber(redis.call('get', key))
        if stock == nil then
            return -1
        end
        if stock <= 0 then
            return 0
        end
        redis.call('decr', key)
        return 1
        """,
        Long.class
    );

    public CourseCapacityService(StringRedisTemplate stringRedisTemplate) {
        this.stringRedisTemplate = stringRedisTemplate;
    }

    public long preDeduct(Long teachingClassId) {
        String key = "course:capacity:" + teachingClassId;
        Long result = stringRedisTemplate.execute(DEDUCT_SCRIPT, List.of(key));
        return result == null ? -1 : result;
    }

    public void rollbackCapacity(Long teachingClassId) {
        stringRedisTemplate.opsForValue().increment("course:capacity:" + teachingClassId);
    }
}
```

### Demo 5：Redis 失败回补

目标：当 RabbitMQ 消费者落库失败时，把预扣减的容量加回 Redis。

触发场景：

- 数据库发现重复选课。
- 数据库发现时间冲突。
- 数据库条件更新失败，说明容量不足。
- 消费者业务异常。

回补命令：

```java
stringRedisTemplate.opsForValue().increment("course:capacity:" + teachingClassId);
```

## 6. 课设业务闭环

Redis 在课设里的最终业务链路：

```text
管理员发布选课轮次
  -> Spring Boot 查询 openGauss 教学班容量
  -> 预热 course:capacity:{teachingClassId}
  -> 学生点击选课
  -> Redis Lua 原子预扣减
  -> 成功后进入 RabbitMQ
  -> 消费者落库失败则 Redis 回补容量
```

Redis 只负责“快判断”和“预扣减”，不保存最终选课结果。最终选课记录必须写入 openGauss。

## 7. 答辩表达

可以这样讲：

```text
我们将热门教学班的剩余容量缓存到 Redis 中，在选课开始前进行容量预热。学生提交选课请求后，后端使用 Redis Lua 脚本原子判断剩余容量并预扣减，避免高并发请求直接冲击 openGauss，也避免多个请求同时扣减导致容量变成负数。

Redis 只是性能优化层，不是最终数据源。如果后续 RabbitMQ 消费者在 openGauss 落库失败，系统会回补 Redis 容量。最终一致性仍然由 openGauss 的事务、唯一约束和条件更新保证。
```

## 8. 最小完成清单

- 云服务器 Redis 容器能启动。
- 本机能通过 `redis-cli` 或 Spring Boot 连上 Redis。
- 能写入并读取 `course:capacity:{teachingClassId}`。
- 能完成课程详情缓存 Demo。
- 能完成教学班容量预热 Demo。
- 能完成 Lua 原子预扣减 Demo。
- 能在失败时回补 Redis 容量。
- 能截图展示 Redis Key、容量变化和 Spring Boot 调用结果。
