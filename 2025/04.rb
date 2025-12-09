def parse_input(input)
  input.split("\n").map { |line|
    line.split("")
  }
end

t1 = parse_input(%(..@@.@@@@.
@@@.@.@.@@
@@@@@.@.@@
@.@@@@..@.
@@.@@@@.@@
.@@@@@@@.@
.@.@.@.@@@
@.@@@.@@@@
.@@@@@@@@.
@.@.@@@.@.))

# puts t1.inspect

def sf(room, y, x)
  if y < 0 || y >= room.length || x < 0 || x >= room[0].length
    nil
  else
    room.fetch(y).fetch(x)
  end
end

def surrounding(room, y, x)
  [
    sf(room, y-1, x-1),
    sf(room, y-1, x),
    sf(room, y-1, x+1),
    sf(room, y, x-1),
    sf(room, y, x+1),
    sf(room, y+1, x-1),
    sf(room, y+1, x),
    sf(room, y+1, x+1),
  ].compact
end

def part1(room)
  height = room.length
  width = room[0].length
  total = 0
  (0...height).each { |y|
    (0...width).each { |x|
      if room[y][x] == "@"
        c = surrounding(room, y, x).tally["@"] || 0
        if c < 4
          total += 1
        end
      end
    }
  }
  p total
end

part1(t1)

input1 = parse_input(File.new("inputs/04.txt").read)
puts input1[-1].inspect
part1(input1)

def part2(room)
  keep_going = true
  height = room.length
  width = room[0].length
  total = 0
  while keep_going
    to_remove = []
    (0...height).each { |y|
      (0...width).each { |x|
        if room[y][x] == "@"
          c = surrounding(room, y, x).tally["@"] || 0
          if c < 4
            to_remove << [y,x]
          end
        end
      }
    }
    to_remove.each { |y,x|
      room[y][x] = "."
    }
    total += to_remove.length
    if to_remove.length == 0
      keep_going = false
    end
  end
  puts total
end

part2(t1)
part2(input1)
