# GuessPrivateKey
This program searches Private Key(s) for given Bitcoin address(es)

## How does it do?

This program generates random Bitcoin Private Key, then check if it is the key for given address(es).

A Bitcoin Private Key is an integer between 0 and 2^256, so to *find* a Private Key for a given address, we *just* need to generate a number between 0 and 2^256, and check if it is the key for that address. Sounds easy, right?

But the problem is, 2^256 is a big number. A really really big number (to compare, the total number of atoms in observable universe is about 10^82 = 86361 * 2^256). If you are able to check a quintillion key (10^18) per second, you still need 3'671'743'063'080'802'746'815'416'825'491'118'336'290'905'145'409'708 years to check all the possible Bitcoin keys. FYI, my CPU (Xeon E3 1231 v3) is able to check ~20000 keys/second (= 2 * 10^4).

But if you are an extremely lucky person, or you have some quantum computers at home, then you could try.

Just in case you are serious about finding a key for a Bitcoin address with positive balance, you may want to try **[Large Bitcoin Collider](https://lbc.cryptoguru.org/about)**

[And there was much more efficient way to find a Private Key](https://www.deepdotweb.com/2017/06/09/bitcoin-brain-wallets-hackers-heaven/), but I am afraid that hackers have done long before.

## Room for improvement

**If Bitcoin Private Keys are truly random**, then doing a thorough search for a particular part is better than guessing randomly. Eg. currently, there are ~8x10^6 Bitcoin addresses with 0.005BTC or more; so we should divide 2^256 into 8x10^6 parts, each part has ~125x10^68 numbers; then we should search for the part near (2^128 + 2^256)/2 first (most wallet generates keys in this range). 125x10^68 is still a big number, but maybe it is better than making randomly guess. **UPDATE: done!**

**Use GPU for calculation.** GPU may work much faster for this kind of task, but this program need to be rewritten in [C, C++, Fortran or Python](https://developer.nvidia.com/how-to-cuda-c-cpp). [Java could do the work](http://www.jcuda.org/), but I doubt if it is a good choice.

## Usage

Install java on your computer, download the **[GuessPrivateKey.jar](https://github.com/scorta/GuessPrivateKey/releases/tag/0.1)** file, then enter this command

`java -jar <this_program> <numbers of threads> <file_contains_bitcoin_addresses> <choice> <start_from>`

`<numbers of threads>`: number of threads, should be equal to your CPU cores (or less, if you want to use it for other tasks). Eg. my CPU has 8 cores, so I set `<Number of threads>` to 7 or 8.

`<choice>`: 0 means guessing keys randomly; while non-zero means sequential check, starting from `<start_from>`. Default value is 0.

`<start_from>`: if you do not specify `<start_from>`, then the program will check from 0.

Eg.
`java -jar GuessPrivateKey.jar 7 bit.txt`

Running with 7 threads, and searching key(s) randomly for list of Bitcoin address(es) in the file `bit.txt`.

Eg.
`java -jar GuessPrivateKey.jar 8 bit.txt 1 666`

Running with 8 threads, and searching key(s) sequentially (starting from 666) for list of Bitcoin address(es) in the file `bit.txt`.

If you have a big list of Bitcoin addresses (like, millions), you may need to use the `-Xmx` option.

### Compile yourself?

This program uses [bitcoinj](https://bitcoinj.github.io/) library, so please remember to include it.
