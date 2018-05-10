package Java.currencyExchange;

import static io.grpc.MethodDescriptor.generateFullMethodName;
import static io.grpc.stub.ClientCalls.asyncServerStreamingCall;
import static io.grpc.stub.ClientCalls.blockingServerStreamingCall;
import static io.grpc.stub.ServerCalls.asyncServerStreamingCall;
import static io.grpc.stub.ServerCalls.asyncUnimplementedUnaryCall;

/**
 */
@javax.annotation.Generated(
    value = "by gRPC proto compiler (version 1.11.0)",
    comments = "Source: currencyExchange.proto")
public final class CurrencyExchangeGrpc {

  private CurrencyExchangeGrpc() {}

  public static final String SERVICE_NAME = "currencyExchange.CurrencyExchange";

  // Static method descriptors that strictly reflect the proto.
  @io.grpc.ExperimentalApi("https://github.com/grpc/grpc-java/issues/1901")
  @Deprecated // Use {@link #getCountMethod()} instead.
  public static final io.grpc.MethodDescriptor<ExchangeArguments,
          CountedExchange> METHOD_COUNT = getCountMethodHelper();

  private static volatile io.grpc.MethodDescriptor<ExchangeArguments,
          CountedExchange> getCountMethod;

  @io.grpc.ExperimentalApi("https://github.com/grpc/grpc-java/issues/1901")
  public static io.grpc.MethodDescriptor<ExchangeArguments,
          CountedExchange> getCountMethod() {
    return getCountMethodHelper();
  }

  private static io.grpc.MethodDescriptor<ExchangeArguments,
          CountedExchange> getCountMethodHelper() {
    io.grpc.MethodDescriptor<ExchangeArguments, CountedExchange> getCountMethod;
    if ((getCountMethod = CurrencyExchangeGrpc.getCountMethod) == null) {
      synchronized (CurrencyExchangeGrpc.class) {
        if ((getCountMethod = CurrencyExchangeGrpc.getCountMethod) == null) {
          CurrencyExchangeGrpc.getCountMethod = getCountMethod =
              io.grpc.MethodDescriptor.<ExchangeArguments, CountedExchange>newBuilder()
              .setType(io.grpc.MethodDescriptor.MethodType.SERVER_STREAMING)
              .setFullMethodName(generateFullMethodName(
                  "currencyExchange.CurrencyExchange", "Count"))
              .setSampledToLocalTracing(true)
              .setRequestMarshaller(io.grpc.protobuf.ProtoUtils.marshaller(
                  ExchangeArguments.getDefaultInstance()))
              .setResponseMarshaller(io.grpc.protobuf.ProtoUtils.marshaller(
                  CountedExchange.getDefaultInstance()))
                  .setSchemaDescriptor(new CurrencyExchangeMethodDescriptorSupplier("Count"))
                  .build();
          }
        }
     }
     return getCountMethod;
  }

  /**
   * Creates a new async stub that supports all call types for the service
   */
  public static CurrencyExchangeStub newStub(io.grpc.Channel channel) {
    return new CurrencyExchangeStub(channel);
  }

  /**
   * Creates a new blocking-style stub that supports unary and streaming output calls on the service
   */
  public static CurrencyExchangeBlockingStub newBlockingStub(
      io.grpc.Channel channel) {
    return new CurrencyExchangeBlockingStub(channel);
  }

  /**
   * Creates a new ListenableFuture-style stub that supports unary calls on the service
   */
  public static CurrencyExchangeFutureStub newFutureStub(
      io.grpc.Channel channel) {
    return new CurrencyExchangeFutureStub(channel);
  }

  /**
   */
  public static abstract class CurrencyExchangeImplBase implements io.grpc.BindableService {

    /**
     */
    public void count(ExchangeArguments request,
        io.grpc.stub.StreamObserver<CountedExchange> responseObserver) {
      asyncUnimplementedUnaryCall(getCountMethodHelper(), responseObserver);
    }

    @Override public final io.grpc.ServerServiceDefinition bindService() {
      return io.grpc.ServerServiceDefinition.builder(getServiceDescriptor())
          .addMethod(
            getCountMethodHelper(),
            asyncServerStreamingCall(
              new MethodHandlers<
                      ExchangeArguments,
                      CountedExchange>(
                  this, METHODID_COUNT)))
          .build();
    }
  }

  /**
   */
  public static final class CurrencyExchangeStub extends io.grpc.stub.AbstractStub<CurrencyExchangeStub> {
    private CurrencyExchangeStub(io.grpc.Channel channel) {
      super(channel);
    }

    private CurrencyExchangeStub(io.grpc.Channel channel,
        io.grpc.CallOptions callOptions) {
      super(channel, callOptions);
    }

    @Override
    protected CurrencyExchangeStub build(io.grpc.Channel channel,
        io.grpc.CallOptions callOptions) {
      return new CurrencyExchangeStub(channel, callOptions);
    }

    /**
     */
    public void count(ExchangeArguments request,
        io.grpc.stub.StreamObserver<CountedExchange> responseObserver) {
      asyncServerStreamingCall(
          getChannel().newCall(getCountMethodHelper(), getCallOptions()), request, responseObserver);
    }
  }

