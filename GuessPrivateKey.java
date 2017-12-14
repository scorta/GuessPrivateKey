import org.bitcoinj.core.*;
import org.bitcoinj.params.MainNetParams;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.PrintWriter;
import java.util.HashSet;

public class GuessPrivateKey{
    public static void main(String[] args){
        int nThread = Integer.parseInt(args[0]);
        String fileName = args[1];
        GuessKeyThread[] guessKeyThreads = new GuessKeyThread[nThread];

        System.out.println("Searching keys for Bitcoin address(es) from file " + fileName + " with " + nThread + " thread(s)");
        GuessKeyThread.readListAddress(fileName);
        for(int i = 0; i < nThread; ++i){
            guessKeyThreads[i] = new GuessKeyThread();
            guessKeyThreads[i].start();
        }
    }
}

class GuessKeyThread extends Thread {
    private static HashSet<String> bitAddress = new HashSet<>();
    private static NetworkParameters netParams = MainNetParams.get();

    public void run(){
        searchForKey();
    }

    public static void readListAddress(String file) {
        if(bitAddress.isEmpty()) {
            try {
                System.out.println("Start importing list Bitcoin address(es)");
                BufferedReader br = new BufferedReader(new FileReader(file));
                for (String line; (line = br.readLine()) != null; ) {
                    bitAddress.add(line);
                }
                System.out.println("Imported " + bitAddress.size() + " " + "address(es). Start searching...");
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

    private void searchForKey(){
        ECKey key;
        while (true){
            key = new ECKey();
            if(foundInSet(key) || foundInSet(key.decompress())){
                printResult(key);
            }
        }
    }
}
