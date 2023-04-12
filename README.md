# 📌 RabbitMQ Nedir?

RabbitMQ, açık kaynaklı bir mesaj kuyruklama aracıdır. AMQP (Advanced Message Queuing Protocol) kullanır ve Erlang proglamlama dilinde geliştirilmiştir. Birden fazla mesajlaşma türünü destekler. Dağıtık uygulamalar (distributed) arasındaki veri alışverişini güvenli bir şekilde iletmeyi sağlar. Sistemlerin iş yükünü birbirleri arasında paylaştırır ve bu iş yükünün yönetimini kolaylaştırır. 

![image](https://user-images.githubusercontent.com/91599453/231185467-7a5af602-f5f3-4eb8-acff-43370aa7e303.png)

RabbitMQ; mesajlaşma uygulamalarında kullanılan publisher - subscriber (producer - consumer) modeli için exchange, binding ve kuyruk (queue) denilen kavramlardan oluşur. Bu türler mesajın hangi şekilde, ne formatta kuyruğa veya kuyruklara yazılacağını belirler. 

## 🎯RabbitMQ Kavramları

 1. Exchange : 
  Producer'dan gelen mesajları yönlendiren mekanizmaya verilen addır. Birden fazla exchange türü mevcuttur.
  - Direct Exchange : Gelen mesajı ilgili routing key'e göre kuyruğa yönlendiren exchange türüdür.
  - Topic Exchange : Gelen mesajı routing key pattern'ine göre kuyruğa yönlendiren türdür.
  - Fanout Exchange : Mesajları bütün kuyruklara eşit şekilde dağıtan exchange türüdür.
  - Header Exchange : Mesajları header bilgilerine göre ilgili queue'lere yönlendirir.

![Rabbitmq-exchanges-topic-fanout-direct](https://user-images.githubusercontent.com/91599453/231192496-53bddec3-7b1a-401e-aab0-cedc0d6161f8.png)

 2. Bindings : 
  Exchange ile kuyruk arasındaki bağlantıyı sağlayan ara katmandır. Bindings, exchange türüne ve routing key'e bağımlı şekilde oluşturulur. Tek yönlü olarak mesajın kuyruğa aktarımı sağlanır.

 3. Queue : 
  Mesajların kalıcı veya geçici olarak saklanabileceği bir kuyruktur. Mesajlar consumer tarafından tüketilene kadar saklanırlar. Bir mesajın kuyrukta kalması ve RabbitMQ sunucusunun yeniden başlatılması durumunda bile verilerin korunması sağlanır.

# 📌 Spring Boot Projesinde RabbitMQ Kullanımı

* Bir Spring Boot projesi oluşturup pom.xml dosyasına RabbitMQ eklentisi eklenir.

```xml
 <dependency>
     <groupId>org.springframework.boot</groupId>
     <artifactId>spring-boot-starter-amqp</artifactId>
 </dependency>
```

* RabbitMQ uygulaması Docker üzerinde çalıştırılacağı için docker-compose.yaml dosyası oluşturulur ve RabbitMQ uygulamasının çalışması sağlanır.

```yaml
version: '3.0'
services:
  springboot-rabbitmq:
    image: rabbitmq:3.12-rc-management
    ports:
      - '5672:5672'
      - '15672:15672'
    environment:
      - RABBITMQ_DEFAULT_USER=mustafafindik
      - RABBITMQ_DEFAULT_PASS=p@ssword
```

* application.properties dosyasının içerisine ilgili dependencyler ve projede kullanılacak olan valuelar eklenir.

```properties
spring.rabbitmq.host=localhost
spring.rabbitmq.port=5672
spring.rabbitmq.username=mustafafindik
spring.rabbitmq.password=p@ssword

springrabbitmq.rabbit.queue.name = mustafa-queue
springrabbitmq.rabbit.routing.name = mustafa-routing
springrabbitmq.rabbit.exchange.name = mustafa-exchange

spring.datasource.url=jdbc:postgresql://localhost:5432/rabbitmq
spring.datasource.username=postgres
spring.datasource.password=123456
spring.datasource.hikari.auto-commit=false
spring.jpa.properties.hibernate.dialect = org.hibernate.dialect.PostgreSQLDialect
spring.jpa.hibernate.ddl-auto=update
spring.jpa.show-sql=false
```

* Modelimizi oluşturduktan sonra config package ı altında gerekli konfigürasyonların tanımlanması için configuration sınıfı oluşturulur.

```java
@Configuration
public class RabbitmqConfiguration {
    @Value("${springrabbitmq.rabbit.queue.name}")
    private String queueName;
    @Value("${springrabbitmq.rabbit.routing.name}")
    private String routingName;
    @Value("${springrabbitmq.rabbit.exchange.name}")
    private String exchangeName;
    @Bean
    public Queue queue() {
        return new Queue(queueName);
    }
    @Bean
    DirectExchange directExchange() {
        return new DirectExchange(this.exchangeName);
    }
    @Bean
    Binding binding() {
        return BindingBuilder.bind(this.queue()).to(this.directExchange()).with(this.routingName);
    }
}
```
* Producer sınıfı oluşturulup Notification sınıfında serilize edilen veriyi RabbitTemplate aracılığıyla kuyruğa gönderim işlemi sağlanır.

```java
@Component
@Slf4j
public class NotificationProducer {
    @Value("${springrabbitmq.rabbit.routing.name}")
    private String routingName;
    @Value("${springrabbitmq.rabbit.exchange.name}")
    private String exchangeName;
    private final RabbitTemplate rabbitTemplate;
    public NotificationProducer(RabbitTemplate rabbitTemplate) {
        this.rabbitTemplate = rabbitTemplate;
    }
    public void sendToQueue(Notification notification) {
        log.info("\nNotification Send ID : " + UUID.randomUUID());
        rabbitTemplate.convertAndSend(exchangeName, routingName, notification);
    }
}
```

* Listener sınıfı içerisinde @RabbitListener kullanılarak kuyruğa gelen veriyi yakalayıp veritabanına kayıt işlemi sağlanır.

```java
@Component
@Slf4j
public class NotificationListener {
    private final NotificationServiceImpl notificationService;

    public NotificationListener(NotificationServiceImpl notificationService) {
        this.notificationService = notificationService;
    }
    @RabbitListener(queues = {"${springrabbitmq.rabbit.queue.name}"})
    public void handleMessage(Notification notification) {
        log.info("Notification caught : " + notification.toString());
        notificationService.saveNotify(notification);
    }
}
```
* Spring Boot uygulamasını çalıştırdıktan sonra container olarak çalışan RabbitMQ'ya erişim için http://localhost:15672/ adresine gidilir. Authentication işlemi sonrası karşımıza böyle bir ekran çıkacaktır.

![image](https://user-images.githubusercontent.com/91599453/231457887-63823e25-1a26-4742-9b75-fd08225e3b0d.png)

* Mesajımızı Json formatında Postman üzerinden localhost:8080/notification adresine istek attığımızda uygulama içerisindeki console ekranına bastırılan loglar görülecektir. 

![image](https://user-images.githubusercontent.com/91599453/231458729-950b30fd-aa27-48ca-af1f-1a0916dfbfa7.png)

* RabbitMQ üzerinden Queues kısmına geldiğimizde ise yolladığımız mesajın kuyruk üzerindeki incelemesi gerçekleştirilebilir.

![image](https://user-images.githubusercontent.com/91599453/231459031-e9f4e9e3-084b-4ae9-8b0a-1ad050f28a8c.png)

* Kuyruktan gelen veri veritabanına kayıt edilmiş şekilde gözükecektir.

![image](https://user-images.githubusercontent.com/91599453/231459383-b87a3f40-7775-4fd4-bc61-deaa9e9d12fa.png)

