# RabbitMQ 与 Spring Boot 课设实战指导

## 1. 学习目标

这份指导面向我们的“高校智慧教务与校园管理系统”课设，重点不是把 RabbitMQ 所有高级特性学完，而是把消息队列用在“高并发选课削峰”这个核心业务里。

你需要掌握：

- 能在云服务器上部署 RabbitMQ，并打开管理控制台。
- 能理解 Producer、Consumer、Queue、Exchange、Routing Key、Binding。
- 能用 Spring AMQP 发送和消费消息。
- 能跑通 Simple、Work Queue、Direct 三类基础 Demo。
- 能实现选课请求入队、消费者异步处理、前端查询处理结果。
- 能理解持久化、手动 ACK、幂等、死信队列这些可靠性设计。
- 能在答辩中说明：RabbitMQ 负责削峰排队，不负责最终一致性，最终一致性由 openGauss 保证。

## 2. 消息队列视频内容提炼

视频用秒杀抢购解释消息队列，和我们的热门课程抢课高度类似。

核心结论：

- 异步：请求先快速受理，耗时操作放到后台处理。
- 解耦：选课接口只负责投递消息，不直接关心后续落库、通知、统计。
- 削峰：大量请求先进入队列，消费者按数据库承受能力慢慢处理。

对应到课设：

```text
秒杀请求 -> 选课请求
商品库存 -> 教学班剩余容量
创建订单 -> 插入选课记录
订单状态 -> 选课处理状态
短信通知 -> 系统通知 notice
订单幂等 -> request_id 防重复消费
```

## 3. 云服务器部署 RabbitMQ

建议把 RabbitMQ 部署在“大厂高配置云服务器”上，和 Redis 放在同一台中间件节点。

### 3.1 Docker 部署

```bash
docker pull rabbitmq:3.13-management

docker run -d \
  --name course-rabbitmq \
  -p 5672:5672 \
  -p 15672:15672 \
  -e RABBITMQ_DEFAULT_USER=course_admin \
  -e RABBITMQ_DEFAULT_PASS=your_rabbitmq_password \
  -v /opt/course-rabbitmq/data:/var/lib/rabbitmq \
  rabbitmq:3.13-management
```

端口说明：

- `5672`：Spring Boot 使用的 AMQP 通信端口。
- `15672`：RabbitMQ Web 管理界面端口。

访问管理界面：

```text
http://your.server.ip:15672
```

### 3.2 安全组建议

建议只开放：

- `5672` 给 Spring Boot 所在机器。
- `15672` 给个人电脑公网 IP，用于查看队列和截图。

不要把 RabbitMQ 管理界面对所有公网 IP 开放。

### 3.3 验收截图建议

- RabbitMQ 容器运行截图。
- Web 管理界面登录成功截图。
- Exchange、Queue、Binding 创建截图。
- 队列消息堆积和消费变化截图。

## 4. Spring Boot SDK 接入

Spring Boot 推荐使用 `spring-boot-starter-amqp`。

### 4.1 Maven 依赖

```xml
<dependency>
    <groupId>org.springframework.boot</groupId>
    <artifactId>spring-boot-starter-amqp</artifactId>
</dependency>
```

### 4.2 application.yml

```yaml
spring:
  rabbitmq:
    host: your.rabbitmq.server.ip
    port: 5672
    username: course_admin
    password: your_rabbitmq_password
    virtual-host: /
    publisher-confirm-type: correlated
    publisher-returns: true
    listener:
      simple:
        acknowledge-mode: manual
        prefetch: 1
```

关键配置说明：

- `publisher-confirm-type: correlated`：开启生产者确认。
- `publisher-returns: true`：消息无法路由时返回给生产者。
- `acknowledge-mode: manual`：消费者手动 ACK，处理成功后再确认。
- `prefetch: 1`：每个消费者一次只拿 1 条消息，便于观察和控制处理速度。

## 5. RabbitMQ 基础练习 Demo

### Demo 1：Simple 模式

目标：一个生产者，一个队列，一个消费者。

业务类比：

```text
管理员发送一条系统通知，后台消费者接收并打印。
```

队列：

```text
course.demo.simple.queue
```

练习点：

- `RabbitTemplate.convertAndSend()`
- `@RabbitListener`
- 管理后台查看消息进入和消费。

### Demo 2：Work Queue 模式

目标：一个队列，多个消费者抢任务，每条消息只会被一个消费者处理。

业务类比：

