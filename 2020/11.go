package main

import (
	"bufio"
	"fmt"
	"io"
	"os"
)

var ex1 = `L.LL.LL.LL
LLLLLLL.LL
L.L.L..L..
LLLL.LL.LL
L.LL.LL.LL
L.LLLLL.LL
..L.L.....
LLLLLLLLLL
L.LLLLLL.L
L.LLLLL.LL`

func main() {
	f, _ := os.Open("inputs/11.txt")
	// f := strings.NewReader(ex1)
	part2(f)
}

func part1(f io.Reader) {
	layout := getLines(f)

	for {
		newState := tick1(layout)
		fmt.Println("")
		for _, l := range newState {
			fmt.Printf("%c\n", l)
		}

		if layoutEq(newState, layout) {
			fmt.Println("stable")
			fmt.Println(countOccupied(newState))
			break
		}
		layout = newState
	}
}

func part2(f io.Reader) {
	layout := getLines(f)

	i := 0
	for {
		newState := tick2(layout)
		// fmt.Println("")
		// for _, l := range newState {
		// 	fmt.Printf("%c\n", l)
		// }

		if layoutEq(newState, layout) {
			fmt.Println("stable")
			fmt.Println(countOccupied(newState))
			break
		}
		layout = newState
		i++
	}
}

func countOccupied(m [][]rune) int {
	count := 0
	for _, row := range m {
		for _, seat := range row {
			if seat == '#' {
				count++
			}
		}
	}
	return count
}

func layoutEq(a [][]rune, b [][]rune) bool {
	if len(a) != len(b) {
		return false
	}
	for i := range a {
		if string(a[i]) != string(b[i]) {
			return false
		}
	}
	return true
}

func tick2(layout [][]rune) [][]rune {
	newState := [][]rune{}
	for rowNum, row := range layout {
		newRow := make([]rune, len(row), len(row))
		for colNum, seat := range row {
			visible := findOccupied(layout, rowNum, colNum)
			if seat == 'L' && visible == 0 {
				newRow[colNum] = '#'
			} else if seat == '#' && visible >= 5 {
				newRow[colNum] = 'L'
			} else {
				newRow[colNum] = seat
			}
		}
		newState = append(newState, newRow)
	}
	return newState
}

func tick1(layout [][]rune) [][]rune {
	newState := [][]rune{}
	for rowNum, row := range layout {
		newRow := make([]rune, len(row), len(row))
		for colNum, seat := range row {
			surr := getSurrounding(layout, rowNum, colNum)
			if seat == 'L' && occupied(surr) == 0 {
				newRow[colNum] = '#'
			} else if seat == '#' && occupied(surr) >= 4 {
				newRow[colNum] = 'L'
			} else {
				newRow[colNum] = seat
			}
		}
		newState = append(newState, newRow)
	}
	return newState
}

func occupied(seats []rune) int {
	count := 0
	for _, s := range seats {
		if s == '#' {
			count++
		}
	}
	return count
}

func copyLayout(layout [][]rune) [][]rune {
	n := make([][]rune, 0, 0)
	for _, row := range layout {
		var nr []rune
		copy(nr, row)
		n = append(n, nr)
	}
	return n
}
func findOccupied(layout [][]rune, rowNum int, colNum int) int {
	count := 0
	cs := [][]int{
		{-1, -1},
		{-1, 0},
		{-1, 1},
		{0, -1},
		{0, 1},
		{1, -1},
		{1, 0},
		{1, 1},
	}
	for _, dir := range cs {
		keepGoing := true
		i := 1
		for keepGoing {
			r := rowNum + (dir[0] * i)
			c := colNum + (dir[1] * i)
			if r < 0 || r > len(layout)-1 {
				keepGoing = false
			} else if c < 0 || c > len(layout[0])-1 {
				keepGoing = false
			} else if layout[r][c] == '#' {
				count++
				keepGoing = false
			} else if layout[r][c] == 'L' {
				keepGoing = false
			}
			i++
		}
	}
	return count
}

func getSurrounding(layout [][]rune, rowNum int, colNum int) []rune {
	surrounding := []rune{}
	cs := [][]int{
		{-1, -1},
		{-1, 0},
		{-1, 1},
		{0, -1},
		{0, 1},
		{1, -1},
		{1, 0},
		{1, 1},
	}
	for _, r := range cs {
		use := true
		if rowNum == 0 && r[0] == -1 {
			use = false
		}
		if colNum == 0 && r[1] == -1 {
			use = false
		}
		if rowNum == len(layout)-1 && r[0] == 1 {
			use = false
		}
		if colNum == len(layout[0])-1 && r[1] == 1 {
			use = false
		}
		if use {
			surrounding = append(surrounding, layout[rowNum+r[0]][colNum+r[1]])
		}
	}
	return surrounding
}

func getLines(f io.Reader) [][]rune {
	var lines [][]rune
	s := bufio.NewScanner(f)
	for s.Scan() {
		line := []rune(s.Text())
		lines = append(lines, line)
	}
	return lines
}
