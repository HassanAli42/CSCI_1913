//Hassan Ali
//CSCI 1913
//Lab #12

class FamilyTree
{
    private class Node
    {
        private String name;
        private Node father;
        private Node mother;

        private Node(Node father, String name, Node mother) {
            this.father = father;
            this.name = name;
            this.mother = mother;
        }
    }

    public Node root;

    public FamilyTree(String ego)
    {
        root = new Node(null, ego, null);
    }

    private Node find(String name)
    {
        return find(name, root);
    }

    private Node find(String name, Node root)
    {
        if(root == null)
        {
            return null;
        }
        else
        {
            if(name.equals(root.name))
            {
                return root;
            }
            else
            {
                Node tree1 = find(name, root.father);
                if (tree1 == null)
                {
                    return (find(name, root.mother));
                }
                else
                {
                    return (tree1);
                }
            }
        }
    }

    public void addParents(String ego, String father, String mother)
    {
        Node newp1 = find(ego);

        if (find(ego, root) == null)
        {
            throw new IllegalArgumentException();
        }

        else
        {
            newp1.father = new Node(null, father, null);
            newp1.mother = new Node(null, mother, null);
        }
    }

    public boolean isDescendant(String ego, String ancestor)
    {
        if (find(ego) != null && find(ancestor) != null)
        {
            return isDescendant(find(ego), find(ancestor));
        }
        else
        {
            return false;
        }
    }

    private boolean isDescendant(Node root, Node ancestor)
    {
        if (root == null)
        {
            return false;
        }
        else if (root == ancestor)
        {
            return true;
        }
        else
        {
            return (isDescendant(root.father, ancestor) || isDescendant(root.mother, ancestor));
        }
    }


}


class Pottery
{

//  MAIN. Harry Potter and the Hairier Pottery.

    public static void main(String [] args)
    {
        FamilyTree family = new FamilyTree("Al");

        family.addParents("Al",    "Harry",  "Ginny");
        family.addParents("Harry", "James",  "Lily" );
        family.addParents("Ginny", "Arthur", "Molly");

        try
        {
            family.addParents("Joanne", "Peter", "Anne");
        }
        catch (IllegalArgumentException ignore)
        {
            System.out.println("No Joanne.");  //  2 No Joanne.
        }

        System.out.println(family.isDescendant("Joanne", "Joanne"));  //  2 false

        System.out.println(family.isDescendant("Al", "Al"));          //  2 true
        System.out.println(family.isDescendant("Al", "Harry"));       //  2 true
        System.out.println(family.isDescendant("Al", "Ginny"));       //  2 true
        System.out.println(family.isDescendant("Al", "James"));       //  2 true
        System.out.println(family.isDescendant("Al", "Lily"));        //  2 true
        System.out.println(family.isDescendant("Al", "Arthur"));      //  2 true
        System.out.println(family.isDescendant("Al", "Molly"));       //  2 true
        System.out.println(family.isDescendant("Al", "Joanne"));      //  2 false

        System.out.println(family.isDescendant("Harry", "Harry"));    //  2 true
        System.out.println(family.isDescendant("Harry", "Al"));       //  2 false
        System.out.println(family.isDescendant("Harry", "James"));    //  2 true
        System.out.println(family.isDescendant("Harry", "Lily"));     //  2 true
        System.out.println(family.isDescendant("Harry", "Ginny"));    //  2 false
        System.out.println(family.isDescendant("Harry", "Arthur"));   //  2 false
        System.out.println(family.isDescendant("Harry", "Molly"));    //  2 false
        System.out.println(family.isDescendant("Harry", "Joanne"));   //  2 false

        System.out.println(family.isDescendant("Ginny", "Arthur"));   //  2 true
        System.out.println(family.isDescendant("Arthur", "Ginny"));   //  2 false
    }
}