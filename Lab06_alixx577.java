/*
Hassan Ali
Lab #6
CSCI 1913
James Moen
 */


class BinaryVsLinear
{

    private static int linearSearch(int key, int[] array)
    {
        int count = 0;
        for (int index = 0; index < array.length; index+=1) {
            count++;
            if (key == array[index]) {
                return count;
                //return index; // success
            }
        }
        return -1; // failure
    }

    private static int binarySearch(int key, int[] array)
    {
        int left = 0;
        int mid;
        int right = array.length - 1;
        int count = 0;

        while(true)
        {
            if (left > right)
            {
                return -1;
            }
            else
            {
                mid = (left + right) / 2;
                count++;
                if (key < array[mid])
                {
                    right = mid - 1;
                }
                else if (key > array[mid])
                {
                    left = mid + 1;
                    count++;
                }
                else
                {
                    count++;
                    return count;
                    //return mid;
                }
            }
        }
    }

    public static void main(String[] args)
    {
        for (int length = 1; length <= 30; length += 1)
        {
            int[] array = new int[length];
            for (int index = 0; index < length; index += 1)
            {
                array[index] = index;
            }

            double linearTotal = 0.0;
            double binaryTotal = 0.0;
            for (int element = 0; element < length; element += 1)
            {
                linearTotal += linearSearch(element, array);
                binaryTotal += binarySearch(element, array);
            }

            double linearAverage = linearTotal / length;
            double binaryAverage = binaryTotal / length;
            System.out.println(length + " " + linearAverage + " " + binaryAverage);
        }
    }
}