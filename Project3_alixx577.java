// Hassan Ali
// CSCI 1913
// Project #3


import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;

class Nomenclator
{
    private char              ch;                 //  Current CHAR from READER.
    private static final char eof = (char) 0x00;  //  End of file sentinel.
    private static final char eol = (char) 0x0A;  //  End of line sentinel.
    private int               index;              //  Index into LINE.
    private String            line;               //  Current LINE from READER.
    private boolean           listing;            //  Are we listing the file?
    private String            name;               //  Current name.
    private int               number;             //  Current line number.
    private String            path;               //  Pathname to READER's file.
    private BufferedReader    reader;             //  Read CHARs from here.

//  Constructor. Initialize a new NOMENCLATOR that reads from a text file whose
//  pathname is PATH. If we can't open it then throw an exception. LISTING says
//  whether we should copy the file to standard output as we read it.

    public Nomenclator(String path, boolean listing)
    {
        try
        {
            index = 0;
            line = "";
            this.listing = listing;
            number = 0;
            this.path = path;
            reader = new BufferedReader(new FileReader(path));
            skipChar();
        }
        catch (IOException ignore)
        {
            throw new IllegalArgumentException("Can't open '" + path + "'.");
        }
    }

//  HAS NEXT. Test if there's another name waiting to be read. If so, then read
//  it, so NEXT NAME and NEXT NUMBER can return it and its line number later.

    public boolean hasNext()
    {
        while (true)
        {
            if (Character.isJavaIdentifierStart(ch))
            {
                skipName();
                return true;
            }
            else if (Character.isDigit(ch))
            {
                skipNumber();
            }
            else
            {
                switch (ch)
                {
                    case eof:
                    {
                        return false;
                    }
                    case '"':
                    case '\'':
                    {
                        skipDelimited();
                        break;
                    }
                    case '/':
                    {
                        skipComment();
                        break;
                    }
                    default:
                    {
                        skipChar();
                        break;
                    }
                }
            }
        }
    }

//  NEXT NAME. If HAS NEXT was true, then return the next name. If HAS NEXT was
//  false, then return an undefined string.

    public String nextName()
    {
        return name;
    }

//  NEXT NUMBER. If HAS NEXT was true, then return the line number on which the
//  next name appears. If HAS NEXT was false, then return an undefined INT.

    public int nextNumber()
    {
        return number;
    }

//  SKIP CHAR. If no more CHARs remain unread in LINE, then read the next line,
//  adding an EOL at the end. If no lines can be read, then read a line with an
//  EOF char in it. Otherwise just read the next char from LINE and return it.

    private void skipChar()
    {
        if (index >= line.length())
        {
            index = 0;
            number += 1;
            try
            {
                line = reader.readLine();
                if (line == null)
                {
                    line = "" + eof;
                }
                else
                {
                    if (listing)
                    {
                        System.out.format("%05d ", number);
                        System.out.println(line);
                    }
                    line += eol;
                }
            }
            catch (IOException ignore)
            {
                line = "" + eof;
            }
        }
        ch = line.charAt(index);
        index += 1;
    }

//  SKIP COMMENT. We end up here if we read a '/'. If it is followed by another
//  '/', or by a '*', then we skip a comment. We must skip comments so that any
//  names that appear in them will be ignored.

    private void skipComment()
    {
        skipChar();
        if (ch == '*')
        {
            skipChar();
            while (true)
            {
                if (ch == '*')
                {
                    skipChar();
                    if (ch == '/')
                    {
                        skipChar();
                        return;
                    }
                }
                else if (ch == eof)
                {
                    return;
                }
                else
                {
                    skipChar();
                }
            }
        }
        else if (ch == '/')
        {
            skipChar();
            while (true)
            {
                if (ch == eof)
                {
                    return;
                }
                else if (ch == eol)
                {
                    skipChar();
                    return;
                }
                else
                {
                    skipChar();
                }
            }
        }
    }

//  SKIP DELIMITED. Skip a string constant or a character constant, so that any
//  names that appear inside them will be ignored.  Throw an exception if there
//  is a missing delimiter at the end.

