package ${packageName};

import com.alibaba.fastjson.JSON;
import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;

import java.util.Map;

/**
 * Created by ${author} on ${date}.
 */
@Service
public class ${serviceUpperName}KafkaMessageHandler {
    @Autowired
    private KafkaTemplate<String, String> template;
    public void sendMessage(int messageId,Map<String,String> payload) {
        this.template.send("${messageTunnel}",String.valueOf(messageId),JSON.toJSONString(payload));
    }


    //*************************************************************************
    @KafkaListener(id = "${serviceLowerName}", topics = "${messageTunnel}", containerFactory = "${serviceLowerName}KafkaListenerContainerFactory")
    public void listen(ConsumerRecord<?, ?> cr) throws Exception {
        System.out.println("${serviceLowerName} consumer");
        Integer messageId = Integer.parseInt(cr.key().toString());

        System.out.println(cr.toString());
    }
}
