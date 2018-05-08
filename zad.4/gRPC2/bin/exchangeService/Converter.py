
import random
from random import randint
import threading
from exchangeService.gen import currencyExchange_pb2 as c

lock = threading.Lock()

exchange = {c.USD: {c.PLN: 3.56, c.EUR: 0.83},
            c.PLN: {c.USD: 0.28, c.EUR: 0.23},
            c.EUR: {c.USD: 1.20, c.PLN: 4.27}}


def convert(from_curr, to_curr):

    with lock:
        random_change_value = random.uniform(0, 0.4)

        result = exchange[from_curr][to_curr]
        randint(0, 10)

        if randint(0, 10) % 4 == 0:
            result = result + random_change_value
            exchange[from_curr][to_curr] = result

        return round(result, 4)