```text
多个选课消费者共同处理选课请求。
```

队列：

```text
course.selection.work.queue
```

练习点：

- 启动两个消费者方法或两个后端实例。
- 连续发送 20 条选课模拟消息。
- 观察消息被不同消费者分摊处理。

### Demo 3：Direct 路由模式

目标：使用 Direct Exchange 根据 Routing Key 精确分发消息。

交换机：

```text
course.direct.exchange
```

队列：

```text
course.selection.queue
course.notice.queue
course.audit.queue
```

路由键：

```text
course.selection
course.notice
course.audit
```

业务类比：

```text
选课请求进入 selection 队列，通知消息进入 notice 队列，审计消息进入 audit 队列。
```

课设建议优先采用 Direct 模式，因为路由关系清晰，答辩也容易讲。

## 6. 课设选课队列设计

### 6.1 消息结构

```java
public class CourseSelectionMessage {
    private String requestId;
    private Long studentId;
    private Long teachingClassId;
    private Long roundId;
    private LocalDateTime requestTime;
}
```

字段说明：

- `requestId`：请求唯一编号，用于幂等和前端查询结果。
- `studentId`：学生编号，实际项目中应从 Token 获取。
- `teachingClassId`：教学班编号。
- `roundId`：选课轮次编号。
- `requestTime`：请求时间。

### 6.2 Exchange、Queue、Routing Key

```java
public class RabbitConstants {
    public static final String SELECTION_EXCHANGE = "course.selection.exchange";
    public static final String SELECTION_QUEUE = "course.selection.queue";
    public static final String SELECTION_ROUTING_KEY = "course.selection";

    public static final String SELECTION_DLX = "course.selection.dlx";
    public static final String SELECTION_DEAD_QUEUE = "course.selection.dead.queue";
    public static final String SELECTION_DEAD_ROUTING_KEY = "course.selection.dead";
}
```

### 6.3 RabbitMQ 配置类

```java
@Configuration
public class CourseSelectionRabbitConfig {

    @Bean
    public DirectExchange selectionExchange() {
        return ExchangeBuilder.directExchange(RabbitConstants.SELECTION_EXCHANGE)
            .durable(true)
            .build();
    }

    @Bean
    public DirectExchange selectionDeadExchange() {
        return ExchangeBuilder.directExchange(RabbitConstants.SELECTION_DLX)
            .durable(true)
            .build();
    }

    @Bean
    public Queue selectionQueue() {
        return QueueBuilder.durable(RabbitConstants.SELECTION_QUEUE)
            .withArgument("x-dead-letter-exchange", RabbitConstants.SELECTION_DLX)
            .withArgument("x-dead-letter-routing-key", RabbitConstants.SELECTION_DEAD_ROUTING_KEY)
            .build();
    }

    @Bean
    public Queue selectionDeadQueue() {
        return QueueBuilder.durable(RabbitConstants.SELECTION_DEAD_QUEUE).build();
    }

    @Bean
    public Binding selectionBinding() {
        return BindingBuilder.bind(selectionQueue())
            .to(selectionExchange())
            .with(RabbitConstants.SELECTION_ROUTING_KEY);
    }

    @Bean
    public Binding selectionDeadBinding() {
        return BindingBuilder.bind(selectionDeadQueue())
            .to(selectionDeadExchange())
            .with(RabbitConstants.SELECTION_DEAD_ROUTING_KEY);
    }
}
```

## 7. 选课生产者 Demo

学生点击选课后，接口不直接落库，而是在 Redis 预扣减成功后发送 RabbitMQ 消息。

```java
@Service
public class CourseSelectionProducer {

    private final RabbitTemplate rabbitTemplate;

    public CourseSelectionProducer(RabbitTemplate rabbitTemplate) {
        this.rabbitTemplate = rabbitTemplate;
    }

    public void sendSelectionMessage(CourseSelectionMessage message) {
        rabbitTemplate.convertAndSend(
            RabbitConstants.SELECTION_EXCHANGE,
            RabbitConstants.SELECTION_ROUTING_KEY,
            message
        );
    }
}
```

接口返回示例：

```json
{
  "requestId": "REQ202607020001",
  "status": "PROCESSING",
  "message": "选课请求已提交，正在处理中"
}
```

## 8. 选课消费者 Demo

消费者从 RabbitMQ 中取出消息，然后执行 openGauss 事务。