    private void skipDelimited()
    {
        char delimiter = ch;
        skipChar();
        while (true)
        {
            if (ch == delimiter)
            {
                skipChar();
                return;
            }
            else
            {
                switch (ch)
                {
                    case eof:
                    case eol:
                    {
                        throw new IllegalStateException("Bad string in '" + path + "'.");
                    }
                    case '\\':
                    {
                        skipChar();
                        if (ch == eol || ch == eof)
                        {
                            throw new IllegalStateException("Bad string in '" + path + "'.");
                        }
                        else
                        {
                            skipChar();
                        }
                        break;
                    }
                    default:
                    {
                        skipChar();
                        break;
                    }
                }
            }
        }
    }

//  SKIP NAME. Skip a name, but convert it to a STRING, stored in NAME.

    private void skipName()
    {
        StringBuilder builder = new StringBuilder();
        while (Character.isJavaIdentifierPart(ch))
        {
            builder.append(ch);
            skipChar();
        }
        name = builder.toString();
    }

//  SKIP NUMBER. Skip something that might be a number. It starts with a digit,
//  followed by zero or more letters and digits. We must do this so the letters
//  aren't treated as names.

    private void skipNumber()
    {
        skipChar();
        while (Character.isJavaIdentifierPart(ch))
        {
            skipChar();
        }
    }

//  MAIN. Get a file pathname from the command line. Read a series of names and
//  their line numbers from the file, and write them one per line. For example,
//  the command "java Nomenclator Nomenclator.java" reads names from the source
//  file you are now looking at. This method is only for debugging!

    public static void main(String [] args)
    {
        Nomenclator reader = new Nomenclator(args[0], false);
        while (reader.hasNext())
        {
            System.out.println(reader.nextNumber() + " " + reader.nextName());
        }
    }
}

class BinarySearchTree<Key extends Comparable<Key>, Value>
{
//  NODE. The BST is built from these.

    private class Node
    {
        private Key   key;    //  The key. Duh.
        private LinkedQueue value;  //  The value associated with KEY.
        private Node  left;   //  Left subtree, whose KEY's are less than KEY.
        private Node  right;  //  Right subtree, whose KEY's are greater than KEY.

//  Constructor. Make a new NODE with KEY and VALUE. Its subtrees are NULL.

        private Node(Key key, LinkedQueue value)
        {
            this.key   = key;
            this.value = value;
            this.left  = null;
            this.right = null;
        }
    }

    private Node root;  //  Root NODE of the BST, or NULL.

//  Constructor. Make a new empty BST.

    public BinarySearchTree()
    {
        root = null;
    }

    public void add(int value, Key key)
    {
        if (root == null)
        {
            root = new Node(key, new LinkedQueue(value));
        }
        else
        {
            Node subtree = root;
            while (true)
            {
                int test = key.compareTo(subtree.key);
                if (test < 0)
                {
                    if (subtree.left == null)
                    {

                        subtree.left = new Node(key, new LinkedQueue(value));
                        return;
                    }
                    else
                    {
                        subtree = subtree.left;
                    }
                }
                else if (test > 0)
                {
                    if (subtree.right == null)
                    {
                        subtree.right = new Node(key, new LinkedQueue(value));
                        return;
                    }
                    else
                    {
                        subtree = subtree.right;
                    }
                }
                else
                {
                    subtree.value.enqueue(value);
                    return;
                }
            }
        }
    }

    public String toString()
    {
        StringBuilder builder = new StringBuilder();
        stringHelper(builder, root);
        return builder.toString();
    }


//  TO STRINGING. Do all the work for TO STRING. It's a preorder traversal.

    private void stringHelper(StringBuilder builder, Node subtree)
    {
        StringBuilder string = new StringBuilder();
        if (subtree != null)
        {
            stringHelper(builder, subtree.left);
            String stringKey = String.format("%-15s", subtree.key);
            string.append(stringKey);
            while(subtree.value.front != null)
            {
                String stringValue = String.format("%05d ", subtree.value.front.value());
                string.append(stringValue);
                subtree.value.front = subtree.value.front.next();
            }
            System.out.println(string);
            stringHelper(builder, subtree.right);
        }
    }
}

class LinkedQueue
{
    public class Node
    {
        private int value;
        private Node next;

