package main

import (
	"bufio"
	"fmt"
	"io"
	"os"
	"strconv"
)

func main() {
	f, _ := os.Open("inputs/12.txt")
	// f := strings.NewReader(ex1)
	// part1(f)
	part2(f)
}

var ex1 = `F10
N3
F7
R90
F11`

var dirIndex = map[rune]int{
	'N': 0,
	'E': 1,
	'S': 2,
	'W': 3,
}
var directions = [][]int{
	{0, -1}, // north = 0
	{1, 0},  // east = 1
	{0, 1},  // south = 2
	{-1, 0}, // west = 3
}

func part2(f io.Reader) {
	shipLocation := []int{0, 0}
	waypointDiff := []int{10, 1}

	s := bufio.NewScanner(f)
	for s.Scan() {
		line := s.Text()
		cmd, val := parse(line)

		fmt.Printf("shipLocation: (%d, %d)  waypointDiff: (%d, %d)\n", shipLocation[0], shipLocation[1], waypointDiff[0], waypointDiff[1])
		switch cmd {
		case 'F': // move ship to waypoint val times
			diffX := waypointDiff[0] * val
			diffY := waypointDiff[1] * val
			shipLocation[0] += diffX
			shipLocation[1] += diffY
		case 'R': // turn right. I'm assuming all vals are multiples of 90
			steps := int(val / 90)
			for steps > 0 {
				x := waypointDiff[0]
				y := waypointDiff[1]
				waypointDiff = []int{y, -x}
				steps--
			}
		case 'L': // turn left
			steps := int(val / 90)
			for steps > 0 {
				x := waypointDiff[0]
				y := waypointDiff[1]
				waypointDiff = []int{-y, x}
				steps--
			}
		case 'N':
			waypointDiff[1] += val
		case 'E':
			waypointDiff[0] += val
		case 'S':
			waypointDiff[1] -= val
		case 'W':
			waypointDiff[0] -= val
		}

	}

	fmt.Printf("ending shipLocation: (%d, %d)\n", shipLocation[0], shipLocation[1])
	fmt.Printf("manhattan distance: %d\n", abs(shipLocation[0])+abs(shipLocation[1]))

}
func part1(f io.Reader) {
	currentDirection := 1
	location := []int{0, 0}

	s := bufio.NewScanner(f)
	for s.Scan() {
		line := s.Text()
		cmd, val := parse(line)

		switch cmd {
		case 'F': //go forward
			dirMult := directions[currentDirection]
			location[0] += val * dirMult[0]
			location[1] += val * dirMult[1]
		case 'R': // turn right. I'm assuming all vals are multiples of 90
			steps := int(val / 90)
			currentDirection = pmod(currentDirection+steps, 4)
		case 'L': // turn left
			steps := int(val / 90)
			currentDirection = pmod(currentDirection-steps, 4)
		case 'N':
			location[1] -= val
		case 'E':
			location[0] += val
		case 'S':
			location[1] += val
		case 'W':
			location[0] -= val
		}

	}

	fmt.Printf("ending location: (%d, %d)\n", location[0], location[1])
	fmt.Printf("manhattan distance: %d\n", abs(location[0])+abs(location[1]))

}

func pmod(x, d int) int {
	x = x % d
	if x >= 0 {
		return x
	}
	if d < 0 {
		return x - d
	}
	return x + d
}

func parse(line string) (rune, int) {
	t := firstRune(line)
	f, _ := strconv.Atoi(line[1:])
	return t, f
}
func firstRune(str string) (r rune) {
	for _, r = range str {
		return
	}
	return
}

func abs(n int) int {
	if n >= 0 {
		return n
	}
	return -n
}
