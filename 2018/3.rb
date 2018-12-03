FABRIC = Hash.new { |hash, key|
  hash[key] = Hash.new { |h2, k2|
    h2[k2] = 0
  }
}

def process_claim(line)
  fields = /#(\d+) @ (\d+),(\d+): (\d+)x(\d+)/.match(line).to_a
  _whole_line, _id, xstart, ystart, xwidth, ywidth = fields

  xstart = xstart.to_i
  ystart = ystart.to_i
  xwidth = xwidth.to_i
  ywidth = ywidth.to_i

  ((ystart)..(ystart + ywidth)).each do |y|
    (( xstart )..(xstart + xwidth)).each do |x|
      FABRIC[y][x] += 1
    end
  end

end


File.open("inputs/input3.txt") do |f|
  f.each_line do |claim|
    process_claim(claim)
  end
end

foo = 0
FABRIC.each_value do |row|
  row.each_value do |column|
    if column >= 2
      foo += 1
    end
  end
end

puts "number:"
puts foo