        private Node(int value, Node next)
        {
            this.value = value;
            this.next = next;
        }

        public int value()
        {
            return value;
        }
        public Node next()
        {
            return next;
        }
    }

    public Node front;
    public Node rear;

    public LinkedQueue(int value)
    {
        front = rear  = new Node(value,null);
    }

    public boolean isEmpty()
    {
        return (front == null) && (rear == null);
    }

    public void enqueue(int num)
    {
        if(isEmpty())
        {
            front = rear = new Node(num,null);
        }
        else
        {
            if(rear.value != num)
            {
                rear.next = new Node(num,null);
                rear = rear.next;
            }
        }
    }
}

class crossReferenceTable
{
    public static void main(String [] args)
    {
        BinarySearchTree<String,Integer> tree = new BinarySearchTree<>();

        Nomenclator nomenclator = new Nomenclator("/Users/hassanali/IdeaProjects/test/src/Test.java", true);

        while (nomenclator.hasNext())
        {
            tree.add(nomenclator.nextNumber(), nomenclator.nextName());
        }
        System.out.println(tree);
    }
}

/*
Java test file

//  FACTORIALS. Print some factorials.

class Factorials
{

    //  FACTORIAL. Return the factorial of N.

    private static int factorial(int n)
    {
        if (n == 0)
        {
            return 1;
        }
        else
        {
            return n * factorial(n - 1);
        }
    }

    //  MAIN. Write the factorials of 0 through 10.

    public static void main(String [] args)
    {
        for (int k = 0; k <= 10; k += 1)
        {
            System.out.println(k + "! = " + factorial(k));
        }
    }
}
 */

