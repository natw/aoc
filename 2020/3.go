package main

import (
	"bufio"
	"fmt"
	"os"
	"strings"
)

func main() {
	f, _ := os.Open("inputs/3.txt")
	defer f.Close()
	s := bufio.NewScanner(f)
	var lines []string

	for s.Scan() {
		line := s.Text()
		lines = append(lines, line)
	}

	width := len(lines[0])
	height := len(lines)

	theMap := strings.Join(lines, "")

	c := treeCount(theMap, width, height, 3, 1)

	fmt.Println(c)

}

func treeCount(theMap string, width int, height int, over int, down int) int {
	count := 0
	x := over
	for y := down; y < height; y += down {
		char := theMap[x%width+(width*y)]
		if char == '#' {
			count++
		}
	}
	return count
}
