
# Network and Internetwork Security Practical

> Claire Denny, Duncan Campbell, Jarryd Dunn, Matthew Poulter

This is the repository for our CSC4012Z practical assignment. If you want to contribute, please review the [Contributing](/CONTRIBUTING.md) file.

### Brief task description
The objective is to simulate/replicate the message confidentiality and authentication aspects of PGP.

### Further task information

 - Create a Client Application that will transmit a message, using the shared key, private key, hashing and compression functions, in the same manner which PGP would do.

 - Create a Server Application which will receive the message and process the decryption and checking of the message as PGP would do.

 - A Client/Server communication system (based on UDP or TCP) should be established, so that the client can “connect” to the server and transmit the secured message.

 - You may assume that public keys are “available” to the Client and Server, so no exchange of certificates is required to obtain the public keys and some kind of local ‘key ring’ can be assumed.

-  Public / private key pairs should be created for use with RSA (for public key encryption) and a shared key generated for use with DES, AES etc. (for shared key encryption).

 - For testing purposes please include debugging statements. i.e. output the encrypted text messages along with the decrypted text to the console so that when it is run we can see what it is doing.

- A short write-up (no more than 5 pages) is required to explain your    implementation, choice of language, choice of crypto algorithms, key    management and communication connectivity model.

### Marking guidelines
| Task | % |
|--|--|
| Overall system design and functionality to achieve stated goal | 40% |
| Communications implementation | 20% |
| Security implementation | 30% |
| Evidence of testing | 10% |


### Repository structure
This directory has three folders which are important: [src](/src), [test](/test) and [docs](/docs). The `src` directory, contains all the code for our project. A loose version of the MVC architectural pattern is to be used. The `test` directory includes the tests for our code, and the `docs` directory contains our documentation for our code.

Further, the `lib` directory contains the dependencies needed for our code.