/*
cross-reference table

/Library/Java/JavaVirtualMachines/jdk1.8.0_144.jdk/Contents/Home/bin/java "-javaagent:/Applications/IntelliJ IDEA.app/Contents/lib/idea_rt.jar=49981:/Applications/IntelliJ IDEA.app/Contents/bin" -Dfile.encoding=UTF-8 -classpath /Library/Java/JavaVirtualMachines/jdk1.8.0_144.jdk/Contents/Home/jre/lib/charsets.jar:/Library/Java/JavaVirtualMachines/jdk1.8.0_144.jdk/Contents/Home/jre/lib/deploy.jar:/Library/Java/JavaVirtualMachines/jdk1.8.0_144.jdk/Contents/Home/jre/lib/ext/cldrdata.jar:/Library/Java/JavaVirtualMachines/jdk1.8.0_144.jdk/Contents/Home/jre/lib/ext/dnsns.jar:/Library/Java/JavaVirtualMachines/jdk1.8.0_144.jdk/Contents/Home/jre/lib/ext/jaccess.jar:/Library/Java/JavaVirtualMachines/jdk1.8.0_144.jdk/Contents/Home/jre/lib/ext/jfxrt.jar:/Library/Java/JavaVirtualMachines/jdk1.8.0_144.jdk/Contents/Home/jre/lib/ext/localedata.jar:/Library/Java/JavaVirtualMachines/jdk1.8.0_144.jdk/Contents/Home/jre/lib/ext/nashorn.jar:/Library/Java/JavaVirtualMachines/jdk1.8.0_144.jdk/Contents/Home/jre/lib/ext/sunec.jar:/Library/Java/JavaVirtualMachines/jdk1.8.0_144.jdk/Contents/Home/jre/lib/ext/sunjce_provider.jar:/Library/Java/JavaVirtualMachines/jdk1.8.0_144.jdk/Contents/Home/jre/lib/ext/sunpkcs11.jar:/Library/Java/JavaVirtualMachines/jdk1.8.0_144.jdk/Contents/Home/jre/lib/ext/zipfs.jar:/Library/Java/JavaVirtualMachines/jdk1.8.0_144.jdk/Contents/Home/jre/lib/javaws.jar:/Library/Java/JavaVirtualMachines/jdk1.8.0_144.jdk/Contents/Home/jre/lib/jce.jar:/Library/Java/JavaVirtualMachines/jdk1.8.0_144.jdk/Contents/Home/jre/lib/jfr.jar:/Library/Java/JavaVirtualMachines/jdk1.8.0_144.jdk/Contents/Home/jre/lib/jfxswt.jar:/Library/Java/JavaVirtualMachines/jdk1.8.0_144.jdk/Contents/Home/jre/lib/jsse.jar:/Library/Java/JavaVirtualMachines/jdk1.8.0_144.jdk/Contents/Home/jre/lib/management-agent.jar:/Library/Java/JavaVirtualMachines/jdk1.8.0_144.jdk/Contents/Home/jre/lib/plugin.jar:/Library/Java/JavaVirtualMachines/jdk1.8.0_144.jdk/Contents/Home/jre/lib/resources.jar:/Library/Java/JavaVirtualMachines/jdk1.8.0_144.jdk/Contents/Home/jre/lib/rt.jar:/Library/Java/JavaVirtualMachines/jdk1.8.0_144.jdk/Contents/Home/lib/ant-javafx.jar:/Library/Java/JavaVirtualMachines/jdk1.8.0_144.jdk/Contents/Home/lib/dt.jar:/Library/Java/JavaVirtualMachines/jdk1.8.0_144.jdk/Contents/Home/lib/javafx-mx.jar:/Library/Java/JavaVirtualMachines/jdk1.8.0_144.jdk/Contents/Home/lib/jconsole.jar:/Library/Java/JavaVirtualMachines/jdk1.8.0_144.jdk/Contents/Home/lib/packager.jar:/Library/Java/JavaVirtualMachines/jdk1.8.0_144.jdk/Contents/Home/lib/sa-jdi.jar:/Library/Java/JavaVirtualMachines/jdk1.8.0_144.jdk/Contents/Home/lib/tools.jar:/Users/hassanali/IdeaProjects/Project3_alixx577/out/production/Project3_alixx577 crossReferenceTable
objc[54112]: Class JavaLaunchHelper is implemented in both /Library/Java/JavaVirtualMachines/jdk1.8.0_144.jdk/Contents/Home/bin/java (0x10455f4c0) and /Library/Java/JavaVirtualMachines/jdk1.8.0_144.jdk/Contents/Home/jre/lib/libinstrument.dylib (0x105df54e0). One of the two will be used. Which one is undefined.
00001 //  FACTORIALS. Print some factorials.
00002
00003 class Factorials
00004 {
00005
00006     //  FACTORIAL. Return the factorial of N.
00007
00008     private static int factorial(int n)
00009     {
00010         if (n == 0)
00011         {
00012             return 1;
00013         }
00014         else
00015         {
00016             return n * factorial(n - 1);
00017         }
00018     }
00019
00020     //  MAIN. Write the factorials of 0 through 10.
00021
00022     public static void main(String [] args)
00023     {
00024         for (int k = 0; k <= 10; k += 1)
00025         {
00026             System.out.println(k + "! = " + factorial(k));
00027         }
00028     }
00029 }
00030 //    public String toString()
00031 //    {
00032 //        StringBuilder builder = new StringBuilder();
00033 //        stringHelper(builder, root);
00034 //        return builder.toString();
00035 //    }
00036 //
00037 //
00038 ////  TO STRINGING. Do all the work for TO STRING. It's a preorder traversal.
00039 //
00040 //    private void stringHelper(StringBuilder builder, Node subtree)
00041 //    {
00042 //        StringBuilder string = new StringBuilder();
00043 //        if (subtree != null)
00044 //        {
00045 //            stringHelper(builder, subtree.left);
00046 //            String stringA = String.format("%-15s", subtree.key);
00047 //            string.append(stringA);
00048 //            while(subtree.value.front != null)
00049 //            {
00050 //                String stringB = String.format("%05d ", subtree.value.front.getValue());
00051 //                string.append(stringB);
00052 //                subtree.value.front = subtree.value.front.getNext();
00053 //            }
00054 //            System.out.println(string);
00055 //            stringHelper(builder, subtree.right);
00056 //        }
00057 //    }
00058 //
00059 //    "/Users/hassanali/IdeaProjects/test/src/Test.java"
00060
00061 // Hassan Ali
00062 // CSCI 1913
00063
00064 /*import java.io.BufferedReader;
00065         import java.io.FileReader;
00066         import java.io.IOException;
00067
00068 //  NOMENCLATOR. Read names from a Java source file.  It acts like an ITERATOR,
00069 //  but it has two NEXT methods: one for names, and one for the line numbers of
00070 //  those names.
00071
00072 class Nomenclator
00073 {
00074     private char              ch;                 //  Current CHAR from READER.
00075     private static final char eof = (char) 0x00;  //  End of file sentinel.
00076     private static final char eol = (char) 0x0A;  //  End of line sentinel.
00077     private int               index;              //  Index into LINE.
00078     private String            line;               //  Current LINE from READER.
00079     private boolean           listing;            //  Are we listing the file?
00080     private String            name;               //  Current name.
00081     private int               number;             //  Current line number.
00082     private String            path;               //  Pathname to READER's file.
00083     private BufferedReader reader;             //  Read CHARs from here.
00084
00085 //  Constructor. Initialize a new NOMENCLATOR that reads from a text file whose
00086 //  pathname is PATH. If we can't open it then throw an exception. LISTING says
00087 //  whether we should copy the file to standard output as we read it.
00088
00089     public Nomenclator(String path, boolean listing)
00090     {
00091         try
00092         {
00093             index = 0;
00094             line = "";
00095             this.listing = listing;
00096             number = 0;
00097             this.path = path;
00098             reader = new BufferedReader(new FileReader(path));
00099             skipChar();
00100         }
00101         catch (IOException ignore)
00102         {
00103             throw new IllegalArgumentException("Can't open '" + path + "'.");
00104         }
00105     }
00106
00107 //  HAS NEXT. Test if there's another name waiting to be read. If so, then read
00108 //  it, so NEXT NAME and NEXT NUMBER can return it and its line number later.
00109
00110     public boolean hasNext()
00111     {
00112         while (true)
00113         {
00114             if (Character.isJavaIdentifierStart(ch))
00115             {
00116                 skipName();
00117                 return true;
00118             }
00119             else if (Character.isDigit(ch))
00120             {
00121                 skipNumber();
00122             }
00123             else
00124             {
00125                 switch (ch)
00126                 {
00127                     case eof:
00128                     {
00129                         return false;
00130                     }
00131                     case '"':
00132                     case '\'':
00133                     {
00134                         skipDelimited();
00135                         break;
00136                     }
00137                     case '/':
00138                     {
00139                         skipComment();
00140                         break;
00141                     }
00142                     default:
00143                     {
00144                         skipChar();
00145                         break;
00146                     }
00147                 }
00148             }
00149         }
00150     }
00151
00152 //  NEXT NAME. If HAS NEXT was true, then return the next name. If HAS NEXT was
00153 //  false, then return an undefined string.
00154
00155     public String nextName()
00156     {
00157         return name;
00158     }
00159
00160 //  NEXT NUMBER. If HAS NEXT was true, then return the line number on which the
00161 //  next name appears. If HAS NEXT was false, then return an undefined INT.
00162
00163     public int nextNumber()
00164     {
00165         return number;
00166     }
00167
00168 //  SKIP CHAR. If no more CHARs remain unread in LINE, then read the next line,
00169 //  adding an EOL at the end. If no lines can be read, then read a line with an
00170 //  EOF char in it. Otherwise just read the next char from LINE and return it.
00171
00172     private void skipChar()
00173     {
00174         if (index >= line.length())
00175         {
00176             index = 0;
00177             number += 1;
00178             try
00179             {
00180                 line = reader.readLine();
00181                 if (line == null)
00182                 {
00183                     line = "" + eof;
00184                 }
00185                 else
00186                 {
00187                     if (listing)
00188                     {
00189                         System.out.format("%05d ", number);
00190                         System.out.println(line);
00191                     }
00192                     line += eol;
00193                 }
00194             }
00195             catch (IOException ignore)
00196             {
00197                 line = "" + eof;
00198             }
00199         }
00200         ch = line.charAt(index);
00201         index += 1;
00202     }
00203
00204 //  SKIP COMMENT. We end up here if we read a '/'. If it is followed by another
00205 //  '/', or by a '*', then we skip a comment. We must skip comments so that any
00206 //  names that appear in them will be ignored.
00207
00208     private void skipComment()
00209     {
00210         skipChar();
00211         if (ch == '*')
00212         {
00213             skipChar();
00214             while (true)
00215             {
00216                 if (ch == '*')
00217                 {
00218                     skipChar();
00219                     if (ch == '/')
00220                     {
00221                         skipChar();
00222                         return;
00223                     }
00224                 }
00225                 else if (ch == eof)
00226                 {
00227                     return;
00228                 }
00229                 else
00230                 {
00231                     skipChar();
00232                 }
00233             }
00234         }
00235         else if (ch == '/')
00236         {
00237             skipChar();
00238             while (true)
00239             {
00240                 if (ch == eof)
00241                 {
00242                     return;
00243                 }
00244                 else if (ch == eol)
00245                 {
00246                     skipChar();
00247                     return;
00248                 }
00249                 else
00250                 {
00251                     skipChar();
00252                 }
00253             }
00254         }
00255     }
00256
00257 //  SKIP DELIMITED. Skip a string constant or a character constant, so that any
00258 //  names that appear inside them will be ignored.  Throw an exception if there
00259 //  is a missing delimiter at the end.
00260
00261     private void skipDelimited()
00262     {
00263         char delimiter = ch;
00264         skipChar();
00265         while (true)
00266         {
00267             if (ch == delimiter)
00268             {
00269                 skipChar();
00270                 return;
00271             }
00272             else
00273             {
00274                 switch (ch)
00275                 {
00276                     case eof:
00277                     case eol:
00278                     {
00279                         throw new IllegalStateException("Bad string in '" + path + "'.");
00280                     }
00281                     case '\\':
00282                     {
00283                         skipChar();
00284                         if (ch == eol || ch == eof)
00285                         {
00286                             throw new IllegalStateException("Bad string in '" + path + "'.");
00287                         }
00288                         else
00289                         {
00290                             skipChar();
00291                         }
00292                         break;
00293                     }
00294                     default:
00295                     {
00296                         skipChar();
00297                         break;
00298                     }
00299                 }
00300             }
00301         }
00302     }
00303
00304 //  SKIP NAME. Skip a name, but convert it to a STRING, stored in NAME.
00305
00306     private void skipName()
00307     {
00308         StringBuilder builder = new StringBuilder();
00309         while (Character.isJavaIdentifierPart(ch))
00310         {
00311             builder.append(ch);
00312             skipChar();
00313         }
00314         name = builder.toString();
00315     }
00316
00317 //  SKIP NUMBER. Skip something that might be a number. It starts with a digit,
00318 //  followed by zero or more letters and digits. We must do this so the letters
00319 //  aren't treated as names.
00320
00321     private void skipNumber()
00322     {
00323         skipChar();
00324         while (Character.isJavaIdentifierPart(ch))
00325         {
00326             skipChar();
00327         }
00328     }
00329
00330 //  MAIN. Get a file pathname from the command line. Read a series of names and
00331 //  their line numbers from the file, and write them one per line. For example,
00332 //  the command "java Nomenclator Nomenclator.java" reads names from the source
00333 //  file you are now looking at. This method is only for debugging!
00334
00335     public static void main(String [] args)
00336     {
00337         Nomenclator reader = new Nomenclator(args[0], false);
00338         while (reader.hasNext())
00339         {
00340             System.out.println(reader.nextNumber() + " " + reader.nextName());
00341         }
00342     }
00343 }
00344
00345 class BinarySearchTree<Key extends Comparable<Key>, Value>
00346 {
00347 //  NODE. The BST is built from these.
00348
00349     private class Node
00350     {
00351         private Key   key;    //  The key. Duh.
00352         private LinkedQueue value;  //  The value associated with KEY.
00353         private Node  left;   //  Left subtree, whose KEY's are less than KEY.
00354         private Node  right;  //  Right subtree, whose KEY's are greater than KEY.
00355
00356 //  Constructor. Make a new NODE with KEY and VALUE. Its subtrees are NULL.
00357
00358         private Node(Key key, LinkedQueue value)
00359         {
00360             this.key   = key;
00361             this.value = value;
00362             this.left  = null;
00363             this.right = null;
00364         }
00365     }
00366
00367     private Node root;  //  Root NODE of the BST, or NULL.
00368
00369 //  Constructor. Make a new empty BST.
00370
00371     public BinarySearchTree()
00372     {
00373         root = null;
00374     }
00375
00376
00377     public void add(Value value, Key key)
00378     {
00379         if (root == null)
00380         {
00381             root = new Node(key, new LinkedQueue<Value>(value));
00382         }
00383         else
00384         {
00385             Node subtree = root;
00386             while (true)
00387             {
00388                 int test = key.compareTo(subtree.key);
00389                 if (test < 0)
00390                 {
00391                     if (subtree.left == null)
00392                     {
00393
00394                         subtree.left = new Node(key, new LinkedQueue<Value>(value));
00395                         return;
00396                     }
00397                     else
00398                     {
00399                         subtree = subtree.left;
00400                     }
00401                 }
00402                 else if (test > 0)
00403                 {
00404                     if (subtree.right == null)
00405                     {
00406                         subtree.right = new Node(key, new LinkedQueue<Value>(value));
00407                         return;
00408                     }
00409                     else
00410                     {
00411                         subtree = subtree.right;
00412                     }
00413                 }
00414                 else
00415                 {
00416                     //System.out.println(subtree.key);
00417                     subtree.value.enqueue(value);
00418                     return;
00419                 }
00420             }
00421         }
00422     }
00423
00424     public String toString()
00425     {
00426         StringBuilder builder = new StringBuilder();
00427         stringHelper(builder, root);
00428         return builder.toString();
00429     }
00430
00431
00432 //  TO STRINGING. Do all the work for TO STRING. It's a preorder traversal.
00433
00434     private void stringHelper(StringBuilder builder, Node subtree)
00435     {
00436         StringBuilder string = new StringBuilder();
00437         if (subtree != null)
00438         {
00439             stringHelper(builder, subtree.left);
00440             String stringA = String.format("%-15s", subtree.key);
00441             string.append(stringA);
00442             while(subtree.value.front != null)
00443             {
00444                 String stringB = String.format("%05d ", subtree.value.front.getValue());
00445                 string.append(stringB);
00446                 subtree.value.front = subtree.value.front.getNext();
00447             }
00448             System.out.println(string);
00449             stringHelper(builder, subtree.right);
00450         }
00451     }
00452 }
00453
00454
00455
00456 class LinkedQueue<Base>
00457 {
00458     public class Node
00459     {
00460         private Base num;
00461         private Node next;
00462
00463         private Node(Base num, Node next)
00464         {
00465             this.num = num;
00466             this.next = next;
00467         }
00468
00469         public Base getValue()
00470         {
00471             return num;
00472         }
00473         public Node getNext()
00474         {
00475             return next;
00476         }
00477     }
00478
00479     public Node front;
00480     public Node rear;
00481
00482     public LinkedQueue(Base value)
00483     {
00484         front = rear  = new Node(value,null);
00485
00486     }
00487
00488     public boolean isEmpty()
00489     {
00490         return front == null && rear == null;
00491     }
00492
00493     public void enqueue(Base base)
00494     {
00495         if(isEmpty())
00496         {
00497             front = rear = new Node(base,null);
00498         }
00499         else
00500         {
00501             if(rear.num != base)
00502             {
00503                 rear.next = new Node(base,null);
00504                 rear = rear.next;
00505             }
00506         }
00507     }
00508 }
00509
00510
00511
00512
00513 class crossReferenceTable
00514 {
00515
00516
00517     public static void main(String [] args)
00518     {
00519
00520         BinarySearchTree<String,Integer> tree = new BinarySearchTree<>();
00521
00522         Nomenclator nomenclator = new Nomenclator("/Users/hassanali/IdeaProjects/test/src/Test.java", false);
00523
00524         while (nomenclator.hasNext())
00525         {
00526             tree.add(nomenclator.nextNumber(), nomenclator.nextName());
00527         }
00528         System.out.println(tree);
00529     }
00530 }

cross reference table

Factorials     00003
String         00022
System         00026
args           00022
class          00003
else           00014
factorial      00008 00016 00026
for            00024
if             00010
int            00008 00024
k              00024 00026
main           00022
n              00008 00010 00016
out            00026
println        00026
private        00008
public         00022
return         00012 00016
static         00008 00022
void           00022
*/
