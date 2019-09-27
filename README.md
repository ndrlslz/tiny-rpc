## tiny-rpc
tiny-rpc is a toy RPC framework based on Netty.

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
Running below code to start RPC client, then you can invoke server's method Just like invoking locally
```
HelloWorldService helloWorldService = (HelloWorldService) TinyRpcService
        .create()
        .service(HelloWorldService.class)
        .server("localhost", 6666);

helloWorldService.hello();
helloWorldService.helloAsync();
```

## TODO
1. protocol
2. server exception handle: https://juejin.im/post/5c8386526fb9a049df24e075
3. client message callback await & signal
