import org.bitcoinj.core.*;
import org.bitcoinj.params.MainNetParams;
import org.omg.PortableInterceptor.SYSTEM_EXCEPTION;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.PrintWriter;
import java.math.BigInteger;
import java.util.HashSet;

public class GuessPrivateKey{
    public static void main(String[] args){
        if(args.length < 2 || args.length > 4){
            System.out.println("Wrong arguments. Please see instruction at https://github.com/scorta/GuessPrivateKey");
        } else {
            int nThread = Integer.parseInt(args[0]);
            String fileName = args[1];
            GuessKeyThread.CHOICE choice = GuessKeyThread.CHOICE.random;
            String start = "0";
            if (args.length >= 3) {
                choice = GuessKeyThread.CHOICE.valueOf(args[2]);
            }
            if(args.length == 4){
                start = args[3];
            }
            GuessKeyThread[] guessKeyThreads = new GuessKeyThread[nThread];

            System.out.println("Searching keys for Bitcoin address(es) from file " + fileName + " with " + nThread + " thread(s)");
            GuessKeyThread.readListAddress(fileName);
            GuessKeyThread.step = nThread;
            GuessKeyThread.choice = choice;

            for (int i = 0; i < nThread; ++i) {
                if(choice == GuessKeyThread.CHOICE.random){
                    guessKeyThreads[i] = new GuessKeyThread();
                    guessKeyThreads[i].start();
                } else {
                    BigInteger bigInteger = new BigInteger(start);
                    guessKeyThreads[i] = new GuessKeyThread(bigInteger.add(BigInteger.valueOf(i)));
                    guessKeyThreads[i].start();
                }
            }
        }
    }
}

class GuessKeyThread extends Thread {
    public enum CHOICE {random, up, down};
    public static int step;
    public static CHOICE choice;

    private static HashSet<String> bitAddress = new HashSet<>();
    private static NetworkParameters netParams = MainNetParams.get();

    private BigInteger start;

    GuessKeyThread(BigInteger start){
        this.start = start;
    }

    GuessKeyThread(){}

    public void run(){
        System.out.println("Run in \"" + choice + "\" mode, checking from: " + start);
        if(choice == CHOICE.random) {
            searchForKey();
        } else if(choice == CHOICE.up) {
            searchForKeyInRangeUpper();
        } else {
            searchForKeyInRangeLower();
        }
    }

    public static void readListAddress(String file) {
        if(bitAddress.isEmpty()) {
            try {
                System.out.println("Start importing list Bitcoin address(es)");
                BufferedReader br = new BufferedReader(new FileReader(file));
                for (String line; (line = br.readLine()) != null; ) {
                    bitAddress.add(line.split(";")[0]);
                }
                System.out.println("Imported " + bitAddress.size() + " " + "address(es)");
            } catch (Exception e) {
                System.out.println("Error at importing list Bitcoin Address. Error: " + e.toString());
            }
        }
    }

    private static boolean foundInSet(ECKey ecKey){
        if(bitAddress.contains(ecKey.toAddress(netParams).toString())){
            return true;
        } else {
            return false;
        }
    }

    private static void printResult(ECKey ecKey){
        System.out.println("Found a key!!!");

        String addr = ecKey.toAddress(netParams).toString();
        String keyWIF = ecKey.getPrivateKeyAsWiF(netParams);
        String keyHex = ecKey.getPrivateKeyAsHex();
        try {
            PrintWriter out = new PrintWriter(keyWIF + ".txt");
            out.println(addr);
            out.println(keyWIF);
            out.println(keyHex);
            out.close();

        } catch (Exception e){
            System.out.println("---");
            System.out.println("Output error: " + e.toString());
            System.out.println(addr + " | " + keyWIF + " | " + keyHex);
            System.out.println("---");
        }

    }

    private void checkKey(ECKey ecKey){
        if(foundInSet(ecKey) || foundInSet(ecKey.decompress())){
            printResult(ecKey);
        }
    }

    private void searchForKey(){
        ECKey key;
        while (true){
            key = new ECKey();
            checkKey(key);
        }
    }

    private void searchForKeyInRangeUpper(){
        ECKey key;
        for(BigInteger bigIntKey = start;; bigIntKey = bigIntKey.add(BigInteger.valueOf(step))){
            key = ECKey.fromPrivate(bigIntKey);
            checkKey(key);
        }
    }

    private void searchForKeyInRangeLower(){
        ECKey key;
        for(BigInteger bigIntKey = start;; bigIntKey = bigIntKey.subtract(BigInteger.valueOf(step))){
            key = ECKey.fromPrivate(bigIntKey);
            checkKey(key);
        }
    }
}
