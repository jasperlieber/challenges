package com.goatwalker.utils;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.Set;

public class Tree<T>
{

    private T head;

    private ArrayList<Tree<T>> leaves = new ArrayList<Tree<T>>();

    private Tree<T> parent = null;

    private HashMap<T, Tree<T>> locate = new HashMap<T, Tree<T>>();

    public Tree(T head)
    {
        this.head = head;
        locate.put(head, this);
    }

    public void addSubTree(Tree<T> subtree)
    {
        leaves.add(subtree);
        subtree.parent = this;
        for (Tree<T> par = subtree.parent; par != null; par = par.parent)
        {
//            System.out.println(
//                    "before putAll, locate = " + par.locate.toString());
            par.locate.putAll(subtree.locate);
//            System.out
//                    .println("after putAll, locate = " + par.locate.toString());
        }
    }

    public void addLeaf(T root, T leaf)
    {
        if (locate.containsKey(root))
        {
            locate.get(root).addLeaf(leaf);
        }
        else
        {
            addLeaf(root).addLeaf(leaf);
        }
    }

    public Tree<T> addLeaf(T leaf)
    {
        Tree<T> t = new Tree<T>(leaf);
        leaves.add(t);
        t.parent = this;
        t.locate = this.locate;
        locate.put(leaf, t);
        return t;
    }

    public Tree<T> setAsParent(T parentRoot)
    {
        Tree<T> t = new Tree<T>(parentRoot);
        t.leaves.add(this);
        this.parent = t;
        t.locate = this.locate;
        t.locate.put(head, this);
        t.locate.put(parentRoot, t);
        return t;
    }

    public T getHead()
    {
        return head;
    }

    public Tree<T> getTree(T element)
    {
        return locate.get(element);
    }

    public Tree<T> getParent()
    {
        return parent;
    }

    public Collection<T> getSuccessors(T root)
    {
        Collection<T> successors = new ArrayList<T>();
        Tree<T> tree = getTree(root);
        if (null != tree)
        {
            for (Tree<T> leaf : tree.leaves)
            {
                successors.add(leaf.head);
            }
        }
        return successors;
    }

    public Collection<Tree<T>> getSubTrees()
    {
        return leaves;
    }

    public static <T> Collection<T> getSuccessors(T of, Collection<Tree<T>> in)
    {
        for (Tree<T> tree : in)
        {
            if (tree.locate.containsKey(of))
            { return tree.getSuccessors(of); }
        }
        return new ArrayList<T>();
    }

    @Override
    public String toString()
    {
        return printTree(0);
    }

    private static final int indent = 2;

    public String printTree(int increment)
    {
        String s;
        String inc = "";
        for (int i = 0; i < increment; ++i)
        {
            inc = inc + " ";
        }
        s = inc + "{" + head;
        for (Tree<T> child : leaves)
        {
            s += "\n" + child.printTree(increment + indent);
        }
        return s + "}";
    }

    public Set<T> locations()
    {
        return locate.keySet();
    }
    

//    @SuppressWarnings("serial")
//    public class ParentCounts extends ArrayList<Pair<T, Integer>>
//    {
//        
//    }

    public int numParents(T body)
    {
        int count = 0;
        Tree<T> parent = getTree(body);

        if (parent != null) while (parent.getParent() != null)
        {
            count += 1;
            parent = getTree(parent.getParent().getHead());
            // System.out.println("found parent " + parent);
        }

        System.out.println("numParents of " + body + " is " + count);
        return count;
    }

    public ArrayList<Pair<T, Integer>> getParentCounts(T body)
    {
        ArrayList<Pair<T, Integer>> pc = new ArrayList<Pair<T, Integer>>();

        int count = 0;
        Tree<T> parent = getTree(body);

        if (parent != null) while (parent.getParent() != null)
        {
            count += 1;
            parent = getTree(parent.getParent().getHead());
            pc.add(new Pair<T, Integer>(parent.getHead(), count));
            // System.out.println("found parent " + parent);
        }

        System.out.println("ParentCounts of " + body + " is " + pc);
        return pc;
    }
}
