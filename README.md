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
