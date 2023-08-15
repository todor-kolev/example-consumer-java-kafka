package io.pactflow.example.kafka;

import java.util.HashMap;
import java.util.Map;

import io.confluent.kafka.schemaregistry.client.CachedSchemaRegistryClient;
import io.confluent.kafka.schemaregistry.client.MockSchemaRegistryClient;
import io.confluent.kafka.schemaregistry.client.SchemaRegistryClient;
import org.apache.kafka.clients.consumer.ConsumerConfig;
import org.apache.kafka.common.serialization.StringDeserializer;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.kafka.annotation.EnableKafka;
import org.springframework.kafka.config.ConcurrentKafkaListenerContainerFactory;
import org.springframework.kafka.core.ConsumerFactory;
import org.springframework.kafka.core.DefaultKafkaConsumerFactory;
import org.springframework.kafka.support.serializer.JsonDeserializer;
import org.springframework.kafka.support.serializer.JsonSerializer;
import io.confluent.kafka.serializers.KafkaAvroDeserializer;
import devflow.pact.kafka.schema.Student;

@EnableKafka
@Configuration
public class KafkaConfiguration {

  @Bean
  public ConsumerFactory<String, Student> productConsumerFactory() {
//  public ConsumerFactory<String, Product> productConsumerFactory() {
    Map<String, Object> props = new HashMap<>();
    props.put(ConsumerConfig.BOOTSTRAP_SERVERS_CONFIG, "localhost:9092");
    props.put(ConsumerConfig.GROUP_ID_CONFIG, "products_group_1");
    props.put(ConsumerConfig.KEY_DESERIALIZER_CLASS_CONFIG, StringDeserializer.class);
    props.put(ConsumerConfig.VALUE_DESERIALIZER_CLASS_CONFIG, JsonSerializer.class);
//    return new DefaultKafkaConsumerFactory<>(props, new StringDeserializer(), new JsonDeserializer<>(Product.class));
    SchemaRegistryClient schemaRegistryClient = new MockSchemaRegistryClient();
    return new DefaultKafkaConsumerFactory(props, new StringDeserializer(), new KafkaAvroDeserializer(schemaRegistryClient));
  }

  @Bean
  public ConcurrentKafkaListenerContainerFactory<String, Student> kafkaListenerContainerFactory() {
//  public ConcurrentKafkaListenerContainerFactory<String, Product> kafkaListenerContainerFactory() {

    ConcurrentKafkaListenerContainerFactory<String, Student> factory = new ConcurrentKafkaListenerContainerFactory<String, Student>();
//    ConcurrentKafkaListenerContainerFactory<String, Product> factory = new ConcurrentKafkaListenerContainerFactory<String, Product>();
    factory.setConsumerFactory(productConsumerFactory());

    return factory;
  }
}