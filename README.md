# GuessPrivateKey
This program searches Private Key(s) for given Bitcoin address(es)

## How does it do?

This program generates random Bitcoin Private Key, then check if it is the key for given address(es).

A Bitcoin Private Key is an integer between 1 and 10^77, so to *find* a Private Key for a given address, we *just* need to generate a number between 1 and 10^77, and check if it is the key for that address. Sounds easy, right?

But the problem is, 10^77 is a big number. A really really big number (to compare, the total number of atoms in observable universe is about 10^82). If you are able to check a quintillion key (10^18) per second, you still need 3'170'979'198'376'458'650'431'253'170'979'198'376'458'650'431'253'170 years to check all the possible Bitcoin keys. FYI, my CPU (Xeon E3 1231 v3) is able to check ~12'000 keys/second.

But if you are an extremely lucky person, or you have some quantum computers at home, then you could try.

Just in case you are serious about finding a key for a Bitcoin address with positive balance, you may want to try **[Large Bitcoin Collider](https://lbc.cryptoguru.org/about)**

[And there was much more efficient way to find a Private Key](https://www.deepdotweb.com/2017/06/09/bitcoin-brain-wallets-hackers-heaven/), but I am afraid that hackers have done long before.

## Room for improvement

**If Bitcoin Private Keys are truly random**, then doing a thorough search for a particular part is better than guessing randomly. Eg. currently, there are ~8x10^6 Bitcoin addresses with 0.005BTC or more; so we should divide 10^77 into 8x10^6 parts, each part has 125x10^68 numbers; then we should search for the part near 10^77/2 first. 125x10^68 is still a big number, but maybe it is better than making randomly guess.

**Use GPU for calculation.** GPU may work much faster for this kind of task, but this program need to be rewritten in [C, C++, Fortran or Python](https://developer.nvidia.com/how-to-cuda-c-cpp). [Java could do the work](http://www.jcuda.org/), but I doubt if it is a good choice.

## Usage

Install java on your computer, download the **[GuessPrivateKey.jar](https://github.com/scorta/GuessPrivateKey/releases/tag/0.1)** file, then enter this command

    java -jar <this_program> <numbers of threads> <file_contains_bitcoin_addresses>    
Eg.

    java -jar GuessPrivateKey.jar 7 bit.txt
It will run with 7 threads, and searching key(s) for list of Bitcoin address(es) in the file `bit.txt`. `<Number of threads>` should be equal to your CPU cores (or less, if you want to use it for other tasks). Eg. my CPU has 8 cores, so I set `<Number of threads>` to 7 or 8.

### Compile yourself?

This program uses [bitcoinj](https://bitcoinj.github.io/) library, so please remember to include it.
