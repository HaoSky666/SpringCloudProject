package hao.you.springcloud.service;

import cn.hutool.core.util.IdUtil;
import com.netflix.hystrix.contrib.javanica.annotation.HystrixCommand;
import com.netflix.hystrix.contrib.javanica.annotation.HystrixProperty;
import org.springframework.stereotype.Service;

import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * Created with IntelliJ IDEA.
 *
 * @Auther: 昊
 * @Date: 2021/01/21/19:40
 * @Description:
 */
@Service
public class PaymentService {
    static AtomicInteger count = new AtomicInteger(0);

    public String paymentInfoOK(Integer id) {
        return "线程池： " + Thread.currentThread().getName() + "\t" + "paymentInfoOK(),id:" + id + "\t" + " \"O(∩_∩) 成功返回哈哈哈";
    }

    // 服务降级
    @HystrixCommand(fallbackMethod = "paymentInfoTimeOutHandler", commandProperties = {
            @HystrixProperty(name = "execution.isolation.thread.timeoutInMilliseconds", value = "4000")})
    public String paymentInfoTimeOut(Integer id) {
        System.out.println("第" + count.getAndIncrement() + "请求");
        Integer timeOutNumber = 3;
        // int age  = 10/0;
        try {
            TimeUnit.SECONDS.sleep(timeOutNumber);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        return "线程池:  " + Thread.currentThread().getName() + " id:  " + id + "\t" + "O(∩_∩)O哈哈~" + "  耗时(秒): " + timeOutNumber;
    }

    public String paymentInfoTimeOutHandler(Integer id) {
        return "线程池:  " + Thread.currentThread().getName() + "  8001系统繁忙或者运行报错，请稍后再试,id:  " + id + "\t" + "o(╥﹏╥)o";
    }

    //=====服务熔断
    @HystrixCommand(fallbackMethod = "paymentCircuitBreaker_fallback", commandProperties = {
            // 1、快照时间窗：断路器确定是否打开需要统计一些请求和错误数据，而统计的时间范围就是快照时间窗，默认为最近的10秒
            // 2、请求总数阀值：在快照时间窗内，必须满足请求总阀值才有资格熔断。默认为20，意味着在10秒内，如果该hystrix命令的调用次数不足20次，
            // 即使所有的请求都超时或其他原因失败，断路器都不会打开。
            // 3、错误百分比阀值：当请求总数在快照时间窗内超过了阀值，比如发生了30次调用，如果在这30次调用中，有15次发送了超时异常，也就是超过50%
            // 的错误百分比，在默认设定50%阀值情况下，这个时候就会将断路器打开
            @HystrixProperty(name = "circuitBreaker.enabled", value = "true"),// 是否开启断路器
            @HystrixProperty(name = "circuitBreaker.requestVolumeThreshold", value = "10"),// 请求次数
            @HystrixProperty(name = "circuitBreaker.sleepWindowInMilliseconds", value = "10000"), // 时间窗口期
            @HystrixProperty(name = "circuitBreaker.errorThresholdPercentage", value = "60"),// 失败率达到多少后跳闸
    })
    public String paymentCircuitBreaker(Integer id) {
        if (id < 0) {
            throw new RuntimeException("******id 不能负数");
        }
        String serialNumber = IdUtil.simpleUUID();

        return Thread.currentThread().getName() + "\t" + "调用成功，流水号: " + serialNumber;
    }

    public String paymentCircuitBreaker_fallback(Integer id) {
        return "id 不能负数，请稍后再试，/(ㄒoㄒ)/~~   id: " + id;
    }

}
