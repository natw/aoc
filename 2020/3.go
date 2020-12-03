package main

import (
	"bufio"
	"fmt"
	"io"
	"os"
	"strings"
)

var ex1 = `..##.......
#...#...#..
.#....#..#.
..#.#...#.#
.#...##..#.
..#.##.....
.#.#.#....#
.#........#
#.##...#...
#...##....#
.#..#...#.#
`

func main() {
	f, _ := os.Open("inputs/3.txt")
	defer f.Close()

	// f := strings.NewReader(ex1)

	theMap, height, width := buildMap(f)

	c1 := treeCount(theMap, width, height, 1, 1)
	c2 := treeCount(theMap, width, height, 3, 1)
	c3 := treeCount(theMap, width, height, 5, 1)
	c4 := treeCount(theMap, width, height, 7, 1)
	c5 := treeCount(theMap, width, height, 1, 2)

	fmt.Println(c1)
	fmt.Println(c2)
	fmt.Println(c3)
	fmt.Println(c4)
	fmt.Println(c5)

	fmt.Println(c1 * c2 * c3 * c4 * c5)

}

func buildMap(r io.Reader) (theMap string, height int, width int) {
	s := bufio.NewScanner(r)
	var lines []string
	for s.Scan() {
		line := s.Text()
		lines = append(lines, line)
	}
	width = len(lines[0])
	height = len(lines)
	theMap = strings.Join(lines, "")
	return
}

func treeCount(theMap string, width int, height int, over int, down int) int {
	count := 0
	x := over
	for y := down; y < height; y += down {
		index := x%width + (width * y)
		char := theMap[index]
		fmt.Printf("at x=%d y=%d index=%d char=%c\n", x, y, index, char)
		if char == '#' {
			count++
		}
		x += over
	}
	return count
}
