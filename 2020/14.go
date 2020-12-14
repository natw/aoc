package main

import (
	"bufio"
	"fmt"
	"io"
	"os"
	"regexp"
	"strconv"
	"strings"
)

var ex1 = `mask = XXXXXXXXXXXXXXXXXXXXXXXXXXXXX1XXXX0X
mem[8] = 11
mem[7] = 101
mem[8] = 0
`

func main() {
	part1(strings.NewReader(ex1))
	f, _ := os.Open("inputs/14.txt")
	part1(f)
}

func part1(f io.Reader) {
	re := regexp.MustCompile(`\d+`)
	var mask string
	memory := make(map[int]int64)
	s := bufio.NewScanner(f)
	for s.Scan() {
		line := s.Text()
		if strings.Contains(line, "mask") {
			parts := strings.Split(line, " ")
			mask = parts[2]
		} else {
			matches := re.FindAllStringSubmatch(line, -1)
			// fmt.Println(line)
			// fmt.Println(matches)
			loc, _ := strconv.Atoi(matches[0][0])
			// fmt.Printf("location: %d\n", loc)
			dataDec, _ := strconv.ParseInt(matches[1][0], 10, 64)
			dataBin := fmt.Sprintf("%36b", dataDec)
			newData := make([]rune, 36, 36)
			for i, r := range dataBin {
				mbit := mask[i]
				if mbit == 'X' {
					if r == ' ' {
						r = '0'
					}
					newData[i] = r
				} else {
					newData[i] = rune(mbit)
				}
			}
			// fmt.Printf("value: %c\n", newData)
			memory[loc] = makeInt(newData)
		}
	}

	var total int64
	for _, v := range memory {
		// fmt.Println(v)
		total += v
	}
	fmt.Println(total)
}

func makeInt(bits []rune) int64 {
	i, _ := strconv.ParseInt(string(bits), 2, 64)
	return i
}
