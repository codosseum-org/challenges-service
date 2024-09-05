import math

n = int(input())
base = int(input())
number_str = input()
number = int(number_str, base)

n = n % int(len(number_str))

q = base ** n

right_part = (number % q) * q
left_part = number // q

result = right_part + left_part;

print(result)
