package com.example.threadprectice;

public class MyRunnable implements Runnable {
    @Override
    public void run() {
        System.out.println("Doing heacy processing - START" + Thread.currentThread().getName());
        try {
            Thread.sleep(1000);
            doDBProcessing();
        } catch (InterruptedException e){
            e.printStackTrace();
        }

    }

    private void doDBProcessing() throws InterruptedException {
        Thread.sleep(5000);
    }
}
