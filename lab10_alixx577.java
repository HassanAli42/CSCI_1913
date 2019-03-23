class AssociationList<Key, Value>
{
    private class Node
    {
        private Key key;
        private Value value;
        private Node next;
        private Node(Key key, Value value, Node next)
        {
            this.key = key;
            this.value = value;
            this.next = next;
        }
    }

    public Node head;
    public Node left;
    public Node right;

    public AssociationList()
    {
        head = new Node(null, null, null );
    }

    public void delete(Key key)
    {
        if (isIn(key))
        {
            left.next = right.next;
        }
    }


    public Value get(Key key)
    {
        if (isIn(key))
        {
            return right.value;
        }
        else
        {
            throw new IllegalArgumentException();
        }
    }

    private boolean isEqual(Key leftKey, Key rightKey)
    {
        if ((leftKey != null) && (rightKey != null))
        {
            return leftKey.equals(rightKey);
        }
        else
        {
            return leftKey == rightKey;
        }
    }

    public boolean isIn(Key key)
    {
      left = head;
      right = head.next;
      while (right != null)
      {
          if (isEqual(right.key, key))
          {
              return true;
          }
          else
          {
              left = right;
              right = right.next;
          }
      }
      return false;
    }

    public void put(Key key, Value value)
    {
        if (isIn(key))
        {
            right.value = value;
        }
        else
        {
            Node temp = new Node(key, value, head.next);
            head.next = temp;
        }
    }

    public boolean isEmpty()
}

//
//  Tests for CSci 1913 Lab 10
//  James Moen
//  14 Nov 17
//
//  The TRY-CATCH statements catch exceptions thrown by ASSOCIATION LIST's
//  methods, so that the program can continue to run even if a method fails.
//  We still haven't talked about TRY-CATCH'es in lecture yet.
//
//  Each test has a comment that shows what it should print and how many points
//  it is worth, for a total of 40 points.

//  HOGWARTS. The Hogwarts dating service again.

class Hogwarts
{

//  MAIN. Make an instance of ASSOCIATION LIST and test it.

    public static void main(String[] args)
    {
        AssociationList<String,String> list = new AssociationList<String,String>();

        System.out.println(list.isIn(null));         //  false         2 points.

        try
        {
            System.out.println(list.get(null));
        }
        catch (IllegalArgumentException ignore)
        {
            System.out.println("No null");             //  No null       2 points.
        }

        list.put(null,        "Wormtail");
        list.put("Ron",       "Lavender");
        list.put("Voldemort", null);
        list.put("Dean",      "Ginny");

        System.out.println(list.isIn("Dean"));       //  true          2 points.
        System.out.println(list.isIn("Ginny"));      //  false         2 points.
        System.out.println(list.isIn("Ron"));        //  true          2 points.
        System.out.println(list.isIn("Voldemort"));  //  true          2 points.
        System.out.println(list.isIn(null));         //  true          2 points.
        System.out.println(list.isIn("Joanne"));     //  false         2 points.

        System.out.println(list.get("Ron"));         //  Lavender      2 points.
        System.out.println(list.get("Dean"));        //  Ginny         2 points.
        System.out.println(list.get("Voldemort"));   //  null          2 points.
        System.out.println(list.get(null));          //  Wormtail      2 points.

        try
        {
            System.out.println(list.get("Joanne"));
        }
        catch (IllegalArgumentException ignore)
        {
            System.out.println("No Joanne");           //  No Joanne     2 points.
        }

        list.delete(null);

        System.out.println(list.isIn(null));         //  false         2 points.

        list.put(null,    null);
        list.put("Harry", "Ginny");
        list.put("Ron",   "Hermione");

        System.out.println(list.isIn(null));         //  true          2 points.
        System.out.println(list.get(null));          //  null          2 points.
        System.out.println(list.get("Harry"));       //  Ginny         2 points.
        System.out.println(list.get("Dean"));        //  Ginny         2 points.
        System.out.println(list.get("Ron"));         //  Hermione      2 points.

        list.delete("Dean");

        try
        {
            System.out.println(list.get("Dean"));
        }
        catch (IllegalArgumentException ignore)
        {
            System.out.println("No Dean");             //  No Dean       2 points.
        }
    }
}
