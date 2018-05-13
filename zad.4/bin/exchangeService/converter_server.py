import threading
import time
from time import sleep

import grpc
from concurrent import futures
from converter import convert
from exchangeService.gen import currencyExchange_pb2 as c, currencyExchange_pb2_grpc as ce

_ONE_HOUR_IN_SECONDS = 60 * 60

condition = threading.Condition()
conversions = [c.PLN, c.EUR, c.USD]
exchange = {c.USD: {c.PLN: 3.56, c.EUR: 0.83},
            c.PLN: {c.USD: 0.28, c.EUR: 0.23},
            c.EUR: {c.USD: 1.20, c.PLN: 4.27}}


class CurrencyExchangeServicer(ce.CurrencyExchangeServicer):
    def Count(self, request, context):

        for v1 in conversions:
            for v2 in conversions:
                if v2 != v1:
                    result = int(exchange[v1][v2] * 10000)
                    yield c.CountedExchange(res=result, from_currencyType=v1, to_currencyType=v2)

        from_curr = request.currencyType
        print from_curr

        prev_read = {c.USD: {c.PLN: -1, c.EUR: -1},
                     c.PLN: {c.USD: -1, c.EUR: -1},
                     c.EUR: {c.USD: -1, c.PLN: -1}}

        def is_exchange_unchanged():
            for v in conversions:
                if v != from_curr:
                    if exchange[from_curr][v] != prev_read[from_curr][v]:
                        print '[CurrencyExchangeServicer:%s]' % (context.peer(),), 'change detected', from_curr, \
                            v, exchange[from_curr][v]
                        return False
                    else:
                        return True

        with condition:
            while True:
                while is_exchange_unchanged():
                    print '[CurrencyExchangeServicer:%s]' % (context.peer(),), 'exchange not changed'
                    condition.wait()

                for v in conversions:
                    if v != from_curr:
                        if exchange[from_curr][v] != prev_read[from_curr][v]:
                            print '[CurrencyExchangeServicer:%s]' % (context.peer(),), 'exchange CHANGED', from_curr, \
                                v, exchange[from_curr][v]

                            prev_read[from_curr][v] = exchange[from_curr][v]
                            result = int(exchange[from_curr][v] * 10000)
                            yield c.CountedExchange(res=result, from_currencyType=from_curr, to_currencyType=v)


class ExchangeChanger(threading.Thread):

    def run(self):
        while True:
            with condition:
                changed = False
                for v1 in conversions:
                    for v2 in conversions:
                        if v1 != v2:
                            converted = convert(v1, v2)
                            if exchange[v1][v2] == converted:
                                print '[ExchangeChanger]', 'exchange not changed', v1, v2
                            else:
                                print '[ExchangeChanger]', 'exchange CHANGED', v1, v2, converted
                                exchange[v1][v2] = converted
                                changed = True
                if changed:
                    condition.notify_all()
            sleep(5)


def serve():
    server = grpc.server(futures.ThreadPoolExecutor(max_workers=10))
    ce.add_CurrencyExchangeServicer_to_server(CurrencyExchangeServicer(), server)
    server.add_insecure_port('[::]:50051')
    server.start()
    print 'server is running'

    try:
        while True:
            time.sleep(_ONE_HOUR_IN_SECONDS)
    except KeyboardInterrupt:
        server.stop(0)


if __name__ == '__main__':
    worker = ExchangeChanger()
    worker.start()
    serve()
