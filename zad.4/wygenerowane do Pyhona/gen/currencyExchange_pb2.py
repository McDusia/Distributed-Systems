# Generated by the protocol buffer compiler.  DO NOT EDIT!
# source: currencyExchange.proto

import sys
_b=sys.version_info[0]<3 and (lambda x:x) or (lambda x:x.encode('latin1'))
from google.protobuf.internal import enum_type_wrapper
from google.protobuf import descriptor as _descriptor
from google.protobuf import message as _message
from google.protobuf import reflection as _reflection
from google.protobuf import symbol_database as _symbol_database
from google.protobuf import descriptor_pb2
# @@protoc_insertion_point(imports)

_sym_db = _symbol_database.Default()


DESCRIPTOR = _descriptor.FileDescriptor(
  name='currencyExchange.proto',
  package='currencyExchange',
  syntax='proto3',
  serialized_pb=_b('\n\x16\x63urrencyExchange.proto\x12\x10\x63urrencyExchange\"I\n\x11\x45xchangeArguments\x12\x34\n\x0c\x63urrencyType\x18\x01 \x01(\x0e\x32\x1e.currencyExchange.CurrencyType\"\x1e\n\x0f\x43ountedExchange\x12\x0b\n\x03res\x18\x01 \x01(\r*)\n\x0c\x43urrencyType\x12\x07\n\x03PLN\x10\x00\x12\x07\n\x03\x45UR\x10\x01\x12\x07\n\x03USD\x10\x02\x32g\n\x10\x43urrencyExchange\x12S\n\x05\x43ount\x12#.currencyExchange.ExchangeArguments\x1a!.currencyExchange.CountedExchange\"\x00\x30\x01\x42\x32\n\x11sr.grpc.generatedB\x15\x43urrencyExchangeProtoP\x01\xa2\x02\x03HLWb\x06proto3')
)

_CURRENCYTYPE = _descriptor.EnumDescriptor(
  name='CurrencyType',
  full_name='currencyExchange.CurrencyType',
  filename=None,
  file=DESCRIPTOR,
  values=[
    _descriptor.EnumValueDescriptor(
      name='PLN', index=0, number=0,
      options=None,
      type=None),
    _descriptor.EnumValueDescriptor(
      name='EUR', index=1, number=1,
      options=None,
      type=None),
    _descriptor.EnumValueDescriptor(
      name='USD', index=2, number=2,
      options=None,
      type=None),
  ],
  containing_type=None,
  options=None,
  serialized_start=151,
  serialized_end=192,
)
_sym_db.RegisterEnumDescriptor(_CURRENCYTYPE)

CurrencyType = enum_type_wrapper.EnumTypeWrapper(_CURRENCYTYPE)
PLN = 0
EUR = 1
USD = 2



_EXCHANGEARGUMENTS = _descriptor.Descriptor(
  name='ExchangeArguments',
  full_name='currencyExchange.ExchangeArguments',
  filename=None,
  file=DESCRIPTOR,
  containing_type=None,
  fields=[
    _descriptor.FieldDescriptor(
      name='currencyType', full_name='currencyExchange.ExchangeArguments.currencyType', index=0,
      number=1, type=14, cpp_type=8, label=1,
      has_default_value=False, default_value=0,
      message_type=None, enum_type=None, containing_type=None,
      is_extension=False, extension_scope=None,
      options=None, file=DESCRIPTOR),
  ],
  extensions=[
  ],
  nested_types=[],
  enum_types=[
  ],
  options=None,
  is_extendable=False,
  syntax='proto3',
  extension_ranges=[],
  oneofs=[
  ],
  serialized_start=44,
  serialized_end=117,
)


_COUNTEDEXCHANGE = _descriptor.Descriptor(
  name='CountedExchange',
  full_name='currencyExchange.CountedExchange',
  filename=None,
  file=DESCRIPTOR,
  containing_type=None,
  fields=[
    _descriptor.FieldDescriptor(
      name='res', full_name='currencyExchange.CountedExchange.res', index=0,
      number=1, type=13, cpp_type=3, label=1,
      has_default_value=False, default_value=0,
      message_type=None, enum_type=None, containing_type=None,
      is_extension=False, extension_scope=None,
      options=None, file=DESCRIPTOR),
  ],
  extensions=[
  ],
  nested_types=[],
  enum_types=[
  ],
  options=None,
  is_extendable=False,
  syntax='proto3',
  extension_ranges=[],
  oneofs=[
  ],
  serialized_start=119,
  serialized_end=149,
)

_EXCHANGEARGUMENTS.fields_by_name['currencyType'].enum_type = _CURRENCYTYPE
DESCRIPTOR.message_types_by_name['ExchangeArguments'] = _EXCHANGEARGUMENTS
DESCRIPTOR.message_types_by_name['CountedExchange'] = _COUNTEDEXCHANGE
DESCRIPTOR.enum_types_by_name['CurrencyType'] = _CURRENCYTYPE
_sym_db.RegisterFileDescriptor(DESCRIPTOR)

ExchangeArguments = _reflection.GeneratedProtocolMessageType('ExchangeArguments', (_message.Message,), dict(
  DESCRIPTOR = _EXCHANGEARGUMENTS,
  __module__ = 'currencyExchange_pb2'
  # @@protoc_insertion_point(class_scope:currencyExchange.ExchangeArguments)
  ))
_sym_db.RegisterMessage(ExchangeArguments)

CountedExchange = _reflection.GeneratedProtocolMessageType('CountedExchange', (_message.Message,), dict(
  DESCRIPTOR = _COUNTEDEXCHANGE,
  __module__ = 'currencyExchange_pb2'
  # @@protoc_insertion_point(class_scope:currencyExchange.CountedExchange)
  ))
_sym_db.RegisterMessage(CountedExchange)


DESCRIPTOR.has_options = True
DESCRIPTOR._options = _descriptor._ParseOptions(descriptor_pb2.FileOptions(), _b('\n\021sr.grpc.generatedB\025CurrencyExchangeProtoP\001\242\002\003HLW'))

_CURRENCYEXCHANGE = _descriptor.ServiceDescriptor(
  name='CurrencyExchange',
  full_name='currencyExchange.CurrencyExchange',
  file=DESCRIPTOR,
  index=0,
  options=None,
  serialized_start=194,
  serialized_end=297,
  methods=[
  _descriptor.MethodDescriptor(
    name='Count',
    full_name='currencyExchange.CurrencyExchange.Count',
    index=0,
    containing_service=None,
    input_type=_EXCHANGEARGUMENTS,
    output_type=_COUNTEDEXCHANGE,
    options=None,
  ),
])
_sym_db.RegisterServiceDescriptor(_CURRENCYEXCHANGE)

DESCRIPTOR.services_by_name['CurrencyExchange'] = _CURRENCYEXCHANGE

# @@protoc_insertion_point(module_scope)