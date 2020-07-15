package com.company;

import javax.swing.*;
public class Demo{
    public static void main(String args[]){
        JFrame mw = new JFrame("我的第一个窗口");
        mw.setSize(250,200);
        //mw.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        mw.setDefaultLookAndFeelDecorated(true);
        JButton button = new JButton("我是一个按钮");
        mw.getContentPane().add(button);
        //mw.pack();
        mw.setVisible(true);
    }
}


