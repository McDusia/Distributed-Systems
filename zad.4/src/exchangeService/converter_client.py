
from __future__ import print_function
import sys

import grpc
from exchangeService.gen import currencyExchange_pb2 as c, currencyExchange_pb2_grpc as ce


def run():
    channel = grpc.insecure_channel('localhost:50051')
    try:
        grpc.channel_ready_future(channel).result(timeout=20)
    except grpc.FutureTimeoutError:
        sys.exit('Error connecting to server')
    else:
        stub = ce.CurrencyExchangeStub(channel)
        request = c.ExchangeArguments(currencyType=c.PLN)
        try:
            result = stub.Count(request)

            for r in result:
                print("Feature called", r)
        except grpc.RpcError:
            print('Error in converter')
        except KeyboardInterrupt:
            print('END')


if __name__ == '__main__':
    run()
