package main

import (
	"bufio"
	"fmt"
	"os"
	"strings"
)

func main() {
	f, _ := os.Open("inputs/1.txt")
	s := bufio.NewScanner(f)
	var lines []string
	for s.Scan() {
		lines = append(lines, s.Text())
	}
	input := strings.Join(lines, "")

	fmt.Println(getFloor(input))

	ex1 := `(())`

	fmt.Println(getFloor(ex1))

}

func getFloor(input string) int {
	floor := 0

	for i, x := range input {
		if x == ')' {
			floor--
		} else if x == '(' {
			floor++
		}
		if floor == -1 {
			fmt.Printf("now entering basement - %d\n", i+1)
		}
	}
	return floor
}
