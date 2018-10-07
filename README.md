<h1 align="center">Stream ciphers</h1>
<p align="center"><img src="https://i.imgur.com/ZfMSTrk.png" width=150></p>

<p align="center">
<a href="https://github.com/N1ghtF1re/Stream-ciphers/stargazers"><img src="https://img.shields.io/github/stars/N1ghtF1re/Stream-ciphers.svg" alt="Stars"></a>
<a href="https://github.com/N1ghtF1re/Stream-ciphers/releases"><img src="https://img.shields.io/badge/downloads-3-brightgreen.svg" alt="Total Downloads"></a>
<a href="https://github.com/N1ghtF1re/Stream-ciphers/releases"><img src="https://img.shields.io/github/tag/N1ghtF1re/Stream-ciphers.svg" alt="Latest Stable Version"></a>
<a href="https://github.com/N1ghtF1re/Stream-ciphers/blob/master/LICENSE"><img src="https://img.shields.io/github/license/N1ghtF1re/Stream-ciphers.svg" alt="License"></a>
</p>
</p>

## About the library
The library contains three stream ciphers: LFSR, Geffe, RC4

## Class LFSR: 
### Constructors: 
- LFSR(long initRegister, int[] polinom) 
- LFSR(String initRegister) - default poinom (x^24 + x^4 + x^3 + x + 1)

### Methods: 
- encrypt(byte[]) - return encoded bytes array.
- decrypt(byte[]) - return decoded bytes array.
- generateKey() - return bytes array of key

## Class GEFFE: 
### Constructors: 
- Geffe(String initRegister1, String initRegister2, String initRegister3, int[] polinom1, int polinom2[], int polinom3[]) 
- Geffe(String initRegister1, String initRegister2, String initRegister3) - default poinoms (x^24 + x^4 + x^3 + x + 1, x^32 + x^28 + x^27 + x + 1, x^40 + x^21 + x^19 + x^2 + 1)

### Methods: 
- encrypt(byte[]) - return encoded bytes array.
- decrypt(byte[]) - return decoded bytes array.
- generateKey() - return bytes array of key

## Class RC4: 
### Constructors:
- RC4(String strKey)

### Methods: 
- encrypt(byte[]) - return encoded bytes array.
- decrypt(byte[]) - return decoded bytes array.
- generateKey() - return bytes array of key


