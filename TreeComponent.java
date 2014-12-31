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
// Each instance of TreeComponent will be populated with seven nodes.
public class TreeComponent extends JComponent
{   
    TreeNode root;
    TreeNode one;
    TreeNode two;
    TreeNode three;
    TreeNode four;
    TreeNode five;
    TreeNode six;
    TreeNode currentNode;
    TreeNode node;
    TreeNode parent;
    TreeNode child;
//  Each node will have a left child, right child, parent, data, and an AVL
//  integer value, which denotes the difference of balance between the left
//  and right sides of the node. A negative AVL value means the node is off 
//  balanced in favor of the left side, while a positive one, the right side.
//  Keeping track of these AVL values is essential to a functioning AVL binary
//  search tree.
    private class TreeNode {
        TreeNode left;
        int data;
        TreeNode right;
        int AVL;
        TreeNode parent;
        
        public TreeNode(TreeNode left,int data, TreeNode right,int AVL, TreeNode parent){
            this.left = left;
            this.data = data;
            this.right = right;
            this.AVL = AVL;
            this.parent = parent;
        }
    }
        
    public TreeComponent(){
        TreeNode root = new TreeNode(null,200,null,0,null);
        TreeNode one = new TreeNode(null,100,null,0,root);
        TreeNode two = new TreeNode(null,300,null,0,root);
        TreeNode three = new TreeNode(null,50,null,0,one);
        TreeNode four = new TreeNode(null,150,null,0,one);
        TreeNode five = new TreeNode(null,250,null,0,two);
        TreeNode six = new TreeNode(null,350,null,0,two);
        root.left = one;
        root.right = two;
        one.left = three;
        one.right = four;
        two.left = five;
        two.right = six;
        
        JTextField numberField = new JTextField();
        
        JButton printButton = new JButton("Print All");
        printButton.addActionListener(new ActionListener(){
            public void actionPerformed(ActionEvent e){
                if(root.parent!=null){
                    getRoot(root);
                }else printAll(root);
            }
        });

        JButton addButton = new JButton("Add Node");
        addButton.addActionListener(new ActionListener(){
            public void actionPerformed(ActionEvent e){
                int data = Integer.parseInt(numberField.getText());
                if(root.parent!=null){
                    getRootAdd(data,root);
                }else addNode(data,root);
            }
        });
        
        setLayout(new FlowLayout());
        JPanel addPanel = new JPanel(new GridLayout(1,2));
        addPanel.add(numberField);
        addPanel.add(addButton);
        JPanel userPanel = new JPanel(new GridLayout(2,1));
        userPanel.add(addPanel);
        userPanel.add(printButton);
        add(userPanel,BorderLayout.SOUTH);
    }
//  Method prints two values of each node. (data, AVL)
    public void printAll(TreeNode currentNode){
        if(currentNode!=null){
            printAll(currentNode.left);
            System.out.println(currentNode.data +", "+ currentNode.AVL);
            printAll(currentNode.right);
        }
    }
//  addNode() inserts a new node at the appropriate spot in the binary search 
//  tree. If the input is a repeated value, this function does nothing.
//  After adding the node, the avlIterator() function is called.
    public void addNode(int data, TreeNode currentNode){
        if(data == currentNode.data) return;
        if(data < currentNode.data){
            if(currentNode.left==null) {
                TreeNode node = new TreeNode(null,data,null,0,currentNode);
                currentNode.left = node;
                currentNode.AVL--;
                avlIterator(currentNode,currentNode);
            }else addNode(data,currentNode.left);
        }
        if(data > currentNode.data) {
            if(currentNode.right==null) {
                TreeNode node = new TreeNode(null,data,null,0,currentNode);
                currentNode.right = node;
                currentNode.AVL++;
                avlIterator(currentNode,currentNode);
            }else addNode(data,currentNode.right);
        }
    }
//  avlIterator() iterates through all of the parent nodes affected by the added
//  node and updates the value of each affected AVL integer value. After all
//  necessary updates are made, avlChecker() is called.
    public void avlIterator(TreeNode node,TreeNode originalParent){
        parent = node.parent;
        if(parent==null){
            avlChecker(originalParent);
            return;
        }
        if(node.AVL==0){
            avlChecker(originalParent);
            return;
        }else{
            if(parent.left==node){
                parent.AVL--;
                avlIterator(parent,originalParent);
            }else{
                parent.AVL++;
                avlIterator(parent,originalParent);
            }
        }
    }
//  avlChecker() checks if any of the parent nodes of the added node is greater
//  than 1 or less than -1, which denotes an unbalanced tree. If the tree is
//  unbalanced, avlDecider() is called.
    public void avlChecker(TreeNode node){
        parent = node.parent;
        if(parent==null)return;
        if(parent.AVL<-1) avlDecider(parent);
        else if(parent.AVL>1) avlDecider(parent);
        else avlChecker(parent);
    }
//  avlDecider() checks if a double rotation or a single rotation is needed, in
//  order to rebalance the tree. This function calls one of four functions:
//  single or double left rotations or single or double right rotations.
    public void avlDecider(TreeNode node){
        if(node.AVL==-2){
            if(node.left.AVL>0) avlDLeft(node.left);
            else avlLeft(node);
        }else {
            if(node.right.AVL<0) avlDRight(node.right);
            else avlRight(node);
        }
    }
//  avlLeft() is called when a single left rotation is needed for rebalancing.
//  It then executes the rotation.
    public void avlLeft(TreeNode node){
        child = node.left;
        parent = node.parent;
        if(parent==null){
            changeRoot(node);
            return;
        }
        
        if(child.right!=null) node.left = child.right;
        else node.left = null;
        if(parent.left==node) parent.left = child;
        else parent.right = child;
        child.parent = parent;
        child.right = node;
        node.parent = child;
        child.AVL = 0;
        node.AVL = 0;
        avlUpdater(child);
    }
//  avlDLeft() is called when a double left rotation is needed for rebalancing.
//  It then executes the first rotation of the double rotation, and then proceeds
//  to call avlLeft() for the second rotation.
    public void avlDLeft(TreeNode node){
        parent = node.parent;
        child = node.right;
        
        if(child.left!=null) node.right = child.left;
        else node.right = null;
        child.left = node;
        node.parent = child;
        parent.left = child;
        child.parent = parent;
        node.AVL = 0;
        if(child.AVL>0) node.AVL--;
        child.AVL = -1;
        avlLeft(parent);
    }
//  avlRight() is called when a single right rotation is needed for rebalancing.
//  It then executes the rotation.
    public void avlRight(TreeNode node) {
        child = node.right;
        parent = node.parent;
        if(parent==null){
            changeRoot(node);
            return;
        }
        
        if(child.left!=null) node.right = child.left;
        else node.right = null;
        if(parent.right==node) parent.right=child;
        else parent.left=child;
        child.parent = parent;
        child.left = node;
        node.parent = child;
        node.AVL = 0;
        child.AVL = 0;
        avlUpdater(child);
    }
//  avlDRight() is called when a double right rotation is needed for rebalancing.
//  It then executes the first rotation of the double rotation, and then proceeds
//  to call avlRight() for the second rotation.
    public void avlDRight(TreeNode node){
        child = node.left;
        parent = node.parent;
        
        if(child.right!=null) node.left = child.right;
        else node.left = null;
        child.right = node;
        node.parent = child;
        parent.right = child;
        child.parent = parent;
        node.AVL = 0;
        if(child.AVL<0) node.AVL++;
        child.AVL = 1;
        
        avlRight(parent);
    }
//  avlUpdater() is called after a rebalancing. It updates the affected AVL
//  integer values.
    public void avlUpdater(TreeNode node){
        parent = node.parent;
        if(parent==null)return;
        if(parent.left==node){
            parent.AVL++;
            if(parent.AVL==1)return;
            else avlUpdater(parent);
        }else{
            parent.AVL--;
            if(parent.AVL==-1)return;
            else avlUpdater(parent);
        }
    }
//  When the root needs to be changed in order to balance the tree, changeRoot()
//  is called.
    public void changeRoot(TreeNode root){
        if(root.AVL<0){
            node = root.left;
            child = node.right;
            
            root.left = child;
            child.parent = root;
            node.right = root;
            root.parent = node;
            node.parent = null;
            root.AVL = 0;
            node.AVL = 0;
        }else{
            node = root.right;
            child = node.left;
            
            root.right = child;
            child.parent = root;
            node.left = root;
            root.parent = node;
            node.parent = null;
            root.AVL = 0;
            node.AVL = 0;
        }
    }
//  Admittedly, this part is a bit messy. When the root is changed for
//  rebalancing purposes, the global root of TreeComponent doesn't get updated.
//  These two functions are called to get the correct root.
    public void getRoot(TreeNode node){
        parent = node.parent;
        if(node.parent==null) printAll(node);
        else getRoot(parent);
    }
    public void getRootAdd(int data,TreeNode node){
        parent = node.parent;
        if(node.parent==null) addNode(data,node);
        else getRootAdd(data,parent);
    }
}
