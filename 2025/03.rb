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

# part1(parse_input(test1))

# part1(parse_input(File.read("inputs/03.txt")))

def largest2(bank)
  digits_left = 12
  start = 0
  total = 0
  11.downto(0).each { |digit|
    wl = (bank.length - start) - digit
    window = bank.slice(start, wl)
    puts "start=#{start} wl=#{wl} window=#{window}"
    max = 0
    max_index = 0
    window.each_with_index { |elem, i|
      if elem > max
        max = elem
        max_index = i + start
      end
    }
    puts "    max=#{max} mi=#{max_index}"
    total += max * 10**digit
    start = max_index + 1
  }
  total

end

def part2(banks)
  total = 0
  banks.each { |bank|
    total += largest2(bank)
  }
  p total
end

part2(parse_input(test1))
# largest2(parse_input("987654321111111")[0]) # 987654321111
# largest2(parse_input("811111111111119")[0])
# largest2(parse_input("234234234234278")[0]) # 434234234278
part2(parse_input(File.read("inputs/03.txt")))
