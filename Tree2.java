/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package tree2;
import java.awt.*;
import java.awt.event.*;
import javax.swing.*;
import javax.swing.event.*;
import javax.swing.tree.*;
import javax.swing.JTextField;
/**
 *
 * @author Luke
 */
public class Tree2 {
    public static void main(String[] args) {
        JFrame frame = new JFrame("AVL Tree");
        frame.add(new TreeComponent());
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setSize(260,200);
        frame.setVisible(true);
    }
}