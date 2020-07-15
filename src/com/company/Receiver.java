package com.company;

import java.awt.Frame;
import java.awt.Image;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.io.*;
import java.net.InetAddress;
import java.net.InetSocketAddress;
import java.net.Socket;
import java.net.SocketAddress;
import java.util.concurrent.TimeUnit;
import java.util.zip.ZipInputStream;
import javax.imageio.ImageIO;
import javax.swing.ImageIcon;
import javax.swing.JFrame;
import javax.swing.JLabel;

/*
 * @KnuthChao 20150625
 * 用于接收服务器端发送的信息，没有写多线程和鼠标的录制，后期有空 *会加上去；
 */

public class Receiver extends Thread {
    public window frame;
    public Socket socket;
    public String IP;

    public static void main(String[] args) {
        new Receiver(new window(), "127.0.0.1").start();
    }

    //接收图片
    public Receiver(window frame, String IP) {
        this.frame = frame;
        this.IP = IP;
    }

    //实现run方法
    public void run() {
        while (frame.getFlag()) {
            try {
                socket = new Socket(IP, 12122);
                DataInputStream ImgInput = new DataInputStream(socket.getInputStream());
                ZipInputStream imgZip = new ZipInputStream(ImgInput);

                imgZip.getNextEntry(); //到Zip文件流的开始处
                Image img = ImageIO.read(imgZip);  //按照字节读取Zip图片流里面的图片
                frame.jlbImg.setIcon(new ImageIcon(img));
                System.out.println("连接第" + (System.currentTimeMillis() / 1000) % 24 % 60 + "秒");
                frame.validate();
                TimeUnit.MILLISECONDS.sleep(1);// 接收图片间隔时间
                imgZip.close();


            } catch (IOException | InterruptedException e) {
                System.out.println("连接断开");
            } finally {
                try {
                    socket.close();
                } catch (IOException e) {
                }
            }
        }
    }
}

class window extends JFrame {
    private static final long serialVersionUID = 1L;
    public JLabel jlbImg;
    private boolean flag;

    public boolean getFlag() {
        return this.flag;
    }

    public window() {

        this.flag = true;
        this.jlbImg = new JLabel();

        this.setTitle("远程监控IP:" + "127.0.0.1");
        this.setSize(1366, 768);
        this.setAlwaysOnTop(true);  //显示窗口始终在最前面
        this.add(jlbImg);
        this.setLocationRelativeTo(null);
        this.setExtendedState(Frame.MAXIMIZED_BOTH);
        this.setDefaultCloseOperation(DISPOSE_ON_CLOSE);
        this.setVisible(true);
        this.validate();

        //窗口关闭事件
        this.addWindowListener(new WindowAdapter() {
            public void windowClosing(WindowEvent e) {
                flag = false;
                window.this.dispose();
                System.out.println("窗体关闭");
                System.gc();
            }
        });
    }
}