  /**
   */
  public static final class CurrencyExchangeBlockingStub extends io.grpc.stub.AbstractStub<CurrencyExchangeBlockingStub> {
    private CurrencyExchangeBlockingStub(io.grpc.Channel channel) {
      super(channel);
    }

    private CurrencyExchangeBlockingStub(io.grpc.Channel channel,
        io.grpc.CallOptions callOptions) {
      super(channel, callOptions);
    }

    @Override
    protected CurrencyExchangeBlockingStub build(io.grpc.Channel channel,
        io.grpc.CallOptions callOptions) {
      return new CurrencyExchangeBlockingStub(channel, callOptions);
    }

    /**
     */
    public java.util.Iterator<CountedExchange> count(
        ExchangeArguments request) {
      return blockingServerStreamingCall(
          getChannel(), getCountMethodHelper(), getCallOptions(), request);
    }
  }

  /**
   */
  public static final class CurrencyExchangeFutureStub extends io.grpc.stub.AbstractStub<CurrencyExchangeFutureStub> {
    private CurrencyExchangeFutureStub(io.grpc.Channel channel) {
      super(channel);
    }

    private CurrencyExchangeFutureStub(io.grpc.Channel channel,
        io.grpc.CallOptions callOptions) {
      super(channel, callOptions);
    }

    @Override
    protected CurrencyExchangeFutureStub build(io.grpc.Channel channel,
        io.grpc.CallOptions callOptions) {
      return new CurrencyExchangeFutureStub(channel, callOptions);
    }
  }

  private static final int METHODID_COUNT = 0;

  private static final class MethodHandlers<Req, Resp> implements
      io.grpc.stub.ServerCalls.UnaryMethod<Req, Resp>,
      io.grpc.stub.ServerCalls.ServerStreamingMethod<Req, Resp>,
      io.grpc.stub.ServerCalls.ClientStreamingMethod<Req, Resp>,
      io.grpc.stub.ServerCalls.BidiStreamingMethod<Req, Resp> {
    private final CurrencyExchangeImplBase serviceImpl;
    private final int methodId;

    MethodHandlers(CurrencyExchangeImplBase serviceImpl, int methodId) {
      this.serviceImpl = serviceImpl;
      this.methodId = methodId;
    }

    @Override
    @SuppressWarnings("unchecked")
    public void invoke(Req request, io.grpc.stub.StreamObserver<Resp> responseObserver) {
      switch (methodId) {
        case METHODID_COUNT:
          serviceImpl.count((ExchangeArguments) request,
              (io.grpc.stub.StreamObserver<CountedExchange>) responseObserver);
          break;
        default:
          throw new AssertionError();
      }
    }

    @Override
    @SuppressWarnings("unchecked")
    public io.grpc.stub.StreamObserver<Req> invoke(
        io.grpc.stub.StreamObserver<Resp> responseObserver) {
      switch (methodId) {
        default:
          throw new AssertionError();
      }
    }
  }

  private static abstract class CurrencyExchangeBaseDescriptorSupplier
      implements io.grpc.protobuf.ProtoFileDescriptorSupplier, io.grpc.protobuf.ProtoServiceDescriptorSupplier {
    CurrencyExchangeBaseDescriptorSupplier() {}

    @Override
    public com.google.protobuf.Descriptors.FileDescriptor getFileDescriptor() {
      return CurrencyExchangeProto.getDescriptor();
    }

    @Override
    public com.google.protobuf.Descriptors.ServiceDescriptor getServiceDescriptor() {
      return getFileDescriptor().findServiceByName("CurrencyExchange");
    }
  }

  private static final class CurrencyExchangeFileDescriptorSupplier
      extends CurrencyExchangeBaseDescriptorSupplier {
    CurrencyExchangeFileDescriptorSupplier() {}
  }

  private static final class CurrencyExchangeMethodDescriptorSupplier
      extends CurrencyExchangeBaseDescriptorSupplier
      implements io.grpc.protobuf.ProtoMethodDescriptorSupplier {
    private final String methodName;

    CurrencyExchangeMethodDescriptorSupplier(String methodName) {
      this.methodName = methodName;
    }

    @Override
    public com.google.protobuf.Descriptors.MethodDescriptor getMethodDescriptor() {
      return getServiceDescriptor().findMethodByName(methodName);
    }
  }

  private static volatile io.grpc.ServiceDescriptor serviceDescriptor;

  public static io.grpc.ServiceDescriptor getServiceDescriptor() {
    io.grpc.ServiceDescriptor result = serviceDescriptor;
    if (result == null) {
      synchronized (CurrencyExchangeGrpc.class) {
        result = serviceDescriptor;
        if (result == null) {
          serviceDescriptor = result = io.grpc.ServiceDescriptor.newBuilder(SERVICE_NAME)
              .setSchemaDescriptor(new CurrencyExchangeFileDescriptorSupplier())
              .addMethod(getCountMethodHelper())
              .build();
        }
      }
    }
    return result;
  }
}
