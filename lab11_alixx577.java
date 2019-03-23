/*
Hassan Ali
CSCI 1913
Lab 11
*/
class Deque<Base>
{
    private class Node
    {
        private Base object;
        private Node left;
        private Node right;

        private Node(Node left, Base object, Node right)
        {
            this.left = left;
            this.object = object;
            this.right = right;
        }
    }

    private Node head;

    public Deque()
    {
        head = new Node(null,null,null);
        head.right = head;
        head.left = head;
    }

    public void enqueueFront(Base object)
    {
        Node q = new Node(head, object, head.right);
        head.right.left = q;
        head.right = q;
    }

    public void enqueueRear(Base object)
    {
        Node q = new Node(head.left, object, head);
        head.left.right = q;
        head.left = q;
    }

    public Base dequeueFront()
    {
        Base temp;
        Node iterate;
        if(isEmpty())
        {
            throw new IllegalStateException();
        }
        else
        {
            iterate = head.right;
            temp = head.right.object;
            iterate.right.left = iterate.left;
            iterate.left.right = iterate.right;
            return temp;
        }
    }

    public Base dequeueRear()
    {
        Base temp;
        Node iterate;
        if(isEmpty())
        {
            throw new IllegalStateException();
        }
        else
        {
            iterate = head.left;
            temp = head.left.object;
            iterate.left.right = iterate.right;
            iterate.right.left = iterate.left;
            return temp;
        }
    }

    public boolean isEmpty()
    {
        return ((head.left == head) && (head.right == head));
    }
}

//  OBSERVATION DEQUE. Test the class DEQUE. 40 points total.

class ObservationDeque
{

//  MAIN. Test the DEQUE on various example arguments.

    public static void main(String [] args)
    {
        Deque<String> deque = new Deque<String>();

        System.out.println(deque.isEmpty());       // true                2 points.

        try
        {
            System.out.println(deque.dequeueFront());
        }
        catch (IllegalStateException ignore)
        {
            System.out.println("No dequeueFront.");  //  No dequeueFront.   2 points.
        }

        try
        {
            System.out.println(deque.dequeueRear());
        }
        catch (IllegalStateException ignore)
        {
            System.out.println("No dequeueRear.");   //  No dequeueRear.    2 points.
        }

//  Enqueueing to the rear and dequeueing from the rear makes the DEQUE act
//  like a stack.

        deque.enqueueRear("A");
        deque.enqueueRear("B");
        deque.enqueueRear("C");

        System.out.println(deque.isEmpty());       //  false              2 points.

        System.out.println(deque.dequeueRear());   //  C                  2 points.
        System.out.println(deque.dequeueRear());   //  B                  2 points.
        System.out.println(deque.dequeueRear());   //  A                  2 points.

        System.out.println(deque.isEmpty());       //  true               2 points.

//  Enqueueing to the rear and dequeueing from the front makes the DEQUE act
//  like a queue.

        deque.enqueueRear("A");
        deque.enqueueRear("B");
        deque.enqueueRear("C");

        System.out.println(deque.dequeueFront());  //  A                  2 points.
        System.out.println(deque.dequeueFront());  //  B                  2 points.
        System.out.println(deque.dequeueFront());  //  C                  2 points.

        System.out.println(deque.isEmpty());       //  true               2 points.

//  Enqueueing to the front and dequeueing from the front makes the DEQUE act
//  like a stack.

        deque.enqueueFront("A");
        deque.enqueueFront("B");
        deque.enqueueFront("C");

        System.out.println(deque.dequeueFront());  //  C                  2 points.
        System.out.println(deque.dequeueFront());  //  B                  2 points.
        System.out.println(deque.dequeueFront());  //  A                  2 points.

        System.out.println(deque.isEmpty());       //  true               2 points.

//  Enqueueing to the front and dequeueing from the rear makes the DEQUE act
//  like a queue.

        deque.enqueueFront("A");
        deque.enqueueFront("B");
        deque.enqueueFront("C");

        System.out.println(deque.dequeueRear());   //  A                  2 points.
        System.out.println(deque.dequeueRear());   //  B                  2 points.
        System.out.println(deque.dequeueRear());   //  C                  2 points.

        System.out.println(deque.isEmpty());       //  true               2 points.

    }
}