```java
@Component
public class CourseSelectionConsumer {

    private final CourseSelectionService courseSelectionService;

    public CourseSelectionConsumer(CourseSelectionService courseSelectionService) {
        this.courseSelectionService = courseSelectionService;
    }

    @RabbitListener(queues = RabbitConstants.SELECTION_QUEUE)
    public void handleSelection(
        CourseSelectionMessage message,
        Channel channel,
        Message rawMessage
    ) throws IOException {
        long deliveryTag = rawMessage.getMessageProperties().getDeliveryTag();
        try {
            courseSelectionService.processSelectionMessage(message);
            channel.basicAck(deliveryTag, false);
        } catch (BusinessException ex) {
            courseSelectionService.markFailed(message, ex.getMessage());
            channel.basicAck(deliveryTag, false);
        } catch (Exception ex) {
            channel.basicReject(deliveryTag, false);
        }
    }
}
```

处理原则：

- 业务失败：例如重复选课、时间冲突，记录失败原因后 ACK。
- 系统异常：例如数据库连接失败，Reject 后进入死信队列。
- 处理成功：插入选课记录、更新已选人数、ACK。

## 9. openGauss 落库逻辑

消费者里真正保证一致性的不是 RabbitMQ，而是 openGauss 事务。

事务内建议步骤：

1. 根据 `request_id` 查询是否已处理，保证幂等。
2. 查询学生、教学班、选课轮次是否存在。
3. 检查是否重复选课。
4. 检查是否时间冲突。
5. 条件更新教学班容量。
6. 插入选课记录。
7. 写入选课请求日志。
8. 提交事务。

防超选 SQL 思路：

```sql
UPDATE teaching_class
SET selected_count = selected_count + 1
WHERE teaching_class_id = #{teachingClassId}
  AND selected_count < capacity;
```

如果影响行数为 0，说明容量不足，选课失败。

防重复选课约束：

```sql
UNIQUE (student_id, teaching_class_id)
```

幂等约束：

```sql
UNIQUE (request_id)
```

## 10. 死信队列 Demo

目标：模拟消费者异常时，消息不会直接丢失，而是进入死信队列。

触发方式：

- 消费者主动 `basicReject(deliveryTag, false)`。
- 队列设置 TTL 后消息过期。
- 队列长度超限。

课设演示方式：

1. 临时让消费者抛出 RuntimeException。
2. 发送一条选课消息。
3. 查看 `course.selection.queue` 消息被拒绝。
4. 查看消息进入 `course.selection.dead.queue`。
5. 截图用于报告说明异常消息处理。

## 11. 完整课设业务链路

最终链路：

```text
学生点击选课
  -> Spring Boot 基础校验
  -> Redis Lua 预扣减容量
  -> RabbitMQ 发送选课消息
  -> 接口返回 PROCESSING
  -> RabbitMQ 消费者异步处理
  -> openGauss 事务落库
  -> 成功或失败状态写入 selection_request_log
  -> 前端用 request_id 查询结果
```

前端轮询接口：

```text
GET /api/student/selections/result/{requestId}
```

返回成功：

```json
{
  "requestId": "REQ202607020001",
  "status": "SELECTED",
  "message": "选课成功"
}
```

返回失败：

```json
{
  "requestId": "REQ202607020001",
  "status": "FAILED",
  "message": "时间冲突"
}
```

## 12. 答辩表达

可以这样讲：

```text
我们使用 RabbitMQ 对高并发选课请求进行削峰。学生提交选课后，接口只完成基础校验、Redis 容量预扣减和消息投递，然后立即返回处理中。真正的数据库写入由后台消费者异步完成。

RabbitMQ 的作用是异步、解耦和削峰，它本身不保证选课一定成功，也不负责防止超选。最终是否成功由 openGauss 中的事务、唯一约束和条件更新决定。对于重复消费问题，我们使用 request_id 和数据库唯一约束保证幂等。对于异常消息，我们配置死信队列，避免消息静默丢失。
```

## 13. 最小完成清单

- 云服务器 RabbitMQ 容器能启动。
- 管理界面 `15672` 能访问。
- Spring Boot 能连接 RabbitMQ。
- Simple 模式能发送和消费消息。
- Work Queue 模式能多个消费者分摊消息。
- Direct 模式能把选课、通知、审计消息路由到不同队列。
- 选课请求能进入 `course.selection.queue`。
- 消费者能处理消息并写入模拟结果。
- 消费失败能进入死信队列。
- 能截图展示 Exchange、Queue、Binding、消息生产和消费过程。

