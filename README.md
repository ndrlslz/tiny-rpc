## Tiny-rpc
tiny-rpc is a lightweight RPC framework based on Netty.

## Quick Start

### Interaction Interface
Firstly define the interaction interface between client and server.
```
public interface HelloWorldService {  
    String hello();  
  
	Future<String> helloAsync();  
}
```

### Server
Implement the interaction interface in server side.
```
public class HelloWorldServiceImpl implements HelloWorldService {  
    @Override  
    public String hello() {  
        return "hello world";  
    }  
  
    @Override  
    public Future<String> helloAsync() {  
        return CompletableFuture.supplyAsync(() -> "hello world");  
    }  
}
```

Then you are able to start RPC server like below.
```
TinyRpcServer
		.create()  
        .registerService(new HelloWorldServiceImpl())  
        .listen(6666);
```

### Client
Running below code to start RPC client, then you can invoke server's method just like invoking locally
```
HelloWorldService helloWorldService = (HelloWorldService) TinyRpcService  
        .create()  
        .service(HelloWorldService.class)  
        .server("localhost", 6666);  
  
helloWorldService.hello();  
helloWorldService.helloAsync().get();
```

## Detailed Documentation

### Protocol
```
                            Protocol
┌ ─ ─ ─ ─ ─ ─ ─ ─ ─ ─ ─ ─ ─ ─ ─ ─ ─ ─ ─ ─ ─ ─ ─ ─ ─ ─ ─ ─ ─ ─ ─ ─ ─ ─  ┐
     2   │   1   │      4      │
├ ─ ─ ─ ─ ─ ─ ─ ─  ─ ─ ─ ─ ─ ─ ─ ─ ─ ─ ─ ─ ─ ─ ─ ─ ─ ─ ─ ─ ─ ─ ─ ─ ─ ─ ┤
         │       │             │
│  MAGIC   Type     Body Size                 Body Content             │
         │       │             │
└ ─ ─ ─ ─ ─ ─ ─ ─ ─ ─ ─ ─ ─ ─ ─ ─ ─ ─ ─ ─ ─ ─ ─ ─ ─ ─ ─ ─ ─ ─ ─ ─ ─ ─  ┘


┌ ─ ─ ─ ─ ─ ─ ─ ─ ─ ─ ─ ─ ─ ─ ─ ─ ─ ─ ─ ─ ─ ─ ─ ─ ─ ─ ─ ─ ─ ─ ─ ─ ─ ─  ┐
    * 消息头7个字节定长
     * = 2 // magic = (short) 0xbabe 
    * + 1 // 消息类型, 0x01 = request, 0x02 = response, 0x03 = heartbeat
    * + 4 // 消息体 body 长度, int 类型
└ ─ ─ ─ ─ ─ ─ ─ ─ ─ ─ ─ ─ ─ ─ ─ ─ ─ ─ ─ ─ ─ ─ ─ ─ ─ ─ ─ ─ ─ ─ ─ ─ ─ ─  ┘
```
