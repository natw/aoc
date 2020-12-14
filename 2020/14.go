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
var ex2 = `mask = 000000000000000000000000000000X1001X
mem[42] = 100
mask = 00000000000000000000000000000000X0XX
mem[26] = 1
`

func main() {
	// part1(strings.NewReader(ex1))
	f, _ := os.Open("inputs/14.txt")
	// part1(f)
	// part2(strings.NewReader(ex2))
	part2(f)
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

func part2(f io.Reader) {
	re := regexp.MustCompile(`\d+`)
	var mask string
	memory := make(map[int64]int64)
	s := bufio.NewScanner(f)
	for s.Scan() {
		line := s.Text()
		if strings.Contains(line, "mask") {
			parts := strings.Split(line, " ")
			mask = parts[2]
		} else {
			matches := re.FindAllStringSubmatch(line, -1)
			locString := matches[0][0]
			dataDec, _ := strconv.ParseInt(matches[1][0], 10, 64)
			locs := generateLocations(mask, locString)
			// fmt.Printf("data: %d\n", dataDec)
			for _, loc := range locs {
				// fmt.Printf("location: %d\n", loc)
				memory[loc] = dataDec
			}
		}
	}

	var total int64
	for _, v := range memory {
		total += v
	}
	fmt.Printf("total: %d\n", total)
}

// I should stop using []rune. it's []byte and those aren't actually the same

func generateLocations(mask string, locDecString string) []int64 {
	l, _ := strconv.ParseInt(locDecString, 10, 64)
	locString := fmt.Sprintf("%36.36b", l)

	locs := []int64{}

	exes := []int{}

	fmt.Printf("raw location: %s\n", locString)

	semiMasked := make([]byte, 36, 36)
	for i, c := range mask {
		switch c {
		case 'X':
			semiMasked[i] = 'X'
		case '1':
			semiMasked[i] = '1'
		case '0':
			semiMasked[i] = locString[i]
		}
	}

	fmt.Printf("semi masked: %c\n", semiMasked)

	for i, c := range mask {
		if c == 'X' {
			exes = append(exes, i)
		}
	}

	// fmt.Printf("X at locations: %+v\n", exes)

	possibilities := []string{}
	for i := 0; i <= 1<<len(exes)-1; i++ {
		possibilities = append(possibilities, fmt.Sprintf("%*.*b", len(exes), len(exes), i))
	}

	fmt.Println("location masks:")
	fmt.Println(possibilities)

	fmt.Println("X locations")
	fmt.Println(exes)
	for _, poss := range possibilities {
		l := []byte(semiMasked)
		for i, xLoc := range exes {
			// fmt.Printf("X %d at location %02d, set to %c\n", i, xLoc, rune(poss[i]))
			l[xLoc] = poss[i]
		}
		l2, _ := strconv.ParseInt(string(l), 2, 64)
		locs = append(locs, l2)
	}

	return locs
}

func makeInt(bits []rune) int64 {
	i, _ := strconv.ParseInt(string(bits), 2, 64)
	return i
}
