/*
Hassan Ali
CSCI sch1913
Project #2
 */


class Poly
{

    private class Term
    {
        int coef;
        int expo;
        Term next;
        private Term(int coef, int expo, Term next)
        {
            this.coef = coef;
            this.expo = expo;
            this.next = next;
        }
    }

    private Term head;

    public Poly()
    {
        head = new Term(0, 0, null);
    }

    public Poly term(int coef, int expo)
    {
        Term left;
        Term right;

        left = head;
        right = head.next;
        if (expo < 0)
        {
            throw new IllegalArgumentException();
        }
        else
        {
            if(right == null)
            {
                left.next = new Term(coef, expo, null);
                return this;
            }
            while (right != null)
            {
                if (expo > right.expo)
                {
                    left.next = new Term(coef, expo, right);
                    return this;
                }
                else if (expo < right.expo)
                {
                    left = right;
                    right = right.next;
                    continue;
                }
                else
                {
                    throw new IllegalArgumentException();
                }
            }
            left.next = new Term(coef, expo, null);
            return this;
        }
    }

    public Poly plus(Poly that)
    {
        Term right = head.next;
        Poly newPoly = new Poly();

        if (right == null)
        {
            return that;
        }

        while(right != null)
        {
            newPoly.term(right.coef, right.expo);
            right = right.next;
        }

        right = that.head.next;

        while(right != null)
        {
            newPoly.add(right.coef, right.expo);
            right = right.next;
        }
        return newPoly;
    }

    private void add(int coef, int expo)
    {
        Term left;
        Term right;
        left = head;
        right = head.next;
        if (expo < 0)
        {
            throw new IllegalArgumentException();
        }
        else
        {
            while (right != null)
            {
                if (expo > right.expo)
                {
                    left.next = new Term(coef, expo, right);
                    return;
                }
                else if (expo < right.expo)
                {
                    left = right;
                    right = right.next;
                    //continue;
                }
                else
                {
                    right.coef += coef;
                    if(right.coef == 0)
                    {
                        left.next = right.next;
                        return;
                    }
                    return;
                }
            }
        }
    }

    public Poly minus()
    {
        Term right;
        Poly newPoly = new Poly();
        right = head.next;
        while (right != null)
        {
            newPoly.term(-right.coef, right.expo);
            right = right.next;

        }
        return newPoly;
    }

    public String toString()
    {
        Term right = head.next;
        StringBuilder builder = new StringBuilder();

        if (right == null) {
            return "0";
        }
        else
        {
            builder.append(right.coef);
            builder.append('x');
            appendExponent(builder, right.expo);

            Term temp = right.next;

            while (temp != null)
            {
                if (temp.coef > 0)
                {
                    builder.append(" + ");
                    builder.append(temp.coef);
                    builder.append("x");
                    appendExponent(builder, temp.expo);
                    temp = temp.next;
                }
                else
                {
                    builder.append(" - ");
                    builder.append(Math.abs(temp.coef));
                    builder.append("x");
                    appendExponent(builder, temp.expo);
                    temp = temp.next;

                }
            }
        }
        return builder.toString();
    }

    //  APPEND EXPONENT. Append Unicode subscript digits for EXPO to BUILDER.

    private void appendExponent(StringBuilder builder, int expo)
    {
        if (expo < 0)
        {
            throw new IllegalStateException("Bad exponent " + expo + ".");
        }
        else if (expo == 0)
        {
            builder.append('⁰');
        }
        else
        {
            appendingExponent(builder, expo);
        }
    }

    //  APPENDING EXPONENT. Do all the work for APPEND EXPONENT when EXPO is not 0.

    private void appendingExponent(StringBuilder builder, int expo)
    {
        if (expo > 0)
        {
            appendingExponent(builder, expo / 10);
            builder.append("⁰¹²³⁴⁵⁶⁷⁸⁹".charAt(expo % 10));
        }
    }
}

class PollyEsther
{
    public static void main(String[] args)
    {
        Poly p0 = new Poly();
        Poly p5 = new Poly().term(0, 0);
        Poly p1 = new Poly().term(1, 3).term(1, 1).term(1, 2);
        Poly p2 = new Poly().term(2, 1).term(3, 2);
        Poly p3 = p2.minus();
        Poly p4 = new Poly().term(-2, 1).term(-3, 2);

        try
        {
            System.out.println(new Poly().term(-2, -3).term(-3, -2));
        }
        catch (IllegalArgumentException ignore)
        {
            System.out.println("Exponent is negative.");             // Exponent is negative.
        }


        System.out.println(p0);             //  0
        System.out.println(p1);             //  1x³ + 1x² + 1x¹
        System.out.println(p2);             //  3x² + 2x¹
        System.out.println(p3);             //  -3x² - 2x¹
        System.out.println(p4);             //  -3x² - 2x¹

        System.out.println(p1.plus(p2));    //  1x³ + 4x² + 3x¹

        System.out.println(p1.plus(p3));    //  1x³ - 2x² - 1x¹

        System.out.println(p2.plus(p3));    // 0

        System.out.println(p0.plus(p1));    //  1x³ + 1x² + 1x¹

        System.out.println(p1.plus(p0));    //  1x³ + 1x² + 1x¹


        System.out.println(p0.plus(p1.plus(p2.plus(p0.plus(p3.minus()))))); //  1x³ + 7x² + 5x¹

        System.out.println(p3.minus());     // 3x² + 2x¹

        System.out.println(p5.plus(p2));    // 3x² + 2x¹ - 0x⁰

        System.out.println(p1.minus().plus(p3.minus()));


    }
}

