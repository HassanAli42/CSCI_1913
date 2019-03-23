// Hassan Ali
// CSCI 1913
// Lab 13

class PriorityQueue<Base>
{
    private class Node
    {
        private Base object;
        private int  rank;
        private Node left;
        private Node right;

        private Node(Base object, int rank)
        {
            this.object = object;
            this.rank = rank;
            left = null;
            right = null;
        }
    }

    private Node root;  //  Root node of the BST.
    private Node head;
    private Node front;
    private Node rear;

    public PriorityQueue()
    {
        head = root = new Node(null,8);
    }

    public Base dequeue()
    {
        Node above = root;  //  The NODE immediately above BELOW.
        Node below = root.left;  //  The NODE we're visiting now.

        while (true)
        {
            if (below == null)
            {
                throw new IllegalStateException();
            }
            else
            {
                int rank = minKey();

                if (rank < below.rank)
                {
                    above = below;
                    below = below.left;
                }
                else if (rank > below.rank)
                {
                    above = below;
                    below = below.right;
                }
                else
                {
                    if (below.left == null)
                    {
                        if (above.left == below)
                        {
                            above.left = below.right;
                            return below.object;
                        }
                        else
                        {
                            above.right = below.right;
                            return below.object;
                        }
                    }
                    else
                    {
                        if (above.left == below)
                        {
                            above.left = below.left;
                            return below.object;
                        }
                        else
                        {
                            above.right = below.left;
                            return below.object;
                        }
                    }
                }
            }
        }
    }


    public void enqueue(Base object, int rank)
    {
        if(rank < 0)
        {
           throw new IllegalArgumentException();
        }
        else
        {
            Node temp = root;
            while(true)
            {
                if(rank <= temp.rank) {
                    if (temp.left == null)
                    {
                        temp.left = new Node(object, rank);
                        return;
                    }
                    else
                    {
                        temp = temp.left;
                    }
                }
                else
                {
                    if (temp.right == null)
                    {
                        temp.right = new Node(object, rank);
                        return;
                    }
                    else
                    {
                        temp = temp.right;
                    }
                }
            }
        }
    }


    public boolean isEmpty()
    {
        return head.left == null;
    }

    public int minKey()
    {
        if (root == null)
        {
            throw new IllegalStateException("BST is empty.");
        }
        else
        {
            Node temp = root;
            while (temp.left != null)
            {
                temp = temp.left;
            }
            return temp.rank;
        }
    }

}

//  SNOBBERY. How the aristocracy behaves in a queue. 20 points.

class Snobbery
{

//  MAIN. Queue them up.

    public static void main(String[] args)
    {
        PriorityQueue<String> queue = new PriorityQueue<String>();

        System.out.println(queue.isEmpty());  //  true        2 points

        try
        {
            System.out.println(queue.dequeue());
        }
        catch (IllegalStateException ignore)
        {
            System.out.println("Blimey!");      //  Blimey!     2 points
        }

        queue.enqueue("Lancelot",  5);
        queue.enqueue("Fawlty",    7);
        queue.enqueue("Elizabeth", 0);
        queue.enqueue("Charles",   1);
        queue.enqueue("Turing",    7);

        try
        {
            queue.enqueue("Zeus", -100);
        }
        catch (IllegalArgumentException ignore)
        {
            System.out.println("No gods!");     //  No gods!    2 points
        }

        System.out.println(queue.isEmpty());  //  false       2 points

        System.out.println(queue.dequeue());  //  Elizabeth   2 points
        System.out.println(queue.dequeue());  //  Charles     2 points
        System.out.println(queue.dequeue());  //  Lancelot    2 points
        System.out.println(queue.dequeue());  //  Turing      2 points
        System.out.println(queue.dequeue());  //  Fawlty      2 points

//  It's OK if Fawlty comes out before Turing, but both must come out last.

        System.out.println(queue.isEmpty());  //  true        2 points.
    }

}