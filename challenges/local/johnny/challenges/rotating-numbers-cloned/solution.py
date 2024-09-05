def rotate_and_convert(rotation_amount, base, number_in_base):
    # Convert the number in the given base to decimal
    number_in_decimal = int(number_in_base, base)

    # Get the length of the number in its base by counting digits
    num_len = len(number_in_base)

    # Effective rotation is rotation_amount % num_len
    effective_rotation = rotation_amount % num_len

    # Extract the high and low parts based on the effective rotation
    low_part = number_in_decimal % (base ** effective_rotation)
    high_part = number_in_decimal // (base ** effective_rotation)

    # Rotate by appending the low part to the front
    rotated_number_in_decimal = low_part * (base ** (num_len - effective_rotation)) + high_part

    return rotated_number_in_decimal

# Example usage:
rotation_amount = int(input())  # Input 1: The amount of digits to rotate
base = int(input())             # Input 2: The base of the number
number_in_base = input().strip() # Input 3: The input number in that base

# Get the rotated number in decimal format
result = rotate_and_convert(rotation_amount, base, number_in_base)

# Output the result
print(result)
