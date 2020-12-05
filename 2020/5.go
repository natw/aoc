package main

import (
	"bufio"
	"fmt"
	"os"
)

func main() {
	part1()
}

func part1() {
	f, _ := os.Open("inputs/5.txt")
	s := bufio.NewScanner(f)
	var _, _, seatID int64
	var maxID int64
	for s.Scan() {
		line := s.Text()
		_, _, seatID = work1(line)
		if seatID > maxID {
			maxID = seatID
		}
	}
	fmt.Println(maxID)

	// fmt.Println(partition(0, 127, "FBFBBFF"))
	// fmt.Println(partition(0, 7, "RLR"))

	// fmt.Println(work1("FBFBBFFRLR"))
	// fmt.Println(work1("BFFFBBFRRR"))
	// fmt.Println(work1("FFFBBBFRRR"))
	// fmt.Println(work1("BBFFBBFRLL"))
}

func work1(line string) (row int64, col int64, seatID int64) {
	row = partition(0, 127, line[:7])
	col = partition(0, 7, line[7:])
	seatID = 8*row + col
	return
}

func partition(lower int64, upper int64, instrs string) int64 {
	end := false
	for _, instr := range instrs {
		// fmt.Printf("(%d, %d)\n", lower, upper)
		if upper == lower+1 {
			end = true
		}
		if instr == 'F' || instr == 'L' {
			if end {
				return lower
			}
			upper = upper - (upper-lower)/2 - 1
		} else if instr == 'B' || instr == 'R' {
			if end {
				return upper
			}
			lower = lower + (upper-lower)/2 + 1
		}
	}
	return -1
}
