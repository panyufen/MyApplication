package com.example.pan.mydemo.test;

import java.util.UUID;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.LinkedBlockingQueue;

/**
 * Created by PAN on 2016/12/10.
 */

public class JavaBlockingQueue {

    public static void main(String[] args) {
        BlockingQueue<Task> buffer = new LinkedBlockingQueue<>(Constants.MAX_BUFFER_SIZE);
        ExecutorService es = Executors.newFixedThreadPool(Constants.NUM_OF_CONSUMER + Constants.NUM_OF_PRODUCER);
        for (int i = 1; i <= Constants.NUM_OF_PRODUCER; ++i) {
            es.execute(new Producer(buffer));
            System.out.println("i "+i);
        }
        for (int i = 1; i <= Constants.NUM_OF_CONSUMER; ++i) {
            es.execute(new Consumer(buffer));
            System.out.println("2i "+i);
        }
    }


    /**
     * 工作任务
     *
     * @author 骆昊
     */
    static class Task {
        private String id;  // 任务的编号

        public Task() {
            id = UUID.randomUUID().toString();
        }

        @Override
        public String toString() {
            return "Task[" + id + "]";
        }
    }

    /**
     * 消费者
     *
     * @author 骆昊
     */
    static class Consumer implements Runnable {
        private BlockingQueue<Task> buffer;

        public Consumer(BlockingQueue<Task> buffer) {
            this.buffer = buffer;
        }

        @Override
        public void run() {
            while (true) {
                try {
                    Task task = buffer.take();
                    System.out.println("Consumer[" + Thread.currentThread().getName() + "] got " + task);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        }
    }

    /**
     * 生产者
     *
     * @author 骆昊
     */
    static class Producer implements Runnable {
        private BlockingQueue<Task> buffer;

        public Producer(BlockingQueue<Task> buffer) {
            this.buffer = buffer;
        }

        @Override
        public void run() {
            while (true) {
                try {
                    Task task = new Task();
                    buffer.put(task);
                    System.out.println("Producer[" + Thread.currentThread().getName() + "] put " + task);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }

            }
        }

    }

}