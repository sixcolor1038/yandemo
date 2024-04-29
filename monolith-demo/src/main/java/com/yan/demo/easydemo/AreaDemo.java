package com.yan.demo.easydemo;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class AreaDemo {

    public static void main(String[] args) {
        AreaDemo service = new AreaDemo();

        // Test cases
        TreeNode tree1 = service.getAdministrativeDivisionTree(1);
        printTree(tree1, 0);

        TreeNode tree2 = service.getAdministrativeDivisionTree(2);
        printTree(tree2, 0);

        TreeNode tree3 = service.getAdministrativeDivisionTree(3);
        printTree(tree3, 0);
    }

    private final Map<Integer, AdministrativeDivision> administrativeDivisions = new HashMap<>();

    public AreaDemo() {
        initializeAdministrativeDivisions();
    }

    private void initializeAdministrativeDivisions() {
        administrativeDivisions.put(1, new AdministrativeDivision("中国", 0));
        administrativeDivisions.put(2, new AdministrativeDivision("浙江省", 1));
        administrativeDivisions.put(3, new AdministrativeDivision("杭州市", 2));
        administrativeDivisions.put(4, new AdministrativeDivision("余杭区", 3));
        // Add more administrative divisions here
    }

    public TreeNode getAdministrativeDivisionTree(int id) {
        AdministrativeDivision division = administrativeDivisions.get(id);
        if (division == null) {
            throw new IllegalArgumentException("Invalid administrative division ID: " + id);
        }
        TreeNode root = new TreeNode(division.getName());
        buildTree(root, id);
        return root;
    }

    private void buildTree(TreeNode node, int parentId) {
        for (Map.Entry<Integer, AdministrativeDivision> entry : administrativeDivisions.entrySet()) {
            AdministrativeDivision division = entry.getValue();
            if (division.getParentId() == parentId) {
                TreeNode childNode = new TreeNode(division.getName());
                node.addChild(childNode);
                buildTree(childNode, entry.getKey());
            }
        }
    }


    private static void printTree(TreeNode root, int level) {
        StringBuilder indent = new StringBuilder();
        for (int i = 0; i < level; i++) {
            indent.append("  ");
        }
        System.out.println(indent + "-- " + root);
        for (TreeNode child : root.getChildren()) {
            printTree(child, level + 1);
        }
    }

    private static class AdministrativeDivision {
        private final String name;
        private final int parentId;

        public AdministrativeDivision(String name, int parentId) {
            this.name = name;
            this.parentId = parentId;
        }

        public String getName() {
            return name;
        }

        public int getParentId() {
            return parentId;
        }
    }

    private static class TreeNode {
        private final String value;
        private final List<TreeNode> children;

        public TreeNode(String value) {
            this.value = value;
            this.children = new ArrayList<>();
        }

        public void addChild(TreeNode child) {
            children.add(child);
        }

        @Override
        public String toString() {
            return value;
        }

        public List<TreeNode> getChildren() {
            return children;
        }
    }
}
