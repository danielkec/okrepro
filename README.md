# okrepro
okhttp client sends h2 RST_STREAM twice after timeout for the same stream. That is prohibited by the spec:

[RFC 9113 §5.1](https://httpwg.org/specs/rfc9113.html#StreamStates)
`A stream also enters the "closed" state after an endpoint either sends or receives a RST_STREAM frame.`
...
`An endpoint MUST NOT send frames other than PRIORITY on a closed stream.`


![image](https://github.com/danielkec/okrepro/assets/1773630/798b6e6d-4748-4e52-b4fb-1deba563c9b5)
