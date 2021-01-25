package hao.you.springcloud.controller;

public class SingleDemo {

    private static volatile SingleDemo instant = null;

    private SingleDemo() {
        System.out.println(Thread.currentThread().getName()+"......构造方法");
    }

    public static SingleDemo getInstant() {
        if (instant == null) {
            synchronized (SingleDemo.class){
                if(instant == null){
                    instant = new SingleDemo();
                }
            }
        }
        return instant;
    }

    public static void main(String[] args) {
        for (int i = 1; i <= 2000000; i++) {
            new Thread(() -> {
                SingleDemo.getInstant();
            }, String.valueOf(i)).start();
        }
    }
}
