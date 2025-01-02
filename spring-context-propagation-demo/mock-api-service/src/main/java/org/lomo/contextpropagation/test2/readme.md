会得到下面的log
```java
初始化Correlation Id 完成
[l-worker-1][                null] Adding product: test-product
[l-worker-1][                null] Notifying shop about: test-product
```

原因是 `ThreadLocal` 类型的变量只在当前的Thread中才拿得到