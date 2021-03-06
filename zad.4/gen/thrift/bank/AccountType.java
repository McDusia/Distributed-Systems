/**
 * Autogenerated by Thrift Compiler (0.11.0)
 *
 * DO NOT EDIT UNLESS YOU ARE SURE THAT YOU KNOW WHAT YOU ARE DOING
 *  @generated
 */
package thrift.bank;


public enum AccountType implements org.apache.thrift.TEnum {
  PREMIUM(0),
  STANDARD(1);

  private final int value;

  private AccountType(int value) {
    this.value = value;
  }

  /**
   * Get the integer value of this enum value, as defined in the Thrift IDL.
   */
  public int getValue() {
    return value;
  }

  /**
   * Find a the enum type by its integer value, as defined in the Thrift IDL.
   * @return null if the value is not found.
   */
  public static AccountType findByValue(int value) { 
    switch (value) {
      case 0:
        return PREMIUM;
      case 1:
        return STANDARD;
      default:
        return null;
    }
  }
}
