# GuessPrivateKey
This program searches Private Key(s) for given Bitcoin address(es)

## How does it do?

This program generates random Bitcoin Private Key, then check if it is the key for given address(es).

A Bitcoin Private Key is an integer between 1 and ~10^77, so to *find* a Private Key for a given address, we *just* need to generate a number in that range, and check if it is the key for that address. Sounds easy, right?

But the problem is, 10&77 is a big number. A really really big number (to compare, the total number of atoms in observable universe is about 10^82). If you are able to check a quintillion key (10^18) per second, you still need '3'170'979'198'376'458'650'431'253'170'979'198'376'458'650'431'253'170 years to check all the possible Bitcoin keys.

But if you are an extremely lucky person, or you have some quantum computers at home, then you could try.

Just in case you are serious about finding a key for a Bitcoin address with positive balance, you may want to try **[Large Bitcoin Collider](https://lbc.cryptoguru.org/about)**

[And there was much more efficient way to find a Private Key](https://www.deepdotweb.com/2017/06/09/bitcoin-brain-wallets-hackers-heaven/), but I am afraid that hackers have done long before.

## Room for improvement

**If Bitcoin Private Keys are truly random**, then doing a thorough search for a particular part is better than guessing randomly. **UPDATE: done!**

**Use GPU for calculation.** GPU may work much faster for this kind of task, but this program need to be rewritten in [C, C++, Fortran or Python](https://developer.nvidia.com/how-to-cuda-c-cpp). [Java could do the work](http://www.jcuda.org/), but I doubt if it is a good choice.

## Usage

Install java on your computer, download the **[GuessPrivateKey.jar](https://github.com/scorta/GuessPrivateKey/releases/tag/0.1)** file, then enter this command

`java -jar <this_program> <numbers of threads> <file_contains_bitcoin_addresses> <choice> <start_from>`

`<numbers of threads>`: number of threads, should be equal to your CPU cores (or less, if you want to use it for other tasks). Eg. my CPU has 8 cores, so I set `<Number of threads>` to 7 or 8.

`<choice>`: `random` means guessing keys randomly; `up` means checking for `<start_from>` and upper. `down` means going down from `<start_from>`.

`<start_from>`. Starting number. Default value is 0.

Eg.
`java -jar GuessPrivateKey.jar 7 bit.txt`

Running with 7 threads, and searching key(s) randomly for list of Bitcoin address(es) in the file `bit.txt`.

Eg.
`java -jar GuessPrivateKey.jar 8 bit.txt up 666`

Running with 8 threads, and searching key(s) sequentially (starting from 666 and upper) for list of Bitcoin address(es) in the file `bit.txt`.

If you have a big list of Bitcoin addresses (like, millions), you may need to use the `-Xmx` option.

## Output

If found a key, the program will display a message (**"Found a key!!!"**), and print info to a file, which has the same name to that WIF key.

Eg. if it found the key **"L59eMgqKqxCGXrDWhGwVBZbQBra482LRLzyAj6g5CGKdq6ABCvXz"**, it will create a file name **"L59eMgqKqxCGXrDWhGwVBZbQBra482LRLzyAj6g5CGKdq6ABCvXz.txt"** and write these info into it:
````
1zJBmcSDHZ97Sjm6TmtM5M7ZdPzuiBABm //address
L59eMgqKqxCGXrDWhGwVBZbQBra482LRLzyAj6g5CGKdq6ABCvXz //key in WIF format
EC9B83954E3E5001E34B02A1705779D2B59264BB54784815B3AA3EBC10C622F0 //key in HEX format
````
So if you find a new .txt file in the same folder with this program, you have found a wallet!

Note: if it cannot create file(s), it will print these info directly into the screen.

## Harmless Error

The program may give you the following error:

```
SLF4J: Failed to load class "org.slf4j.impl.StaticLoggerBinder".
SLF4J: Defaulting to no-operation (NOP) logger implementation
SLF4J: See http://www.slf4j.org/codes.html#StaticLoggerBinder for further details.
```
**Do not worry!** It is just an harmless error from the library bitcoinj. The program will still run fine.

## Compile yourself?

This program uses [bitcoinj](https://bitcoinj.github.io/) library, so please remember to include it.

## Tip?

BTC: 169HJrugesvXG4K7CHmoTqGruY6pKEaMWW

ETH: 0x573c8408d1b5a1fdec8f2bbfe206cd954977cbdd

LTC: LhYQgMpRtY3zGJAvFwmGa5oKMedYmHA7eW

Thank you so much :)
