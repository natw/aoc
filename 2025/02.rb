t1 = %{11-22,95-115,998-1012,1188511880-1188511890,222220-222224,
1698522-1698528,446443-446449,38593856-38593862,565653-565659,
824824821-824824827,2121212118-2121212124}.gsub("\n", "")

def make_ranges(i)
  i.split(",").map { |p|
    p.split("-")
  }.map { |p|
    p.map { |n| n.to_i }
  }
end
tr = make_ranges(t1)

def part1(ranges)
  sum = 0
  ranges.each { |range|
    a,b = range
    (a..(b + 1)).each { |x|
      if is_invalid1?(x.to_s)
        sum += x
      end
    }
  }
  puts sum
end

def is_invalid1?(num)
  return false if num.length.odd?
  a = num.slice(0, num.length / 2)
  b = num.slice(num.length / 2, num.length)
  a == b
end

# part1(tr)

input = make_ranges("67562556-67743658,62064792-62301480,4394592-4512674,3308-4582,69552998-69828126,9123-12332,1095-1358,23-48,294-400,3511416-3689352,1007333-1150296,2929221721-2929361280,309711-443410,2131524-2335082,81867-97148,9574291560-9574498524,648635477-648670391,1-18,5735-8423,58-72,538-812,698652479-698760276,727833-843820,15609927-15646018,1491-1766,53435-76187,196475-300384,852101-903928,73-97,1894-2622,58406664-58466933,6767640219-6767697605,523453-569572,7979723815-7979848548,149-216")

# part1(input)

def is_invalid2?(num)
  (num.length / 2).downto(1).each { |substrlen|
    if num.chars.each_slice(substrlen).uniq.length == 1
      return true
    end
  }
  return false
end

def part2(ranges)
  sum = 0
  ranges.each { |range|
    a,b = range
    (a..(b + 1)).each { |x|
      if is_invalid2?(x.to_s)
        sum += x
      end
    }
  }
  puts sum
end

# part2(tr)
part2(input)


