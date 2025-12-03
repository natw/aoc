test1 = %{987654321111111
811111111111119
234234234234278
818181911112111}

def parse_input(input)
  input.split("\n").map(&:strip).map { |line|
    line.chars.map(&:to_i)
  }
end

# p parse_input(test1)
# File.readlines


def part1(banks)
  total = 0
  banks.each { |bank|
    a = bank.slice(0, bank.length-1).max
    ai = bank.find_index(a)
    b = bank.slice(ai+1, bank.length).max
    total += (a*10) + b
  }
  p total
end

part1(parse_input(test1))

part1(parse_input(File.read("inputs/03.txt")))

