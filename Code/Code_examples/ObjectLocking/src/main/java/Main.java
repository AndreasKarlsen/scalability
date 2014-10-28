/**
 * Created by Kasper on 10/28/2014.
 */
public class Main {

    public static void main(String [] args)
    {
        Account a1 = new Account(500);
        Account a2 = new Account(100);
        a1.debit(200);
        a2.credit(200);
    }
}
