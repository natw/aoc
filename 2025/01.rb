t1 = %{
L68
L30
R48
L5
R60
L55
L1
L99
R14
L82
}.strip.split("\n")

def day1(lines)
  zeroes = 0
  pos = 50
  lines.each { |line|
    clicks = line.slice(1..).to_i
    if line[0] == "L"
      pos -= clicks
    else
      pos += clicks
    end
    pos %= 100
    puts pos
    if pos == 0
      zeroes += 1
    end
  }
  puts "--"
  puts zeroes
end

# day1(t1)
# 3

# day1(File.readlines("inputs/01.txt"))


# kinda dumb, but it works
# instead of doing "math", just model every single click and check every time if you're at 0

def part2(lines)
  zeroes = 0
  pos = 50
  lines.each { |line|
    clicks = line.slice(1..).to_i
    if line[0] == "L"
      step = -1
    else
      step = 1
    end

    clicks.times { |n|
      pos = pos + step
      pos %= 100
      if pos == 0
          zeroes += 1
      end
    }
    puts pos
  }
  puts "--"
  puts zeroes
end

# part2(t1)
# 6

part2(File.readlines("inputs/01.txt"))
