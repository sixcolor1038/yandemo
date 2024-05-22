package com.yan.demo.leetcodedemo;

/**
 * @Author: sixcolor
 * @Date: 2024-05-18 17:31
 * @Description: 翻转二叉树
 */
public class BinaryTreeInverter {

    // 测试方法
    public static void main(String[] args) {
        // 构建一个二叉树
        TreeNode root = new TreeNode(4);
        root.left = new TreeNode(2);
        root.right = new TreeNode(7);
        root.left.left = new TreeNode(1);
        root.left.right = new TreeNode(3);
        root.right.left = new TreeNode(6);
        root.right.right = new TreeNode(9);

        // 翻转二叉树
        BinaryTreeInverter inverter = new BinaryTreeInverter();
        TreeNode invertedRoot = inverter.invertTree(root);

        // 打印翻转后的二叉树
        printTree(invertedRoot);
    }

    public TreeNode invertTree(TreeNode root) {
        if (root == null) return null;
        // 保存右子树
        TreeNode rightTree = root.right;
        // 交换左右子树的位置
        root.right = invertTree(root.left);
        root.left = invertTree(rightTree);
        return root;
    }

    // 打印二叉树
    private static void printTree(TreeNode root) {
        if (root == null) return;
        System.out.print(root.val + " ");
        printTree(root.left);
        printTree(root.right);
    }

    static class TreeNode {
        int val;
        TreeNode left;
        TreeNode right;

        TreeNode(int x) {
            val = x;
        }
    }
}
