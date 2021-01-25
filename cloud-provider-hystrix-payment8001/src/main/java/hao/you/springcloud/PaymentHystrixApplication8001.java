package hao.you.springcloud;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.circuitbreaker.EnableCircuitBreaker;
import org.springframework.cloud.netflix.eureka.EnableEurekaClient;

/**
 * Created with IntelliJ IDEA.
 *
 * @Auther: 昊
 * @Date: 2021/01/21/19:38
 * @Description:
 */
@SpringBootApplication
@EnableEurekaClient
//服务降级
@EnableCircuitBreaker
public class PaymentHystrixApplication8001 {
    public static void main(String[] args) {
        SpringApplication.run(PaymentHystrixApplication8001.class, args);
    }
}
